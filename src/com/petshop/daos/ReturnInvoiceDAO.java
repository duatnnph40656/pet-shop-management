/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Customers;
import com.petshop.models.Employees;
import com.petshop.models.Invoices;
import com.petshop.models.ReturnInvoices;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duat
 */
public class ReturnInvoiceDAO {

    private Connection conn;

    public ReturnInvoiceDAO() {
        conn = DBConnect.getConnection();
    }

    public List<ReturnInvoices> getListReturnInvoice() {
        String sql = """
        SELECT 
            ri.id, 
            ri.return_invoice_code, 
            ri.id_invoice, 
            ri.id_customer, 
            ri.id_employee,
            ri.total_price, 
            COALESCE(ri.total_price_return, 0) AS total_price_return,
            ri.is_deleted, 
            ri.is_status, 
            ri.payment_status, 
            ri.note, 
            ri.costs_incurred, 
            ri.payment_method, 
            ri.created_at, 
            c.customer_name, 
            e.employee_name
        FROM return_invoices ri
        LEFT JOIN customers c ON ri.id_customer = c.id
        LEFT JOIN employees e ON ri.id_employee = e.id
        WHERE ri.is_status = 0
        ORDER BY ri.created_at DESC
    """;

        List<ReturnInvoices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRsReturnInvoice(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ReturnInvoices mapRsReturnInvoice(ResultSet rs) throws SQLException {
        ReturnInvoices r = new ReturnInvoices();
        r.setId(rs.getInt("id"));
        r.setReturnInvoiceCode(rs.getString("return_invoice_code"));
        r.setTotalPrice(rs.getBigDecimal("total_price"));
        r.setTotalPriceReturn(rs.getBigDecimal("total_price_return"));
        r.setDeleted(rs.getBoolean("is_deleted"));
        r.setStatus(rs.getBoolean("is_status"));
        r.setPaymentStatus(rs.getBoolean("payment_status"));
        r.setNote(rs.getString("note"));
        r.setCostsIncurred(rs.getBigDecimal("costs_incurred") != null ? rs.getBigDecimal("costs_incurred") : BigDecimal.ZERO);
        r.setPaymentMethod(rs.getBoolean("payment_method"));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Đối tượng Customers
        Customers c = new Customers();
        c.setId(rs.getInt("id_customer"));
        c.setCustomerName(rs.getString("customer_name"));
        r.setCustomers(c);

        // Đối tượng Employees
        Employees e = new Employees();
        e.setId(rs.getInt("id_employee"));
        e.setEmployeeName(rs.getString("employee_name"));
        r.setEmployees(e);

        // Đối tượng Invoices
        Invoices i = new Invoices();
        i.setId(rs.getInt("id_invoice"));
        r.setInvoices(i);

        return r;
    }

    public boolean insertReturnInvoice(ReturnInvoices r) {
        String sql = "INSERT INTO return_invoices (return_invoice_code, id_invoice, id_customer, "
                + "id_employee, total_price, is_deleted, is_status, payment_status, note, "
                + "costs_incurred, payment_method,total_price_return) "
                + "VALUES (?, ?, ?, ?, ?, 0, 0,? , ?, ?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getReturnInvoiceCode());
            ps.setInt(2, r.getInvoices().getId());
            ps.setInt(3, r.getCustomers().getId());
            ps.setInt(4, r.getEmployees().getId());
            ps.setBigDecimal(5, r.getTotalPrice());
            ps.setBoolean(6, r.isPaymentStatus());
            ps.setString(7, r.getNote());
            ps.setBigDecimal(8, r.getCostsIncurred());
            ps.setBoolean(9, r.isPaymentMethod());
            ps.setBigDecimal(10, r.getTotalPriceReturn());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        r.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ReturnInvoices getReturnInvoiceByCode(String code) {
        String sql = "SELECT \n"
                + "    ri.id, \n"
                + "    ri.return_invoice_code, \n"
                + "    ri.id_invoice, \n"
                + "    ri.id_customer, \n"
                + "    c.customer_name, \n"
                + "    ri.id_employee, \n"
                + "    e.employee_name, \n"
                + "    ri.total_price, \n"
                + "    ri.total_price_return, \n"
                + "    ri.is_deleted, \n"
                + "    ri.is_status, \n"
                + "    ri.payment_status, \n"
                + "    ri.note, \n"
                + "    ri.costs_incurred, \n"
                + "    ri.payment_method, \n"
                + "    ri.created_at\n"
                + "FROM return_invoices ri\n"
                + "LEFT JOIN customers c ON ri.id_customer = c.id\n"
                + "LEFT JOIN employees e ON ri.id_employee = e.id\n"
                + "WHERE ri.return_invoice_code = ?\n"
                + "AND ri.is_status = 0;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);  // Gán mã hóa đơn vào câu lệnh SQL

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Kiểm tra nếu có dữ liệu thì mới gọi `mapRsReturnInvoice`
                    return mapRsReturnInvoice(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy hóa đơn
    }

    public boolean deletedReturnInvoice(int id) {
        String sql = "UPDATE return_invoices SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTotalPrice(int returnInvoiceId) {
        String sumSql = """
        SELECT COALESCE(SUM(
            CASE 
                WHEN rid.type_invoice_detail = 0 THEN pd.price * rid.usage_or_quantity  -- Hàng đổi (cộng)
                WHEN rid.type_invoice_detail = 1 THEN -pd.price * rid.usage_or_quantity -- Hàng trả (trừ)
                ELSE 0
            END
        ), 0)
        FROM return_invoice_details rid
        JOIN product_details pd ON rid.id_product_detail = pd.id_product_detail
        WHERE rid.id_return_invoice = ? AND rid.is_deleted = 0;
    """;

        String updateSql = "UPDATE return_invoices SET total_price = ? WHERE id = ?";

        try (PreparedStatement sumStmt = conn.prepareStatement(sumSql); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Lấy tổng giá trị hóa đơn có cả hàng đổi và hàng trả
            sumStmt.setInt(1, returnInvoiceId);
            ResultSet rs = sumStmt.executeQuery();
            BigDecimal totalPrice = BigDecimal.ZERO;

            if (rs.next()) {
                totalPrice = rs.getBigDecimal(1);
            }

            // Cập nhật total_price trong return_invoices
            updateStmt.setBigDecimal(1, totalPrice);
            updateStmt.setInt(2, returnInvoiceId);

            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTotalPriceForExchange(int returnInvoiceId) {
        String sumSql = """
        SELECT COALESCE(SUM(pd.price * rid.usage_or_quantity), 0)
        FROM return_invoice_details rid
        JOIN product_details pd ON rid.id_product_detail = pd.id
        WHERE rid.id_return_invoice = ? 
              AND rid.type_invoice_detail = 0  -- Chỉ lấy hàng đổi
              AND rid.is_deleted = 0;
    """;

        String updateSql = "UPDATE return_invoices SET total_price = ? WHERE id = ?";

        try (PreparedStatement sumStmt = conn.prepareStatement(sumSql); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Lấy tổng tiền từ hàng đổi
            sumStmt.setInt(1, returnInvoiceId);
            ResultSet rs = sumStmt.executeQuery();
            BigDecimal totalPrice = BigDecimal.ZERO;

            if (rs.next()) {
                totalPrice = rs.getBigDecimal(1);
            }

            // Cập nhật total_price trong return_invoices
            updateStmt.setBigDecimal(1, totalPrice);
            updateStmt.setInt(2, returnInvoiceId);

            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTotalPriceReturnForExchange(int returnInvoiceId) {
        String sumSql = """
        SELECT COALESCE(SUM(pd.price * rid.usage_or_quantity), 0)
        FROM return_invoice_details rid
        JOIN product_details pd ON rid.id_product_detail = pd.id
        WHERE rid.id_return_invoice = ? 
              AND rid.type_invoice_detail = 1  -- Chỉ lấy hàng trả
              AND rid.is_deleted = 0;
    """;

        String updateSql = "UPDATE return_invoices SET total_price_return = ? WHERE id = ?";

        try (PreparedStatement sumStmt = conn.prepareStatement(sumSql); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Lấy tổng tiền từ hàng đổi
            sumStmt.setInt(1, returnInvoiceId);
            ResultSet rs = sumStmt.executeQuery();
            BigDecimal totalPrice = BigDecimal.ZERO;

            if (rs.next()) {
                totalPrice = rs.getBigDecimal(1);
            }

            // Cập nhật total_price trong return_invoices
            updateStmt.setBigDecimal(1, totalPrice);
            updateStmt.setInt(2, returnInvoiceId);

            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ReturnInvoices> getReturnInvoicesByInvoiceId(int invoiceId) {
        String sql = """
           SELECT ri.id, ri.return_invoice_code, ri.id_invoice, 
                       c.customer_name, e.employee_name, ri.total_price, 
                       ri.is_deleted, ri.is_status, ri.payment_status, ri.note, 
                       ri.costs_incurred, ri.payment_method, ri.created_at, ri.total_price_return"
                FROM return_invoices ri
                JOIN customers c ON ri.id_customer = c.id
                JOIN employees e ON ri.id_employee = e.id
                WHERE ri.id_invoice = ? AND ri.is_status = 0;
    """;

        List<ReturnInvoices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRsReturnInvoice(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ReturnInvoices getReturnInvoicesByInvoiceIdModel(int invoiceId) {
        String sql = """
       SELECT ri.id, ri.return_invoice_code, ri.id_invoice, 
                  c.customer_name,c.id AS id_customer, e.id AS id_employee, e.employee_name, ri.total_price, 
                  ri.is_deleted, ri.is_status, ri.payment_status, ri.note, 
                  ri.costs_incurred, ri.payment_method, ri.created_at,ri.total_price_return
           FROM return_invoices ri
           JOIN customers c ON ri.id_customer = c.id
           JOIN employees e ON ri.id_employee = e.id
           WHERE ri.id_invoice = ? AND ri.is_status = 0;
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Chỉ lấy một bản ghi
                    return mapRsReturnInvoice(rs); // Trả về đối tượng ReturnInvoices
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }

}
