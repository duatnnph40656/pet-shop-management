package com.petshop.form;

import com.petshop.daos.CustomerDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.Products;
import com.petshop.popup.PopupCategoryProduct;
import com.petshop.popup.PopupShowHistoryDeleted;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import raven.glasspanepopup.GlassPanePopup;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.List;
import javax.swing.ImageIcon;
import java.awt.Image;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
//package com.petshop.form;
/**
 *
 * @author dut
 */
public class CustomerManagement extends javax.swing.JPanel {

    /**
     * Creates new form CustomerManagement
     */
    private CustomerDAO customerRepo;
    private int currentPage = 1;
    private final int pageSize = 5;  // Số lượng bản ghi trên mỗi trang
    private int totalRecords = 0;
    private int totalPages = 1;

    public CustomerManagement() {
        initComponents();
        tblKhachHang.fixTable(jScrollPane1);
        this.customerRepo = new CustomerDAO();
        loadTable();

        String[] trangThaiOptions = {"Tất cả", "Đã kích hoạt", "Chưa kích hoạt"};
        cbbTrangThai.addActionListener(e -> searchCombobox());
        cbbSapxepTheoTen.addActionListener(e -> orderByName());

        //Nút Search và event nút search
        txtSerch.addEvent(new EventTextField() {
            @Override
            public void onPressed(EventCallBack call) {
                //Test
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(5); //Time Sleep
                    }
                    searchCustomer(txtSerch.getText());
                    txtSerch.setText("");
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {
            }
        });
        btnFirst.addActionListener(e -> goToFirstPage());
        btnPrev.addActionListener(e -> goToPreviousPage());
        btnNext.addActionListener(e -> goToNextPage());
        btnLast.addActionListener(e -> goToLastPage());

    }

