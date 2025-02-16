/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.CustomerDAO;
import com.petshop.daos.PetDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.Pets;
import com.petshop.models.TypePets;
import com.petshop.popup.PopupCategoryPet;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.ultils.Ultil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class PetManagement extends javax.swing.JPanel {

    private final PetDAO petDao = new PetDAO();
    private final TypePetDAO typetDao = new TypePetDAO();
    private final CustomerDAO customerDao = new CustomerDAO();

    public PetManagement() {
        initComponents();
        init();
    }

    private void init() {
        tblPet.fixTable(jScrollPane3);
        fillToTable(petDao.getList());
        fillCboCustomer(customerDao.getListCustomers());
//        fillCboLocGiongThuCung(petDao.loadCboLocGiong());
        search();
        txtMaThuCung.setEditable(false);
    }

    public void fillCboLoaiThuCung(List<TypePets> list) {
        cboLoaiThuCung.removeAllItems();
        for (TypePets typePet : list) {
            cboLoaiThuCung.addItem(typePet);
        }
        cboLoaiThuCung.setSelectedIndex(-1);
    }

    public void fillCboCustomer(List<Customers> list) {
        cboKhachHang.removeAllItems();
        for (Customers customer : list) {
            cboKhachHang.addItem(customer);
        }
        cboKhachHang.setSelectedIndex(-1);
    }

    private boolean iz = true;

    public void fillCboLocLoaiThuCung(List<TypePets> list) {
        iz = true;
        cboLocLoaiThuCung.removeAllItems();
        cboLocLoaiThuCung.addItem("Loài");
        for (TypePets typePet : list) {
            cboLocLoaiThuCung.addItem(typePet);
        }
        iz = false;
    }

    public void fillCboLocGiongThuCung(List<Pets> list) {
        iz = true;
        cboLocGiongThuCung.removeAllItems();
        cboLocGiongThuCung.addItem("Giống");
        for (Pets pet : list) {
            cboLocGiongThuCung.addItem(pet.getBreed());
        }
        iz = false;
    }

    private void locTheoGiong() {
        if (iz) {
            return;
        }
        String loai = (String) cboLocLoaiThuCung.getSelectedItem();
        String giong = (String) cboLocGiongThuCung.getSelectedItem();

        if ("Loài".equals(loai)) {
            loai = null;
        }
        if ("Giống".equals(giong)) {
            giong = null;
        }

//        fillToTable(petDao.locPet(loai, giong));
    }

    private void locTheoLoai() {
        if (iz) {
            return;
        }
        String loai = (String) cboLocLoaiThuCung.getSelectedItem();

        if ("Loài".equals(loai)) {
            fillToTable(petDao.getList()); // Hiển thị toàn bộ danh sách nếu không lọc
        } else {
//            fillToTable(petDao.locPet(loai, null));
        }
    }

    // search
    private void search() {

        txtSearch.addEvent(new EventTextField() {
            public void onPressed(EventCallBack call) {
                try {
                    String search = txtSearch.getText().trim();
                    List<Pets> firstList = petDao.getList();
                    List<Pets> result = new ArrayList<>();

                    for (Pets pet : firstList) {
                        if (pet.getPetName().toLowerCase().contains(search)) {
                            result.add(pet);
                        }
                    }
                    if (search.isEmpty()) {
                        for (int i = 1; i <= 100; i++) {
                            Thread.sleep(10);
                        }
                        showMessageError("Chưa nhập dữ liệu cần tìm kiếm!");
                    } else if (!result.isEmpty()) {
                        for (int i = 1; i <= 100; i++) {
                            Thread.sleep(10);
                        }
                        fillToTable(result);
                    } else {
                        for (int i = 1; i <= 100; i++) {
                            Thread.sleep(10);
                        }
                        showMessageError("Không tìm thấy !");
                    }
                    txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            autoClick();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            autoClick();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            autoClick();
                        }

                    });
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

    // message 
    private void showMessageError(String message) {
        DialogMessageError error = new DialogMessageError(message);
        GlassPanePopup.showPopup(error);
    }

    private void showMessageSuccess(String message) {
        DialogMessageSuccess success = new DialogMessageSuccess(message);
        GlassPanePopup.showPopup(success);
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

    // auto click new search
    private void autoClick() {
        String search = txtSearch.getText();
        if (search.isEmpty()) {
            fillToTable(petDao.getList());
        }

    }

    // load table
    private void fillToTable(List<Pets> list) {
        int index = 1;
        tblPet.setRowCount(0);
        for (Pets pet : list) {
            tblPet.addRow(new Object[]{
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
                pet.getCreatedAt(),
                pet.isStatus() ? "Đang hoạt động" : "Không hoạt động"
            });
            index++;
        }
    }

    // inser data pet
    private Pets readForm() {
        String maThuCung = "PET" + Ultil.generateRandomCode();
        String giong = txtGiongThuCung.getText();
        String ten = txtTenThuCung.getText();
        String mauSac = txtMauSac.getText();
        DecimalFormat df = new DecimalFormat("#.##");
        double canNang = Double.parseDouble(txtCanNang.getText());
        String tuoi = txtTuoi.getText();
        boolean tiem = false;
        if (cbDaTiem.isSelected()) {
            tiem = true;
        } else {
            tiem = false;
        }
        boolean gender = false;
        if (rdoDuc.isSelected()) {
            gender = true;
        } else if (rdoCai.isSelected()) {
            gender = false;
        }
        String owner = txtOwner.getText();

        TypePets loai = typetDao.getTypePetByName(cboLoaiThuCung.getSelectedItem().toString());

        Customers khachHang = customerDao.searchCustomerByCustomerName(cboKhachHang.getSelectedItem().toString());

        return new Pets(maThuCung, ten, khachHang, loai, giong, canNang, mauSac, gender, tiem,owner ,tuoi);
    }

    private boolean check() {
        String mauSac = txtMauSac.getText().trim();
        String canNang = txtCanNang.getText();
        String tuoi = txtTuoi.getText();
        String giong = txtGiongThuCung.getText();
        if (giong.isEmpty()) {
            showMessageError("Giống thú cưng trống!!");
            return false;
        }

        Object selectedLoai = cboLoaiThuCung.getSelectedItem();

        if (selectedLoai == null) {
            showMessageError("Vui lòng chọn loài thú cưng!");
            return false;
        }
        if (mauSac.isEmpty() || canNang.isEmpty() || tuoi.isEmpty()) {
            showMessageError("Không được để trống các trường dữ liệu!");
            return false;
        }
        if (!mauSac.matches("[\\p{L}\\s]+")) {
            showMessageError("Ô Màu sắc  không được phép nhập số và kí tự đặc biệt!");
            return false;
        }
        if (!canNang.matches("^\\d+(\\.\\d+)?$")) {
            showMessageError("Sai định dạng cân nặng (VD :1.5) ");
            return false;
        }
        return true;
    }

    private void getDataTable() {
        int selectedRow = tblPet.getSelectedRow();

        if (selectedRow != -1) {

            String ma = tblPet.getValueAt(selectedRow, 2).toString();
            String loai = tblPet.getValueAt(selectedRow, 3).toString();
            String giong = tblPet.getValueAt(selectedRow, 4).toString();
            String ten = tblPet.getValueAt(selectedRow, 5).toString();
            String tuoi = tblPet.getValueAt(selectedRow, 6).toString();
            String canNang = tblPet.getValueAt(selectedRow, 7).toString();
            String mauSac = tblPet.getValueAt(selectedRow, 8).toString();
            String gioiTinhStr = tblPet.getValueAt(selectedRow, 9).toString();
            boolean gioiTinh = gioiTinhStr.equalsIgnoreCase("Đực");

            String vaccinStr = tblPet.getValueAt(selectedRow, 10).toString();
            boolean vaccin = vaccinStr.equalsIgnoreCase("Đã tiêm");

            String tenKhachHang = tblPet.getValueAt(selectedRow, 11).toString();
            Customers khachHang = customerDao.searchCustomerByCustomerName(tenKhachHang);

            txtMaThuCung.setText(ma);
            txtGiongThuCung.setText(giong);
            txtTenThuCung.setText(ten);
            txtTuoi.setText(tuoi);
            txtCanNang.setText(canNang);
            txtMauSac.setText(mauSac);
            cboLoaiThuCung.setSelectedItem(loai);
            if (gioiTinh) {
                rdoDuc.setSelected(true);
            } else {
                rdoCai.setSelected(true);
            }
            if (vaccin) {
                cbDaTiem.setSelected(true);
            } else {
                cbDaTiem.setSelected(false);
            }
            cboKhachHang.setSelectedItem(khachHang.getCustomerName());

        }
    }

    private void update() {

        int select = tblPet.getSelectedRow();
        if (select == -1) {
            showMessageError("Chưa có dữ liệu nào được chọn!");
            return;
        }
        if (!check()) {
            return;
        }

        int id = Integer.parseInt(tblPet.getValueAt(select, 0).toString());
        // Pet ban đầu
        Pets petFrist = null; //petDao.getById(id)

        // Pet từ bảng
        Pets p = readForm();
        p.setId(id);

        if (petFrist.getPetName() == p.getPetName()
                && petFrist.getCustomer().getId() == p.getCustomer().getId()
                && petFrist.getTypePet().getId() == p.getTypePet().getId()
                && petFrist.getAge().equals(p.getAge())
                && Double.compare(petFrist.getWeight(), p.getWeight()) == 0
                && petFrist.isGender() == p.isGender()
                && petFrist.isVaccinated() == p.isVaccinated()
                && petFrist.getBreed().equals(p.getBreed())
                && petFrist.getColor().equals(p.getColor())) {
            showMessageFail("Dữ liệu không thay đổi, không cần cập nhật!");
            return;
        }

//        if (petDao.update(p, id)) {
//            showMessageSuccess("Cập nhật thành công!");
//            fillToTable(petDao.getList());
//        } else {
//            showMessageError("Cập nhật thất bại!");
//        }
    }

    private void insertPet() {
        if (!check()) {
            return;
        }

//        if (petDao.insertPet(readForm())) {
//            showMessageSuccess("Thêm thành công !");
//            fillToTable(petDao.getList());
//            fillCboLocGiongThuCung(petDao.loadCboLocGiong());
//        } else {
//            showMessageFail("Thêm thất bại");
//        }

    }

    private void lamMoi() {
        txtMaThuCung.setText("Mã thú cưng");
        txtMauSac.setText("");
        txtCanNang.setText("");
        txtTuoi.setText("");
        cboKhachHang.setSelectedIndex(-1);
        cboLoaiThuCung.setSelectedIndex(-1);
        cbDaTiem.setSelected(false);
    }

    private void delete() {
        String id = txtMaThuCung.getText().trim();
        if (tblPet.getRowCount() == 0) {
            showMessageError("Không có dữ liệu nào được chọn");
            return;
        }
//        if (petDao.delete(id)) {
//            fillToTable(petDao.getList());
//            fillCboLocGiongThuCung(petDao.loadCboLocGiong());
//            fillCboGiongThuCung(petDao.loadCboGiong());
//            lamMoi();
//            showMessageSuccess("Xóa thành công!");
//        } else {
//            showMessageFail("Xóa không thành công!");
//        }
    }

//    private void showPopDeleteHistory() {
//        PopupDeleteHistory deleteHistory = new PopupDeleteHistory();
//        deleteHistory.setConfirmListener(new com.petshop.event.ConfirmListener() {
//            @Override
//            public void onConfirm() {
//                fillToTable(petDao.getList());
//            }
//
//            @Override
//            public void onCancel() {
//                fillToTable(petDao.getList());
//            }
//        });
//        GlassPanePopup.showPopup(deleteHistory, "pdeleteHistory");
//
//    }

    private void showPopTypePet() {
        PopupCategoryPet typePet = new PopupCategoryPet();
        typePet.setConfirmListener(new com.petshop.event.ConfirmListener() {
            @Override
            public void onConfirm() {
//                fillCboLoaiThuCung(typetDao.fillCboLoaiThuCung());
            }

            @Override
            public void onCancel() {
//                fillCboLoaiThuCung(typetDao.fillCboLoaiThuCung());
            }
        });
        GlassPanePopup.showPopup(typePet, "pType");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPet = new com.petshop.swing.table.Table();
        jLabel43 = new javax.swing.JLabel();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        cboLocGiongThuCung = new com.petshop.swing.combobox.Combobox();
        cboLocLoaiThuCung = new com.petshop.swing.combobox.Combobox();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtCanNang = new com.petshop.swing.textfield.TextFieldRounded();
        txtMauSac = new com.petshop.swing.textfield.TextFieldRounded();
        jPanel1 = new javax.swing.JPanel();
        txtMaThuCung = new com.petshop.swing.textfield.TextField1();
        rdoDuc = new com.petshop.swing.radio_button.RadioButtonCustom();
        rdoCai = new com.petshop.swing.radio_button.RadioButtonCustom();
        cbDaTiem = new com.petshop.swing.checkbox.JCheckBoxCustom();
        btnPopupPets = new com.petshop.swing.ButtonBadges();
        cboLoaiThuCung = new com.petshop.swing.combobox.Combobox();
        txtTuoi = new com.petshop.swing.textfield.TextFieldRounded();
        cboKhachHang = new com.petshop.swing.combobox.Combobox();
        txtTenThuCung = new com.petshop.swing.textfield.TextFieldRounded();
        txtGiongThuCung = new com.petshop.swing.textfield.TextFieldRounded();
        txtOwner = new com.petshop.swing.textfield.TextFieldRounded();
        jLabel3 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnThem = new com.petshop.swing.Button();
        btnCapNhat = new com.petshop.swing.Button();
        btnLamMoi = new com.petshop.swing.Button();
        btnXoa = new com.petshop.swing.Button();
        btnLichSuaXoa = new com.petshop.swing.Button();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblPet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã ", "Loài ", "Giống", "Tên thú cưng", "Tuổi", "Cân nặng", "Màu sắc ", "Giới tính", "Vaccine", "Tên khách", "Ngày tạo"
            }
        ));
        tblPet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPetMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPetMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblPet);
        if (tblPet.getColumnModel().getColumnCount() > 0) {
            tblPet.getColumnModel().getColumn(0).setMinWidth(0);
            tblPet.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblPet.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jLabel43.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel43.setText("THÔNG TIN PET");

        txtSearch.setBackground(new java.awt.Color(250, 250, 250));
        txtSearch.setHintText("Search...");
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        cboLocGiongThuCung.setLabeText("");
        cboLocGiongThuCung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocGiongThuCungActionPerformed(evt);
            }
        });

        cboLocLoaiThuCung.setLabeText("");
        cboLocLoaiThuCung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocLoaiThuCungActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1070, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboLocLoaiThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cboLocGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(txtSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboLocGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboLocLoaiThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtCanNang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCanNang.setLabelText("Cân nặng ( Kg )");

        txtMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMauSac.setLabelText("Màu sắc");

        txtMaThuCung.setEditable(false);
        txtMaThuCung.setForeground(new java.awt.Color(140, 140, 140));
        txtMaThuCung.setText("Mã thú cưng");
        txtMaThuCung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup1.add(rdoDuc);
        rdoDuc.setSelected(true);
        rdoDuc.setText("Đực");
        rdoDuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDucActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoCai);
        rdoCai.setText("Cái");

        cbDaTiem.setText("Đã tiêm");
        cbDaTiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDaTiemActionPerformed(evt);
            }
        });

        btnPopupPets.setBackground(new java.awt.Color(204, 255, 255));
        btnPopupPets.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-add-24 (1).png"))); // NOI18N
        btnPopupPets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopupPetsActionPerformed(evt);
            }
        });

        cboLoaiThuCung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboLoaiThuCung.setLabeText("Loài thú cưng");
        cboLoaiThuCung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiThuCungActionPerformed(evt);
            }
        });

        txtTuoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTuoi.setLabelText("Tuổi thú cưng ( Tháng )");

        cboKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboKhachHang.setLabeText("Khách hàng");
        cboKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKhachHangActionPerformed(evt);
            }
        });

        txtTenThuCung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenThuCung.setLabelText("Tên thú cưng");

        txtGiongThuCung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiongThuCung.setLabelText("Giống thú cưng");

        txtOwner.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtOwner.setLabelText("Thông tin chủ sở hữu");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboLoaiThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGiongThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPopupPets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(rdoDuc, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(rdoCai, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(cbDaTiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCanNang, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(txtTuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                .addComponent(cboKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(txtTenThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addComponent(txtGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCanNang, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTenThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoDuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoCai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbDaTiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPopupPets, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLoaiThuCung, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("THÔNG TIN THÚ CƯNG");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnThem.setBackground(new java.awt.Color(204, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnCapNhat.setBackground(new java.awt.Color(255, 255, 204));
        btnCapNhat.setText("Cập nhập");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(204, 204, 204));
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 204, 204));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLichSuaXoa.setBackground(new java.awt.Color(204, 204, 255));
        btnLichSuaXoa.setText("Lịch sử đã xóa");
        btnLichSuaXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLichSuaXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnLichSuaXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnLichSuaXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void btnPopupPetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupPetsActionPerformed
        showPopTypePet();
    }//GEN-LAST:event_btnPopupPetsActionPerformed

    private void cboLoaiThuCungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiThuCungActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLoaiThuCungActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed

        showMessageConfirm("Bạn có chắc chắn muốn thêm không?", () -> {
            insertPet();
        });
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        lamMoi();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void cbDaTiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDaTiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDaTiemActionPerformed

    private void rdoDucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDucActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        showMessageConfirm("Bạn có chắc chắn muốn cập nhật không?", () -> {
            update();
        });
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        showMessageConfirm("Bạn có chắc chắn muốn xóa không?", () -> {
            delete();
        });
    }//GEN-LAST:event_btnXoaActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        search();
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        //         txtSearch.setText("1231");
    }//GEN-LAST:event_txtSearchMouseClicked

    private void tblPetMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPetMousePressed
        getDataTable();
    }//GEN-LAST:event_tblPetMousePressed

    private void tblPetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPetMouseClicked

    }//GEN-LAST:event_tblPetMouseClicked

    private void cboLocGiongThuCungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocGiongThuCungActionPerformed
        locTheoGiong();
    }//GEN-LAST:event_cboLocGiongThuCungActionPerformed

    private void cboLocLoaiThuCungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocLoaiThuCungActionPerformed
        locTheoLoai();
    }//GEN-LAST:event_cboLocLoaiThuCungActionPerformed

    private void btnLichSuaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLichSuaXoaActionPerformed
