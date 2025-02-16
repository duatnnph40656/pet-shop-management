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
            ps.setBoolean(9, customer.isGender());

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
    
    public Customers searchCustomerByCustomerName(String name) {
        String sql = """
            SELECT id, customer_code, customer_name, phone_number, email, address, 
                   created_at, is_deleted, is_status, gender
            FROM customers
            WHERE customer_name = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
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

    public void create(Customers cs) throws SQLException {
        String sql = "INSERT INTO customers(customer_code, customer_name, phone_number, email, address, is_status,gender,is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?,0)";

        try {
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, cs.getCustomerCode());
            ps.setString(2, cs.getCustomerName());
            ps.setString(3, cs.getPhoneNumber());
            ps.setString(4, cs.getEmail());
            ps.setString(5, cs.getAddress());
            ps.setBoolean(6, cs.isStatus());
            ps.setBoolean(7, cs.isGender());

            ps.execute();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void update(Customers cs) throws SQLException {
        String sql = "UPDATE customers SET customer_code = ?,customer_name = ?,phone_number = ?,email = ?,address = ?,is_status = ?,gender = ? WHERE id = ?";

        try {
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, cs.getCustomerCode());
            ps.setString(2, cs.getCustomerName());
            ps.setString(3, cs.getPhoneNumber());
            ps.setString(4, cs.getEmail());
            ps.setString(5, cs.getAddress());
            ps.setBoolean(6, cs.isStatus());
            ps.setBoolean(7, cs.isGender());
            ps.setInt(8, cs.getId());

            ps.executeUpdate();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean delete(int id) {
        String sql = "UPDATE customers SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }

    public boolean reset_delete(int id) {
        String sql = "UPDATE customers SET is_deleted = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }

    public List<Customers> delete_history() {
        List<Customers> list = new ArrayList<>();
        String sql = " SELECT * FROM customers WHERE is_deleted = 1;";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCustomer(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<Customers> search(String keyword, Integer trangThai) {
        ArrayList<Customers> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE (is_deleted = 0 OR is_deleted IS NULL)";

        if (trangThai != null) {
            sql += " AND is_status = ?";
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND (customer_code LIKE ? OR customer_name LIKE ?)";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;

            if (trangThai != null) {
                ps.setInt(paramIndex++, trangThai);
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
            }

            System.out.println("Final Query with Params: " + ps.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCustomer(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<Customers> orderByName(String orderBy) {
        ArrayList<Customers> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE (is_deleted = 0 OR is_deleted IS NULL)";

        sql += " ORDER BY customer_name " + orderBy; // Thêm sắp xếp theo tên

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCustomer(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
