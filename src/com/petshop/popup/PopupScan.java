/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.petshop.swing.popup.GlassPanePopup;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import com.petshop.event.CodeListener;
import com.petshop.event.ConfirmListener;

/**
 *
 * @author duat
 */
public class PopupScan extends javax.swing.JPanel {

    /**
     * Creates new form PopupScanBarCode
     */
    private Webcam webcam;
    private ConfirmListener confirmListener;

    private CodeListener codeListener;

    // Đăng ký ConfirmListener
    public void setConfirmListener(ConfirmListener listener) {
        this.confirmListener = listener;
    }

    public PopupScan() {
        initComponents();
        setOpaque(false);
        startWebcam();
        btnClose.addActionListener(evt -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            webcam.close();
            raven.glasspanepopup.GlassPanePopup.closePopup("pWebCam");
        });
    }

    public void setCodeListener(CodeListener listener) {
        this.codeListener = listener;
    }

    private void startWebcam() {
        new Thread(() -> {

            webcam = Webcam.getDefault();
            if (webcam == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy webcam!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Chọn độ phân giải cao nhất
            Dimension[] sizes = webcam.getViewSizes();
            webcam.setViewSize(sizes[sizes.length - 1]);

            for (Dimension d : sizes) {
                System.out.println(d.width + "x" + d.height);
            }

            webcam.open();
            System.out.println("📸 Webcam đang hoạt động, chờ quét mã vạch...");

            while (webcam.isOpen()) {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    lbWebCam.setIcon(new ImageIcon(image)); // Cập nhật ảnh lên JLabel

                    // Quét mã vạch
                    String code = scanCode(image);
                    if (code != null) {
                        System.out.println("📌 Mã vạch quét được: " + code);

                        // 🔴 Gửi dữ liệu về JFrame cha
                        if (codeListener != null) {
                            codeListener.onCodeListener(code);
                        }

                        // 🔴 Tắt webcam ngay sau khi quét
                        webcam.close();
                        System.out.println("❌ Webcam đã tắt.");

                        // ❌ Đóng popup ngay lập tức
                        raven.glasspanepopup.GlassPanePopup.closePopup("pWebCam");

                        break; // Thoát vòng lặp sau khi quét xong
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String scanCode(BufferedImage image) {
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(image)));
        try {
            // Cấu hình định dạng để nhận diện cả QR Code và Barcode
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(
                    BarcodeFormat.QR_CODE, // Hỗ trợ QR Code
                    BarcodeFormat.CODE_39, // Barcode chuẩn CODE_39
                    BarcodeFormat.CODE_128, // Barcode chuẩn CODE_128
                    BarcodeFormat.EAN_13, // Barcode chuẩn EAN_13
                    BarcodeFormat.EAN_8 // Barcode chuẩn EAN_8
            ));

            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            return null; // Không tìm thấy mã vạch hoặc mã QR
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
        g2.dispose();
        super.paintComponent(grphcs);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbWebCam = new javax.swing.JLabel();
        btnClose = new com.petshop.swing.Button();

        setBackground(new java.awt.Color(245, 245, 245));
        setPreferredSize(new java.awt.Dimension(680, 550));

        btnClose.setBackground(new java.awt.Color(245, 245, 245));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-close-15.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(655, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lbWebCam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbWebCam, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnClose;
    private javax.swing.JLabel lbWebCam;
    // End of variables declaration//GEN-END:variables
}
