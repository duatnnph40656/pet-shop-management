/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.component.Header;
import com.petshop.daos.CustomerDAO;
import com.petshop.daos.EmployeeDAO;
import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.daos.ProductDAO;
import com.petshop.daos.ProductDetailDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.main.Main;
import com.petshop.models.Customers;
import com.petshop.models.Employees;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.models.ProductDetails;
import com.petshop.popup.PopupCustomer;
import com.petshop.services.RememberMeService;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.swing.table.ModelImage;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class Shop extends javax.swing.JPanel {

    /**
     * Creates new form Shop
     */
    private final ProductDetailDAO productDetailDAO;
    private final ProductDAO productDAO;
    private final InvoiceDAO invoiceDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private RememberMeService rememberMeService;

    public Shop() {
        initComponents();
        tbInvoice.fixTable(jScrollPane1);
        tbInvoiceDetail.fixTable(jScrollPane2);
        tbProductDetail.fixTable(jScrollPane3);
        productDetailDAO = new ProductDetailDAO();
        productDAO = new ProductDAO();
        invoiceDAO = new InvoiceDAO();
        invoiceDetailDAO = new InvoiceDetailDAO();
        customerDAO = new CustomerDAO();
        employeeDAO = new EmployeeDAO();
        rememberMeService = new RememberMeService();
        loadCbbPaymen();
        init();
    }

    public void init() {
        getListProductDetail(productDetailDAO.getListProductDetail());
        getListInvoice(invoiceDAO.getListInvoice());

        resetForm();
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
        input.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
        GlassPanePopup.showPopup(input, "input");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{Popup...">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="{CBB...">
    public void loadCbbPaymen() {
        cbbPayment.removeAllItems();
        cbbPayment.addItem("Tiền mặt");
        cbbPayment.addItem("Thanh toán qua banking");
        cbbPayment.setSelectedIndex(-1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{ProductDetail...">
    public void getListProductDetail(List<ProductDetails> list) {
        int stt = 1;
        tbProductDetail.setRowCount(0);
        for (ProductDetails p : list) {
            if (p.isStatus()) {
                tbProductDetail.addRow(new Object[]{
                    p.getId(),
                    stt,
                    new ModelImage(p.getIcon().toString(), p.getProductDetailName()),
                    p.getProductDetailCode(),
                    p.getBarCode(),
                    p.getTypePet().getTypePetName(),
                    p.getFlavor(),
                    p.getQuantityInStock(),
                    p.getWeight() + "KG",
                    p.getFormattedProductionDate(),
                    p.getExpirydate() + " Tháng",
                    p.getFormattedPriceBase(),
                    p.isStatus() ? "Còn hàng" : "Hết hàng",
                    new ModelAction<>(p, new EventAction<ProductDetails>() {
                        @Override
                        public void delete(ProductDetails p) {

                        }

                        @Override
                        public void update(ProductDetails p) {

                        }

                        @Override
                        public void add(ProductDetails model) {

                        }
                    })

                });
                stt++;
            }

        }
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="{Invoice...">
    public void getListInvoice(List<Invoices> list) {
        int stt = 1;
        tbInvoice.setRowCount(0);
        for (Invoices i : list) {
            tbInvoice.addRow(new Object[]{
                i.getId(),
                stt,
                i.getInvoiceCode(),
                i.getCustomer().getCustomerName(),
                i.getEmployee().getEmployeeName(),
                i.getTotalPrice(),
                i.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",});
            stt++;
        }
    }

    public void resetForm() {
        Customers c = customerDAO.searchCustomerById(1);
        lbCustomerName.setText(c.getCustomerName());
        lbCustomerCode.setText(c.getCustomerCode());
    }

    public Invoices readFormInsert() {
        String invoiceCode = "HD" + Ultil.generateRandomCode();
//        Ultil.generateInvoice(invoiceCode, invoiceCode, new ArrayList<>(), invoiceCode);

        Customers c = customerDAO.searchCustomerByCustomerCode(lbCustomerCode.getText());
        if (c == null) {
            showMessageFail("Có lỗi sảy ra");
        }
        // Lấy username của nhân viên đang đăng nhập
        int idEmployee = rememberMeService.getEmployeeId();

        Employees e = employeeDAO.findEmployeeById(idEmployee);

//        BigDecimal totalPrice = BigDecimal.ZERO;
//        BigDecimal costsIncurred = BigDecimal.ZERO;
//        boolean paymentMethod = "Tiền mặt".equals(cbbPayment.getSelectedItem());
//        boolean paymentStatus = checkBoxPaymStatus.isSelected();
//        String note = "";
//        boolean deleted = false;
        boolean status = true; // Mặc định hóa đơn hoạt động

        return new Invoices(invoiceCode, c, e,status);
    }

    public void insertInvoice() {
        // Kiểm tra xem khách hàng có hóa đơn chờ hay không
        Customers c = customerDAO.searchCustomerByCustomerCode(lbCustomerCode.getText());
        if (c == null) {
            showMessageFail("Có lỗi sảy ra");
        }

        if (invoiceDAO.hasPendingInvoice(c.getId())) {
            showMessageFail("Khách hàng này đã có hóa đơn chờ!");
            return;
        }

        // Nếu không có hóa đơn chờ, tiếp tục tạo hóa đơn mới
        Invoices invoice = readFormInsert();
        if (invoice == null) {
            showMessageFail("Tạo hóa đơn thất bại!!");
            return;
        }

        invoiceDAO.createPendingInvoice(invoice);
        getListInvoice(invoiceDAO.getListInvoice());
        showMessageSuccess("Tạo hóa đơn thành công");
    }
    
    public void deleteInvoice(){
        int selectRow = tbInvoice.getSelectedRow();
        int id = (int) tbInvoice.getValueAt(selectRow, 0);
        invoiceDAO.updateDeletedStatus(id, true);
        getListInvoice(invoiceDAO.getListInvoice());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{InvoiceDetail...">
    public void getListInvoiceDetail(List<InvoiceDetails> list) {
//        tb
    }

    public void insertPToInvoiceD() {

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{Customers...">
    public Customers readFormCustomer() {

        return new Customers();
    }

    public void insertCustomer() {

    }

    public void showPopupInsertCustomer() {
        PopupCustomer pC = new PopupCustomer();

        GlassPanePopup.showPopup(pC, "pCustomer");
    }

    public void searchCustomer(String keyword) {
        Customers t = customerDAO.searchCustomerByPhoneNumber(keyword);
        if (t != null) {
            txtSearchCustomer.setText(t.getPhoneNumber());
            lbCustomerCode.setText(t.getCustomerCode());
            lbCustomerName.setText(t.getCustomerName());
        } else {
            showMessageConfirm("Không tìm thấy khách hàng \nbạn có muốn thêm mới khách hàng", () -> {
                showPopupInsertCustomer();
            });
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoice = new com.petshop.swing.table.Table();
        jPanel5 = new javax.swing.JPanel();
        button12 = new com.petshop.swing.Button1();
        jPanel13 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnConfirmInvoice = new com.petshop.swing.Button();
        jLabel33 = new javax.swing.JLabel();
        txtSearchCustomer = new com.petshop.swing.textfield_suggestion.TextFieldSuggestion();
        btnSearchCustomer = new com.petshop.swing.Button();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        lbCustomerCode = new javax.swing.JLabel();
        lbCustomerName = new javax.swing.JLabel();
        btnAddInvoice = new com.petshop.swing.Button();
        btnDeleteInvoice = new com.petshop.swing.Button();
        btnResetInvoice = new com.petshop.swing.Button();
        jLabel38 = new javax.swing.JLabel();
        lbTotalPrice = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        lbTotalPrice1 = new javax.swing.JLabel();
        checkBoxPaymStatus = new com.petshop.swing.checkbox.JCheckBoxCustom();
        cbbPayment = new com.petshop.swing.combobox.Combobox();
        jPanel2 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txtSearchProductDetail = new com.petshop.swing.textfield.TextFieldAnimation();
        comboboxRounded2 = new com.petshop.swing.combobox.ComboboxRounded();
        cbbFilterTypePet = new com.petshop.swing.combobox.ComboboxRounded();
        cbbFilterProduct = new com.petshop.swing.combobox.ComboboxRounded();
        btnScanBarcode = new com.petshop.swing.Button1();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbProductDetail = new com.petshop.swing.tableMore.TableMore();
        jPanel4 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbInvoiceDetail = new com.petshop.swing.tableMore.TableMore1();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setMaximumSize(new java.awt.Dimension(1058, 741));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel1.setText("HÓA ĐƠN CHỜ");

        tbInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã HD", "Tên khách hàng", "Tên nhân viên", "Tổng giá", "Trạng thái thanh toán"
            }
        ));
        jScrollPane1.setViewportView(tbInvoice);
        if (tbInvoice.getColumnModel().getColumnCount() > 0) {
            tbInvoice.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoice.getColumnModel().getColumn(0).setMaxWidth(0);
            tbInvoice.getColumnModel().getColumn(1).setMinWidth(40);
            tbInvoice.getColumnModel().getColumn(1).setMaxWidth(40);
        }

        button12.setText("button12");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button12, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Hóa đơn");

        btnConfirmInvoice.setBackground(new java.awt.Color(0, 255, 255));
        btnConfirmInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-yes-30.png"))); // NOI18N
        btnConfirmInvoice.setText("Xác nhận");

        jLabel33.setText("Nhập số điện thại khách hàng:");

        btnSearchCustomer.setBackground(new java.awt.Color(204, 255, 255));
        btnSearchCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-search-15.png"))); // NOI18N
        btnSearchCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCustomerActionPerformed(evt);
            }
        });

        jLabel34.setText("Mã kh  :");

        jLabel35.setText("Tên kh : ");

        lbCustomerCode.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbCustomerCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCustomerCode.setText("KH");

        lbCustomerName.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCustomerName.setText("Chưa có thông tin");

        btnAddInvoice.setBackground(new java.awt.Color(204, 255, 255));
        btnAddInvoice.setText("Tạo hóa đơn");
        btnAddInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddInvoiceActionPerformed(evt);
            }
        });

        btnDeleteInvoice.setBackground(new java.awt.Color(255, 204, 204));
        btnDeleteInvoice.setText("Hủy hóa đơn");
        btnDeleteInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteInvoiceActionPerformed(evt);
            }
        });

        btnResetInvoice.setBackground(new java.awt.Color(255, 255, 204));
        btnResetInvoice.setText("Làm mới");
        btnResetInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetInvoiceActionPerformed(evt);
            }
        });

        jLabel38.setText("Tổng tiền :");

        lbTotalPrice.setBackground(new java.awt.Color(255, 255, 255));
        lbTotalPrice.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbTotalPrice.setForeground(new java.awt.Color(255, 0, 0));
        lbTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTotalPrice.setText("VND");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setText("Thành tiền:");

        lbTotalPrice1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbTotalPrice1.setForeground(new java.awt.Color(255, 51, 0));
        lbTotalPrice1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTotalPrice1.setText("VND");

        checkBoxPaymStatus.setBackground(new java.awt.Color(255, 204, 204));
        checkBoxPaymStatus.setText("Đã thanh toán");

        cbbPayment.setLabeText("Phương thức thanh toán");
        cbbPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbPaymentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConfirmInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTotalPrice1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCustomerCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(btnResetInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(btnDeleteInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtSearchCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(checkBoxPaymStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txtSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(lbCustomerCode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(lbCustomerName)))
                    .addComponent(btnSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(lbTotalPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbbPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(lbTotalPrice1))
                .addGap(10, 10, 10)
                .addComponent(checkBoxPaymStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConfirmInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel43.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel43.setText("DANH SÁCH SẢN PHẨM");

        txtSearchProductDetail.setBackground(new java.awt.Color(250, 250, 250));

        comboboxRounded2.setLabeText("Sắp xếp theo");

        cbbFilterTypePet.setLabeText("Loại thú cưng");

        cbbFilterProduct.setLabeText("Sản phẩm");

        btnScanBarcode.setText("Quét barcode");

        tbProductDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Tên SP", "Mã SP", "BarCode", "Dành cho", "Hương vị", "SL", "TL", "NSX", "HSD", "Giá bán", "Trạng thái", "Thao tác"
            }
        ));
        jScrollPane3.setViewportView(tbProductDetail);
        if (tbProductDetail.getColumnModel().getColumnCount() > 0) {
            tbProductDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbProductDetail.getColumnModel().getColumn(1).setMinWidth(40);
            tbProductDetail.getColumnModel().getColumn(1).setMaxWidth(40);
            tbProductDetail.getColumnModel().getColumn(2).setMinWidth(180);
            tbProductDetail.getColumnModel().getColumn(2).setMaxWidth(180);
            tbProductDetail.getColumnModel().getColumn(7).setMinWidth(40);
            tbProductDetail.getColumnModel().getColumn(7).setMaxWidth(40);
            tbProductDetail.getColumnModel().getColumn(8).setMinWidth(60);
            tbProductDetail.getColumnModel().getColumn(8).setMaxWidth(60);
            tbProductDetail.getColumnModel().getColumn(10).setMinWidth(70);
            tbProductDetail.getColumnModel().getColumn(10).setMaxWidth(70);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addGap(18, 18, 18)
                .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboboxRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(btnScanBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnScanBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboboxRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel42.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel42.setText("HÓA ĐƠN CHI TIẾT");

        tbInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã HDCT", "Mã SP", "Tên SP", "Số lượng", "Giá bán", "Thao tác"
            }
        ));
        jScrollPane2.setViewportView(tbInvoiceDetail);
        if (tbInvoiceDetail.getColumnModel().getColumnCount() > 0) {
            tbInvoiceDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetInvoiceActionPerformed

    private void cbbPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbPaymentActionPerformed

    private void btnAddInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddInvoiceActionPerformed
        // TODO add your handling code here:
        insertInvoice();
    }//GEN-LAST:event_btnAddInvoiceActionPerformed

    private void btnSearchCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCustomerActionPerformed
        // TODO add your handling code here:
        searchCustomer(txtSearchCustomer.getText());
    }//GEN-LAST:event_btnSearchCustomerActionPerformed

    private void btnDeleteInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteInvoiceActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận hủy hóa đơn", () -> {deleteInvoice();});
    }//GEN-LAST:event_btnDeleteInvoiceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnAddInvoice;
    private com.petshop.swing.Button btnConfirmInvoice;
    private com.petshop.swing.Button btnDeleteInvoice;
    private com.petshop.swing.Button btnResetInvoice;
    private com.petshop.swing.Button1 btnScanBarcode;
    private com.petshop.swing.Button btnSearchCustomer;
    private com.petshop.swing.Button1 button12;
    private com.petshop.swing.combobox.ComboboxRounded cbbFilterProduct;
    private com.petshop.swing.combobox.ComboboxRounded cbbFilterTypePet;
    private com.petshop.swing.combobox.Combobox cbbPayment;
    private com.petshop.swing.checkbox.JCheckBoxCustom checkBoxPaymStatus;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbCustomerCode;
    private javax.swing.JLabel lbCustomerName;
    private javax.swing.JLabel lbTotalPrice;
    private javax.swing.JLabel lbTotalPrice1;
    private com.petshop.swing.table.Table tbInvoice;
    private com.petshop.swing.tableMore.TableMore1 tbInvoiceDetail;
    private com.petshop.swing.tableMore.TableMore tbProductDetail;
    private com.petshop.swing.textfield_suggestion.TextFieldSuggestion txtSearchCustomer;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProductDetail;
    // End of variables declaration//GEN-END:variables
}
