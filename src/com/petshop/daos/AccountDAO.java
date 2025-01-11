/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import java.sql.*;

/**
 *
 * @author dut
 */
public class AccountDAO {

    private Connection conn;
    private String sql = null;

    public AccountDAO() {
        conn = DBConnect.getConnection();
    }

    public boolean login(String username, String password) {
        String query = "SELECT username, password FROM accounts WHERE username = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            // If a record is found, credentials are correct
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if no match found or in case of an exception
    }
}
