/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Customers;
import com.petshop.models.Employees;
import com.petshop.models.Invoices;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author duat
 */
public class InvoiceDAO {

    private Connection conn;

    public InvoiceDAO() {
        conn = DBConnect.getConnection();
    }

    public List<Invoices> getListInvoice() {
        String sql = "SELECT \n"
                + "    i.id, \n"
                + "    i.invoice_code, \n"
                + "    i.total_price, \n"
                + "    i.costs_incurred, \n"
                + "    i.payment_method, \n"
                + "    i.payment_status, \n"
                + "    i.note, \n"
                + "    i.created_at, \n"
                + "    c.customer_code, \n"
                + "    c.customer_name, \n"
                + "    c.phone_number, \n"
                + "    e.employee_name \n"
                + "FROM invoices i\n"
                + "JOIN customers c ON i.id_customer = c.id\n"
                + "JOIN employees e ON i.id_employee = e.id\n"
                + "WHERE i.is_status = 1 AND i.is_deleted = 0"
                + "ORDER BY i.id DESC";

        List<Invoices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapInvoice(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Invoices> getListInvoiceAll() {
        String sql = "SELECT \n"
                + "    i.id, \n"
                + "    i.invoice_code, \n"
                + "    i.total_price, \n"
                + "    i.costs_incurred, \n"
                + "    i.payment_method, \n"
                + "    i.payment_status, \n"
                + "    i.note, \n"
                + "    i.created_at, \n"
                + "    c.customer_code, \n"
                + "    c.customer_name, \n"
                + "    c.phone_number, \n"
                + "    e.employee_name \n"
                + "FROM invoices i\n"
                + "JOIN customers c ON i.id_customer = c.id\n"
                + "JOIN employees e ON i.id_employee = e.id\n"
                + "WHERE i.is_deleted = 0 AND i.is_status = 0"
                + "ORDER BY i.id DESC";

        List<Invoices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapInvoice(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createPendingInvoice(Invoices invoice) {
        String sql = "INSERT INTO invoices (invoice_code, id_customer, id_employee, total_price,costs_incurred,is_status, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?,?,0)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoice.getInvoiceCode());
            ps.setInt(2, invoice.getCustomer().getId());
            ps.setInt(3, invoice.getEmployee().getId());
            ps.setBigDecimal(4, invoice.getTotalPrice());
            ps.setBigDecimal(5, invoice.getCostsIncurred());
            ps.setBoolean(6, invoice.isStatus()); // Trạng thái "Chờ thanh toán"
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePaymentStatus(int invoiceId, boolean paymentStatus) {
        String sql = "UPDATE invoices SET payment_status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, paymentStatus);
            ps.setInt(2, invoiceId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasPendingInvoice(int customerId) {
        String sql = "SELECT COUNT(*) FROM invoices WHERE is_status = 1 AND id_customer = ? AND is_deleted = 0";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu có ít nhất 1 hóa đơn chờ, trả về true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int invoiceId, boolean status) {
        String sql = "UPDATE invoices SET is_status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, invoiceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDeletedStatus(int invoiceId, boolean isDeleted) {
        String sql = "UPDATE invoices SET is_deleted = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isDeleted);
            ps.setInt(2, invoiceId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateInvoice(int id, Invoices i) {
        String sql = "UPDATE invoices SET payment_status = ?, payment_method = ?, note = ?, costs_incurred = ?, is_status = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, i.isPaymentStatus());
            ps.setBoolean(2, i.isPaymentMethod());
            ps.setString(3, i.getNote());
            ps.setBigDecimal(4, i.getCostsIncurred());
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isValidInvoiceTotal(int invoiceId) {
        String sql = "SELECT total_price FROM invoices WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal totalPrice = rs.getBigDecimal("total_price");
                    return totalPrice != null && totalPrice.compareTo(BigDecimal.ZERO) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Trả về false nếu không tìm thấy hóa đơn hoặc có lỗi xảy ra
    }

    public Invoices getInvoiceById(int invoiceId) {
        String sql = "SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred, "
                + "i.payment_method, i.payment_status, i.note, i.created_at, "
                + "c.id AS customer_id, c.customer_code, c.customer_name, c.phone_number, "
                + "e.id AS employee_id, e.employee_code, e.employee_name "
                + "FROM invoices i "
                + "LEFT JOIN customers c ON i.id_customer = c.id "
                + "LEFT JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapInvoice(rs); // Chuyển đổi dữ liệu từ ResultSet thành đối tượng Invoices
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy hóa đơn
    }

    public Invoices getInvoiceByCode(String invoiceCode) {
        String sql = "SELECT id,invoice_code FROM invoices WHERE invoice_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Invoices i = new Invoices();
                    i.setId(rs.getInt("id"));
                    i.setInvoiceCode(rs.getString("invoice_code"));
                    return i; // Chuyển đổi kết quả thành object Invoices
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy hóa đơn
    }

    public List<Invoices> searchInvoiceByCode(String invoiceCode) {
        String sql = """
        SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred,
               i.payment_method, i.payment_status, i.note, i.created_at,
               c.customer_code, e.employee_code, 
               c.customer_name, e.employee_name,
               c.phone_number      
        FROM invoices i
        JOIN customers c ON i.id_customer = c.id
        JOIN employees e ON i.id_employee = e.id
        WHERE i.invoice_code LIKE ?
    """;

        List<Invoices> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + invoiceCode + "%"); // Tìm kiếm gần đúng

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapInvoice(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Invoices searchInvoiceByCodeResultModel(String invoiceCode) {
        String sql = """
        SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred,
               i.payment_method, i.payment_status, i.note, i.created_at,
               c.customer_code, e.employee_code, 
               c.customer_name, e.employee_name,
               c.phone_number      
        FROM invoices i
        JOIN customers c ON i.id_customer = c.id
        JOIN employees e ON i.id_employee = e.id
        WHERE i.invoice_code = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {  // Chỉ cần lấy 1 kết quả
                    return mapInvoice(rs); // Trả về đối tượng Invoices đã map
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy hóa đơn
    }

    public List<Invoices> searchInvoiceByCustomerId(int customerId) {
        String sql = "SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred, "
                + "i.payment_method, i.payment_status, i.note, "
                + "c.customer_name, e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.id_customer = ?";

        List<Invoices> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapInvoice(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Invoices> searchInvoiceByEmployeeId(int employeeId) {
        String sql = "SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred, "
                + "i.payment_method, i.payment_status, i.note, "
                + "c.customer_name, e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.id_employee = ?";

        List<Invoices> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapInvoice(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Invoices> searchInvoiceByDateRange(String startDateStr, String endDateStr, Boolean paymentStatus) {
        List<Invoices> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred, "
                + "i.payment_method, i.payment_status, i.note, i.created_at, "
                + "c.customer_code, c.customer_name, c.phone_number, e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.is_deleted = 0 AND i.is_status = 0 "
                + "AND i.created_at BETWEEN ? AND ?"
        );

        if (paymentStatus != null) {
            sql.append(" AND i.payment_status = ?");
        }

        sql.append(" ORDER BY i.id DESC"); // Sắp xếp theo ID giảm dần

        try {
            // Chuyển đổi ngày từ String sang LocalDateTime
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Thời gian bắt đầu là 00:00:00, thời gian kết thúc là 23:59:59
            Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(endDate.atTime(23, 59, 59));

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                ps.setTimestamp(1, startTimestamp);
                ps.setTimestamp(2, endTimestamp);

                if (paymentStatus != null) {
                    ps.setBoolean(3, paymentStatus);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapInvoice(rs));
                    }
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi định dạng ngày: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Lỗi truy vấn SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<Invoices> searchInvoicesByPeriod(String period) {
        List<Invoices> invoices = new ArrayList<>();
        String sql = "SELECT "
                + "    i.id, "
                + "    i.invoice_code, "
                + "    i.total_price, "
                + "    i.costs_incurred, "
                + "    i.payment_method, "
                + "    i.payment_status, "
                + "    i.note, "
                + "    i.created_at, "
                + "    c.customer_code, "
                + "    c.customer_name, "
                + "    c.phone_number, "
                + "    e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.is_deleted = 0 AND i.is_status = 0 "
                + "AND i.created_at BETWEEN ? AND ?"
                + "ORDER BY i.id DESC";

        // Xác định khoảng thời gian
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "last_1_day": // 1 ngày qua
                startDate = endDate.minusDays(1);
                break;
            case "last_7_days": // 7 ngày qua
                startDate = endDate.minusDays(7);
                break;
            case "last_30_days": // 30 ngày qua
                startDate = endDate.minusDays(30);
                break;
            default:
                throw new IllegalArgumentException("Invalid period! Use: 'last_1_day', 'last_7_days', 'last_30_days'");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, formattedStartDate);
            ps.setString(2, formattedEndDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(mapInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public List<Invoices> searchInvoicesByFilters(String period, Boolean paymentStatus) {
        List<Invoices> invoices = new ArrayList<>();
        String sql = "SELECT "
                + "    i.id, "
                + "    i.invoice_code, "
                + "    i.total_price, "
                + "    i.costs_incurred, "
                + "    i.payment_method, "
                + "    i.payment_status, "
                + "    i.note, "
                + "    i.created_at, "
                + "    c.customer_code, "
                + "    c.customer_name, "
                + "    c.phone_number, "
                + "    e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.is_deleted = 0 AND i.is_status = 0 ";

        List<Object> params = new ArrayList<>();

        // Nếu có lọc theo khoảng thời gian, thêm điều kiện
        if (period != null) {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate;

            switch (period) {
                case "last_1_day":
                    startDate = endDate.minusDays(1);
                    break;
                case "last_7_days":
                    startDate = endDate.minusDays(7);
                    break;
                case "last_30_days":
                    startDate = endDate.minusDays(30);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period! Use: 'last_1_day', 'last_7_days', 'last_30_days'");
            }

            sql += " AND i.created_at BETWEEN ? AND ? ";
            params.add(startDate.toString());
            params.add(endDate.toString());
        }

        // Nếu có lọc theo trạng thái thanh toán, thêm điều kiện
        if (paymentStatus != null) {
            sql += " AND i.payment_status = ? ";
            params.add(paymentStatus);
        }

        sql += " ORDER BY i.id DESC"; // Sắp xếp theo ID giảm dần

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    ps.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Boolean) {
                    ps.setBoolean(i + 1, (Boolean) params.get(i));
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(mapInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public List<Invoices> searchInvoicesByPaymentStatus(Boolean paymentStatus) {
        List<Invoices> invoices = new ArrayList<>();
        String sql = "SELECT "
                + "    i.id, "
                + "    i.invoice_code, "
                + "    i.total_price, "
                + "    i.costs_incurred, "
                + "    i.payment_method, "
                + "    i.payment_status, "
                + "    i.note, "
                + "    i.created_at, "
                + "    c.customer_code, "
                + "    c.customer_name, "
                + "    c.phone_number, "
                + "    e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.is_deleted = 0 AND i.is_status = 0 ";

        // Nếu có giá trị paymentStatus, thêm điều kiện WHERE
        if (paymentStatus != null) {
            sql += " AND i.payment_status = ?";
        }

        sql += " ORDER BY i.id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (paymentStatus != null) {
                ps.setBoolean(1, paymentStatus);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(mapInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public List<Invoices> searchInvoices(String startDateStr, String endDateStr, String period, Boolean paymentStatus) {
        List<Invoices> invoices = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred, "
                + "i.payment_method, i.payment_status, i.note, i.created_at, "
                + "c.customer_code, c.customer_name, c.phone_number, e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.is_deleted = 0 AND i.is_status = 0 "
        );

        List<Object> params = new ArrayList<>();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDate startDate = null;
            LocalDate endDate = LocalDate.now();

            // Nếu có truyền khoảng ngày cụ thể, sử dụng nó
            if (startDateStr != null && endDateStr != null) {
                startDate = LocalDate.parse(startDateStr, inputFormatter);
                endDate = LocalDate.parse(endDateStr, inputFormatter);
            } // Nếu không có khoảng ngày, kiểm tra theo period
            else if (period != null) {
                switch (period.toLowerCase()) {
                    case "last_1_day":
                        startDate = endDate.minusDays(1);
                        break;
                    case "last_7_days":
                        startDate = endDate.minusDays(7);
                        break;
                    case "last_30_days":
                        startDate = endDate.minusDays(30);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid period! Use: 'last_1_day', 'last_7_days', 'last_30_days'");
                }
            }

            if (startDate != null) {
                sql.append(" AND i.created_at BETWEEN ? AND ? ");
                params.add(startDate.atStartOfDay().format(sqlFormatter));
                params.add(endDate.atTime(23, 59, 59).format(sqlFormatter));
            }

            if (paymentStatus != null) {
                sql.append(" AND i.payment_status = ? ");
                params.add(paymentStatus);
            }

            sql.append(" ORDER BY i.id DESC");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    if (params.get(i) instanceof String) {
                        ps.setString(i + 1, (String) params.get(i));
                    } else if (params.get(i) instanceof Boolean) {
                        ps.setBoolean(i + 1, (Boolean) params.get(i));
                    }
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    invoices.add(mapInvoice(rs));
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi chuyển đổi định dạng ngày: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public boolean updateTotalPrice(int invoiceId) {
        String sumSql = "SELECT COALESCE(SUM(total_price), 0) FROM invoice_details WHERE id_invoice = ? AND is_deleted = 0";
        String updateSql = "UPDATE invoices SET total_price = ? WHERE id = ?";

        try (PreparedStatement sumStmt = conn.prepareStatement(sumSql); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Lấy tổng giá trị từ invoice_details
            sumStmt.setInt(1, invoiceId);
            ResultSet rs = sumStmt.executeQuery();
            BigDecimal totalPrice = BigDecimal.ZERO;

            if (rs.next()) {
                totalPrice = rs.getBigDecimal(1);
            }

            // Cập nhật total_price trong invoices
            updateStmt.setBigDecimal(1, totalPrice);
            updateStmt.setInt(2, invoiceId);

            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateInvoiceByCostsAndNote(int id, BigDecimal costsIncurred, BigDecimal totalPrice, String note) {
        String sql = "UPDATE invoices SET costs_incurred = ?, note = ?, payment_status = 1 , total_price = ?WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, costsIncurred);
            ps.setString(2, note);
            ps.setBigDecimal(3, totalPrice);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Invoices mapInvoice(ResultSet rs) throws SQLException {
        Invoices i = new Invoices();
        i.setId(rs.getInt("id"));
        i.setInvoiceCode(rs.getString("invoice_code"));

        Customers c = new Customers();
        c.setCustomerCode(rs.getString("customer_code"));
        c.setCustomerName(rs.getString("customer_name"));
        c.setPhoneNumber(rs.getString("phone_number"));// Lấy đúng tên khách hàng
        i.setCustomer(c);

        Employees e = new Employees();
        e.setEmployeeName(rs.getString("employee_name")); // Sửa lỗi: Lấy đúng tên nhân viên từ ResultSet
        i.setEmployee(e);

        i.setTotalPrice(rs.getBigDecimal("total_price"));
        i.setCostsIncurred(rs.getBigDecimal("costs_incurred"));
        i.setPaymentMethod(rs.getBoolean("payment_method"));
        i.setPaymentStatus(rs.getBoolean("payment_status"));
        i.setNote(rs.getString("note"));
        i.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return i;
    }

    // Lấy tổng số đơn hàng hôm nay
    public int getTodayOrders() {
        String sql = "SELECT COUNT(*) FROM Invoices WHERE is_status = 0 AND CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE())";
        return getOrderCount(sql);
    }

    // Lấy tổng số đơn hàng hôm qua
    public int getYesterdayOrders() {
        String sql = "SELECT COUNT(*) FROM Invoices WHERE is_status = 0 AND CONVERT(DATE, created_at) = CONVERT(DATE, DATEADD(DAY, -1, GETDATE()))";
        return getOrderCount(sql);
    }

    // Lấy tổng số đơn hàng trong tháng này
    public int getCurrentMonthOrders() {
        String sql = "SELECT COUNT(*) FROM Invoices WHERE is_status = 0 AND MONTH(created_at) = MONTH(GETDATE()) AND YEAR(created_at) = YEAR(GETDATE())";
        return getOrderCount(sql);
    }

    // Lấy tổng số đơn hàng trong tháng trước
    public int getLastMonthOrders() {
        String sql = "SELECT COUNT(*) FROM Invoices WHERE is_status = 0 AND MONTH(created_at) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(created_at) = YEAR(DATEADD(MONTH, -1, GETDATE()))";
        return getOrderCount(sql);
    }

    // Hàm chung để lấy số lượng đơn hàng
    private int getOrderCount(String sql) {
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM invoices WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getYesterdayRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM invoices WHERE CAST(created_at AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCurrentMonthRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM invoices WHERE MONTH(created_at) = MONTH(GETDATE()) AND YEAR(created_at) = YEAR(GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getLastMonthRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM invoices WHERE MONTH(created_at) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(created_at) = YEAR(DATEADD(MONTH, -1, GETDATE()))";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
