/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Account;
import com.petshop.models.Employees;
import com.petshop.models.Roles;
import java.sql.*;

/**
 *
 * @author duat
 */
public class AccountDAO {

    private Connection conn;

    public AccountDAO() {
        conn = DBConnect.getConnection();
    }

    public Account getAccount(String username, String password) {
        String sql = "SELECT a.id, a.username, a.password, a.created_at, a.is_deleted, a.is_status, "
                + "e.id AS id, e.employee_name, " // Lấy thông tin nhân viên
                + "r.id AS id, r.role_name " // Lấy thông tin role
                + "FROM accounts a "
                + "JOIN employees e ON a.id = e.id " // Liên kết với bảng employees
                + "JOIN roles r ON a.id = r.id " // Liên kết với bảng roles
                + "WHERE a.username = ? AND a.password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employees employee = new Employees(rs.getInt("id"), rs.getString("employee_name"));
                Roles role = new Roles(rs.getInt("id"), rs.getString("role_name"));

                return new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        employee, // Gán nhân viên vào tài khoản
                        role, // Gán role vào tài khoản
                        rs.getDate("created_at"),
                        rs.getBoolean("is_deleted"),
                        rs.getBoolean("is_status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy tài khoản
    }
}
