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

    public String getUsername() {
        return prefs.get("username", null);
    }

    public String getPassword() {
        return prefs.get("password", null);
    }

    public void clearLoginInfo() {
        prefs.remove("username");
        prefs.remove("password");
    }
}
