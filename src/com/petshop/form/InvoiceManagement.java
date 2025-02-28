/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.daos.ReturnInvoiceDAO;
import com.petshop.daos.ReturnInvoiceDetailDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.models.ReturnInvoices;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import raven.glasspanepopup.GlassPanePopup;

/**
 * @author dut
 */
public class InvoiceManagement extends javax.swing.JPanel {

    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;
    private ReturnInvoiceDAO returnInvoicesDAO;
    private ReturnInvoiceDetailDAO returnInvoiceDetailDAO;

    public InvoiceManagement() {
        initComponents();
        tbInvoice.fixTable(jScrollPane1);
        tbInvoiceDetail.fixTable(jScrollPane3);
        tbInvoiceReturn.fixTable(jScrollPane2);
        tblInvoiceReturnDetail.fixTable(jScrollPane4);
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceDetailDAO = new InvoiceDetailDAO();
        returnInvoicesDAO = new ReturnInvoiceDAO();
        returnInvoiceDetailDAO = new ReturnInvoiceDetailDAO();
        init();
    }

    private void init() {
        getListInvoice(invoiceDAO.getListInvoiceAll());
        loadFilters();
        searchEvent();
        txtDateStart.setText("Chọn ngày");
        txtDateEnd.setText("Chọn ngày");
        getListReturnInvoice(returnInvoicesDAO.getListReturnInvoice());
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

    //<editor-fold defaultstate="collapsed" desc="{invoices...">
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
        cbbFilter.setSelectedIndex(0);
        cbbFilterPaymentStatus.setSelectedIndex(0);
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
                    : (i.getPetService() != null ? i.getPetService().getServiceName() : "Chưa có thông tin");

            String code = (i.getProductDetail() != null && i.getProductDetail().getProductDetailCode() != null)
                    ? i.getProductDetail().getProductDetailCode()
                    : (i.getPetService() != null ? i.getPetService().getServiceCode() : "Chưa có thông tin");
            String petName = (i.getPet() != null) ? i.getPet().getPetName() : "Không có thông tin";

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
                (i.isTypeInvoiceDetail() ? i.getServiceDuration() + " Ngày" : "Không có"),
                Ultil.formatCurrency(price),
                Ultil.formatCurrency(i.getTotalPrice()),
                petName == null ? "Không có thông tin" : petName,
                i.getFormattedCreatedAt()
            });
            stt++;
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

            Boolean paymentStatus = null;
            String selectedStatus = (String) cbbFilterPaymentStatus.getSelectedItem();

            if ("Đã thanh toán".equals(selectedStatus)) {
                paymentStatus = true;
            } else if ("Chưa thanh toán".equals(selectedStatus)) {
                paymentStatus = false;
            }

            List<Invoices> list = invoiceDAO.searchInvoiceByDateRange(formattedStartDate, formattedEndDate, paymentStatus);
            getListInvoice(list);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng ngày (dd/MM/yyyy)", "Lỗi định dạng ngày", JOptionPane.ERROR_MESSAGE);
        }
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

        cbbFilter.addActionListener(e -> filterInvoicesByDate());
        cbbFilterPaymentStatus.addActionListener(e -> filterInvoicesByStatus());
    }

    private void filterInvoicesByDate() {
        String selectedPeriod = (String) cbbFilter.getSelectedItem();
        String period = null;

        if ("1 ngày trước".equals(selectedPeriod)) {
            period = "last_1_day";
        } else if ("7 ngày trước".equals(selectedPeriod)) {
            period = "last_7_days";
        } else if ("30 ngày trước".equals(selectedPeriod)) {
            period = "last_30_days";
        }

        // Nếu chọn "Tất cả", hiển thị toàn bộ hóa đơn
        if ("Tất cả".equals(selectedPeriod)) {
            getListInvoice(invoiceDAO.getListInvoiceAll());
            return;
        }

        // Lọc theo khoảng thời gian
        List<Invoices> invoices = invoiceDAO.searchInvoicesByPeriod(period);
        getListInvoice(invoices);
    }

    private void filterInvoicesByStatus() {
        String selectedPaymentStatus = (String) cbbFilterPaymentStatus.getSelectedItem();
        Boolean paymentStatus = null;

        if ("Đã thanh toán".equals(selectedPaymentStatus)) {
            paymentStatus = true;
        } else if ("Chưa thanh toán".equals(selectedPaymentStatus)) {
            paymentStatus = false;
        }

        // Nếu chọn "Tất cả", hiển thị toàn bộ hóa đơn
        if ("Tất cả".equals(selectedPaymentStatus)) {
            getListInvoice(invoiceDAO.getListInvoiceAll());
            return;
        }

        // Lọc theo trạng thái thanh toán
        List<Invoices> invoices = invoiceDAO.searchInvoicesByPaymentStatus(paymentStatus);
        getListInvoice(invoices);
    }

    private void searchInvoiceByCode(String keyword) {
        List<Invoices> list = invoiceDAO.searchInvoiceByCode(keyword);
        getListInvoice(list);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{returnInvoices...">
    private void getListReturnInvoice(List<ReturnInvoices> list) {
        int stt = 1;
        tbInvoiceReturn.setRowCount(0);
        for (ReturnInvoices returnInvoices : list) {
            tbInvoiceReturn.addRow(new Object[]{
                returnInvoices.getId(),
                stt,
                returnInvoices.getReturnInvoiceCode() != null && !returnInvoices.getReturnInvoiceCode().isEmpty()
                ? returnInvoices.getReturnInvoiceCode() : "Chưa có thông tin",
                returnInvoices.getCreatedAt() != null ? Ultil.getFormatted(returnInvoices.getCreatedAt()) : "Chưa có thông tin",
                returnInvoices.getTotalPrice() != null ? Ultil.formatCurrency(returnInvoices.getTotalPrice()) : "Chưa có thông tin",
                returnInvoices.getTotalPriceReturn() != null ? Ultil.formatCurrency(returnInvoices.getTotalPriceReturn()) : "Chưa có thông tin",
                returnInvoices.getEmployees() != null && returnInvoices.getEmployees().getEmployeeName() != null
                ? returnInvoices.getEmployees().getEmployeeName() : "Chưa có thông tin",
                returnInvoices.getCustomers() != null && returnInvoices.getCustomers().getCustomerName() != null
                ? returnInvoices.getCustomers().getCustomerName() : "Chưa có thông tin",
                returnInvoices.isPaymentMethod() ? "Tiền mặt" : "Thanh toán qua banking",
                returnInvoices.getCostsIncurred() != null ? Ultil.formatCurrency(returnInvoices.getCostsIncurred()) : "Chưa có thông tin",
                returnInvoices.getNote() != null && !returnInvoices.getNote().isEmpty()
                ? returnInvoices.getNote() : "Chưa có thông tin"
            });
            stt++;
        }
    }

    //</editor-fold>
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
        jPanel7 = new javax.swing.JPanel();
        lblKhachHang = new javax.swing.JLabel();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        lblKhachHang3 = new javax.swing.JLabel();
        lblKhachHang4 = new javax.swing.JLabel();
        btnDeleteHistory = new com.petshop.swing.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoice = new com.petshop.swing.table.Table();
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
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblKhachHang1 = new javax.swing.JLabel();
        txtSearchReturn = new com.petshop.swing.textfield.TextFieldAnimation();
        lblKhachHang5 = new javax.swing.JLabel();
        lblKhachHang6 = new javax.swing.JLabel();
        btnFitterReturn = new com.petshop.swing.Button();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbInvoiceReturn = new com.petshop.swing.table.Table();
        txtDateStartReturn = new com.petshop.swing.textfield.TextField1();
        txtDateEndReturn = new com.petshop.swing.textfield.TextField1();
        cbbFilterPaymentStatusReturn = new com.petshop.swing.combobox.Combobox();
        cbbFilterReturn = new com.petshop.swing.combobox.Combobox();
        jLabel3 = new javax.swing.JLabel();
        button2 = new com.petshop.swing.Button();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblInvoiceReturnDetail = new com.petshop.swing.table.Table();
        jLabel4 = new javax.swing.JLabel();

        dateChooser1.setTextRefernce(txtDateStart);

        dateChooser2.setTextRefernce(txtDateEnd);

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(204, 255, 255));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

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
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(cbbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbbFilterPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
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
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        tbInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã HDCT", "Mã SP/DV", "Tên SP/DV", "SL", "Số ngày", "Giá bán/giá DV", "Tổng tiền", "Thông tin khác", "Ngày tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(902, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1053, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(4, 4, 4))
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 694, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        materialTabbed1.addTab("Hóa đơn", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        lblKhachHang1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang1.setText("Tìm kiếm hoá đơn: ");

        txtSearchReturn.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchReturnActionPerformed(evt);
            }
        });

        lblKhachHang5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang5.setText("Từ:");

        lblKhachHang6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblKhachHang6.setText("Đến:");

        btnFitterReturn.setBackground(new java.awt.Color(204, 204, 255));
        btnFitterReturn.setText("Lọc");
        btnFitterReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFitterReturnActionPerformed(evt);
            }
        });

        tbInvoiceReturn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã HDTH", "Ngày Tạo ", "T. tiền hàng đổi", "T. tiền hàng trả", "Thông tin NV", "Thông tin KH", "Hình thức TT", "Phí phát sinh", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbInvoiceReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInvoiceReturnMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbInvoiceReturn);
        if (tbInvoiceReturn.getColumnModel().getColumnCount() > 0) {
            tbInvoiceReturn.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoiceReturn.getColumnModel().getColumn(0).setMaxWidth(0);
            tbInvoiceReturn.getColumnModel().getColumn(1).setMinWidth(34);
            tbInvoiceReturn.getColumnModel().getColumn(1).setMaxWidth(34);
            tbInvoiceReturn.getColumnModel().getColumn(3).setMinWidth(150);
            tbInvoiceReturn.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        cbbFilterPaymentStatusReturn.setLabeText("Trạng thái thanh toán");

        cbbFilterReturn.setLabeText("Lọc theo thời gian");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Hoá đơn đổi trả");

        button2.setBackground(new java.awt.Color(204, 204, 204));
        button2.setText("Làm mới");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(cbbFilterReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbFilterPaymentStatusReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                        .addComponent(lblKhachHang5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateStartReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachHang6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateEndReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnFitterReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 498, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(lblKhachHang1)
                                .addGap(5, 5, 5)
                                .addComponent(txtSearchReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(button2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearchReturn, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(lblKhachHang1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3))
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbbFilterReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterPaymentStatusReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDateEndReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnFitterReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDateStartReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKhachHang5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        tblInvoiceReturnDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã HDTHCT", "Mã HDTH", "Mã SP", "Tên SP", "SL", "Giá bán", "Tổng tiền", "Ngày tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoiceReturnDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoiceReturnDetailMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblInvoiceReturnDetail);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Hoá đơn chi tiết ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 49, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Hóa đơn đổi trả", jPanel2);

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

    private void tbInvoiceDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceDetailMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbInvoiceDetailMouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_button1ActionPerformed

    private void tbInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceMouseClicked
        showInvoiceDetailByIdInvoice();
    }//GEN-LAST:event_tbInvoiceMouseClicked

    private void btnDeleteHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistoryActionPerformed
        searchInvoiceByDateRange();
    }//GEN-LAST:event_btnDeleteHistoryActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchReturnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchReturnActionPerformed

    private void btnFitterReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFitterReturnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFitterReturnActionPerformed

    private void tbInvoiceReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceReturnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbInvoiceReturnMouseClicked

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button2ActionPerformed

    private void tblInvoiceReturnDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoiceReturnDetailMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblInvoiceReturnDetailMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnDeleteHistory;
    private com.petshop.swing.Button btnFitterReturn;
    private com.petshop.swing.Button button1;
    private com.petshop.swing.Button button2;
    private com.petshop.swing.combobox.Combobox cbbFilter;
    private com.petshop.swing.combobox.Combobox cbbFilterPaymentStatus;
    private com.petshop.swing.combobox.Combobox cbbFilterPaymentStatusReturn;
    private com.petshop.swing.combobox.Combobox cbbFilterReturn;
    private com.petshop.swing.datechooser.DateChooser dateChooser1;
    private com.petshop.swing.datechooser.DateChooser dateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblKhachHang1;
    private javax.swing.JLabel lblKhachHang3;
    private javax.swing.JLabel lblKhachHang4;
    private javax.swing.JLabel lblKhachHang5;
    private javax.swing.JLabel lblKhachHang6;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.table.Table tbInvoice;
    private com.petshop.swing.table.Table tbInvoiceDetail;
    private com.petshop.swing.table.Table tbInvoiceReturn;
    private com.petshop.swing.table.Table tblInvoiceReturnDetail;
    private com.petshop.swing.textfield.TextField1 txtDateEnd;
    private com.petshop.swing.textfield.TextField1 txtDateEndReturn;
    private com.petshop.swing.textfield.TextField1 txtDateStart;
    private com.petshop.swing.textfield.TextField1 txtDateStartReturn;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchReturn;
    // End of variables declaration//GEN-END:variables

}
