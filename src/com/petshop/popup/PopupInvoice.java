/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
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
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author duat
 */
public class PopupInvoice extends javax.swing.JPanel {

    /**
     * Creates new form PopupInvoices
     */
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final InvoiceDAO invoiceDAO;

    public void setInvoiceCode(String code) {
        txtInvoiceCode.setText(code);
        loadFormInvoice();
    }

    public PopupInvoice() {
        initComponents();
        setOpaque(false);
        invoiceDAO = new InvoiceDAO();
        invoiceDetailDAO = new InvoiceDetailDAO();
        txtInvoiceCode.setEditable(false);
        txtCustomer.setEditable(false);
        txtEmployee.setEditable(false);
        txtTotalPrice.setEditable(false);
        txtPaymentStatus.setEditable(false);
        tbInvoiceDetail.fixTable(jScrollPane1);
    }

    private void loadFormInvoice() {
        Invoices i = invoiceDAO.searchInvoiceByCodeResultModel(txtInvoiceCode.getText());
        txtCustomer.setText(i.getCustomer().getCustomerName() == null ? i.getCustomer().getPhoneNumber() : i.getCustomer().getCustomerName());
        txtEmployee.setText(i.getEmployee().getEmployeeName());
        txtTotalPrice.setText(Ultil.formatCurrency(i.getTotalPrice()));
        txtCostsIncurred.setText(Ultil.formatCurrency(i.getCostsIncurred()));
        txtPaymentStatus.setText(i.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán");
        txtNotes.setText(i.getNote());
        List<InvoiceDetails> list = invoiceDetailDAO.getInvoiceDetailsByInvoiceId(i.getId());
        getListInvoiceDetail(list);
    }

    public void getListInvoiceDetail(List<InvoiceDetails> list) {
        int stt = 1;
        tbInvoiceDetail.setRowCount(0);
        for (InvoiceDetails i : list) {
            String name = (i.getProductDetail() != null && i.getProductDetail().getProductDetailName() != null)
                    ? i.getProductDetail().getProductDetailName()
                    : (i.getPetService() != null ? i.getPetService().getServiceName() : "N/A");

            String code = (i.getProductDetail() != null && i.getProductDetail().getProductDetailCode() != null)
                    ? i.getProductDetail().getProductDetailCode()
                    : (i.getPetService() != null ? i.getPetService().getServiceCode() : "N/A");
            String petName = (i.getPet() != null) ? i.getPet().getPetName() : "N/A";

            BigDecimal price = (i.getProductDetail() != null && i.getProductDetail().getPrice() != null)
                    ? i.getProductDetail().getPrice()
                    : (i.getPetService() != null && i.getPetService().getPriceService() != null)
                    ? i.getPetService().getPriceService()
                    : BigDecimal.ZERO; // Nếu không có giá thì để là 0

            tbInvoiceDetail.addRow(new Object[]{
                stt,
                code,
                name,
                i.getUsageOrQuantity(),
                Ultil.formatCurrency(price),
                Ultil.formatCurrency(i.getTotalPrice()),
                petName == null ? "N/A" : petName,
                i.isTypeInvoiceDetail() ? "Dịch vụ" : "Sản phẩm"
            });
            stt++;
        }
    }

    public void updateInvoice() {
        // Tìm hóa đơn theo mã nhập vào từ txtInvoiceCode
        Invoices i = invoiceDAO.searchInvoiceByCodeResultModel(txtInvoiceCode.getText());

        if (i != null) {
            try {
                // Chuyển đổi chi phí phát sinh sang BigDecimal
                BigDecimal costsIncurred = new BigDecimal(
                        txtCostsIncurred.getText()
                                .replace("₫", "")
                                .replace(".", "")
                                .replace("\u00A0", "")
                                .replaceAll("\\s+", "")
                                .trim()
                );
                String note = txtNotes.getText().trim();

                // Lấy totalPrice hiện tại từ txtTotalPrice và chuyển đổi sang BigDecimal
                BigDecimal totalPrice = new BigDecimal(
                        txtTotalPrice.getText()
                                .replace("₫", "")
                                .replace(".", "")
                                .replace("\u00A0", "")
                                .replaceAll("\\s+", "")
                                .trim()
                );

                // Cập nhật totalPrice bằng totalPrice + costsIncurred
                BigDecimal newTotalPrice = totalPrice.add(costsIncurred);

                // Cập nhật hóa đơn với chi phí phát sinh, totalPrice mới và ghi chú
                invoiceDAO.updateInvoiceByCostsAndNote(i.getId(), costsIncurred, newTotalPrice, note);

                showMessageSuccess("Cập nhật hóa đơn thành công!");
            } catch (NumberFormatException | NullPointerException e) {
                showMessageFail("Chi phí phát sinh phải là số hợp lệ!");
            }
        } else {
            showMessageFail("Không tìm thấy hóa đơn!");
        }
        raven.glasspanepopup.GlassPanePopup.closePopup("pInvoice");
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoiceDetail = new com.petshop.swing.table.Table();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtInvoiceCode = new com.petshop.swing.textfield.TextField1();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCustomer = new com.petshop.swing.textfield.TextField1();
        jLabel4 = new javax.swing.JLabel();
        txtEmployee = new com.petshop.swing.textfield.TextField1();
        jLabel5 = new javax.swing.JLabel();
        txtTotalPrice = new com.petshop.swing.textfield.TextField1();
        jLabel6 = new javax.swing.JLabel();
        txtCostsIncurred = new com.petshop.swing.textfield.TextField1();
        textAreaScroll1 = new com.petshop.swing.textarea.TextAreaScroll();
        txtNotes = new com.petshop.swing.textarea.TextArea();
        btnConfirm = new com.petshop.swing.Button1();
        btnCancel = new com.petshop.swing.Button1();
        jLabel7 = new javax.swing.JLabel();
        txtPaymentStatus = new com.petshop.swing.textfield.TextField1();

        setBackground(new java.awt.Color(255, 255, 255));

        tbInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã SP/DV", "Tên SP/DV", "SL", "Giá", "Tổng tiền", "Thông tin khác", "Loại hóa đơn"
            }
        ));
        jScrollPane1.setViewportView(tbInvoiceDetail);
        if (tbInvoiceDetail.getColumnModel().getColumnCount() > 0) {
            tbInvoiceDetail.getColumnModel().getColumn(0).setMinWidth(35);
            tbInvoiceDetail.getColumnModel().getColumn(0).setMaxWidth(35);
            tbInvoiceDetail.getColumnModel().getColumn(1).setMinWidth(70);
            tbInvoiceDetail.getColumnModel().getColumn(1).setMaxWidth(70);
            tbInvoiceDetail.getColumnModel().getColumn(2).setMinWidth(230);
            tbInvoiceDetail.getColumnModel().getColumn(2).setMaxWidth(230);
            tbInvoiceDetail.getColumnModel().getColumn(3).setMinWidth(35);
            tbInvoiceDetail.getColumnModel().getColumn(3).setMaxWidth(35);
            tbInvoiceDetail.getColumnModel().getColumn(4).setMinWidth(80);
            tbInvoiceDetail.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thông tin hóa đơn");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setText("Mã HD:");

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setText("Thông tin KH:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel4.setText("Thông tin NV:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setText("Thành tiền");

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel6.setText("Phí phát sinh:");

        textAreaScroll1.setLabelText("Ghi chú");

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        textAreaScroll1.setViewportView(txtNotes);

        btnConfirm.setBackground(new java.awt.Color(204, 255, 204));
        btnConfirm.setText("Cập nhập");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 204, 204));
        btnCancel.setText("Đóng");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel7.setText("Trạng thái thanh toán:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtInvoiceCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCostsIncurred, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtPaymentStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(txtInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(0, 0, 0)
                .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(0, 0, 0)
                .addComponent(txtEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(0, 0, 0)
                .addComponent(txtCostsIncurred, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 821, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        raven.glasspanepopup.GlassPanePopup.closePopup("pInvoice");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận cập nhập hóa đơn?", () -> {
            updateInvoice();
        });
    }//GEN-LAST:event_btnConfirmActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button1 btnCancel;
    private com.petshop.swing.Button1 btnConfirm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.petshop.swing.table.Table tbInvoiceDetail;
    private com.petshop.swing.textarea.TextAreaScroll textAreaScroll1;
    private com.petshop.swing.textfield.TextField1 txtCostsIncurred;
    private com.petshop.swing.textfield.TextField1 txtCustomer;
    private com.petshop.swing.textfield.TextField1 txtEmployee;
    private com.petshop.swing.textfield.TextField1 txtInvoiceCode;
    private com.petshop.swing.textarea.TextArea txtNotes;
    private com.petshop.swing.textfield.TextField1 txtPaymentStatus;
    private com.petshop.swing.textfield.TextField1 txtTotalPrice;
    // End of variables declaration//GEN-END:variables
}
