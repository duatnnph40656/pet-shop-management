/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.ultils;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Paragraph;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.kernel.colors.ColorConstants;
import com.petshop.swing.message.DialogMessageFail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import javax.swing.JTextField;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author duat
 */
public class Ultil {

    public static LocalDateTime calculateEndDate(int days) {
        LocalDateTime startDate = LocalDateTime.now(); // Lấy ngày hiện tại
        return startDate.plusDays(days); // Cộng thêm số ngày
    }

    public static String getFormatted(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    public static String getFormattedCreatedAt(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 VND";
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    public static String formatCurrencyDouble(double amount) {
        if (amount <= 0) { // Nếu số tiền là 0 hoặc âm, trả về "0 VND"
            return "0 VND";
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount).replace("₫", "VND"); // Thay "₫" thành "VND"
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

    // ✅ Tạo hình ảnh barcode và lưu vào thư mục src/main/resources/barcodes/
    public static void generateBarcodeImage(String barcodeText) {
        try {
            int width = 300;
            int height = 150;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeText, BarcodeFormat.EAN_13, width, height);

            // Lấy đường dẫn thư mục resources/barcodes/
            Path resourceDir = Paths.get("src/com/resources/barcodes");

            // Tạo thư mục nếu nó chưa tồn tại
            if (!Files.exists(resourceDir)) {
                Files.createDirectories(resourceDir);
            }

            // Đường dẫn tệp ảnh
            Path imagePath = resourceDir.resolve(barcodeText + ".png");

            // Lưu hình ảnh barcode
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", imagePath);

            System.out.println("✅ Barcode image saved at: " + imagePath.toAbsolutePath());
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

    // ✅ Tạo QR Code từ một đoạn mã cho trước và lưu vào file ảnh
    public static void generateQRCodeImage(String qrText) {
        try {
            int width = 300;
            int height = 300;

            // Tạo ma trận QR Code từ đoạn mã đầu vào
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height);

            // Đường dẫn thư mục lưu ảnh QR Code
            Path resourceDir = Paths.get("src/com/resources/qrcodes");

            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(resourceDir)) {
                Files.createDirectories(resourceDir);
            }

            // Đường dẫn file ảnh QR Code
            Path imagePath = resourceDir.resolve(qrText + ".png");

            // Lưu hình ảnh QR Code
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", imagePath);

            System.out.println("✅ QR Code image saved at: " + imagePath.toAbsolutePath());
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

    public static String removeAccent(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", ""); // Loại bỏ dấu
    }

    public static void generateInvoice(String invoiceId, String employee, String customerName, String phoneNumber, List<String[]> items, String totalAmount) {
        try {
            // Đường dẫn thư mục lưu hóa đơn và QR Code
            Path invoiceDir = Paths.get("src/com/resources/invoices");
            Path qrCodeDir = Paths.get("src/com/resources/qrcodes");

            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(invoiceDir);
            Files.createDirectories(qrCodeDir);

            // Đường dẫn file hóa đơn PDF
            Path invoicePath = invoiceDir.resolve("Invoice_" + invoiceId + ".pdf");

            // Tạo PDF
            String fontPath = "src/com/resources/fonts/NotoSans-Regular.ttf";

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(invoicePath.toString()));
            Document document = new Document(pdfDoc, PageSize.A6);
            document.setMargins(10, 10, 10, 10);

            // Tiêu đề hóa đơn
            PdfFont vietnameseFont = PdfFontFactory.createFont(fontPath, "Identity-H", true);
            document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG").setFont(vietnameseFont).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setBold());

            // Thông tin nhân viên
//            Table infoTable = new Table(new float[]{10, 10}).useAllAvailableWidth();
//            infoTable.addCell(new Cell().add(new Paragraph("HD: " + invoiceId)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
//            infoTable.addCell(new Cell().add(new Paragraph("Ngày bán: " + employee)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
//            document.add(infoTable);
            document.add(new Paragraph("Ngày bán: " + phoneNumber).setFontSize(9).setMultipliedLeading(0.0f).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("HD: " + invoiceId).setFontSize(9).setMultipliedLeading(0.5f));
            document.add(new Paragraph("Nhân viên : " + phoneNumber).setFontSize(9).setMultipliedLeading(0.5f));
            document.add(new Paragraph("Khách hàng: " + phoneNumber).setFontSize(9).setMultipliedLeading(0.5f));
            document.add(new Paragraph("Phone: " + phoneNumber).setFontSize(9).setMultipliedLeading(0.5f));

            document.add(new Paragraph("---------------------------------------------------------------------"));

            // Bảng sản phẩm
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("Name")).setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Qty")).setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Price")).setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Total")).setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY));

            document.setMargins(5, 5, 5, 5); // Giảm lề để có thêm không gian
            table.setFontSize(8); // Giảm kích thước font chữ

//            // 👉 Header của bảng
//            String[] headers = {"Name", "Qty", "Price", "Total"};
//            for (String header : headers) {
//                table.addHeaderCell(new Cell()
//                        .add(new Paragraph(header))
//                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                        .setBold()
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setPadding(5)); // Giảm padding để tiết kiệm không gian
//            }
            // 👉 Dữ liệu sản phẩm
            for (String[] item : items) {
                table.addCell(new Paragraph(item[0]));
                table.addCell(new Paragraph(item[1]));
                table.addCell(new Paragraph(item[2] + "₫"));
                table.addCell(new Paragraph(item[3] + "₫"));
            }
            document.add(table);
            document.add(new Paragraph("---------------------------------------------------------------------"));

            // Tổng tiền
            document.add(new Paragraph("Total: " + totalAmount + "₫").setBold().setTextAlignment(TextAlignment.RIGHT));

            // Tạo QR Code
            Path qrCodePath = qrCodeDir.resolve(invoiceId + ".png");
            generateQRCode(invoiceId, qrCodePath.toString());

            // Thêm QR Code vào hóa đơn
            Image qrImage = new Image(ImageDataFactory.create(qrCodePath.toString()));
            qrImage.scaleAbsolute(100, 100);
            qrImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(qrImage);

            document.close();
            System.out.println("✅ Hóa đơn đã tạo: " + invoicePath.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// ✅ Hàm tạo QR Code
    public static void generateQRCode(String qrText, String filePath) {
        try {
            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        List<String[]> items = List.of(
//                new String[]{"Dog Food", "2", "10.00", "20.00"},
//                new String[]{"Leash", "1", "15.00", "15.00"}
//        );
//        generateInvoice("INV123456", removeAccent("Nguyễn Nông Duật"), "0365190926", "John Doe", items, "206.00");

        List<Object[]> employees = Arrays.asList(
                new Object[]{"ID", "Họ tên", "Lương"},
                new Object[]{1, "Nguyễn Văn A", 1000},
                new Object[]{2, "Trần Thị B", 1200},
                new Object[]{3, "Lê Văn C", 1100}
        );

        // Đường dẫn mặc định
        String folderPath = "src/com/resources/excels/employee";
        String fileName = "employees.xlsx";
        File directory = new File(folderPath);

        // Kiểm tra thư mục, nếu chưa có thì tạo mới
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Thư mục đã được tạo: " + directory.getAbsolutePath());
        }

        // Tạo workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhân viên");

        // Ghi dữ liệu vào Excel
        int rowNum = 0;
        for (Object[] emp : employees) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < emp.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
                if (emp[i] instanceof String) {
                    cell.setCellValue((String) emp[i]);
                } else if (emp[i] instanceof Integer) {
                    cell.setCellValue((Integer) emp[i]);
                }
            }
        }

        // Xuất file Excel vào thư mục
        String filePath = folderPath + "/" + fileName;
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Xuất file Excel thành công: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
