/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.ultils;

import java.util.Random;

/**
 *
 * @author duat
 */
public class Ultil {

    public static String generateRandomProductCode() {
        int length = 8; // Độ dài mã sản phẩm
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Ký tự hợp lệ
        StringBuilder productCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            productCode.append(characters.charAt(index));
        }

        return productCode.toString();
    }
}
