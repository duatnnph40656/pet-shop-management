/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.CustomerDAO;
import com.petshop.daos.PetCareServiceDAO;
import com.petshop.daos.PetDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.PetCareServices;
import com.petshop.models.Pets;
import com.petshop.models.ProductDetails;
import com.petshop.models.TypePets;
import com.petshop.popup.PopupCategoryPet;
import com.petshop.popup.PopupShowHistoryDeleted;
import com.petshop.popup.PopupShowPet;
import com.petshop.popup.PopupUpdateService;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        txtThongTinKhachHang.setEditable(false);
        tbPetCareService.fixTable(jScrollPane4);
        fillToTable(petDao.getList());
        fillCboTypePet(typetDao.getListTypePet());
        fillFilterBreedPet(petDao.getList());
        fillFilterTypePet(typetDao.getListTypePet());
        search();
        txtThongTinKhachHang.setText("Khách hàng lẻ");
        txtMaThuCung.setText("PET" + Ultil.generateRandomCode());
        getListPetCareS(careServiceDAO.getListPetCareService());
        loadCombobox();
    }

    //<editor-fold defaultstate="collapsed" desc="{Pets...">
    public void fillCboTypePet(List<TypePets> list) {
        cboLoaiThuCung.removeAllItems();
        for (TypePets typePet : list) {
            cboLoaiThuCung.addItem(typePet.getTypePetName());
        }
        cboLoaiThuCung.setSelectedIndex(-1);
    }

    private boolean iz = true;

    public void fillFilterTypePet(List<TypePets> list) {
        iz = true;
        cboLocLoaiThuCung.removeAllItems();
        cboLocLoaiThuCung.addItem("Loài");
        for (TypePets typePet : list) {
            if (typePet != null) {
                cboLocLoaiThuCung.addItem(typePet.getTypePetName());  // Đổi thành String
            }
        }
        iz = false;
    }

    public void fillFilterBreedPet(List<Pets> list) {
        iz = true;
        cboLocGiongThuCung.removeAllItems();
        cboLocGiongThuCung.addItem("Giống");

        // Sử dụng Set để lưu các giống không trùng lặp
        Set<String> uniqueBreeds = new HashSet<>();

        // Duyệt qua danh sách pets và thêm các giống vào Set
        for (Pets pet : list) {
            if (pet.getBreed() != null && !pet.getBreed().isEmpty()) {
                uniqueBreeds.add(pet.getBreed());
            }
        }

        // Thêm các giống duy nhất vào ComboBox
        for (String breed : uniqueBreeds) {
            cboLocGiongThuCung.addItem(breed);
        }

        iz = false;
    }

    private void filterBreed() {
        if (iz) {
            return;
        }

        String type = (cboLocLoaiThuCung.getSelectedItem() != null) ? cboLocLoaiThuCung.getSelectedItem().toString() : "";
        String breed = (cboLocGiongThuCung.getSelectedItem() != null) ? cboLocGiongThuCung.getSelectedItem().toString() : "";

        if ("Loài".equalsIgnoreCase(type)) {
            type = null;
        }
        if ("Giống".equalsIgnoreCase(breed)) {
            breed = null;
        }

        List<Pets> filteredPets = petDao.filterPet(type, breed);
        if (filteredPets != null) {
            fillToTable(filteredPets);
        }
    }

    private void filterType() {
        if (iz) {
            return;
        }

        String type = (cboLocLoaiThuCung.getSelectedItem() != null) ? cboLocLoaiThuCung.getSelectedItem().toString() : "";

        if (type == null || type.trim().isEmpty() || "Loài".equalsIgnoreCase(type)) {
            fillToTable(petDao.getList());
        } else {
            List<Pets> filteredPets = petDao.filterPet(type, null);
            if (filteredPets != null) {
                fillToTable(filteredPets);
            }
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
        TypePets loai = typetDao.getTypePetByName(cboLoaiThuCung.getSelectedItem().toString());

        Customers customer = petDao.searchCustomerId(txtThongTinKhachHang.getText());

        p.setPetCode(maThuCung);
        p.setPetName(ten);
        p.setAge(tuoi);
        p.setBreed(giong);
        p.setGender(gender);
        p.setColor(mauSac);
        p.setWeight(canNang);
        p.setVaccinated(tiem);
        p.setTypePet(loai);
        p.setCustomer(customer);
        return p;
    }

    private boolean check() {
        String color = txtMauSac.getText().trim();
        String weight = txtCanNang.getText();
        String age = txtTuoi.getText();
        String breed = txtGiongThuCung.getText();
        String name = txtTenThuCung.getText();

        Object selectTypePet = cboLoaiThuCung.getSelectedItem();

        if (selectTypePet == null) {
            showMessageError("Vui lòng chọn loài thú cưng!");
            return false;
        }
        if (color.isEmpty() || name.isEmpty() || weight.isEmpty()
                || age.isEmpty() || weight.isEmpty() || breed.isEmpty()) {
            showMessageError("Không được để trống các trường dữ liệu!");
            return false;
        }
        if (!breed.matches("[\\p{L}\\s]+")) {
            showMessageError("Trường giống không được phép nhập số và kí tự đặc biệt!");
            return false;
        }
        if (!name.matches("[\\p{L}\\s]+")) {
            showMessageError("Trường tên thú cưng không được phép nhập số và kí tự đặc biệt!");
            return false;
        }
        if (!color.matches("[\\p{L}\\s]+")) {
            showMessageError("Trường màu sắc không được phép nhập số và kí tự đặc biệt!");
            return false;
        }
        if (!weight.matches("^\\d+(\\.\\d+)?$")) {
            showMessageError("Sai định dạng cân nặng (VD :1.5) ");
            return false;
        }
        return true;
    }

    private void getDataTable() {
        int selectedRow = tblPet.getSelectedRow();

        if (selectedRow != -1) {

            String petCode = tblPet.getValueAt(selectedRow, 2).toString();
            String type = tblPet.getValueAt(selectedRow, 3).toString();
            String breed = tblPet.getValueAt(selectedRow, 4).toString();
            String name = tblPet.getValueAt(selectedRow, 5).toString();
            String age = tblPet.getValueAt(selectedRow, 6).toString();
            String weight = tblPet.getValueAt(selectedRow, 7).toString();
            String color = tblPet.getValueAt(selectedRow, 8).toString();
            String genderSt = tblPet.getValueAt(selectedRow, 9).toString();
            boolean gender = genderSt.equalsIgnoreCase("Đực");

            String vaccineSt = tblPet.getValueAt(selectedRow, 10).toString();
            boolean vaccine = vaccineSt.equalsIgnoreCase("Đã tiêm");

            String tenKhachHang = tblPet.getValueAt(selectedRow, 11).toString();
            Customers khachHang = customerDao.searchCustomerByCustomerName(tenKhachHang);

            String owner = tblPet.getValueAt(selectedRow, 12).toString();

            txtMaThuCung.setText(petCode);
            txtGiongThuCung.setText(breed);
            txtTenThuCung.setText(name);
            txtTuoi.setText(age);
            txtCanNang.setText(weight);
            txtMauSac.setText(color);
            cboLoaiThuCung.setSelectedItem(type);
            if (gender) {
                rdoDuc.setSelected(true);
            } else {
                rdoCai.setSelected(true);
            }
            if (vaccine) {
                cbDaTiem.setSelected(true);
            } else {
                cbDaTiem.setSelected(false);
            }
            txtThongTinKhachHang.setText(khachHang.toString());

        }
    }

    private void update() {
        int select = tblPet.getSelectedRow();
        if (!check()) {
            return;
        }

        int id = Integer.parseInt(tblPet.getValueAt(select, 0).toString());

        // Pet ban đầu
        Pets petFrist = petDao.getById(id);
        // Pet người dùng nhập
        Pets p = readForm();
        p.setId(id);
        if (isPetUnchanged(petFrist, p)) {
            showMessageFail("Dữ liệu không thay đổi, không cần cập nhật!");
            return;
        }
        if (petDao.update(p, id)) {
            showMessageSuccess("Cập nhật thành công!");
            fillToTable(petDao.getList());
            fillFilterBreedPet(petDao.getList());
            txtSearchCustomer.setText("");
        } else {
            showMessageError("Cập nhật thất bại!");
        }
    }

    private boolean isPetUnchanged(Pets petFirst, Pets p) {
        return Objects.equals(petFirst.getTypePet().getId(), p.getTypePet().getId())
                && Objects.equals(petFirst.getBreed(), p.getBreed())
                && Objects.equals(petFirst.getPetName(), p.getPetName())
                && Objects.equals(petFirst.getAge(), p.getAge())
                && petFirst.getWeight().compareTo(p.getWeight()) == 0
                && Objects.equals(petFirst.getColor(), p.getColor())
                && petFirst.isGender() == p.isGender()
                && petFirst.isVaccinated() == p.isVaccinated()
                && Objects.equals(petFirst.getCustomer().getId(), p.getCustomer().getId());
    }

    private void insertPet() {
        if (!check()) {
            return;
        }
        if (petDao.insertPet(readForm())) {
            showMessageSuccess("Thêm thành công !");
            fillToTable(petDao.getList());
            fillFilterBreedPet(petDao.getList());
            txtThongTinKhachHang.setText("Khách hàng lẻ");
        } else {
            showMessageFail("Thêm thất bại");
        }
    }

    private void refresh() {
        txtMaThuCung.setText("Không điền thông tin");
        txtMauSac.setText("");
        txtCanNang.setText("");
        txtTuoi.setText("");
        cboLoaiThuCung.setSelectedIndex(-1);
        txtSearchCustomer.setText("");
        txtThongTinKhachHang.setText("Khách hàng lẻ");
        txtTenThuCung.setText("");
        txtGiongThuCung.setText("");
        cbDaTiem.setSelected(false);
        cboLocGiongThuCung.setSelectedItem("Giống");
        cboLocLoaiThuCung.setSelectedItem("Loài");
    }

    private void delete() {
        String id = txtMaThuCung.getText().trim();
        if (petDao.delete(id)) {
            fillToTable(petDao.getList());
            refresh();
            showMessageSuccess("Xóa thành công!");
            fillFilterBreedPet(petDao.getList());
        } else {
            showMessageFail("Xóa không thành công!");
        }
    }

//    private void showPopDeleteHistory() {
//        PopupDeleteHistory deleteHistory = new PopupDeleteHistory();
//        deleteHistory.setConfirmListener(new com.petshop.event.ConfirmListener() {
//            @Override
//            public void onConfirm() {
//                fillToTable(petDao.getList());
//                fillFilterBreedPet(petDao.getList());
//            }
//
//            @Override
//            public void onCancel() {
//                fillToTable(petDao.getList());
//                fillFilterBreedPet(petDao.getList());
//            }
//        });
//        GlassPanePopup.showPopup(deleteHistory, "pdeleteHistory");
//
//    }
    // Tìm kiếm khách hàng
    private void searchCustomer() {
        String search = txtSearchCustomer.getText();

        Customers c = petDao.searchCustomer(search);
        System.out.println(c);
        if (search.isEmpty()) {
            showMessageFail("Chưa nhập thông tin là số điện thoại!");
            return;
        }
        if (c == null) {
            showMessageFail("Số điện thoại không tồn tại : " + search);
            txtSearchCustomer.setText("");
            return;
        }
        txtThongTinKhachHang.setText(c.getCustomerName());
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
                (p.getPetS() != null) ? checkNullOrEmpty(p.getPetS().getServiceCode()) : "Chưa có thông tin",
                (p.getPetS() != null) ? checkNullOrEmpty(p.getPetS().getServiceName()) : "Chưa có thông tin",
                (p.getPet() != null) ? checkNullOrEmpty(p.getPet().getPetCode()) : "Chưa có thông tin",
                (p.getPet() != null) ? checkNullOrEmpty(p.getPet().getPetName()) : "Chưa có thông tin",
                (p.getPet() != null) ? checkNullOrEmpty(p.getPet().getBreed()) : "Chưa có thông tin",
                (p.getDateStart() != null) ? Ultil.getFormatted(p.getDateStart()) : "Chưa có thông tin",
                (p.getDateEnd() != null) ? Ultil.getFormatted(p.getDateEnd()) : "Chưa có thông tin",
                (p.getActualEnd() != null) ? Ultil.getFormatted(p.getActualEnd()) : "Chưa có thông tin", // Định dạng ngày kết thúc thực tế
                checkNullOrEmpty(p.getNote()),
                p.isStatus() ? "Đang thực hiện" : "Đã hoàn thành",
                new ModelAction<>(p, new EventAction<PetCareServices>() {
                    @Override
                    public void delete(PetCareServices p) {
                        // Xử lý xóa
                        showPopupUpdateService(p);
                    }

                    @Override
                    public void update(PetCareServices p) {
                        // Xử lý cập nhật
                    }

                    @Override
                    public void add(PetCareServices model) {
                        // Xử lý thêm mới
                        showPopInfPet(model);
                    }
                })
            });
            stt++;
        }
    }

    private String checkNullOrEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? "Chưa có thông tin" : value;
    }

    private void loadCombobox() {
        cbbStatus.addItem("Tất cả");
        cbbStatus.addItem("Hoàn thành");
        cbbStatus.addItem("Chưa hoàn thành");

        cbbSortDate.addItem("Tất cả");
        cbbSortDate.addItem("1 ngày trước");
        cbbSortDate.addItem("7 ngày trước");
        cbbSortDate.addItem("1 tháng trước");

        // Gắn sự kiện lọc dữ liệu khi chọn giá trị trong ComboBox
        cbbStatus.addActionListener(e -> filterByCBB());
        cbbSortDate.addActionListener(e -> filterByCBB());
    }

    private void filterByCBB() {
        // Xác định khoảng thời gian dựa trên lựa chọn trong cbbSortDate
        int daysAgo = -1; // -1 nghĩa là không lọc theo ngày
        String selectedDate = (String) cbbSortDate.getSelectedItem();
        if ("1 ngày trước".equals(selectedDate)) {
            daysAgo = 1;
        } else if ("7 ngày trước".equals(selectedDate)) {
            daysAgo = 7;
        } else if ("1 tháng trước".equals(selectedDate)) {
            daysAgo = 30;
        }

        // Xác định trạng thái dựa trên cbbStatus
        Boolean isStatus = null; // Mặc định là null (không lọc)
        String selectedStatus = (String) cbbStatus.getSelectedItem();
        if ("Hoàn thành".equals(selectedStatus)) {
            isStatus = true;
        } else if ("Chưa hoàn thành".equals(selectedStatus)) {
            isStatus = false;
        }

        // Gọi DAO để lấy danh sách đã lọc
        List<PetCareServices> list = careServiceDAO.searchServices(daysAgo, isStatus);
        getListPetCareS(list); // Load lại danh sách hiển thị
    }

    private void showPopInfPet(PetCareServices p) {
        PopupShowPet pa = new PopupShowPet(p);

        GlassPanePopup.showPopup(pa, "pShowInfPet");
    }

    private void showPopupUpdateService(PetCareServices p) {
        PopupUpdateService pop = new PopupUpdateService();
        pop.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                updateServicePetCare(p, pop.getTxtNote());
                GlassPanePopup.closePopup("pServiceCare");
            }

            @Override
            public void onCancel() {
                GlassPanePopup.closePopup("pServiceCare");
            }
        });
        GlassPanePopup.showPopup(pop, "pServiceCare");
    }

    private void updateServicePetCare(PetCareServices p, String note) {
        LocalDateTime now = LocalDateTime.now(); // Lấy ngày giờ hiện tại

        if (careServiceDAO.updateActualEndAndNote(p.getId(), now, note)) {
            showMessageSuccess("Cập nhật thành công");
            getListPetCareS(careServiceDAO.getListPetCareService());
        } else {
            showMessageFail("Cập nhật thất bại");
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
        cbbSortDate = new com.petshop.swing.combobox.ComboboxRounded();
        cbbStatus = new com.petshop.swing.combobox.ComboboxRounded();
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
        txtTenThuCung = new com.petshop.swing.textfield.TextFieldRounded();
        txtGiongThuCung = new com.petshop.swing.textfield.TextFieldRounded();
        txtSearchCustomer = new com.petshop.swing.textfield.TextFieldRounded();
        txtMaThuCung = new com.petshop.swing.textfield.TextField();
        jLabel1 = new javax.swing.JLabel();
        txtThongTinKhachHang = new com.petshop.swing.textfield.TextFieldRounded();
        btnCustomerSearch = new com.petshop.swing.Button();
        btnPopupPets1 = new com.petshop.swing.ButtonBadges();
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

        cbbSortDate.setLabeText("Tìm kiếm theo ngày");

        cbbStatus.setLabeText("Trạng thái");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbbSortDate, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(cbbSortDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(textFieldAnimation1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0)
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
                .addGap(0, 46, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Quản lý dịch vụ thú cưng", jPanel1);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtCanNang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCanNang.setLabelText("Cân nặng ( Kg )");

        txtMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMauSac.setLabelText("Màu sắc");

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

        txtTenThuCung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenThuCung.setLabelText("Tên thú cưng");

        txtGiongThuCung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiongThuCung.setLabelText("Giống thú cưng");

        txtSearchCustomer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchCustomer.setLabelText("Thông tin chủ sở hữu");

        txtMaThuCung.setEnabled(false);
        txtMaThuCung.setLabelText("Mã Pet");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÔNG TIN PET");

        txtThongTinKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtThongTinKhachHang.setLabelText("Tên khách hàng");
        txtThongTinKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtThongTinKhachHangActionPerformed(evt);
            }
        });

        btnCustomerSearch.setBackground(new java.awt.Color(0, 153, 153));
        btnCustomerSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-search-15.png"))); // NOI18N
        btnCustomerSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerSearchActionPerformed(evt);
            }
        });

        btnPopupPets1.setBackground(new java.awt.Color(204, 255, 255));
        btnPopupPets1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-add-24 (1).png"))); // NOI18N
        btnPopupPets1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopupPets1ActionPerformed(evt);
            }
        });

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
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(rdoDuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rdoCai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMauSac, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addComponent(txtCanNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(cbDaTiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenThuCung, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(txtTuoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSearchCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                    .addComponent(txtThongTinKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnPopupPets1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTuoi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtGiongThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCanNang, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTenThuCung, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtThongTinKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPopupPets1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        if (tblPet.getColumnModel().getColumnCount() > 0) {
            tblPet.getColumnModel().getColumn(0).setMinWidth(0);
            tblPet.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblPet.getColumnModel().getColumn(0).setMaxWidth(0);
        }

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

        cboLocLoaiThuCung.setLabeText("");
        cboLocLoaiThuCung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocLoaiThuCungActionPerformed(evt);
            }
        });

        cboLocGiongThuCung.setLabeText("");
        cboLocGiongThuCung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocGiongThuCungActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 243, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        if (!check()) {
            return;
        }
        showMessageConfirm("Bạn có chắc chắn muốn thêm không?", () -> {
            insertPet();
        });
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        if (tblPet.getRowCount() == 0) {
            showMessageError("Bảng không có dữ liệu!");
            return;
        }

        int select = tblPet.getSelectedRow();
        if (select == -1) {
            showMessageError("Không có dữ liệu nào được chọn!");
            return;
        }
        showMessageConfirm("Bạn có chắc chắn muốn cập nhật không?", () -> {
            update();
        });
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        refresh();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        if (tblPet.getRowCount() == 0) {
            showMessageError("Bảng không có dữ liệu!");
            return;
        }

        int select = tblPet.getSelectedRow();
        if (select == -1) {
            showMessageError("Không có dữ liệu nào được chọn!");
            return;
        }
        showMessageConfirm("Bạn có chắc chắn muốn cập nhật không?", () -> {
            delete();
        });
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLichSuaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLichSuaXoaActionPerformed
//                showPopDeleteHistory();
    }//GEN-LAST:event_btnLichSuaXoaActionPerformed

    private void cboLocLoaiThuCungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocLoaiThuCungActionPerformed
        filterType();
    }//GEN-LAST:event_cboLocLoaiThuCungActionPerformed

    private void cboLocGiongThuCungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocGiongThuCungActionPerformed
        filterBreed();
    }//GEN-LAST:event_cboLocGiongThuCungActionPerformed

    private void btnCustomerSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerSearchActionPerformed
        searchCustomer();
    }//GEN-LAST:event_btnCustomerSearchActionPerformed

    private void txtThongTinKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtThongTinKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThongTinKhachHangActionPerformed

    private void btnPopupPets1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupPets1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPopupPets1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnCapNhat;
    private com.petshop.swing.Button btnCustomerSearch;
    private com.petshop.swing.Button btnLamMoi;
    private com.petshop.swing.Button btnLichSuaXoa;
    private com.petshop.swing.ButtonBadges btnPopupPets;
    private com.petshop.swing.ButtonBadges btnPopupPets1;
    private com.petshop.swing.Button btnThem;
    private com.petshop.swing.Button btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.petshop.swing.checkbox.JCheckBoxCustom cbDaTiem;
    private com.petshop.swing.combobox.ComboboxRounded cbbSortDate;
    private com.petshop.swing.combobox.ComboboxRounded cbbStatus;
    com.petshop.swing.combobox.Combobox cboLoaiThuCung;
    private com.petshop.swing.combobox.Combobox cboLocGiongThuCung;
    private com.petshop.swing.combobox.Combobox cboLocLoaiThuCung;
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
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextFieldRounded txtSearchCustomer;
    private com.petshop.swing.textfield.TextFieldRounded txtTenThuCung;
    private com.petshop.swing.textfield.TextFieldRounded txtThongTinKhachHang;
    private com.petshop.swing.textfield.TextFieldRounded txtTuoi;
    // End of variables declaration//GEN-END:variables

}
