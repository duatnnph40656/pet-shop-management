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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import raven.glasspanepopup.GlassPanePopup;

/**
 * @author dut
 */
public class InvoiceManagement extends javax.swing.JPanel {

    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;

    public InvoiceManagement() {
        initComponents();
        tbInvoice.fixTable(jScrollPane1);
        tbInvoiceDetail.fixTable(jScrollPane3);
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceDetailDAO = new InvoiceDetailDAO();
        init();
    }

    private void init() {
        getListInvoice(invoiceDAO.getListInvoiceAll());
        loadFilters();
        searchEvent();
    }

    private void searchEvent() {
        txtSearch.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchInvoiceByCode(txtSearch.getText());
                    txtSearch.setText("");
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
            }

            @Override
            public void onCancel() {

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
                (i.getCustomer() != null)
                ? (i.getCustomer().getCustomerName() != null && !i.getCustomer().getCustomerName().trim().isEmpty()
                ? i.getCustomer().getCustomerName()
                : (i.getCustomer().getPhoneNumber() != null && !i.getCustomer().getPhoneNumber().trim().isEmpty()
                ? i.getCustomer().getPhoneNumber()
                : "Chưa có thông tin"))
                : "Chưa có thông tin",
                i.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",
                i.isPaymentMethod() ? "Tiền mặt" : "Thanh toán qua banking",
                i.getCostsIncurred() != null ? Ultil.formatCurrency(i.getCostsIncurred()) : "Không có",
                i.getNote() != null && !i.getNote().isEmpty() ? i.getNote() : "Chưa có thông tin"
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

    private void resetForm() {
        cbbFilter.setSelectedIndex(-1);
        getListInvoice(invoiceDAO.getListInvoiceAll());
        tbInvoice.clearSelection();
        getListInvoiceDetail(new ArrayList<>());
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
    }

    private void searchInvoiceByDateRange() {
        String startDate = txtDateStart.getText();
        String endDate = txtDateEnd.getText();

        // Lấy trạng thái thanh toán từ ComboBox
        Boolean paymentStatus = null; // null nghĩa là không lọc
        String selectedStatus = (String) cbbFilterPaymentStatus.getSelectedItem();

        if ("Đã thanh toán".equals(selectedStatus)) {
            paymentStatus = true;  // Giả sử `false` là Tiền mặt
        } else if ("Chưa thanh toán".equals(selectedStatus)) {
            paymentStatus = false;   // Giả sử `true` là Banking
        }

        // Gọi DAO để lấy danh sách hóa đơn
        List<Invoices> list = invoiceDAO.searchInvoiceByDateRange(startDate, endDate, paymentStatus);
        getListInvoice(list);
    }

    private void loadFilters() {
        cbbFilterPaymentStatus.removeAllItems();
        cbbFilter.removeAllItems();

        cbbFilter.addItem("Tất cả");
        cbbFilter.addItem("1 ngày trước");
        cbbFilter.addItem("7 ngày trước");
        cbbFilter.addItem("30 ngày trước");

        cbbFilterPaymentStatus.addItem("Tất cả");
        cbbFilterPaymentStatus.addItem("Đã thanh toán");
        cbbFilterPaymentStatus.addItem("Chưa thanh toán");

        cbbFilter.setSelectedIndex(0);
        cbbFilterPaymentStatus.setSelectedIndex(0);

        ActionListener filterAction = e -> filterInvoices();
        cbbFilter.addActionListener(filterAction);
        cbbFilterPaymentStatus.addActionListener(e -> searchInvoiceByDateRange());
        cbbFilterPaymentStatus.addActionListener(filterAction);
    }

    private void filterInvoices() {
        String selectedPeriod = (String) cbbFilter.getSelectedItem();
        String selectedPaymentStatus = (String) cbbFilterPaymentStatus.getSelectedItem();
        String period = null;
        Boolean paymentStatus = null;

        // Nếu chọn khoảng thời gian, gán giá trị phù hợp
        if ("1 ngày trước".equals(selectedPeriod)) {
            period = "last_1_day";
        } else if ("7 ngày trước".equals(selectedPeriod)) {
            period = "last_7_days";
        } else if ("30 ngày trước".equals(selectedPeriod)) {
            period = "last_30_days";
        }

        // Nếu chọn trạng thái thanh toán, gán giá trị phù hợp
        if ("Đã thanh toán".equals(selectedPaymentStatus)) {
            paymentStatus = true;
        } else if ("Chưa thanh toán".equals(selectedPaymentStatus)) {
            paymentStatus = false;
        }

        // Gọi phương thức DAO để lấy danh sách hóa đơn theo bộ lọc
        List<Invoices> invoices = invoiceDAO.searchInvoicesByFilters(period, paymentStatus);

        // Hiển thị danh sách hóa đơn lên bảng
        getListInvoice(invoices);
    }

    private void searchInvoiceByCode(String keyword) {
        List<Invoices> list = invoiceDAO.searchInvoiceByCode(keyword);
        getListInvoice(list);
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
        jPanel7 = new javax.swing.JPanel();
        lblKhachHang = new javax.swing.JLabel();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        lblKhachHang3 = new javax.swing.JLabel();
        lblKhachHang4 = new javax.swing.JLabel();
        btnDeleteHistory = new com.petshop.swing.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoice = new com.petshop.swing.table.Table();
        btnDeleteHistory1 = new com.petshop.swing.Button();
        txtDateStart = new com.petshop.swing.textfield.TextField1();
        txtDateEnd = new com.petshop.swing.textfield.TextField1();
        cbbFilterPaymentStatus = new com.petshop.swing.combobox.Combobox();
        cbbFilter = new com.petshop.swing.combobox.Combobox();
        jLabel1 = new javax.swing.JLabel();
        button1 = new com.petshop.swing.Button();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbInvoiceDetail = new com.petshop.swing.table.Table();
        jLabel2 = new javax.swing.JLabel();

        dateChooser1.setTextRefernce(txtDateStart);

        dateChooser2.setTextRefernce(txtDateEnd);

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(204, 255, 255));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang.setText("Tìm kiếm hoá đơn: ");

        txtSearch.setBackground(new java.awt.Color(250, 250, 250));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
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

        cbbFilterPaymentStatus.setLabeText("Trạng thái thanh toán");

        cbbFilter.setLabeText("Lọc theo thời gian");

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hoá đơn");

        button1.setBackground(new java.awt.Color(204, 204, 204));
        button1.setText("Làm mới");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(cbbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbFilterPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
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
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteHistory1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(lblKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbbFilterPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteHistory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
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

    private void btnDeleteHistory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistory1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteHistory1ActionPerformed

    private void tbInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceMouseClicked
        showInvoiceDetailByIdInvoice();
    }//GEN-LAST:event_tbInvoiceMouseClicked

    private void btnDeleteHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistoryActionPerformed
        searchInvoiceByDateRange();
    }//GEN-LAST:event_btnDeleteHistoryActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnDeleteHistory;
    private com.petshop.swing.Button btnDeleteHistory1;
    private com.petshop.swing.Button button1;
    private com.petshop.swing.combobox.Combobox cbbFilter;
    private com.petshop.swing.combobox.Combobox cbbFilterPaymentStatus;
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
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    // End of variables declaration//GEN-END:variables

}