    private void searchCustomer(String text) {
        String keyword = this.txtSerch.getText().trim();
        System.out.println(keyword);
        Integer trangThai = this.rdoActiveSearch.isSelected() ? 1 : null;// Sử dụng Integer thay vì int để tránh lỗi null
        if (this.rdoActiveSearch.isSelected()) {
            trangThai = 1;
        } else if (this.rdoInActiveSearch.isSelected()) {
            trangThai = 0;
        }
        ArrayList<Customers> ds = (ArrayList<Customers>) this.customerRepo.search(keyword, trangThai);
        DefaultTableModel dtm = (DefaultTableModel) this.tblKhachHang.getModel();
        int stt = 1;
        dtm.setRowCount(0);
        for (Customers customer : ds) {
            Object[] row = {
                customer.getId(),
                stt,
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getCreatedAt(),
                customer.getEmail(),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                customer.isGender() ? "Nam" : "Nữ"
            };
            stt++;
            dtm.addRow(row);
        }
        // Ẩn cột ID
        tblKhachHang.getColumnModel()
                .getColumn(0).setMinWidth(0); // Giả sử cột ID là cột 1
        tblKhachHang.getColumnModel()
                .getColumn(0).setMaxWidth(0);
        tblKhachHang.getColumnModel()
                .getColumn(0).setWidth(0);
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        btnDeleteHistory = new com.petshop.swing.Button();
        btnXoa = new com.petshop.swing.Button();
        jPanel12 = new javax.swing.JPanel();
        btnThem = new com.petshop.swing.Button();
        btnSua = new com.petshop.swing.Button();
        btnClear = new com.petshop.swing.Button();
        jPanel9 = new javax.swing.JPanel();
        exportToExcel = new com.petshop.swing.Button();
        btnImportExcel = new com.petshop.swing.Button();
        btnDownloadTemplate = new com.petshop.swing.Button();
        jPanel7 = new javax.swing.JPanel();
        txtHoten = new com.petshop.swing.textfield.TextFieldRounded();
        txtMaKH = new com.petshop.swing.textfield.TextFieldRounded();
        txtDiaChi = new com.petshop.swing.textfield.TextFieldRounded();
        rdoActive = new com.petshop.swing.radio_button.RadioButtonCustom();
        jLabel2 = new javax.swing.JLabel();
        rdoInActive = new com.petshop.swing.radio_button.RadioButtonCustom();
        txtSdt = new com.petshop.swing.textfield.TextFieldRounded();
        txtNgayTao = new com.petshop.swing.textfield.TextFieldRounded();
        txtEmail = new com.petshop.swing.textfield.TextFieldRounded();
        jLabel3 = new javax.swing.JLabel();
        rdoNam = new com.petshop.swing.radio_button.RadioButtonCustom();
        rdoNu = new com.petshop.swing.radio_button.RadioButtonCustom();
        jButton1 = new javax.swing.JButton();
        btnXoaMaKH = new com.petshop.swing.Button();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lblKhachHang = new javax.swing.JLabel();
        txtSerch = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbTrangThai = new com.petshop.swing.combobox.Combobox();
        cbbSapxepTheoTen = new com.petshop.swing.combobox.Combobox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new com.petshop.swing.table.Table();
        jPanel13 = new javax.swing.JPanel();
        rdoActiveSearch = new com.petshop.swing.radio_button.RadioButtonCustom();
        rdoInActiveSearch = new com.petshop.swing.radio_button.RadioButtonCustom();
        btnFirst = new com.petshop.swing.Button();
        btnPrev = new com.petshop.swing.Button();
        lblPage = new javax.swing.JLabel();
        btnNext = new com.petshop.swing.Button();
        btnLast = new com.petshop.swing.Button();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÔNG TIN KHÁCH HÀNG");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnDeleteHistory.setBackground(new java.awt.Color(204, 204, 255));
        btnDeleteHistory.setText("Lịch sử đã xóa");
        btnDeleteHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHistoryActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 153, 153));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDeleteHistory, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnDeleteHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnThem.setBackground(new java.awt.Color(204, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 255, 204));
        btnSua.setText("Cập nhập");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(204, 204, 204));
        btnClear.setText("Làm mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .addComponent(btnThem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        exportToExcel.setBackground(new java.awt.Color(51, 153, 255));
        exportToExcel.setText("Export Excel");
        exportToExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToExcelActionPerformed(evt);
            }
        });

        btnImportExcel.setBackground(new java.awt.Color(204, 255, 204));
        btnImportExcel.setText("Import Excel");
        btnImportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportExcelActionPerformed(evt);
            }
        });

        btnDownloadTemplate.setBackground(new java.awt.Color(0, 153, 51));
        btnDownloadTemplate.setText("Tải file mẫu ");
        btnDownloadTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadTemplateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDownloadTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportToExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDownloadTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportToExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtHoten.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtHoten.setLabelText("Họ tên ");
        txtHoten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHotenActionPerformed(evt);
            }
        });

        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new java.awt.Color(204, 204, 204));
        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaKH.setLabelText("Mã khách hàng");

        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDiaChi.setLabelText("Địa chỉ ");

        buttonGroup1.add(rdoActive);
        rdoActive.setText("Đã kích hoạt");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Trạng thái");

        buttonGroup1.add(rdoInActive);
        rdoInActive.setText("Chưa kích hoạt");

        txtSdt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSdt.setLabelText("SĐT");
        txtSdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSdtActionPerformed(evt);
            }
        });

        txtNgayTao.setEditable(false);
        txtNgayTao.setBackground(new java.awt.Color(204, 204, 204));
        txtNgayTao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNgayTao.setLabelText("Ngày Tạo");
        txtNgayTao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayTaoActionPerformed(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setLabelText("Email");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Giới tính");

        buttonGroup2.add(rdoNam);
        rdoNam.setText("Nam");

        buttonGroup2.add(rdoNu);
        rdoNu.setText("Nữ");

        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/customer.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setRequestFocusEnabled(false);
        jButton1.setVerifyInputWhenFocusTarget(false);

        btnXoaMaKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/delete.png"))); // NOI18N
        btnXoaMaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMaKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtHoten, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(rdoActive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rdoInActive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoActive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoInActive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblKhachHang.setText("Danh sách khách hàng");

        txtSerch.setBackground(new java.awt.Color(250, 250, 250));
        txtSerch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerchActionPerformed(evt);
            }
        });

        cbbTrangThai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Đã kích hoạt", "Chưa kích hoạt" }));
        cbbTrangThai.setLabeText("Trạng thái ");

        cbbSapxepTheoTen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sắp xếp từ A - Z", "Sắp xếp từ Z - A" }));
        cbbSapxepTheoTen.setLabeText("Trạng thái ");

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã KH", "Họ Tên", "Địa chỉ", "SĐT", "Ngày Tạo", "Email", "Trạng Thái", "Giới Tính"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKhachHang);
        if (tblKhachHang.getColumnModel().getColumnCount() > 0) {
            tblKhachHang.getColumnModel().getColumn(0).setMinWidth(0);
            tblKhachHang.getColumnModel().getColumn(0).setMaxWidth(0);
            tblKhachHang.getColumnModel().getColumn(1).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(2).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(3).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(4).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(5).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(6).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(7).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(8).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(9).setResizable(false);
        }

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonGroup3.add(rdoActiveSearch);
        rdoActiveSearch.setText("Đã kích hoạt");

        buttonGroup3.add(rdoInActiveSearch);
        rdoInActiveSearch.setText("Chưa kích hoạt ");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdoActiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoInActiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdoActiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoInActiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1093, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbbSapxepTheoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(lblKhachHang)
                            .addGap(3, 3, 3))
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbSapxepTheoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnFirst.setBackground(new java.awt.Color(204, 204, 255));
        btnFirst.setText("<<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setBackground(new java.awt.Color(204, 204, 255));
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        lblPage.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPage.setText("...");

        btnNext.setBackground(new java.awt.Color(204, 204, 255));
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setBackground(new java.awt.Color(204, 204, 255));
        btnLast.setText(">>");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(328, 328, 328)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(lblPage)
                .addGap(18, 18, 18)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 15, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPage)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed

        Customers cs = this.getFormData();

        if (cs == null) {
            return; // Dừng lại nếu dữ liệu không hợp lệ
        }

        try {
            this.customerRepo.create(cs); // Chỉ gọi khi cs != null
            this.clearForm();
            // ⭐ Cập nhật totalRecords và totalPages để hiển thị chính xác
            totalRecords = customerRepo.getTotalRecords();
            totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            // ⭐ Chuyển đến trang cuối cùng để hiển thị dòng vừa thêm
            currentPage = totalPages;
            loadTable();

            // ⭐ Bôi đen dòng vừa thêm
            highlightAddedCustomer(cs.getCustomerCode());
            showMessageSuccess("Thêm thành công!");
        } catch (SQLException ex) {
            Logger.getLogger(CustomerManagement.class.getName()).log(Level.SEVERE, null, ex);
            showMessageFail("Lỗi khi thêm khách hàng!");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void exportToExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToExcelActionPerformed
        exportToExcel();
    }//GEN-LAST:event_exportToExcelActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        this.clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseClicked
        int viTri = this.tblKhachHang.getSelectedRow();
        if (viTri == -1) {
            return;
        }

        String ma = "", ten = "", diaChi = "", sdt = "", ngayTao = "", email = "", trangThai = "", gioiTinh = "";

        if (tblKhachHang.getColumnCount() > 9) {
            ma = (tblKhachHang.getValueAt(viTri, 2) != null) ? tblKhachHang.getValueAt(viTri, 2).toString() : "";
            ten = (tblKhachHang.getValueAt(viTri, 3) != null) ? tblKhachHang.getValueAt(viTri, 3).toString() : "";
            diaChi = (tblKhachHang.getValueAt(viTri, 4) != null) ? tblKhachHang.getValueAt(viTri, 4).toString() : "";
            sdt = (tblKhachHang.getValueAt(viTri, 5) != null) ? tblKhachHang.getValueAt(viTri, 5).toString() : "";
            ngayTao = (tblKhachHang.getValueAt(viTri, 6) != null) ? tblKhachHang.getValueAt(viTri, 6).toString() : "";
            email = (tblKhachHang.getValueAt(viTri, 7) != null) ? tblKhachHang.getValueAt(viTri, 7).toString() : "";
            trangThai = (tblKhachHang.getValueAt(viTri, 8) != null) ? tblKhachHang.getValueAt(viTri, 8).toString() : "";
            gioiTinh = (tblKhachHang.getValueAt(viTri, 9) != null) ? tblKhachHang.getValueAt(viTri, 9).toString() : "";
        }

        this.txtMaKH.setText(ma);
        this.txtHoten.setText(ten);
        this.txtDiaChi.setText(diaChi);
        this.txtSdt.setText(sdt);
        this.txtEmail.setText(email);

        this.txtNgayTao.setText(ngayTao);
        if (trangThai.equals("Đã kích hoạt")) {
            this.rdoActive.setSelected(true);
        } else {
            this.rdoInActive.setSelected(true);
        }
        if (gioiTinh.equals("Nam")) {
            this.rdoNam.setSelected(true);
        } else {
            this.rdoNu.setSelected(true);
        }

    }//GEN-LAST:event_tblKhachHangMouseClicked

    private void txtHotenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHotenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHotenActionPerformed

    private void txtNgayTaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayTaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayTaoActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtSdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSdtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSdtActionPerformed

    private void btnDeleteHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteHistoryActionPerformed

        popupDanhSachXoa();
    }//GEN-LAST:event_btnDeleteHistoryActionPerformed

    private void btnImportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportExcelActionPerformed
        importFromExcel();
    }//GEN-LAST:event_btnImportExcelActionPerformed

    private void txtSerchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerchActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnXoaMaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMaKHActionPerformed
        txtMaKH.setText("");
    }//GEN-LAST:event_btnXoaMaKHActionPerformed

    private void btnDownloadTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadTemplateActionPerformed
        downloadExcelTemplate();
    }//GEN-LAST:event_btnDownloadTemplateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnClear;
    private com.petshop.swing.Button btnDeleteHistory;
    private com.petshop.swing.Button btnDownloadTemplate;
    private com.petshop.swing.Button btnFirst;
    private com.petshop.swing.Button btnImportExcel;
    private com.petshop.swing.Button btnLast;
    private com.petshop.swing.Button btnNext;
    private com.petshop.swing.Button btnPrev;
    private com.petshop.swing.Button btnSua;
    private com.petshop.swing.Button btnThem;
    private com.petshop.swing.Button btnXoa;
    private com.petshop.swing.Button btnXoaMaKH;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private com.petshop.swing.combobox.Combobox cbbSapxepTheoTen;
    private com.petshop.swing.combobox.Combobox cbbTrangThai;
    private com.petshop.swing.Button exportToExcel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblPage;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoActive;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoActiveSearch;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoInActive;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoInActiveSearch;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoNam;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoNu;
    private com.petshop.swing.table.Table tblKhachHang;
    private com.petshop.swing.textfield.TextFieldRounded txtDiaChi;
    private com.petshop.swing.textfield.TextFieldRounded txtEmail;
    private com.petshop.swing.textfield.TextFieldRounded txtHoten;
    private com.petshop.swing.textfield.TextFieldRounded txtMaKH;
    private com.petshop.swing.textfield.TextFieldRounded txtNgayTao;
    private com.petshop.swing.textfield.TextFieldRounded txtSdt;
    private com.petshop.swing.textfield.TextFieldAnimation txtSerch;
    // End of variables declaration//GEN-END:variables

