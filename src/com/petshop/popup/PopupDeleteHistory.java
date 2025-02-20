/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.petshop.daos.PetDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.form.PetManagement;
import com.petshop.models.CategoryProducts;
import com.petshop.models.Pets;
import com.petshop.models.TypePets;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.table.DefaultTableModel;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author admin
 */
public class PopupDeleteHistory extends javax.swing.JPanel {

    PetDAO petDao = new PetDAO();
    DefaultTableModel model = new DefaultTableModel();
    private ConfirmListener confirmListener;

    public void setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public PopupDeleteHistory() {
        setOpaque(false);
        initComponents();
        tblLichSuXoa.fixTable(jScrollPane1);
        model = (DefaultTableModel) tblLichSuXoa.getModel();
        btnKhoiPhuc.addActionListener((e) -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
        });
        btnTroVe.addActionListener((e) -> {
            if (confirmListener != null) {
                confirmListener.onCancel();
            }
        });
        fillToTable(petDao.getListHistoryDelete());
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

    private void fillToTable(List<Pets> list) {
        int index = 1;
        model.setRowCount(0);
        for (Pets pet : list) {
            model.addRow(new Object[]{
                pet.getId(),
                index,
                pet.getPetCode(),
                pet.getTypePet().getTypePetName(),
                pet.getBreed(),
                pet.getPetName(),
                pet.getAge(),
                pet.getWeight(),
                pet.getColor(),
                pet.isGender() ? "Đực" : "Cái",
                pet.isVaccinated() ? "Đã tiêm" : "Chưa tiêm",
                pet.getCustomer().getCustomerName(),
                pet.getOwner(),
                pet.getCreatedAt(),
                pet.isDeleted()? "Đang hoạt động" : "Không hoạt động"
            });
            index++;
        }

    }

    private void restore() {
        int selectRow = tblLichSuXoa.getSelectedRow();
         
        String  pet_code =  (String) tblLichSuXoa.getValueAt(selectRow, 2);
        if (petDao.restore(pet_code)) {
            fillToTable(petDao.getListHistoryDelete());
            
            showMessageSuccess("Khôi phục dữ liệu thành công!");
        }else{
            showMessageFail("Khôi phục dữ liệu thất bại!");
        }
    }

    private void showMessageSuccess(String message) {
        DialogMessageSuccess success = new DialogMessageSuccess(message);
        GlassPanePopup.showPopup(success);
    }

    private void showMessageError(String message) {
        DialogMessageError error = new DialogMessageError(message);
        GlassPanePopup.showPopup(error);
    }

    private void showMessageFail(String message) {
        DialogMessageFail fail = new DialogMessageFail(message);
        GlassPanePopup.showPopup(fail);
    }
    
    public void showMessageConfirm(String message, Runnable onRunnable) {
        DialogConfirm confirm = new DialogConfirm(message);
        confirm.setConfirmListener(new com.petshop.event.ConfirmListener() {
            @Override
            public void onConfirm() {
                if (onRunnable != null) {
                    onRunnable.run();

                }
                GlassPanePopup.closePopup("confirm");
            }

            @Override
            public void onCancel() {

            }
        });
        GlassPanePopup.showPopup(confirm, "confirm");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblLichSuXoa = new com.petshop.swing.table.Table();
        btnKhoiPhuc = new com.petshop.swing.Button();
        jLabel3 = new javax.swing.JLabel();
        btnTroVe = new com.petshop.swing.Button();

        tblLichSuXoa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã", "Loài", "Giống", "Tên thú cưng", "Tuổi", "Cân nặng", "Màu sắc", "Giới tính", "Vaccine", "Tên khách", "Thông tin chủ sỡ hữu", "Ngày xóa"
            }
        ));
        jScrollPane1.setViewportView(tblLichSuXoa);
        if (tblLichSuXoa.getColumnModel().getColumnCount() > 0) {
            tblLichSuXoa.getColumnModel().getColumn(0).setMinWidth(0);
            tblLichSuXoa.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblLichSuXoa.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        btnKhoiPhuc.setBackground(new java.awt.Color(204, 255, 255));
        btnKhoiPhuc.setText("Khôi phục");
        btnKhoiPhuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoiPhucActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("LỊCH SỬ XÓA");

        btnTroVe.setBackground(new java.awt.Color(255, 204, 204));
        btnTroVe.setText("Trở về");
        btnTroVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTroVeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTroVe, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnKhoiPhuc, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1023, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTroVe, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhoiPhuc, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnKhoiPhucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoiPhucActionPerformed
        int selectRow = tblLichSuXoa.getSelectedRow();
        if (selectRow == -1) {
            showMessageError("Chưa có dữ liệu nào được chọn!");
            return;
        }
        showMessageConfirm("Bạn có muốn khôi phục không?", () -> {
            restore();
        });
    }//GEN-LAST:event_btnKhoiPhucActionPerformed

    private void btnTroVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTroVeActionPerformed
        GlassPanePopup.closePopupLast();
    }//GEN-LAST:event_btnTroVeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnKhoiPhuc;
    private com.petshop.swing.Button btnTroVe;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.petshop.swing.table.Table tblLichSuXoa;
    // End of variables declaration//GEN-END:variables
}
