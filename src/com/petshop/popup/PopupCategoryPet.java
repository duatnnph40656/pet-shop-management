/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.petshop.daos.TypePetDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.models.Products;
import com.petshop.models.TypePets;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.popup.GlassPanePopup;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dut
 */
public class PopupCategoryPet extends javax.swing.JPanel {

    /**
     * Creates new form PopupCategoryPet
     */
    private final TypePetDAO tDao;
    private DefaultTableModel model;

    private ConfirmListener listener;

    // Đăng ký ConfirmListener
    public void setConfirmListener(ConfirmListener listener) {
        this.listener = listener;
    }

    public PopupCategoryPet() {
        initComponents();
        tbTypePet.fixTable(jProduct);

        tDao = new TypePetDAO();
        model = new DefaultTableModel();

        this.getListTypePet(tDao.getListTypePet());

        txtCode.setText("TP" + Ultil.generateRandomCode());
        
        btnThem.addActionListener(evt -> {
            if (listener != null) {
                listener.onConfirm();
            }
            
        });
        btnHuy.addActionListener(evt -> {
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

    public void getListTypePet(List<TypePets> list) {
        int stt = 1;
        tbTypePet.setRowCount(0);

        for (TypePets t : list) {
            ModelAction<TypePets> actionData = new ModelAction<>(t,
                    new EventAction<TypePets>() {
                @Override
                public void delete(TypePets typePet) {
                    showMessageConfirm("Xác nhận xóa!", () -> {
                        deleteTypePet();
                    });
                }

                @Override
                public void update(TypePets typePet) {

                }

                @Override
                public void add(TypePets model) {
                }
            }
            );
            if (t.isStatus()) {
                tbTypePet.addRow(new Object[]{
                    t.getId(),
                    stt++,
                    t.getTypePetCode(),
                    t.getTypePetName(),
                    t.getCreatedAt(),
                    t.isStatus() ? "Hoạt động" : "Không hoạt động",
                    actionData
                });
                stt++;
            }
        }
        // Ẩn cột ID
        tbTypePet.getColumnModel()
                .getColumn(0).setMinWidth(0); // Giả sử cột ID là cột 1
        tbTypePet.getColumnModel()
                .getColumn(0).setMaxWidth(0);
        tbTypePet.getColumnModel()
                .getColumn(0).setWidth(0);
    }

    public boolean checkT() {
        if(tDao.isTypePetNameExists(txtName.getText())){
            showMessageFail("Tên loại thú cưng đã tồn tại");
            return false;
        }
        if (txtCode.getText().isEmpty()) {
            showMessageError("Mã không được để trống!!");
            return false;
        } else if (txtName.getText().isEmpty()) {
            showMessageFail("Tên trống");
            return false;
        }
        return true;
    }

    public void resetForm() {
        txtCode.setText("TP" + Ultil.generateRandomCode());
        txtName.setText("");
    }

    public void insertTypePet() {
        if (!checkT()) {
            return;
        }
        String tCode = txtCode.getText();
        String tName = txtName.getText();
        TypePets t = new TypePets(tCode, tName, false, true);
        if (tDao.insertTypePet(t)) {
            showMessageSuccess("Thêm thành công");
            this.getListTypePet(tDao.getListTypePet());
            this.resetForm();
        } else {
            this.showMessageFail("Thêm thất bại");
        }

    }

    public void deleteTypePet() {
        int selectedRow = tbTypePet.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tbTypePet.getValueAt(selectedRow, 0);
            tDao.deleteTypePet(id);
            showMessageSuccess("Xóa thành công");
            this.getListTypePet(tDao.getListTypePet());
        } else {
            showMessageFail("Chưa chọn thông tin để xóa!!");
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

        materialTabbed1 = new com.petshop.swing.tabbed.MaterialTabbed();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnHuy = new com.petshop.swing.popup.Button();
        btnThem = new com.petshop.swing.popup.Button();
        txtCode = new com.petshop.swing.textfield.TextField1();
        txtName = new com.petshop.swing.textfield.TextField1();
        jPanel1 = new javax.swing.JPanel();
        jProduct = new javax.swing.JScrollPane();
        tbTypePet = new com.petshop.swing.table.Table();

        setBackground(new java.awt.Color(255, 255, 255));

        materialTabbed1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Tên loại thú cưng");

        jLabel2.setText("Mã loại thú cưng");

        btnHuy.setBackground(new java.awt.Color(255, 204, 204));
        btnHuy.setText("Hủy");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(204, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        txtCode.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 325, Short.MAX_VALUE)
                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 49, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        materialTabbed1.addTab("Tạo mới", jPanel2);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tbTypePet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "STT", "Mã L.Thú Cưng", "Tên L.Thú Cưng", "Ngày tạo", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jProduct.setViewportView(tbTypePet);
        if (tbTypePet.getColumnModel().getColumnCount() > 0) {
            tbTypePet.getColumnModel().getColumn(1).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addContainerGap())
        );

        materialTabbed1.addTab("Danh sách", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        raven.glasspanepopup.GlassPanePopup.closePopupLast();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận thêm mới!!", () -> {
            insertTypePet();
        });
    }//GEN-LAST:event_btnThemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.popup.Button btnHuy;
    private com.petshop.swing.popup.Button btnThem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jProduct;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.table.Table tbTypePet;
    private com.petshop.swing.textfield.TextField1 txtCode;
    private com.petshop.swing.textfield.TextField1 txtName;
    // End of variables declaration//GEN-END:variables
}
