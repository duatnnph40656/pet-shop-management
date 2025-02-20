/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class InvoiceManagement extends javax.swing.JPanel {

    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;

    public InvoiceManagement() {
        initComponents();
<<<<<<< HEAD
        this.invoicesRepo = new InvoiceDAO();
        this.invoicesDetailRepo = new InvoiceDetailDAO();
        this.loadTable();
        this.loadTable_InvoiceDetail();
        txtSerch.addEvent(new EventTextField() {
            @Override
            public void onPressed(EventCallBack call) {
                //Test
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(5); //Time Sleep
                    }
                    searchInvoices(txtSerch.getText());
                    txtSerch.setText("");
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
=======
        tbInvoice.fixTable(jScrollPane1);
        tbInvoiceDetail.fixTable(jScrollPane3);
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceDetailDAO = new InvoiceDetailDAO();

        init();
    }

    private void init() {
        getListInvoice(invoiceDAO.getListInvoiceAll());
    }

    //<editor-fold defaultstate="collapsed" desc="{Message...">
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

    public void showMessageConfirm(String message, Runnable onConfirmAction) {
        DialogConfirm confirm = new DialogConfirm(message);
        confirm.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                if (onConfirmAction != null) {
                    onConfirmAction.run(); // Thực hiện hành động truyền vào
                }
                GlassPanePopup.closePopup("confirm");
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
            }

            @Override
            public void onCancel() {
<<<<<<< HEAD
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

        });
=======

            }
        });
        GlassPanePopup.showPopup(confirm, "confirm"); // Hiển thị popup
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
        GlassPanePopup.showPopup(input, "input");
    }
    //</editor-fold>

    private void getListInvoice(List<Invoices> list) {
        int stt = 1;
        tbInvoice.setRowCount(0);
        for (Invoices i : list) {
            tbInvoice.addRow(new Object[]{
                i.getId(),
                stt,
                i.getInvoiceCode() != null ? i.getInvoiceCode() : "Chưa có thông tin",
                i.getCreatedAt() != null ? Ultil.getFormattedCreatedAt(i.getCreatedAt()) : "Chưa có thông tin",
                i.getTotalPrice() != null ? Ultil.formatCurrency(i.getTotalPrice()) : "Chưa có thông tin",
                i.getEmployee() != null && i.getEmployee().getEmployeeName() != null ? i.getEmployee().getEmployeeName() : "Chưa có thông tin",
                i.getCustomer() != null
                ? (i.getCustomer().getCustomerName() != null ? i.getCustomer().getCustomerName() : i.getCustomer().getPhoneNumber() != null ? i.getCustomer().getPhoneNumber() : "Chưa có thông tin")
                : "Chưa có thông tin",
                i.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",
                i.isPaymentMethod() ? "Tiền mặt" : "Thanh toán qua banking",
                i.getCostsIncurred() != null ? Ultil.formatCurrency(i.getCostsIncurred()) : "Không có",
                i.getNote() != null ? i.getNote() : "Chưa có thông tin"
            });
            stt++;
        }
    }

    private int getSelectedRowInvoice() {
        return tbInvoice.getSelectedRow();
    }

    private Integer getIdInvoice() {
        return (Integer) tbInvoice.getValueAt(getSelectedRowInvoice(), 0);
    }

    public void showInvoiceDetailByIdInvoice() {
        if (getSelectedRowInvoice() == -1) {
            showMessageFail("Bạn chưa chọn hóa đơn!!");// Kiểm tra xem có hàng nào được chọn không
            return;
        }

        int id = getIdInvoice(); // Lấy ID hóa đơn

        List<InvoiceDetails> list = invoiceDetailDAO.getInvoiceDetailsByInvoiceId(id);
        getListInvoiceDetail(list); // Load dữ liệu lên bảng
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
                i.getInvoiceDetailCode(),
                code,
                name,
                i.getUsageOrQuantity(),
                Ultil.formatCurrency(price),
                Ultil.formatCurrency(i.getTotalPrice()),
                petName == null ? "N/A" : petName,
                i.getFormattedCreatedAt()
            });
            stt++;
        }
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
<<<<<<< HEAD
    private void searchInvoices(String text) {
        String keyword = this.txtSerch.getText().trim();
        System.out.println(keyword);

        ArrayList<Invoices> ds = (ArrayList<Invoices>) this.invoicesRepo.search(keyword);
        DefaultTableModel dtm = (DefaultTableModel) this.tblHoaDon.getModel();
        int stt = 1;
        dtm.setRowCount(0);

        for (Invoices invoice : ds) {
            Object[] row = {
                invoice.getId(),
                stt,
                invoice.getInvoiceCode(),
                invoice.getCreatedAt(),
                invoice.getTotalPrice(),
                invoice.getEmployee() != null ? invoice.getEmployee().getEmployeeCode() : "N/A", // Lấy mã nhân viên
                invoice.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",
                invoice.isPaymentMethod() ? "Chuyển khoản" : "Tiền mặt",
                invoice.getNote(),
                invoice.getCostsIncurred()
            };

            stt++;
            dtm.addRow(row);
        }

        // Ẩn cột ID
        tblHoaDon.getColumnModel().getColumn(0).setMinWidth(0);
        tblHoaDon.getColumnModel().getColumn(0).setMaxWidth(0);
        tblHoaDon.getColumnModel().getColumn(0).setWidth(0);
    }