//        showPopDeleteHistory();
    }//GEN-LAST:event_btnLichSuaXoaActionPerformed

    private void cboKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboKhachHangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnCapNhat;
    private com.petshop.swing.Button btnLamMoi;
    private com.petshop.swing.Button btnLichSuaXoa;
    private com.petshop.swing.ButtonBadges btnPopupPets;
    private com.petshop.swing.Button btnThem;
    private com.petshop.swing.Button btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.petshop.swing.checkbox.JCheckBoxCustom cbDaTiem;
    com.petshop.swing.combobox.Combobox cboKhachHang;
    com.petshop.swing.combobox.Combobox cboLoaiThuCung;
    com.petshop.swing.combobox.Combobox cboLocGiongThuCung;
    com.petshop.swing.combobox.Combobox cboLocLoaiThuCung;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoCai;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoDuc;
    private com.petshop.swing.table.Table tblPet;
    private com.petshop.swing.textfield.TextFieldRounded txtCanNang;
    private com.petshop.swing.textfield.TextFieldRounded txtGiongThuCung;
    private com.petshop.swing.textfield.TextField1 txtMaThuCung;
    private com.petshop.swing.textfield.TextFieldRounded txtMauSac;
    private com.petshop.swing.textfield.TextFieldRounded txtOwner;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextFieldRounded txtTenThuCung;
    private com.petshop.swing.textfield.TextFieldRounded txtTuoi;
    // End of variables declaration//GEN-END:variables

}
