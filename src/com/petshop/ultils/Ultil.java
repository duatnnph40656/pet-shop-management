/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.ultils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author duat
 */
public class Ultil {

    public static String generateRandomCode() {
        int length = 5; // Độ dài mã sản phẩm
        String characters = "0123456789"; // Ký tự hợp lệ
        StringBuilder productCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            productCode.append(characters.charAt(index));
        }

        return productCode.toString();
    }

    public static String generateEAN13Barcode() {
        Random random = new Random();
        StringBuilder barcode = new StringBuilder();

        // Sinh 12 chữ số ngẫu nhiên
        for (int i = 0; i < 12; i++) {
            barcode.append(random.nextInt(10));
        }

        // Tính checksum và thêm vào barcode
        String barcodeWithChecksum = barcode.toString() + calculateEAN13Checksum(barcode.toString());
        return barcodeWithChecksum;
    }

    // ✅ Tạo hình ảnh barcode và lưu vào đường dẫn mặc định
    public static void generateBarcodeImage(String barcodeText) {
        try {
            int width = 300;
            int height = 150;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeText, BarcodeFormat.EAN_13, width, height);

            // Đường dẫn mặc định
            String directory = "D:\\FPT\\DA1\\Demo\\pet-shop\\src\\com\\petshop\\barcode\\";
            String filePath = directory + barcodeText + ".png";

            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("✅ Barcode image saved at: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tính toán số checksum cho EAN-13
    private static int calculateEAN13Checksum(String barcode) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checksum = (10 - (sum % 10)) % 10;
        return checksum;
    }

    

}