=======
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new com.petshop.swing.datechooser.DateChooser();
        dateChooser2 = new com.petshop.swing.datechooser.DateChooser();
        jPanel7 = new javax.swing.JPanel();
        lblKhachHang = new javax.swing.JLabel();
        txtSerch = new com.petshop.swing.textfield.TextFieldAnimation();
        lblKhachHang3 = new javax.swing.JLabel();
        lblKhachHang4 = new javax.swing.JLabel();
        btnDeleteHistory = new com.petshop.swing.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoice = new com.petshop.swing.table.Table();
        btnDeleteHistory1 = new com.petshop.swing.Button();
        btnDeleteHistory3 = new com.petshop.swing.Button();
        txtDateStart = new com.petshop.swing.textfield.TextField1();
        txtDateEnd = new com.petshop.swing.textfield.TextField1();
        combobox1 = new com.petshop.swing.combobox.Combobox();
        combobox2 = new com.petshop.swing.combobox.Combobox();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbInvoiceDetail = new com.petshop.swing.table.Table();
        jLabel2 = new javax.swing.JLabel();

        dateChooser1.setTextRefernce(txtDateStart);

        dateChooser2.setTextRefernce(txtDateEnd);

        setForeground(new java.awt.Color(204, 255, 255));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang.setText("Tìm kiếm hoá đơn: ");

        txtSerch.setBackground(new java.awt.Color(250, 250, 250));
        txtSerch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerchActionPerformed(evt);
            }
        });

        lblKhachHang3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang3.setText("Từ:");

        lblKhachHang4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang4.setText("Đến:");

        btnDeleteHistory.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory.setText("Lọc");
        btnDeleteHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistoryActionPerformed(evt);
            }
        });

        tbInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã HD", "Ngày Tạo ", "Thành tiền", "Thông tin NV", "Thông tin KH", "Trạng thái TT", "Hình thức TT", "Phí phát sinh", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInvoiceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbInvoice);
        if (tbInvoice.getColumnModel().getColumnCount() > 0) {
            tbInvoice.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoice.getColumnModel().getColumn(0).setMaxWidth(0);
            tbInvoice.getColumnModel().getColumn(1).setMinWidth(35);
            tbInvoice.getColumnModel().getColumn(1).setMaxWidth(35);
            tbInvoice.getColumnModel().getColumn(9).setResizable(false);
        }

        btnDeleteHistory1.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory1.setText("In hoá đơn");
        btnDeleteHistory1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistory1ActionPerformed(evt);
            }
        });

        btnDeleteHistory3.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory3.setText("Xuất danh sách ");
        btnDeleteHistory3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistory3ActionPerformed(evt);
            }
        });

        combobox1.setLabeText("Trạng thái thanh toán");

        combobox2.setLabeText("Lọc theo thời gian");

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hoá đơn");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
<<<<<<< HEAD
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblKhachHang)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(471, 471, 471))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblKhachHang1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblKhachHang2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbbTrangThai1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblKhachHang3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblKhachHang4)
                                .addGap(153, 153, 153)))
                        .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblKhachHang5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachHang6)
                        .addGap(55, 55, 55)
                        .addComponent(btnDeleteHistory4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteHistory5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(lblKhachHang7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(btnDeleteHistory6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteHistory7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
=======
                        .addComponent(combobox1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(combobox2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                        .addComponent(lblKhachHang3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachHang4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblKhachHang)
                        .addGap(5, 5, 5)
                        .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
                        .addComponent(btnDeleteHistory1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteHistory3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
<<<<<<< HEAD
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKhachHang)
                            .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKhachHang1)
                            .addComponent(cbbTrangThai1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKhachHang2)
                            .addComponent(lblKhachHang3)
                            .addComponent(lblKhachHang4))))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
=======
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSerch, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(lblKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(combobox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combobox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteHistory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteHistory3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã HDCT", "Mã SP/DV", "Tên SP/DV", "SL", "Giá bán/giá DV", "Tổng tiền", "Thông tin khác", "Ngày tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbInvoiceDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInvoiceDetailMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbInvoiceDetail);
        if (tbInvoiceDetail.getColumnModel().getColumnCount() > 0) {
            tbInvoiceDetail.getColumnModel().getColumn(0).setMinWidth(40);
            tbInvoiceDetail.getColumnModel().getColumn(0).setMaxWidth(40);
            tbInvoiceDetail.getColumnModel().getColumn(3).setMinWidth(230);
            tbInvoiceDetail.getColumnModel().getColumn(3).setMaxWidth(230);
            tbInvoiceDetail.getColumnModel().getColumn(4).setMinWidth(40);
            tbInvoiceDetail.getColumnModel().getColumn(4).setMaxWidth(40);
        }

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Hoá đơn chi tiết ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
<<<<<<< HEAD
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
=======
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1042, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbInvoiceDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceDetailMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbInvoiceDetailMouseClicked

    private void btnDeleteHistory3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory3ActionPerformed

    private void btnDeleteHistory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory1ActionPerformed

    private void tbInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceMouseClicked
        showInvoiceDetailByIdInvoice();
    }//GEN-LAST:event_tbInvoiceMouseClicked

    private void btnDeleteHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistoryActionPerformed

    }//GEN-LAST:event_btnDeleteHistoryActionPerformed

    private void txtSerchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnDeleteHistory;
    private com.petshop.swing.Button btnDeleteHistory1;
    private com.petshop.swing.Button btnDeleteHistory3;
    private com.petshop.swing.combobox.Combobox combobox1;
    private com.petshop.swing.combobox.Combobox combobox2;
    private com.petshop.swing.datechooser.DateChooser dateChooser1;
    private com.petshop.swing.datechooser.DateChooser dateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblKhachHang3;
    private javax.swing.JLabel lblKhachHang4;
    private com.petshop.swing.table.Table tbInvoice;
    private com.petshop.swing.table.Table tbInvoiceDetail;
    private com.petshop.swing.textfield.TextField1 txtDateEnd;
    private com.petshop.swing.textfield.TextField1 txtDateStart;
    private com.petshop.swing.textfield.TextFieldAnimation txtSerch;
    // End of variables declaration//GEN-END:variables

