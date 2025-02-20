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
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author duat
 */
public class Ultil {

    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 VND";
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

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

    // Hàm tạo QR Code từ một đoạn mã cho trước và lưu vào file ảnh
    public static void generateQRCodeImage(String qrText) throws IOException {
        try {
            int width = 300;
            int height = 300;
            // Tạo ma trận QR code từ đoạn mã đầu vào
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height);

            // Đường dẫn lưu ảnh QR code
            String directory = "D:\\FPT\\DA1\\Demo\\pet-shop\\src\\com\\petshop\\qrcode\\";
            String filePath = directory + qrText + ".png";

            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("✅ QR Code image saved at: " + filePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomQRText(int length) {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(index));
        }

        return sb.toString();
    }

    public static void generateInvoice(String invoiceId, String customerName, List<String[]> items, String totalAmount) {
        try {
            String invoicePath = "D:\\FPT\\DA1\\Demo\\pet-shop\\invoices\\";
            String qrCodePath = "D:\\FPT\\DA1\\Demo\\pet-shop\\qrcodes\\";
            String filePath = invoicePath + "Invoice_" + invoiceId + ".pdf";

            // Tạo thư mục nếu chưa có
            new File(invoicePath).mkdirs();
            new File(qrCodePath).mkdirs();

            // Tạo PDF
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filePath));
            Document document = new Document(pdfDoc, PageSize.A6);
            document.setMargins(10, 10, 10, 10);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Tiêu đề hóa đơn
            Paragraph title = new Paragraph("INVOICE").setFont(boldFont).setFontSize(16).setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Thông tin nhân viên
            Table infoTable = new Table(new float[]{50, 50}).useAllAvailableWidth();
            infoTable.addCell(new Cell().add(new Paragraph("Staff")).setBorder(null).setTextAlignment(TextAlignment.LEFT));
            infoTable.addCell(new Cell().add(new Paragraph("Admin")).setBorder(null).setTextAlignment(TextAlignment.RIGHT));
            document.add(infoTable);

            // Thông tin khách hàng
            document.add(new Paragraph("Customer\t\t\t " + customerName).setFont(font).setFontSize(10));
            document.add(new Paragraph("-------------------------------------------------------------"));

            // Bảng sản phẩm
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("name").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("qty").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("price").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("total").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            for (String[] item : items) {
                table.addCell(new Paragraph(item[0]).setFont(font));
                table.addCell(new Paragraph(item[1]).setFont(font));
                table.addCell(new Paragraph("$ " + item[2]).setFont(font));
                table.addCell(new Paragraph("$ " + item[3]).setFont(font));
            }
            document.add(table);
            document.add(new Paragraph("---------------------------------------------------------------"));

            // Tổng tiền
            Paragraph total = new Paragraph("Total\t\t\t\t\t $ " + totalAmount).setFont(boldFont).setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            // Tạo QR Code
            String qrCodeFile = qrCodePath + invoiceId + ".png";
            generateQRCode(invoiceId, qrCodeFile);

            Image qrImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(qrCodeFile));
            qrImage.scaleAbsolute(100, 100);
            qrImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(qrImage);

            document.close();
            System.out.println("✅ Hóa đơn đã tạo: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo QR Code
    public static void generateQRCode(String text, String filePath) {
        try {
            int width = 200;
            int height = 200;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);

            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String[]> items = List.of(
                new String[]{"Coca", "2", "0.5", "2"},
                new String[]{"Fanta", "2", "0.5", "2"},
                new String[]{"Beer", "1", "0.5", "0.5"},
                new String[]{"Orange", "2", "0.5", "2"},
                new String[]{"Apple", "2", "0.5", "2"}
        );
        generateInvoice("INV12345", "MR A", items, "8.00");
    }
}
