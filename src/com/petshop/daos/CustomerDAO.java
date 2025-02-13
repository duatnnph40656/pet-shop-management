package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Customers;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class CustomerDAO {

    private Connection conn;

    public CustomerDAO() {
        conn = DBConnect.getConnection();
    }

    // 1. Lấy danh sách khách hàng
    public List<Customers> getListCustomers() {
        String sql = """
            SELECT id, customer_code, customer_name, phone_number, email, address, 
                   created_at, is_deleted, is_status, gender
            FROM customers
        """;

        List<Customers> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm khách hàng mới
    public boolean insertCustomer(Customers customer) {
        String sql = """
            INSERT INTO customers (customer_code, customer_name, phone_number, email, address, 
                                   created_at, is_deleted, is_status, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerCode());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getEmail());
            ps.setString(5, customer.getAddress());
            ps.setDate(6, new java.sql.Date(customer.getCreatedAt().getTime()));
            ps.setBoolean(7, customer.isDeleted());
            ps.setBoolean(8, customer.isStatus());
            ps.setBoolean(9, customer.getGender());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Tìm khách hàng theo số điện thoại
    public Customers searchCustomerByPhoneNumber(String phoneNumber) {
        String sql = """
            SELECT id, customer_code, customer_name, phone_number, email, address, 
                   created_at, is_deleted, is_status, gender
            FROM customers
            WHERE phone_number = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customers searchCustomerByCustomerCode(String customerCode) {
        String sql = """
            SELECT id, customer_code, customer_name, phone_number, email, address, 
                   created_at, is_deleted, is_status, gender
            FROM customers
            WHERE customer_code = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customers searchCustomerById(int id) {
        String sql = """
            SELECT id, customer_code, customer_name, phone_number, email, address, 
                   created_at, is_deleted, is_status, gender
            FROM customers
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm ánh xạ ResultSet thành đối tượng Customer
    private Customers mapCustomer(ResultSet rs) throws SQLException {
        Customers c = new Customers();
        c.setId(rs.getInt("id"));
        c.setCustomerCode(rs.getString("customer_code"));
        c.setCustomerName(rs.getString("customer_name"));
        c.setPhoneNumber(rs.getString("phone_number"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setCreatedAt(rs.getDate("created_at"));
        c.setDeleted(rs.getBoolean("is_deleted"));
        c.setStatus(rs.getBoolean("is_status"));
        c.setGender(rs.getBoolean("gender"));
        return c;
    }
}