<<<<<<< HEAD
    private void loadTable() {
        int stt = 1;
        ArrayList<Invoices> ds = (ArrayList<Invoices>) this.invoicesRepo.getListInvoice();
        DefaultTableModel dtm = (DefaultTableModel) this.tblHoaDon.getModel();
        dtm.setRowCount(0);

        for (Invoices invoice : ds) {
            Object[] row = {
                invoice.getId(),
                stt,
                invoice.getInvoiceCode(),
                invoice.getCreatedAt(),
                invoice.getTotalPrice(),
                invoice.getEmployee() != null ? invoice.getEmployee().getEmployeeCode() : "N/A", // Lấy mã nhân viên
                invoice.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",
                invoice.isPaymentMethod() ? "Chuyển khoản" : "Tiền mặt",
                invoice.getNote(),
                invoice.getCostsIncurred()
            };

            stt++;
            dtm.addRow(row);
        }

        // Ẩn cột ID
        tblHoaDon.getColumnModel().getColumn(0).setMinWidth(0);
        tblHoaDon.getColumnModel().getColumn(0).setMaxWidth(0);
        tblHoaDon.getColumnModel().getColumn(0).setWidth(0);
    }

    private void loadTable_InvoiceDetail() {
        int stt = 1;
        ArrayList<InvoiceDetails> ds = (ArrayList<InvoiceDetails>) this.invoicesDetailRepo.getListInvoiceDetailProduct();
        DefaultTableModel dtm = (DefaultTableModel) this.tblHoaDonChiTiet.getModel();
        dtm.setRowCount(0);

        for (InvoiceDetails invoiceDetails : ds) {
            Object[] row = {
                invoiceDetails.getId(),
                stt,
                invoiceDetails.getInvoiceDetailCode(),
                invoiceDetails.getProductOrServiceCode(),
                invoiceDetails.getProductOrServiceName(),
                invoiceDetails.getPet() != null ? invoiceDetails.getPet().getPetCode() : "N/A",
                invoiceDetails.getUsageOrQuantity(),
                invoiceDetails.getPrice(),
                invoiceDetails.getTotal(),
                invoiceDetails.getDuration(),
                invoiceDetails.getCreatedAt()
            };

            stt++;
            dtm.addRow(row);
        }

        // Ẩn cột ID
        tblHoaDon.getColumnModel().getColumn(0).setMinWidth(0);
        tblHoaDon.getColumnModel().getColumn(0).setMaxWidth(0);
        tblHoaDon.getColumnModel().getColumn(0).setWidth(0);
    }

=======
>>>>>>> 2f3c1b27bbce710f97e5cd1a260b797aef437f51
}
