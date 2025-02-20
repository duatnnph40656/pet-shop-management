/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.services;

import java.util.prefs.Preferences;

/**
 *
 * @author dut
 */
public class RememberMeService {

    private Preferences prefs = Preferences.userNodeForPackage(RememberMeService.class);

    public void saveLoginInfo(String username, String password) {
        prefs.put("username", username);
        prefs.put("password", password);
    }

    public void saveIdEmployee(int employeeId) {
        prefs.putInt("employeeId", employeeId);
    }

    public String getUsername() {
        return prefs.get("username", null);
    }

    public String getPassword() {
        return prefs.get("password", null);
    }

    public int getEmployeeId() {
        return prefs.getInt("employeeId", -1); // Trả về -1 nếu không tìm thấy
    }

    public void clearLoginInfo() {
        prefs.remove("username");
        prefs.remove("password");
        prefs.remove("employeeId"); // Xóa ID nhân viên
    }
}
