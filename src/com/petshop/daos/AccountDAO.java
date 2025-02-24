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
        String sql = "SELECT "
                + "a.id AS account_id, a.username, a.password, a.created_at, "
                + "a.is_deleted, a.is_status, "
                + "e.id AS id_employee, e.employee_name, " // Lấy thông tin nhân viên
                + "r.id AS id_role, r.role_name " // Lấy thông tin role
                + "FROM accounts a "
                + "JOIN employees e ON a.id_employee = e.id " // Liên kết đúng với employee_id
                + "JOIN roles r ON a.id_role = r.id " // Liên kết đúng với role_id
                + "WHERE a.username = ? AND a.password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employees employee = new Employees(rs.getInt("id_employee"), rs.getString("employee_name"));
                Roles role = new Roles(rs.getInt("id_role"), rs.getString("role_name"));

                return new Account(
                        rs.getInt("account_id"),
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
