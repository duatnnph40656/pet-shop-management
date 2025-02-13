/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Employees;
import com.petshop.models.Roles;
import java.sql.*;

/**
 *
 * @author duat
 */
public class EmployeeDAO {

    private Connection conn;

    public EmployeeDAO() {
        conn = DBConnect.getConnection();
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

}
