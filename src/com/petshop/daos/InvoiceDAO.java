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
import java.util.ArrayList;
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
        String sql = "SELECT  i.id, \n"
                + "                   i.invoice_code,  \n"
                + "                   i.created_at,  \n"
                + "                    i.total_price,  \n"
                + "                    i.payment_status,  \n"
                + "                    i.note,  \n"
                + "       i.costs_incurred,  \n"
                + "                    c.customer_name,  \n"
                + "                    e.employee_code \n"
                + "               FROM invoices i \n"
                + "               JOIN customers c ON i.id_customer = c.id \n"
                + "               JOIN employees e ON i.id_employee = e.id \n"
                + "               WHERE i.is_status = 1 AND i.is_deleted = 0";

        List<Invoices> list = new ArrayList<>();
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
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
                + "    e.employee_name \n"
                + "FROM invoices i\n"
                + "JOIN customers c ON i.id_customer = c.id\n"
                + "JOIN employees e ON i.id_employee = e.id\n"
                + "WHERE i.is_deleted = 0;";

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
        String sql = "UPDATE invoices SET status = ? WHERE id = ?";
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
                + "i.payment_method, i.payment_status, i.note, "
                + "c.customer_name, e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapInvoice(rs); // Chuyển đổi kết quả thành object Invoices
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy hóa đơn
    }

    public List<Invoices> searchInvoiceByCode(String invoiceCode) {
        String sql = "SELECT i.id, i.invoice_code, i.total_price, i.costs_incurred, "
                + "i.payment_method, i.payment_status, i.note, "
                + "c.customer_name, e.employee_name "
                + "FROM invoices i "
                + "JOIN customers c ON i.id_customer = c.id "
                + "JOIN employees e ON i.id_employee = e.id "
                + "WHERE i.invoice_code LIKE ?";

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


    public ArrayList<Invoices> search(String keyword) {
        String sql = "SELECT  i.id, \n"
                + "                   i.invoice_code,  \n"
                + "                   i.created_at,  \n"
                + "                    i.total_price,  \n"
                + "                    i.payment_status,  \n"
                + "                    i.note,  \n"
                + "       i.costs_incurred,  \n"
                + "                    c.customer_name,  \n"
                + "                    e.employee_code \n"
                + "               FROM invoices i \n"
                + "               JOIN customers c ON i.id_customer = c.id \n"
                + "               JOIN employees e ON i.id_employee = e.id \n"
                + "               WHERE i.is_status = 1 AND i.is_deleted = 0";

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        if (hasKeyword) {
            sql += " AND i.invoice_code LIKE ?";
        }

        ArrayList<Invoices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (hasKeyword) {
                ps.setString(1, "%" + keyword.trim() + "%"); // Chỉ set nếu có từ khóa
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapInvoice(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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

    public Invoices mapInvoice(ResultSet rs) throws SQLException {
        Invoices i = new Invoices();
        i.setId(rs.getInt("id"));
        i.setInvoiceCode(rs.getString("invoice_code"));

        Customers c = new Customers();
        c.setCustomerCode(rs.getString("customer_code"));
        c.setCustomerName(rs.getString("customer_name")); // Lấy đúng tên khách hàng
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
}