//
//    private void loadTable() {
//        totalRecords = customerRepo.getTotalRecords();
//        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
//
//        List<Customers> list = customerRepo.getCustomersByPage(currentPage, pageSize);
//        DefaultTableModel dtm = (DefaultTableModel) tblKhachHang.getModel();
//        dtm.setRowCount(0);
//
//        int stt = (currentPage - 1) * pageSize + 1;  // STT theo trang
//        for (Customers customer : list) {
//            Object[] row = {
//                customer.getId(),
//                stt++,
//                customer.getCustomerCode(),
//                customer.getCustomerName(),
//                customer.getAddress(),
//                customer.getPhoneNumber(),
//                customer.getCreatedAt(),
//                customer.getEmail(),
//                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
//                customer.isGender() ? "Nam" : "Nữ"
//            };
//            dtm.addRow(row);
//        }
//
//        lblPage.setText("Trang " + currentPage + " / " + totalPages);
//        updatePaginationButtons();
//    }
    private void loadTable() {
        totalRecords = customerRepo.getTotalRecords();
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        List<Customers> list = customerRepo.getCustomersByPage(currentPage, pageSize);
        DefaultTableModel dtm = (DefaultTableModel) tblKhachHang.getModel();
        dtm.setRowCount(0);

        int stt = (currentPage - 1) * pageSize + 1;  // STT theo trang
        for (Customers customer : list) {
            Object[] row = {
                customer.getId(),
                stt++,
                checkEmpty(customer.getCustomerCode()),
                checkEmpty(customer.getCustomerName()),
                checkEmpty(customer.getAddress()),
                checkEmpty(customer.getPhoneNumber()),
                checkEmpty(customer.getCreatedAt() != null ? customer.getCreatedAt().toString() : null),
                checkEmpty(customer.getEmail()),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                customer.isGender() ? "Nam" : "Nữ"
            };
            dtm.addRow(row);
        }

        lblPage.setText("Trang " + currentPage + " / " + totalPages);
        updatePaginationButtons();
    }

    private String checkEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? "Chưa có thông tin" : value;
    }

    private void updatePaginationButtons() {
        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);
    }

    private void goToFirstPage() {
        if (currentPage != 1) {
            currentPage = 1;
            loadTable();
        }
    }

    private void goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            loadTable();
        }
    }

    private void goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            loadTable();
        }
    }

    private void goToLastPage() {
        if (currentPage != totalPages) {
            currentPage = totalPages;
            loadTable();
        }
    }

    private void loadTable_deleteHistory() {
        lblKhachHang.setText("Lịch sử KH đã xóa");
        this.rdoActive.setSelected(true);
        this.rdoNam.setSelected(true);
//        String keyword = this.txtSearch.getText().trim();
//        int trangThai = this.rdoActiveSearch.isSelected() ? 1 : 0;
        int stt = 1;
        ArrayList<Customers> ds = (ArrayList<Customers>) this.customerRepo.delete_history();
        DefaultTableModel dtm = (DefaultTableModel) this.tblKhachHang.getModel();
        dtm.setRowCount(0);
        for (Customers customer : ds) {
            Object[] row = {
                customer.getId(),
                stt,
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getCreatedAt(),
                customer.getEmail(),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                customer.isGender() ? "Nam" : "Nữ"
            };
            stt++;
            dtm.addRow(row);
        }
        // Ẩn cột ID
        tblKhachHang.getColumnModel()
                .getColumn(0).setMinWidth(0); // Giả sử cột ID là cột 1
        tblKhachHang.getColumnModel()
                .getColumn(0).setMaxWidth(0);
        tblKhachHang.getColumnModel()
                .getColumn(0).setWidth(0);
    }

    private void clearForm() {
        this.txtHoten.setText("");
        this.txtDiaChi.setText("");
        this.txtMaKH.setText("");
        this.txtSdt.setText("");
        this.txtEmail.setText("");
        this.rdoActive.setSelected(true);
        this.rdoInActive.setSelected(false);
        this.rdoNam.setSelected(true);
        this.rdoNu.setSelected(false);
        this.txtNgayTao.setText("");

    }

    private Customers getFormData() {
        String ten = this.txtHoten.getText().trim();
        String ma = this.txtMaKH.getText().trim(); // Lấy mã từ ô nhập liệu, nếu có
        if (ma.isEmpty()) { // Chỉ tạo mã nếu chưa có mã
            ma = "C" + Ultil.generateRandomCode();
        }
        System.out.println(ma);
        String diaChi = this.txtDiaChi.getText().trim();
        String sdt = this.txtSdt.getText().trim();
        String email = this.txtEmail.getText().trim();
        boolean trangThai = this.rdoActive.isSelected();
        boolean gioiTinh = this.rdoNam.isSelected();

        // Kiểm tra xem có trường nào bị bỏ trống không
        if (ten.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
            showMessageFail("Không được để trống bất kỳ ô nào!");
            return null;
        }

        // Gọi phương thức kiểm tra dữ liệu
        if (!validateCustomerData(ma, ten, sdt, email, diaChi)) {
            return null; // Nếu dữ liệu không hợp lệ thì dừng lại
        }

        return new Customers(ma, ten, sdt, email, diaChi, trangThai, gioiTinh);
    }

    private boolean validateCustomerData(String maKH, String ten, String sdt, String email, String diaChi) {
        // Kiểm tra xem mã khách hàng đã tồn tại trong database chưa
        if (customerRepo.isCustomerIdExists(maKH)) {
            showMessageFail("Mã khách hàng đã tồn tại! Vui lòng kiểm tra lại.");
            return false;
        }
        // Kiểm tra tên: Không chứa số hoặc ký tự đặc biệt
        if (!ten.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            showMessageFail("Tên không hợp lệ! Chỉ được chứa chữ cái và dấu cách.");
            return false;
        }

        // Kiểm tra số điện thoại: Bắt đầu bằng số 0 và có đúng 10 chữ số
        if (!sdt.matches("^0\\d{9}$")) {
            showMessageFail("Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và có đúng 10 số.");
            return false;
        }

        // Kiểm tra email theo regex chuẩn
        if (!email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$")) {
            showMessageFail("Email không hợp lệ! Vui lòng nhập đúng định dạng example@domain.com.");
            return false;
        }

        // Kiểm tra địa chỉ: Không chứa ký tự đặc biệt (ngoại trừ dấu phẩy, dấu chấm, gạch ngang)
        if (!diaChi.matches("^[a-zA-Z0-9À-ỹ\\s,.-]+$")) {
            showMessageFail("Địa chỉ không hợp lệ! Không được chứa ký tự đặc biệt.");
            return false;
        }

        return true; // Dữ liệu hợp lệ
    }

    private void searchCombobox() {
        String keyword = this.txtSerch.getText().trim();
        Integer trangThai = null;

        // Lấy trạng thái từ ComboBox "Trạng thái"
        String selectedStatus = (String) cbbTrangThai.getSelectedItem();
        if ("Đã kích hoạt".equals(selectedStatus)) {
            trangThai = 1;
        } else if ("Chưa kích hoạt".equals(selectedStatus)) {
            trangThai = 0;
        }

        // Gọi DAO để lấy danh sách khách hàng
        ArrayList<Customers> ds = this.customerRepo.search(keyword, trangThai);

        DefaultTableModel dtm = (DefaultTableModel) this.tblKhachHang.getModel();
        dtm.setRowCount(0);
        int stt = 1;

        for (Customers customer : ds) {
            Object[] row = {
                customer.getId(),
                stt,
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getCreatedAt(),
                customer.getEmail(),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                customer.isGender() ? "Nam" : "Nữ"
            };
            stt++;
            dtm.addRow(row);
        }
    }

    private void orderByName() {

        String orderBy = "ASC"; // Mặc định sắp xếp A-Z

        // Lấy kiểu sắp xếp từ ComboBox "Sắp xếp theo tên"
        String selectedSort = (String) cbbSapxepTheoTen.getSelectedItem();
        if ("Sắp xếp từ Z - A".equals(selectedSort)) {
            orderBy = "DESC";
        }

        // Gọi DAO để lấy danh sách khách hàng
        ArrayList<Customers> ds = this.customerRepo.orderByName(orderBy);

        DefaultTableModel dtm = (DefaultTableModel) this.tblKhachHang.getModel();
        dtm.setRowCount(0);
        int stt = 1;

        for (Customers customer : ds) {
            Object[] row = {
                customer.getId(),
                stt,
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getCreatedAt(),
                customer.getEmail(),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                customer.isGender() ? "Nam" : "Nữ"
            };
            stt++;
            dtm.addRow(row);
        }
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

    private void delete() {
        //lấy ra dòng được chọn 
        int row = this.tblKhachHang.getSelectedRow();
        // kết quả trả về -1 là không có dòng được chọn -> dừng luôn, ko sử lý gì cả 
        if (row == -1) {
            showMessageFail("Chọn một dòng trước khi xoá!");
            return;
        }
        showMessageConfirm("Bạn có muốn xoá không?", () -> {

            int id = (int) this.tblKhachHang.getValueAt(row, 0);

            this.customerRepo.delete(id);
            showMessageSuccess("Xoá thành công!");
            loadTable();
            this.clearForm();
        });
    }

    private void update() {
        int row = this.tblKhachHang.getSelectedRow();
        if (row == -1) {
            showMessageFail("Chọn một dòng bạn muốn cập nhật!");
            return;
        }

        int id = (int) this.tblKhachHang.getValueAt(row, 0);

        Customers cs = this.getFormData();
        if (cs == null) {
            return;
        }
        cs.setId(id);
        try {
            this.customerRepo.update(cs);
            this.clearForm();
            loadTable();
            //this.loadTable(this.msRepo.paging(page, limit));
            showMessageSuccess("Cập nhật thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            showMessageError("Cập nhật thất bại!");
        }
    }

    private void popupDanhSachXoa() {

        int stt = 1;
        PopupShowHistoryDeleted popup = new PopupShowHistoryDeleted();
        List<Customers> customer = customerRepo.getListCustomers_Delete();
        List<Object[]> data = new ArrayList<>();
        for (Customers c : customer) {
            data.add(new Object[]{
                stt,
                c.getCustomerCode(),
                c.getCustomerName(),
                c.getAddress(),
                c.getPhoneNumber(),
                c.getCreatedAt(),
                c.getEmail(),
                c.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                c.isGender() ? "Nam" : "Nữ",
                new ModelAction<>(c, new EventAction<Customers>() {
                    @Override
                    public void delete(Customers customer) {
                        showMessageConfirm("Xác nhận khôi phục khách hàng này?", () -> {
                            restoreCustomer(customer);
                            reloadTableCustomer(popup); // Gọi lại để cập nhật giao diện
                        });
                    }

                    @Override
                    public void update(Customers customer) {
                    }

                    @Override
                    public void add(Customers model) {
                    }

                })
            });
            stt++;
        }
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Mã KH", "Họ tên", "Địa chỉ", "SĐT", "Ngày Tạo", "Email", "Trạng thái", "Giới tính", "Thao tác"};

        // Hiển thị popup
        popup.setLbText("Danh sách khách hàng đã xóa");
        popup.fillTable(data, columnNames); // Đảm bảo bảng có dữ liệu trước khi hiển thị

        popup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                loadTable();
            }
        });
        GlassPanePopup.showPopup(popup);
    }

    private void restoreCustomer(Customers customer) {
        customerRepo.reset_delete(customer.getId());
        loadTable();
    }

    private void reloadTableCustomer(PopupShowHistoryDeleted popup) {
        int stt = 1;
        List<Customers> cus = customerRepo.getListCustomers_Delete();
        List<Object[]> data = new ArrayList<>();

        for (Customers customer : cus) {
            data.add(new Object[]{
                stt,
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getCreatedAt(),
                customer.getEmail(),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                customer.isGender() ? "Nam" : "Nữ",
                new ModelAction<>(customer, new EventAction<Customers>() {
                    @Override
                    public void delete(Customers customer) {
                        showMessageConfirm("Xác nhận khôi phục khách hàng này?", () -> {
                            restoreCustomer(customer);
                            reloadTableCustomer(popup); // Gọi lại để cập nhật giao diện
                        });
                    }

                    @Override
                    public void update(Customers customer) {
                    }

                    @Override
                    public void add(Customers model) {
                    }
                })
            });
            stt++;
        }

        String[] columnNames = {"STT", "Mã KH", "Họ tên", "Địa chỉ", "SĐT", "Ngày Tạo", "Email", "Trạng thái", "Giới tính", "Thao tác"};

        popup.fillTable(data, columnNames); // Cập nhật lại dữ liệu bảng
    }

    private void highlightAddedCustomer(String customerCode) {
        DefaultTableModel dtm = (DefaultTableModel) tblKhachHang.getModel();

        for (int i = 0; i < dtm.getRowCount(); i++) {
            String currentCode = dtm.getValueAt(i, 2).toString(); // Cột 2 là "Mã KH"
            if (currentCode.equals(customerCode)) {
                tblKhachHang.setRowSelectionInterval(i, i); // Bôi đen dòng tương ứng

                // Cuộn xuống dòng vừa thêm nếu nằm ngoài vùng hiển thị
                tblKhachHang.scrollRectToVisible(tblKhachHang.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void downloadExcelTemplate() {
        String[] columns = {"Mã KH", "Họ tên", "SĐT", "Email", "Địa chỉ", "Trạng thái", "Giới tính"};
        String[] sampleData = {"C12345", "Nguyễn Văn A", "0389230662", "a88@gmail.com", "123 Mễ Trì, Hà Nội", "1", "0"};

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mẫu Khách Hàng");

        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Thêm dòng dữ liệu mẫu
        Row sampleRow = sheet.createRow(1);
        for (int i = 0; i < sampleData.length; i++) {
            Cell cell = sampleRow.createCell(i);
            cell.setCellValue(sampleData[i]);
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();

            // Kiểm tra thư mục có tồn tại không, nếu không thì tạo
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = directory.getAbsolutePath() + File.separator + "Customer_Template.xlsx";
            File outputFile = new File(filePath);

            try {
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                    workbook.write(fileOut);
                    JOptionPane.showMessageDialog(null, "File mẫu đã được lưu tại: " + filePath, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu file!\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importFromExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls", "xlsx"));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try (FileInputStream fis = new FileInputStream(selectedFile); Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                List<Customers> customersList = new ArrayList<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // Bỏ qua dòng tiêu đề
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }

                    Customers customer = new Customers();
                    customer.setCustomerCode(row.getCell(0).getStringCellValue());
                    customer.setCustomerName(row.getCell(1).getStringCellValue());
                    customer.setPhoneNumber(row.getCell(2).getStringCellValue());
                    customer.setEmail(row.getCell(3).getStringCellValue());
                    customer.setAddress(row.getCell(4).getStringCellValue());
                    customer.setStatus("Đã kích hoạt".equals(row.getCell(5).getStringCellValue()));
                    customer.setGender("Nam".equals(row.getCell(6).getStringCellValue()));

                    customersList.add(customer);
                }

                if (customerRepo.insertMultipleCustomers(customersList)) {
                    JOptionPane.showMessageDialog(this, "Nhập dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadTable();  // Refresh bảng khách hàng
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi nhập dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn thư mục để lưu file");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();

            // Đảm bảo thư mục tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Tự động đặt tên file là "DanhSachKhachHang.xlsx"
            String filePath = directory.getAbsolutePath() + File.separator + "DanhSachKhachHang.xlsx";
            File outputFile = new File(filePath);

            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(outputFile)) {

                Sheet sheet = workbook.createSheet("Danh sách khách hàng");

                // Lấy danh sách khách hàng từ database
                List<Customers> customersList = customerRepo.getListCustomers();

                // Tạo dòng tiêu đề
                String[] columns = {"Mã KH", "Họ tên", "SĐT", "Email", "Địa chỉ", "Trạng thái", "Giới tính"};
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                }

                // Đổ dữ liệu khách hàng vào file Excel
                int rowNum = 1;
                for (Customers customer : customersList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(customer.getCustomerCode());
                    row.createCell(1).setCellValue(customer.getCustomerName());
                    row.createCell(2).setCellValue(customer.getPhoneNumber());
                    row.createCell(3).setCellValue(customer.getEmail());
                    row.createCell(4).setCellValue(customer.getAddress());
                    row.createCell(5).setCellValue(customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt");
                    row.createCell(6).setCellValue(customer.isGender() ? "Nam" : "Nữ");
                }

                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Xuất file thành công!\nFile lưu tại: " + filePath, "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xuất file Excel!\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
