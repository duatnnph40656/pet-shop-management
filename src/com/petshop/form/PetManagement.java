/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.CustomerDAO;
import com.petshop.daos.PetCareServiceDAO;
import com.petshop.daos.PetDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.PetCareServices;
import com.petshop.models.Pets;
import com.petshop.models.ProductDetails;
import com.petshop.models.TypePets;
import com.petshop.popup.PopupCategoryPet;
import com.petshop.popup.PopupShowHistoryDeleted;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
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
    private final PetCareServiceDAO careServiceDAO = new PetCareServiceDAO();

    public PetManagement() {
        initComponents();
        init();
    }

    private void init() {
        tblPet.fixTable(jScrollPane3);
        tbPetCareService.fixTable(jScrollPane4);
        fillToTable(petDao.getListPet());
        fillCboCustomer(customerDao.getListCustomers());
//        fillCboLocGiongThuCung(petDao.loadCboLocGiong());
        search();
        txtMaThuCung.setText("PET" + Ultil.generateRandomCode());

        getListPetCareS(careServiceDAO.getListPetCareService());
    }

    //<editor-fold defaultstate="collapsed" desc="{Pets...">
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
            fillToTable(petDao.getListPet()); // Hiển thị toàn bộ danh sách nếu không lọc
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
                    List<Pets> firstList = petDao.getListPet();
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
            fillToTable(petDao.getListPet());
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
        Pets p = new Pets();
        String maThuCung = "PET" + Ultil.generateRandomCode();
        String giong = txtGiongThuCung.getText();
        String ten = txtTenThuCung.getText();
        String mauSac = txtMauSac.getText();
        BigDecimal canNang = new BigDecimal(txtCanNang.getText());
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

        p.setPetCode(maThuCung);
        p.setPetName(ten);
        p.setAge(tuoi);
        p.setBreed(giong);
        p.setGender(gender);
        p.setColor(mauSac);
        p.setWeight(canNang);
        p.setVaccinated(tiem);
        p.setTypePet(loai);
        p.setCustomer(khachHang);

        return p;
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

        if (petFrist.getPetName().equals(p.getPetName())
                && petFrist.getCustomer().getId() == p.getCustomer().getId()
                && petFrist.getTypePet().getId() == p.getTypePet().getId()
                && petFrist.getAge().equals(p.getAge())
                && petFrist.getWeight().compareTo(p.getWeight()) == 0
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

    private void showPopDeleteHistory() {
        PopupShowHistoryDeleted deleteHistory = new PopupShowHistoryDeleted();
        deleteHistory.setConfirmListener(new com.petshop.event.ConfirmListener() {
            @Override
            public void onConfirm() {
//                fillToTable(petDao.getList());
            }

            @Override
            public void onCancel() {
//                fillToTable(petDao.getList());
            }
        });
        GlassPanePopup.showPopup(deleteHistory, "pdeleteHistory");
    }
    
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
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="{PetCareService...">
    private void getListPetCareS(List<PetCareServices> list) {
        int stt = 1;
        tbPetCareService.setRowCount(0);
        for (PetCareServices p : list) {
            tbPetCareService.addRow(new Object[]{
                p.getId(),
                stt,
                (p.getPetS() != null && p.getPetS().getServiceCode() != null) ? p.getPetS().getServiceCode() : "Chưa có thông tin",
                (p.getPetS() != null && p.getPetS().getServiceName() != null) ? p.getPetS().getServiceName() : "Chưa có thông tin",
                (p.getPet() != null && p.getPet().getPetCode() != null) ? p.getPet().getPetCode() : "Chưa có thông tin",
                (p.getPet() != null && p.getPet().getPetName() != null) ? p.getPet().getPetName() : "Chưa có thông tin",
                (p.getPet() != null && p.getPet().getBreed() != null) ? p.getPet().getBreed() : "Chưa có thông tin",
                (p.getDateStart() != null) ? Ultil.getFormatted(p.getDateStart()) : "Chưa có thông tin",
                (p.getDateEnd() != null) ? Ultil.getFormatted(p.getDateEnd()) : "Chưa có thông tin",
                (p.getActualEnd() != null) ? p.getActualEnd() : "Chưa có thông tin",
                (p.getNote() != null) ? p.getNote() : "Chưa có thông tin",
                p.isStatus() ? "Đang tiến hành" : "Đã hoàn thành",
                new ModelAction<>(p, new EventAction<PetCareServices>() {
                    @Override
                    public void delete(PetCareServices p) {
                        // Xử lý xóa
                    }

                    @Override
                    public void update(PetCareServices p) {
                        // Xử lý cập nhật
                    }

                    @Override
                    public void add(PetCareServices model) {
                        // Xử lý thêm mới
                    }
                })
            });
            stt++;
        }
    }
    //</editor-fold>
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        materialTabbed1 = new com.petshop.swing.tabbed.MaterialTabbed();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        textFieldAnimation1 = new com.petshop.swing.textfield.TextFieldAnimation();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbPetCareService = new com.petshop.swing.tableMore.TableMore3();
        comboboxRounded1 = new com.petshop.swing.combobox.ComboboxRounded();
        comboboxRounded2 = new com.petshop.swing.combobox.ComboboxRounded();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtCanNang = new com.petshop.swing.textfield.TextFieldRounded();
        txtMauSac = new com.petshop.swing.textfield.TextFieldRounded();
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
        txtMaThuCung = new com.petshop.swing.textfield.TextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPet = new com.petshop.swing.table.Table();
        jLabel43 = new javax.swing.JLabel();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        cboLocLoaiThuCung = new com.petshop.swing.combobox.Combobox();
        cboLocGiongThuCung = new com.petshop.swing.combobox.Combobox();
        jPanel11 = new javax.swing.JPanel();
        btnThem = new com.petshop.swing.Button();
        btnCapNhat = new com.petshop.swing.Button();
        btnLamMoi = new com.petshop.swing.Button();
        btnXoa = new com.petshop.swing.Button();
        btnLichSuaXoa = new com.petshop.swing.Button();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        materialTabbed1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("DANH SÁCH DỊCH VỤ");

        textFieldAnimation1.setBackground(new java.awt.Color(250, 250, 250));

        tbPetCareService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "STT", "Mã DV", "Tên DV", "Mã Pet", "Tên Pet", "Giống loài", "Ngày bắt đầu", "Ngày kết thúc", "Ngày kết thúc thực tế", "Note", "Trạng thái", "Thao tác"
            }
        ));
        jScrollPane4.setViewportView(tbPetCareService);
        if (tbPetCareService.getColumnModel().getColumnCount() > 0) {
            tbPetCareService.getColumnModel().getColumn(0).setMinWidth(0);
            tbPetCareService.getColumnModel().getColumn(0).setMaxWidth(0);
            tbPetCareService.getColumnModel().getColumn(1).setMinWidth(35);
            tbPetCareService.getColumnModel().getColumn(1).setMaxWidth(35);
        }

        comboboxRounded1.setLabeText("Tìm kiếm theo ngày");

        comboboxRounded2.setLabeText("Trạng thái");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboboxRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboboxRounded1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldAnimation1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboboxRounded1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboboxRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(textFieldAnimation1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 40, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Quản lý dịch vụ thú cưng", jPanel1);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtCanNang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCanNang.setLabelText("Cân nặng ( Kg )");

        txtMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMauSac.setLabelText("Màu sắc");

        rdoDuc.setSelected(true);
        rdoDuc.setText("Đực");
        rdoDuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDucActionPerformed(evt);
            }
        });

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

        txtMaThuCung.setEnabled(false);
        txtMaThuCung.setLabelText("Mã Pet");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÔNG TIN PET");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(cboLoaiThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPopupPets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMaThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtGiongThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(rdoDuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rdoCai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCanNang, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                    .addComponent(txtOwner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(cbDaTiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanNang, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPopupPets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLoaiThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoDuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoCai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbDaTiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jLabel43.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel43.setText("DANH SÁCH PET");

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                        .addComponent(cboLocLoaiThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(cboLocGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel43)
                        .addComponent(txtSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboLocGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboLocLoaiThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        materialTabbed1.addTab("Quản lý thú cưng", jPanel2);

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

    private void rdoDucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDucActionPerformed

    private void cbDaTiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDaTiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDaTiemActionPerformed

    private void btnPopupPetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupPetsActionPerformed
        showPopTypePet();
    }//GEN-LAST:event_btnPopupPetsActionPerformed

    private void cboLoaiThuCungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiThuCungActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLoaiThuCungActionPerformed

    private void cboKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboKhachHangActionPerformed

    private void tblPetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPetMouseClicked

    }//GEN-LAST:event_tblPetMouseClicked

    private void tblPetMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPetMousePressed
        getDataTable();
    }//GEN-LAST:event_tblPetMousePressed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        //         txtSearch.setText("1231");
    }//GEN-LAST:event_txtSearchMouseClicked

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        search();
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed

        showMessageConfirm("Bạn có chắc chắn muốn thêm không?", () -> {
            insertPet();
        });
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        showMessageConfirm("Bạn có chắc chắn muốn cập nhật không?", () -> {
            update();
        });
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        lamMoi();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        showMessageConfirm("Bạn có chắc chắn muốn xóa không?", () -> {
            delete();
        });
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLichSuaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLichSuaXoaActionPerformed
        //        showPopDeleteHistory();
    }//GEN-LAST:event_btnLichSuaXoaActionPerformed


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
    private com.petshop.swing.combobox.Combobox cboLocGiongThuCung;
    private com.petshop.swing.combobox.Combobox cboLocLoaiThuCung;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded1;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoCai;
    private com.petshop.swing.radio_button.RadioButtonCustom rdoDuc;
    private com.petshop.swing.tableMore.TableMore3 tbPetCareService;
    private com.petshop.swing.table.Table tblPet;
    private com.petshop.swing.textfield.TextFieldAnimation textFieldAnimation1;
    private com.petshop.swing.textfield.TextFieldRounded txtCanNang;
    private com.petshop.swing.textfield.TextFieldRounded txtGiongThuCung;
    private com.petshop.swing.textfield.TextField txtMaThuCung;
    private com.petshop.swing.textfield.TextFieldRounded txtMauSac;
    private com.petshop.swing.textfield.TextFieldRounded txtOwner;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextFieldRounded txtTenThuCung;
    private com.petshop.swing.textfield.TextFieldRounded txtTuoi;
    // End of variables declaration//GEN-END:variables

}
