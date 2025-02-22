/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.petshop.daos.CustomerDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.models.Customers;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.popup.GlassPanePopup;
import com.petshop.ultils.Ultil;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author duat
 */
public class PopupCustomer extends javax.swing.JPanel {

    /**
     * Creates new form PopupCustomer
     */
    private final CustomerDAO customerDAO;

    private ConfirmListener listener;

    // Đăng ký ConfirmListener
    public void setConfirmListener(ConfirmListener listener) {
        this.listener = listener;
    }

    public PopupCustomer() {
        initComponents();
        setOpaque(false);
        customerDAO = new CustomerDAO();
        btnAdd.addActionListener(evt -> {
            if (listener != null) {
                listener.onConfirm();
            }

        });
        btnCanel.addActionListener(evt -> {
            if (listener != null) {
                listener.onCancel();
            }
            raven.glasspanepopup.GlassPanePopup.closePopupLast();
        });
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

    //<editor-fold defaultstate="collapsed" desc="{Message...">
    private void showMessageSuccess(String message) {
        DialogMessageSuccess success = new DialogMessageSuccess(message);
        raven.glasspanepopup.GlassPanePopup.showPopup(success);
    }

    private void showMessageError(String message) {
        DialogMessageError error = new DialogMessageError(message);
        raven.glasspanepopup.GlassPanePopup.showPopup(error);
    }

    private void showMessageFail(String message) {
        DialogMessageFail fail = new DialogMessageFail(message);
        raven.glasspanepopup.GlassPanePopup.showPopup(fail);
    }

    public void showMessageConfirm(String message, Runnable onConfirmAction) {
        DialogConfirm confirm = new DialogConfirm(message);
        confirm.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                if (onConfirmAction != null) {
                    onConfirmAction.run(); // Thực hiện hành động truyền vào
                }
                raven.glasspanepopup.GlassPanePopup.closePopup("confirm");
            }

            @Override
            public void onCancel() {

            }
        });
        raven.glasspanepopup.GlassPanePopup.showPopup(confirm, "confirm"); // Hiển thị popup
    }

    public void showInputDialog(int amount) {
        DialogInput input = new DialogInput(amount);
        input.setConfirmListener(new ConfirmListenerInput() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int amount) {
            }
        });
        raven.glasspanepopup.GlassPanePopup.showPopup(input, "input");
    }
    //</editor-fold>

    private boolean check() {
        String phone = txtPhoneNumber.getText().trim();

        if (phone.isEmpty()) {
            showMessageFail("Không được để trống số điện thoại!");
            return false;
        }

        // Kiểm tra số điện thoại chỉ chứa số và có độ dài phù hợp (ví dụ: 10 số)
        if (!phone.matches("\\d{10}")) {
            showMessageFail("Số điện thoại không hợp lệ! Vui lòng nhập 10 chữ số.");
            return false;
        }

        if (!rdMale.isSelected() && !rdFemale.isSelected()) {
            showMessageFail("Vui lòng chọn giới tính!");
            return false;
        }

        return true;
    }

    private Customers readForm() {
        Customers c = new Customers();
        c.setCustomerCode("C" + Ultil.generateRandomCode());
        c.setCustomerName(txtName.getText());
        c.setPhoneNumber(txtPhoneNumber.getText());
        c.setGender(rdMale.isSelected());
        return c;
    }

    private void insertCustomer() {
        if (!check()) {
            return;
        }
        if (customerDAO.isPhoneNumberExists(txtPhoneNumber.getText())) {
            showMessageFail("Số điện thoại này đã tồn tại!!");
            return;
        }
        if (customerDAO.insertCustomerNew(readForm())) {
            showMessageSuccess("Thêm thành công");
        } else {
            showMessageFail("Thêm thất bại");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        txtName = new com.petshop.swing.textfield.TextField1();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPhoneNumber = new com.petshop.swing.textfield.TextField1();
        btnAdd = new com.petshop.swing.Button1();
        btnCanel = new com.petshop.swing.Button1();
        rdMale = new com.petshop.swing.radio_button.RadioButtonCustom();
        rdFemale = new com.petshop.swing.radio_button.RadioButtonCustom();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("NHẬP THÔNG TIN KHÁCH HÀNG");

        jLabel2.setText("Tên khách hàng");

        jLabel3.setText("Số điện thoại");

        btnAdd.setBackground(new java.awt.Color(153, 255, 153));
        btnAdd.setText("Thêm mới");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnCanel.setBackground(new java.awt.Color(255, 153, 153));
        btnCanel.setText("Hủy");
        btnCanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanelActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdMale);
        rdMale.setText("Nam");

        buttonGroup1.add(rdFemale);
        rdFemale.setText("Nữ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtPhoneNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCanel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdMale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdFemale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 210, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdMale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdFemale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCanelActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận thêm mới?", () -> {
            insertCustomer();
        });
    }//GEN-LAST:event_btnAddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button1 btnAdd;
    private com.petshop.swing.Button1 btnCanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.petshop.swing.radio_button.RadioButtonCustom rdFemale;
    private com.petshop.swing.radio_button.RadioButtonCustom rdMale;
    private com.petshop.swing.textfield.TextField1 txtName;
    private com.petshop.swing.textfield.TextField1 txtPhoneNumber;
    // End of variables declaration//GEN-END:variables
}
