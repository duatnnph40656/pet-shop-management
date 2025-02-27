/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Employees;
import com.petshop.models.Roles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class EmployeeDAO {

    private Connection conn;

    public EmployeeDAO() {
        conn = DBConnect.getConnection();
    }

    public List<Employees> getListEmployee() {
        List<Employees> employeesList = new ArrayList<>();
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees WHERE is_deleted = 0";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeesList.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    public List<Employees> getListEmployeeByGender(boolean gender) {
        List<Employees> employeesList = new ArrayList<>();
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees WHERE is_deleted = 0 AND gender = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            stmt.setBoolean(1, gender);
            while (rs.next()) {
                employeesList.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    public List<Employees> getListEmployeeDeleted() {
        List<Employees> employeesList = new ArrayList<>();
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees WHERE is_deleted = 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeesList.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    public boolean insertEmployee(Employees employee) {
        String sql = "INSERT INTO employees(employee_code, employee_name, gender, phone_number, email, address, id_role, is_deleted, is_status) VALUES (?,?,?,?,?,?,?,0,1)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employee.getEmployeeCode());
            ps.setString(2, employee.getEmployeeName());
            ps.setBoolean(3, employee.isGender());
            ps.setString(4, employee.getPhoneNumber());
            ps.setString(5, employee.getEmail());
            ps.setString(6, employee.getAddress());
            ps.setInt(7, employee.getRole().getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Employees findEmployeeByName(String keyword) {
        String sql = "  SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender \n"
                + "              FROM employees \n"
                + "               WHERE employee_name = ?"; // Giả sử chỉ lấy nhân viên chưa bị xóa

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%"); // Tìm kiếm theo tên chứa từ khóa

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Roles role = new Roles();
                    role.setId(rs.getInt("id"));
                    return new Employees(
                            rs.getInt("id"),
                            rs.getString("employee_code"),
                            rs.getString("employee_name"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            role,
                            rs.getString("address"),
                            rs.getTimestamp("created_at"),
                            rs.getBoolean("is_deleted"),
                            rs.getBoolean("is_status"),
                            rs.getBoolean("gender")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy nhân viên
    }

    public List<Employees> searchEmployeeByName(String keyword) {
        List<Employees> employeesList = new ArrayList<>();
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees "
                + "WHERE employee_name LIKE ? AND is_deleted = 0"; // Chỉ lấy nhân viên chưa bị xóa

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%"); // Tìm kiếm theo tên chứa từ khóa

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employeesList.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    public List<Employees> searchEmployee(String keyword) {
        List<Employees> employeesList = new ArrayList<>();
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees "
                + "WHERE (employee_name LIKE ? OR employee_code LIKE ?) AND is_deleted = 0"; // Tìm theo tên hoặc mã, chỉ lấy nhân viên chưa bị xóa

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");  // Tìm kiếm theo tên chứa từ khóa
            stmt.setString(2, "%" + keyword + "%"); // Tìm theo mã chứa từ khóa

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employeesList.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    public List<Employees> getSortedEmployeesByName(boolean ascending) {
        List<Employees> employeesList = new ArrayList<>();
        String order = ascending ? "ASC" : "DESC"; // Xác định thứ tự sắp xếp

        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees WHERE is_deleted = 0 ORDER BY employee_name " + order; // Sắp xếp theo tên

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeesList.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

    public boolean updateEmployee(int id, Employees e) {
        String sql = "UPDATE employees SET employee_name = ?, phone_number = ?, email = ?, address = ?, gender = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getEmployeeName());
            ps.setString(2, e.getPhoneNumber());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getAddress());
            ps.setBoolean(5, e.isGender());
            ps.setInt(6, id); // Thêm điều kiện WHERE

            return ps.executeUpdate() > 0; // Nếu có ít nhất 1 dòng được cập nhật, trả về true
        } catch (SQLException ex) {
            ex.printStackTrace(); // In lỗi SQL nếu có
        }
        return false;
    }

    public boolean restoreEmployee(int id) {
        String sql = "UPDATE employees SET is_deleted = 0 WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
        } catch (Exception e) {
        }
        return false;
    }

    public boolean deletedEmployee(int id) {
        String sql = "UPDATE employees SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Employees findEmployeeById(int id) {
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender "
                + "FROM employees "
                + "WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Đặt giá trị cho tham số ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Roles role = new Roles();
                    role.setId(rs.getInt("id_role")); // Lấy đúng cột ID role

                    return new Employees(
                            rs.getInt("id"),
                            rs.getString("employee_code"),
                            rs.getString("employee_name"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            role,
                            rs.getString("address"),
                            rs.getTimestamp("created_at"),
                            rs.getBoolean("is_deleted"),
                            rs.getBoolean("is_status"),
                            rs.getBoolean("gender")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy nhân viên
    }

    private Employees mapEmployee(ResultSet rs) throws SQLException {
        Employees i = new Employees();
        i.setId(rs.getInt("id"));
        i.setEmployeeCode(rs.getString("employee_code"));
        i.setEmployeeName(rs.getString("employee_name"));
        i.setGender(rs.getBoolean("gender"));
        i.setPhoneNumber(rs.getString("phone_number"));
        i.setEmail(rs.getString("email"));
        i.setAddress(rs.getString("address"));
        Roles role = new Roles();
        role.setId(rs.getInt("id_role")); // Lấy ID role chính xác
        i.setRole(role);
        i.setCreatedAt(rs.getTimestamp("created_at"));
        i.setDeleted(rs.getBoolean("is_deleted"));
        i.setStatus(rs.getBoolean("is_status"));

        return i;
    }

    public List<Employees> getEmployeesByGender(Boolean gender) {
        List<Employees> employeesList = new ArrayList<>();
        String sql = "SELECT id, employee_code, employee_name, phone_number, email, id_role, address, created_at, is_deleted, is_status, gender FROM employees WHERE is_deleted = 0";

        if (gender != null) {
            sql += " AND gender = ?"; // Chỉ lọc khi có giá trị giới tính
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (gender != null) {
                stmt.setBoolean(1, gender);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employeesList.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeesList;
    }

}
