/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.EmployeeDAO;
import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.daos.ReturnInvoiceDAO;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Employees;
import com.petshop.models.Invoices;
import com.petshop.models.Products;
import com.petshop.models.ReturnInvoices;
import com.petshop.popup.PopupReturn;
import com.petshop.services.RememberMeService;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class ReturnManagement extends javax.swing.JPanel {

    /**
     * Creates new form ReturnManagement
     */
    private final InvoiceDAO invoiceDAO;
    private final ReturnInvoiceDAO returnInvoiceDAO;
    private final RememberMeService rememberMeService;
    private final EmployeeDAO employeeDAO;

    public ReturnManagement() {
        initComponents();
        txtDateEnd.setText("Chọn ngày");
        txtDateStart.setText("Chọn ngày");
        invoiceDAO = new InvoiceDAO();
        returnInvoiceDAO = new ReturnInvoiceDAO();
        rememberMeService = new RememberMeService();
        employeeDAO = new EmployeeDAO();
        getListInvoices(invoiceDAO.getListInvoiceWithDetails());
        tbInvoice.fixTable(jScrollPane1);
        searchEventMaHD();
        searchEventKhachHang();
        searchEventTenSanPham();
        searchEventMaSanPham();
    }

    public void getListInvoices(List<Invoices> list) {
        int stt = 1;
        tbInvoice.setRowCount(0); // Xóa dữ liệu cũ

        for (Invoices i : list) {
            tbInvoice.addRow(new Object[]{
                stt,
                i.getInvoiceCode() != null ? i.getInvoiceCode() : "Chưa có thông tin",
                i.getEmployee() != null && i.getEmployee().getEmployeeName() != null ? i.getEmployee().getEmployeeName() : "Chưa có thông tin",
                (i.getCustomer() != null)
                ? (i.getCustomer().getCustomerName() != null && !i.getCustomer().getCustomerName().trim().isEmpty()
                ? i.getCustomer().getCustomerName()
                : (i.getCustomer().getPhoneNumber() != null && !i.getCustomer().getPhoneNumber().trim().isEmpty()
                ? i.getCustomer().getPhoneNumber()
                : "Chưa có thông tin"))
                : "Chưa có thông tin",
                i.getCreatedAt() != null ? Ultil.getFormattedCreatedAt(i.getCreatedAt()) : "Chưa có thông tin",
                i.getTotalPrice() != null ? Ultil.formatCurrency(i.getTotalPrice()) : "Chưa có thông tin",
                i.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",
                new ModelAction<>(i, new EventAction<Invoices>() {
                    @Override
                    public void delete(Invoices i) {
                    }

                    @Override
                    public void update(Invoices i) {
                    }

                    @Override
                    public void add(Invoices i) {
                        // Lấy return_invoice theo invoiceId
                        ReturnInvoices r = returnInvoiceDAO.getReturnInvoicesByInvoiceIdModel(i.getId());
                        if (r == null) {
                            String newCode = "HDT" + Ultil.generateRandomCode();
                            createReturnInvoice(i, newCode);
                            showPopreturn(i.getInvoiceCode(), newCode);
                        } else {
                            // Hiển thị return_invoice_code nếu có
                            showPopreturn(i.getInvoiceCode(), r.getReturnInvoiceCode());
                        }
                    }
                })
            });
            stt++;
        }
    }

    private void showPopreturn(String invoiceCode, String returnInvoiceCode) {
        PopupReturn pop = new PopupReturn();
        pop.setInvoiceCode(invoiceCode);
        pop.setReturnInvoiceCode(returnInvoiceCode);
        GlassPanePopup.showPopup(pop, "pInvoice");
    }

    private void createReturnInvoice(Invoices i, String newReturnInvoiceCode) {
        Invoices ic = invoiceDAO.getInvoiceById(i.getId());
        // Lấy username của nhân viên đang đăng nhập
        int idEmployee = rememberMeService.getEmployeeId();
        Employees e = employeeDAO.findEmployeeById(idEmployee);
        
        ReturnInvoices r = new ReturnInvoices();
        r.setReturnInvoiceCode(newReturnInvoiceCode);
        r.setEmployees(e);
        r.setCustomers(ic.getCustomer());
        r.setInvoices(ic);
        r.setCostsIncurred(new BigDecimal(0));
        r.setPaymentMethod(true);
        r.setPaymentStatus(true);
        r.setNote("");
        r.setTotalPrice(new BigDecimal(0));
        r.setTotalPriceReturn(new BigDecimal(0));
        returnInvoiceDAO.insertReturnInvoice(r);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new com.petshop.swing.datechooser.DateChooser();
        dateChooser2 = new com.petshop.swing.datechooser.DateChooser();
        materialTabbed1 = new com.petshop.swing.tabbed.MaterialTabbed();
        jPanel1 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtSearchMaHd = new com.petshop.swing.textfield.TextFieldAnimation();
        txtSearchKhachHang = new com.petshop.swing.textfield.TextFieldAnimation();
        txtSerchMaSP = new com.petshop.swing.textfield.TextFieldAnimation();
        txtSerchTenSP = new com.petshop.swing.textfield.TextFieldAnimation();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoice = new com.petshop.swing.tableMore.TableMore5();
        jLabel3 = new javax.swing.JLabel();
        lblKhachHang7 = new javax.swing.JLabel();
        btnDeleteHistory6 = new com.petshop.swing.Button();
        btnDeleteHistory4 = new com.petshop.swing.Button();
        btnDeleteHistory7 = new com.petshop.swing.Button();
        btnDeleteHistory5 = new com.petshop.swing.Button();
        txtDateEnd = new com.petshop.swing.textfield.TextField1();
        btnLoc = new com.petshop.swing.Button();
        txtDateStart = new com.petshop.swing.textfield.TextField1();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();

        dateChooser1.setTextRefernce(txtDateEnd);

        dateChooser2.setTextRefernce(txtDateStart);

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(204, 255, 255));
        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tìm kiếm");

        txtSearchMaHd.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchMaHd.setHintText("Theo mã hoá đơn");
        txtSearchMaHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchMaHdActionPerformed(evt);
            }
        });

        txtSearchKhachHang.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchKhachHang.setHintText("Theo khách hàng hoặc SĐT");
        txtSearchKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchKhachHangActionPerformed(evt);
            }
        });

        txtSerchMaSP.setBackground(new java.awt.Color(250, 250, 250));
        txtSerchMaSP.setHintText("Theo mã sản phẩm");
        txtSerchMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerchMaSPActionPerformed(evt);
            }
        });

        txtSerchTenSP.setBackground(new java.awt.Color(250, 250, 250));
        txtSerchTenSP.setHintText("Theo tên sản phẩm");
        txtSerchTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerchTenSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtSearchKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSearchMaHd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSerchMaSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSerchTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchMaHd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSearchKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSerchMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(txtSerchTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tbInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã HD", "Thông tin NV", "Thông tin KH", "Ngày tạo", "Tổng tiền", "Trạng thái TT", "Thao tác"
            }
        ));
        jScrollPane1.setViewportView(tbInvoice);

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Danh sách hóa đơn");

        lblKhachHang7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang7.setText("...");

        btnDeleteHistory6.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory6.setText(">");
        btnDeleteHistory6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistory6ActionPerformed(evt);
            }
        });

        btnDeleteHistory4.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory4.setText("<<");
        btnDeleteHistory4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistory4ActionPerformed(evt);
            }
        });

        btnDeleteHistory7.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory7.setText(">>");
        btnDeleteHistory7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistory7ActionPerformed(evt);
            }
        });

        btnDeleteHistory5.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory5.setText("<");
        btnDeleteHistory5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistory5ActionPerformed(evt);
            }
        });

        txtDateEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateEndActionPerformed(evt);
            }
        });

        btnLoc.setBackground(new java.awt.Color(204, 204, 255));
        btnLoc.setText("Lọc");
        btnLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Từ:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Đến:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel33)
                                .addGap(0, 0, 0)
                                .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(jLabel34)
                                .addGap(0, 0, 0)
                                .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(btnDeleteHistory4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteHistory5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(lblKhachHang7)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteHistory6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteHistory7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 183, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteHistory4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteHistory5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKhachHang7)
                    .addComponent(btnDeleteHistory6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteHistory7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        materialTabbed1.addTab("Đổi trả", jPanel1);

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

    private void txtSearchMaHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMaHdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMaHdActionPerformed

    private void txtSearchKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchKhachHangActionPerformed

    private void txtSerchMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerchMaSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerchMaSPActionPerformed

    private void txtSerchTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerchTenSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerchTenSPActionPerformed

    private void btnDeleteHistory6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory6ActionPerformed

    private void btnDeleteHistory4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory4ActionPerformed

    private void btnDeleteHistory7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory7ActionPerformed

    private void btnDeleteHistory5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory5ActionPerformed

    private void txtDateEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateEndActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateEndActionPerformed

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        searchInvoiceByDateRange();
    }//GEN-LAST:event_btnLocActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnDeleteHistory4;
    private com.petshop.swing.Button btnDeleteHistory5;
    private com.petshop.swing.Button btnDeleteHistory6;
    private com.petshop.swing.Button btnDeleteHistory7;
    private com.petshop.swing.Button btnLoc;
    private com.petshop.swing.datechooser.DateChooser dateChooser1;
    private com.petshop.swing.datechooser.DateChooser dateChooser2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKhachHang7;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.tableMore.TableMore5 tbInvoice;
    private com.petshop.swing.textfield.TextField1 txtDateEnd;
    private com.petshop.swing.textfield.TextField1 txtDateStart;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchKhachHang;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchMaHd;
    private com.petshop.swing.textfield.TextFieldAnimation txtSerchMaSP;
    private com.petshop.swing.textfield.TextFieldAnimation txtSerchTenSP;
    // End of variables declaration//GEN-END:variables

    private void searchEventMaHD() {
        txtSearchMaHd.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchInvoiceByCode(txtSearchMaHd.getText());
                    txtSearchMaHd.setText("");
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void searchEventKhachHang() {
        txtSearchKhachHang.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchInvoiceByCustomer();
                    txtSearchKhachHang.setText("");
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void searchEventTenSanPham() {
        txtSerchTenSP.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchInvoiceByProductName();
                    txtSerchTenSP.setText("");
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void searchEventMaSanPham() {
        txtSerchMaSP.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchInvoiceByProductCode();
                    txtSerchMaSP.setText("");
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void searchInvoiceByCode(String keyword) {
        List<Invoices> list = invoiceDAO.searchInvoiceByCode(keyword);
        getListInvoices(list);
    }

    private void searchInvoiceByCustomer() {
        String keyword = txtSearchKhachHang.getText().trim();
        if (!keyword.isEmpty()) {
//            List<Invoices> list = invoiceDAO.searchInvoiceByCustomer(keyword);
//            getListInvoices(list);
        }
    }

    private void searchInvoiceByProductName() {
        String keyword = txtSerchTenSP.getText().trim();
        if (!keyword.isEmpty()) {
//            List<Invoices> list = invoiceDAO.searchInvoiceByProductName(keyword);
//            getListInvoices(list);
        }
    }

    private void searchInvoiceByProductCode() {
        String keyword = txtSerchMaSP.getText().trim();
        if (!keyword.isEmpty()) {
//            List<Invoices> list = invoiceDAO.searchInvoiceByProductCode(keyword);
//            getListInvoices(list);
        }
    }

    private void searchInvoiceByDateRange() {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Chuyển đổi ngày từ dd/MM/yyyy sang yyyy-MM-dd dưới dạng String
            LocalDate startDate = LocalDate.parse(txtDateStart.getText(), inputFormatter);
            LocalDate endDate = LocalDate.parse(txtDateEnd.getText(), inputFormatter);

            String formattedStartDate = startDate.format(outputFormatter);
            String formattedEndDate = endDate.format(outputFormatter);

            List<Invoices> list = invoiceDAO.searchInvoiceByDate(formattedStartDate, formattedEndDate);
            getListInvoices(list);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng ngày (dd/MM/yyyy)", "Lỗi định dạng ngày", JOptionPane.ERROR_MESSAGE);
        }
    }

}
