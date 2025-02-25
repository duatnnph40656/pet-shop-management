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
import java.util.ArrayList;
import java.util.List;

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
                + "e.id AS id_employee, e.employee_name, e.id_role AS employee_role_id, " // Lấy id_role từ employee
                + "r.id AS id_role, r.role_name " // Lấy role từ bảng roles
                + "FROM accounts a "
                + "JOIN employees e ON a.id_employee = e.id " // Liên kết với bảng employees
                + "JOIN roles r ON e.id_role = r.id " // Lấy role từ employees thay vì accounts
                + "WHERE a.username = ? AND a.password = ? AND a.is_status = 1 AND a.is_deleted = 0";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employees employee = new Employees(rs.getInt("id_employee"), rs.getString("employee_name"));
                Roles role = new Roles(rs.getInt("id_role"), rs.getString("role_name")); // Lấy role từ bảng employees

                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        employee, // Gán employee vào Account
                        role, // Gán role vào Account
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

    public List<Account> getListAccount() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT "
                + "a.id AS account_id, a.username, a.password, a.created_at, "
                + "a.is_deleted, a.is_status, "
                + "e.id AS id_employee, e.employee_code, e.employee_name, e.email, e.phone_number, " // Thêm thông tin nhân viên
                + "a.id_role AS id_role, r.role_name " // Lấy id_role từ bảng accounts
                + "FROM accounts a "
                + "JOIN employees e ON a.id_employee = e.id " // Liên kết với bảng employees
                + "JOIN roles r ON a.id_role = r.id " // Lấy role từ bảng accounts
                + "WHERE a.is_deleted = 0"; // Lọc các tài khoản chưa bị xóa

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Employees employee = new Employees(
                        rs.getInt("id_employee"),
                        rs.getString("employee_code"),
                        rs.getString("employee_name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
                Roles role = new Roles(rs.getInt("id_role"), rs.getString("role_name")); // Lấy role từ accounts

                list.add(new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        employee, // Gán employee vào Account
                        role, // Gán role vào Account
                        rs.getDate("created_at"),
                        rs.getBoolean("is_deleted"),
                        rs.getBoolean("is_status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (username, password, id_employee, id_role,  is_deleted, is_status) "
                + "VALUES (?, ?, ?, ?,  0, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getEmployee().getId());
            stmt.setInt(4, account.getEmployee().getRole().getId());
            stmt.setBoolean(5, account.isStatus()); // Trạng thái tài khoản
            return stmt.executeUpdate() > 0; // Trả về `true` nếu thêm thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateIsDeleted(int accountId) {
        String sql = "UPDATE accounts SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateIsStatus(int accountId, boolean status) {
        String sql = "UPDATE accounts SET is_status = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(2, accountId);
            stmt.setBoolean(1, status);
            return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu username đã tồn tại
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmployeeHasAccount(int employeeId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE id_employee = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu nhân viên đã có tài khoản
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkOldPassword(int accountId, String oldPassword) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE id = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setString(2, oldPassword);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu COUNT > 0 tức là mật khẩu cũ đúng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(int accountId, String newPassword) {
        String sql = "UPDATE accounts SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, accountId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
