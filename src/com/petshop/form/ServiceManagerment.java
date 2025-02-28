/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.PetServiceDAO;
import com.petshop.daos.TypeServiceDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.PetServices;
import com.petshop.models.Products;
import com.petshop.models.TypeServices;
import com.petshop.popup.PopupShowHistoryDeleted;
import com.petshop.popup.PopupTypeService;
import com.petshop.services.RememberMeService;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class ServiceManagerment extends javax.swing.JPanel {

    /**
     * Creates new form ServiceManagerment
     */
    private final PetServiceDAO petServiceDAO;
    private final TypeServiceDAO typeServiceDAO;
    private RememberMeService rememberMeService;

    public ServiceManagerment() {
        initComponents();
        tbService.fixTable(jScrollPane4);
        petServiceDAO = new PetServiceDAO();
        typeServiceDAO = new TypeServiceDAO();
        rememberMeService = new RememberMeService();
        init();
    }

    public void init() {
        checkPermission();
        txtServiceCode.setText("SV" + Ultil.generateRandomCode());
        getListService(petServiceDAO.getListServiceAll());
        loadCBBTypeService(typeServiceDAO.getListTypeS());
        loadComboBoxes(typeServiceDAO.getListTypeS());
        loadCBBTimeUnit();
        searchEvent();
        txtServiceCode.setEditable(false);
    }

    private void checkPermission(){
        if(rememberMeService.getEmployeeId()!=1){
            btnEdit.setEnabled(false);
            btnAdd.setEnabled(false);
        }
    }
    
    private void searchEvent() {
        txtSearchService.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchServiceByName(txtSearchService.getText()); // tìm kiếm sau khi ấn, lấy text của thanh search
                    txtSearchService.setText("");
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

    public void showPopUpHistoryDeleted() {
        int stt = 1;
        PopupShowHistoryDeleted popup = new PopupShowHistoryDeleted();
        List<PetServices> petServiceses = petServiceDAO.getListServiceDeleted();
        // Chuyển đổi danh sách sản phẩm thành List<Object[]>
        List<Object[]> data = new ArrayList<>();
        for (PetServices p : petServiceses) {
            data.add(new Object[]{
                stt,
                p.getServiceCode(),
                p.getServiceName(),
                p.getTypeService().getTypeServiceName(),
                p.getDuration(),
                p.getTimeUnit(),
                Ultil.formatCurrency(p.getPriceService()),
                p.getFormattedCreatedAt(),
                new ModelAction<>(p, new EventAction<PetServices>() {
                    @Override
                    public void delete(PetServices p) {
                        showMessageConfirm("Xác nhận khôi phục sản phẩm này", () -> {
                            restoreService(p);
                            reloadTable(popup);
                        });
                    }

                    @Override
                    public void update(PetServices p) {
                    }

                    @Override
                    public void add(PetServices p) {
                    }
                })
            });
            stt++;
        }

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Mã DV", "Tên DV", "Loại DV", "T.gian", "Đơn vị TG", "Giá DV", "Ngày tạo", "Thao tác"};

        // Hiển thị popup
        popup.setLbText("Danh sách dịch vụ đã xóa");
        popup.fillTable(data, columnNames); // Đảm bảo bảng có dữ liệu trước khi hiển thị

        popup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                getListService(petServiceDAO.getListService());
            }
        });
        GlassPanePopup.showPopup(popup);
    }

    private void reloadTable(PopupShowHistoryDeleted popup) {
        int stt = 1;
        List<PetServices> petServiceses = petServiceDAO.getListServiceDeleted();
        // Chuyển đổi danh sách sản phẩm thành List<Object[]>
        List<Object[]> data = new ArrayList<>();
        for (PetServices p : petServiceses) {
            data.add(new Object[]{
                stt,
                p.getServiceCode(),
                p.getServiceName(),
                p.getTypeService().getTypeServiceName(),
                p.getDuration(),
                p.getTimeUnit(),
                Ultil.formatCurrency(p.getPriceService()),
                p.getFormattedCreatedAt(),
                new ModelAction<>(p, new EventAction<PetServices>() {
                    @Override
                    public void delete(PetServices p) {
                        showMessageConfirm("Xác nhận khôi phục sản phẩm này", () -> {
                            restoreService(p);
                            reloadTable(popup);
                        });
                    }

                    @Override
                    public void update(PetServices p) {
                    }

                    @Override
                    public void add(PetServices p) {
                    }
                })
            });
            stt++;
        }

        // Cập nhật lại bảng
        popup.fillTable(data, new String[]{"STT", "Mã DV", "Tên DV", "Loại DV", "T.gian", "Đơn vị TG", "Giá DV", "Ngày tạo", "Thao tác"});
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
            public void onConfirm(int amount) {

            }

            @Override
            public void onCancel() {

            }
        });
        GlassPanePopup.showPopup(input, "input");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Popup...">
    public void popupTypeService() {
        PopupTypeService tPopup = new PopupTypeService();
        tPopup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                loadCBBTypeService(typeServiceDAO.getListTypeS());
            }

            @Override
            public void onCancel() {
                loadCBBTypeService(typeServiceDAO.getListTypeS());
            }
        });
        GlassPanePopup.showPopup(tPopup, "tPopup");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadCBB...">
    public void loadCBBTypeService(List<TypeServices> list) {
        cbbTypeService.removeAllItems();
        for (TypeServices p : list) {
            cbbTypeService.addItem(p);
        }
        cbbTypeService.setSelectedIndex(-1);
    }

    public void loadComboBoxes(List<TypeServices> typeServicesList) {
        // Load combobox loại dịch vụ
        cbbFilterTypeService.removeAllItems();
        cbbFilterTypeService.addItem("Tất cả"); // Thêm mục "Tất cả"
        for (TypeServices type : typeServicesList) {
            cbbFilterTypeService.addItem(type);
        }
        cbbFilterTypeService.setSelectedIndex(0); // Mặc định chọn "Tất cả"

        // Load combobox trạng thái
        cbbFilterStatus.removeAllItems();
        cbbFilterStatus.addItem("Tất cả"); // Thêm mục "Tất cả"
        cbbFilterStatus.addItem("Hoạt động");
        cbbFilterStatus.addItem("Tạm ngưng");
        cbbFilterStatus.setSelectedIndex(0); // Mặc định chọn "Tất cả"

        // Load combobox sắp xếp
        cbbSort.removeAllItems();
        cbbSort.addItem("Tất cả"); // Thêm mục "Tất cả"
        cbbSort.addItem("Theo giá tăng dần");
        cbbSort.addItem("Giá giảm dần");
        cbbSort.setSelectedIndex(0); // Mặc định chọn "Tất cả"

        // Thêm sự kiện lắng nghe cho cả ba combobox
        ActionListener filterListener = e -> getListServiceByFilter();
        cbbFilterTypeService.addActionListener(filterListener);
        cbbFilterStatus.addActionListener(filterListener);
        cbbSort.addActionListener(filterListener);
    }

    public void getListServiceByFilter() {
        Object selectedType = cbbFilterTypeService.getSelectedItem();
        Object selectedStatus = cbbFilterStatus.getSelectedItem();

        Integer typeServiceId = null;
        Boolean status = null; // Null nghĩa là không lọc theo trạng thái

        if (selectedType instanceof TypeServices) {
            typeServiceId = ((TypeServices) selectedType).getId();
        }

        if (!"Tất cả".equals(selectedStatus)) {
            status = "Hoạt động".equals(selectedStatus);
        }

        List<PetServices> filteredList;
        if (typeServiceId == null && status == null) {
            // Nếu cả loại dịch vụ và trạng thái đều là "Tất cả", lấy toàn bộ
            filteredList = petServiceDAO.getListServiceAll();
        } else if (typeServiceId == null) {
            // Nếu chỉ lọc theo trạng thái
            filteredList = petServiceDAO.filterServiceByStatus(status);
        } else if (status == null) {
            // Nếu chỉ lọc theo loại dịch vụ
            filteredList = petServiceDAO.filterServiceByIdTypeService(typeServiceId, true);
        } else {
            // Lọc theo cả loại dịch vụ và trạng thái
            filteredList = petServiceDAO.filterServiceByIdTypeService(typeServiceId, status);
        }

        // Áp dụng sắp xếp
        String sortBy = (String) cbbSort.getSelectedItem();
        if (sortBy != null && !"Tất cả".equals(sortBy)) {
            if (sortBy.equals("Theo giá tăng dần")) {
                filteredList.sort(Comparator.comparing(PetServices::getPriceService));
            } else if (sortBy.equals("Giá giảm dần")) {
                filteredList.sort(Comparator.comparing(PetServices::getPriceService).reversed());
            }
        }

        getListService(filteredList);
    }

    public void loadCBBTimeUnit() {
        cbbTimeUnit.removeAllItems();
        cbbTimeUnit.addItem("Phút");
        cbbTimeUnit.addItem("Giờ");
        cbbTimeUnit.addItem("Ngày");
        cbbTimeUnit.addItem("Tháng");
        cbbTimeUnit.addItem("Năm");
        cbbTimeUnit.setSelectedIndex(-1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Service...">
    public void getListService(List<PetServices> list) {
        tbService.setRowCount(0);
        int stt = 1;
        for (PetServices p : list) {
            tbService.addRow(new Object[]{
                p.getId(),
                stt,
                getSafeValue(p.getServiceCode()),
                getSafeValue(p.getServiceName()),
                (p.getTypeService() != null) ? getSafeValue(p.getTypeService().getTypeServiceName()) : "Chưa có thông tin",
                getSafeValue(p.getDuration()),
                getSafeValue(p.getTimeUnit()),
                getSafeValue(Ultil.formatCurrency(p.getPriceService())),
                getSafeValue(p.getDescribeService()),
                getSafeValue(p.getFormattedCreatedAt()),
                (p.isStatus() ? "Hoạt động" : "Tạm ngưng"),
                new ModelAction<>(p, new EventAction<PetServices>() {
                    @Override
                    public void delete(PetServices p) {
                        showMessageConfirm("Xác nhận xóa", () -> {
                            deleteService(p);
                        });
                    }

                    @Override
                    public void update(PetServices p) {
                    }

                    @Override
                    public void add(PetServices model) {
                    }
                })
            });
            stt++;
        }
    }

    private String getSafeValue(Object value) {
        return (value == null || value.toString().trim().isEmpty()) ? "Chưa có thông tin" : value.toString();
    }

    private PetServices readForm() {
        PetServices p = new PetServices();
        p.setServiceCode("SV"+Ultil.generateRandomCode());
        p.setServiceName(txtServiceName.getText());
        p.setDescribeService(txtDescribeService.getText());
        p.setDuration(Integer.parseInt(txtDuration.getText()));
        BigDecimal priceService = new BigDecimal(txtPriceService.getText().trim());
        p.setPriceService(priceService);

        TypeServices t = (TypeServices) cbbTypeService.getSelectedItem();
        p.setTypeService(t);

        String timeDuration = cbbTimeUnit.getSelectedItem().toString();
        p.setTimeUnit(timeDuration);

        p.setDeleted(false);
        p.setStatus(true);

        return p;
    }

    private PetServices readFormUpdate() {
        PetServices p = new PetServices();
        p.setServiceName(txtServiceName.getText());
        p.setDescribeService(txtDescribeService.getText());
        p.setDuration(Integer.parseInt(txtDuration.getText()));
        BigDecimal priceService = new BigDecimal(txtPriceService.getText().trim());
        p.setPriceService(priceService);

        TypeServices t = (TypeServices) cbbTypeService.getSelectedItem();
        p.setTypeService(t);

        String timeDuration = cbbTimeUnit.getSelectedItem().toString();
        p.setTimeUnit(timeDuration);
        return p;
    }

    private void resetFormService() {
        txtServiceCode.setText("SV" + Ultil.generateRandomCode());
        txtServiceName.setText("");
        txtDuration.setText("");
        txtDescribeService.setText("");
        txtPriceService.setText("");

        cbbTypeService.setSelectedIndex(-1);
        cbbTimeUnit.setSelectedIndex(-1);
        tbService.clearSelection();
    }

    private void showData() {
        int selectedRow = tbService.getSelectedRow();
        if (selectedRow == -1) {
            showMessageFail("Vui lòng chọn một dịch vụ từ bảng!");
            return;
        }

        // Lấy dữ liệu từ bảng dựa vào hàng được chọn
        String serviceCode = tbService.getValueAt(selectedRow, 2).toString();
        String serviceName = tbService.getValueAt(selectedRow, 3).toString();
        String typeServiceName = tbService.getValueAt(selectedRow, 4).toString();
        int duration = Integer.parseInt(tbService.getValueAt(selectedRow, 5).toString());
        String timeUnit = tbService.getValueAt(selectedRow, 6).toString();

        String priceStr = tbService.getValueAt(selectedRow, 7).toString().replace("₫", "").replace(".", "").replace("\u00A0", "").replaceAll("\\s+", "").trim(); // Chỉnh lại cột nếu cần

        priceStr = priceStr.replaceAll("[^0-9]", "");

        if (priceStr.isEmpty()) {
            showMessageFail("Giá dịch vụ không hợp lệ!");
            return;
        }

        BigDecimal priceService = new BigDecimal(priceStr);
        String describeService = tbService.getValueAt(selectedRow, 8).toString();

        // Đưa dữ liệu lên các JTextField
        txtServiceCode.setText(serviceCode);
        txtServiceName.setText(serviceName);
        txtPriceService.setText(priceService.toString().replace("VND", ""));
        txtDuration.setText(String.valueOf(duration));
        txtDescribeService.setText(describeService);

        boolean status = tbService.getValueAt(selectedRow, 10).equals("Hoạt động");
        if (status) {
            btnEditStatus.setText("Tạm ngưng");

        } else {
            btnEditStatus.setText("Tiếp tục DV");

        }

        // Đặt giá trị cho cbbTypeService dựa vào tên loại dịch vụ
        for (int i = 0; i < cbbTypeService.getItemCount(); i++) {
            TypeServices type = (TypeServices) cbbTypeService.getItemAt(i);
            if (type.getTypeServiceName().equals(typeServiceName)) {
                cbbTypeService.setSelectedIndex(i);
                break;
            }
        }

        // Đặt giá trị cho cbbTimeUnit
        for (int i = 0; i < cbbTimeUnit.getItemCount(); i++) {
            if (cbbTimeUnit.getItemAt(i).toString().equals(timeUnit)) {
                cbbTimeUnit.setSelectedIndex(i);
                break;
            }
        }
    }

    public boolean checkService() {
        if (txtServiceCode.getText().trim().isEmpty()) {
            showMessageFail("Mã dịch vụ không được để trống!");
            return false;
        }

        if (petServiceDAO.isServiceCodeExists(txtServiceCode.getText())) {
            showMessageFail("Mã đã tồn tại!");
            return false;
        }

        if (txtServiceName.getText().trim().isEmpty()) {
            showMessageFail("Tên dịch vụ không được để trống!");
            return false;
        }
        if (txtDuration.getText().trim().isEmpty()) {
            showMessageFail("Thời gian dịch vụ không được để trống!");
            return false;
        }
        if (txtDescribeService.getText().isEmpty()) {
            showMessageFail("Mô tả dịch vụ không được để trống!");
            return false;
        }
        try {
            Integer.valueOf(txtDuration.getText().trim());
        } catch (NumberFormatException e) {
            showMessageFail("Thời gian dịch vụ phải là số nguyên!");
            return false;
        }
        if (txtPriceService.getText().trim().isEmpty()) {
            showMessageFail("Giá dịch vụ không được để trống!");
            return false;
        }
        try {
            BigDecimal price = new BigDecimal(txtPriceService.getText().trim());
            if (price.scale() > 0) { // Kiểm tra xem có phải số nguyên không
                showMessageFail("Giá dịch vụ phải là số nguyên!");
                return false;
            }
            if (price.compareTo(BigDecimal.valueOf(1000)) < 0 || price.compareTo(BigDecimal.valueOf(1_000_000_000)) > 0) {
                showMessageFail("Giá dịch vụ phải từ 1,000 đến 1,000,000,000!");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessageFail("Giá dịch vụ phải là số hợp lệ!");
            return false;
        }
        if (cbbTypeService.getSelectedIndex() == -1) {
            showMessageFail("Vui lòng chọn loại dịch vụ!");
            return false;
        }
        if (cbbTimeUnit.getSelectedIndex() == -1) {
            showMessageFail("Vui lòng chọn đơn vị thời gian!");
            return false;
        }
        return true;
    }

    public boolean checkUpdate() {
        if (txtServiceCode.getText().trim().isEmpty()) {
            showMessageFail("Mã dịch vụ không được để trống!");
            return false;
        }

        if (txtServiceName.getText().trim().isEmpty()) {
            showMessageFail("Tên dịch vụ không được để trống!");
            return false;
        }
        if (txtDuration.getText().trim().isEmpty()) {
            showMessageFail("Thời gian dịch vụ không được để trống!");
            return false;
        }
        try {
            Integer.valueOf(txtDuration.getText().trim());
        } catch (NumberFormatException e) {
            showMessageFail("Thời gian dịch vụ phải là số nguyên!");
            return false;
        }
        if (txtPriceService.getText().trim().isEmpty()) {
            showMessageFail("Giá dịch vụ không được để trống!");
            return false;
        }
        try {
            BigDecimal price = new BigDecimal(txtPriceService.getText().trim());
            if (price.scale() > 0) { // Kiểm tra xem có phải số nguyên không
                showMessageFail("Giá dịch vụ phải là số nguyên!");
                return false;
            }
            if (price.compareTo(BigDecimal.valueOf(1000)) < 0 || price.compareTo(BigDecimal.valueOf(1_000_000_000)) > 0) {
                showMessageFail("Giá dịch vụ phải từ 1,000 đến 1,000,000,000!");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessageFail("Giá dịch vụ phải là số hợp lệ!");
            return false;
        }
        if (cbbTypeService.getSelectedIndex() == -1) {
            showMessageFail("Vui lòng chọn loại dịch vụ!");
            return false;
        }
        if (cbbTimeUnit.getSelectedIndex() == -1) {
            showMessageFail("Vui lòng chọn đơn vị thời gian!");
            return false;
        }
        return true;
    }

    public void insertService() {
        if (!checkService()) {
            return;
        }
        if (petServiceDAO.insertPetService(readForm())) {
            showMessageSuccess("Thêm thành công!!");
            getListService(petServiceDAO.getListServiceAll());
            resetFormService();
        } else {
            showMessageFail("Thêm thất bại!!");
        }
    }

    public void deleteService(PetServices p) {
        if (rememberMeService.getEmployeeId() != 1) {
            showMessageFail("Bạn không có quyền xóa!!!");
            return;
        }
        int selectedRow = tbService.getSelectedRow();
        if (selectedRow != -1) {
            int id = p.getId();
            petServiceDAO.deletePetService(id);
            showMessageSuccess("Xóa thành công!");
            getListService(petServiceDAO.getListService());
        } else {
            showMessageFail("Xóa thất bại!");
        }
    }

    public void updateService() {
        if (!checkUpdate()) {
            return;
        }
        int selectedRow = tbService.getSelectedRow();
        int id = (int) tbService.getValueAt(selectedRow, 0);
        if (petServiceDAO.updatePetService(id, readFormUpdate())) {
            showMessageSuccess("Cập nhập thành công!");
            getListService(petServiceDAO.getListService());
        } else {
            showMessageFail("Cập nhập thất bại!!!");
        }
    }

    public void updateStatusService() {
        // TODO add your handling code here:
        int selectRow = tbService.getSelectedRow();
        if (selectRow == -1) {
            showMessageFail("Vui lòng chọn thông tin dịch vụ!!");
            return;
        }
        int id = (int) tbService.getValueAt(selectRow, 0);
        boolean status = tbService.getValueAt(selectRow, 10).equals("Tạm ngưng");
        petServiceDAO.updateStatusService(id, status);
        getListService(petServiceDAO.getListService());
    }

    public void searchServiceByName(String keyword) {
        List<PetServices> list = petServiceDAO.searchByServiceNameOrCode(keyword);
        if (list.isEmpty()) {
            showMessageFail("Không tìm thấy!");
        } else {
            getListService(list);
        }
    }

    private void restoreService(PetServices p) {
        petServiceDAO.restoreService(p.getId());
        getListService(petServiceDAO.getListService());
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

        materialTabbed1 = new com.petshop.swing.tabbed.MaterialTabbed();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        txtServiceCode = new com.petshop.swing.textfield.TextFieldRounded();
        txtServiceName = new com.petshop.swing.textfield.TextFieldRounded();
        button2 = new com.petshop.swing.Button();
        txtDuration = new com.petshop.swing.textfield.TextFieldRounded();
        txtPriceService = new com.petshop.swing.textfield.TextFieldRounded();
        textAreaScroll1 = new com.petshop.swing.textarea.TextAreaScroll();
        txtDescribeService = new com.petshop.swing.textarea.TextArea();
        cbbTypeService = new com.petshop.swing.combobox.Combobox();
        cbbTimeUnit = new com.petshop.swing.combobox.Combobox();
        jPanel9 = new javax.swing.JPanel();
        btnAdd = new com.petshop.swing.Button();
        btnEdit = new com.petshop.swing.Button();
        btnReset = new com.petshop.swing.Button();
        button7 = new com.petshop.swing.Button();
        btnEditStatus = new com.petshop.swing.Button();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbService = new com.petshop.swing.table.Table();
        jLabel10 = new javax.swing.JLabel();
        cbbSort = new com.petshop.swing.combobox.ComboboxRounded();
        txtSearchService = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterStatus = new com.petshop.swing.combobox.Combobox();
        cbbFilterTypeService = new com.petshop.swing.combobox.Combobox();

        setBackground(new java.awt.Color(245, 245, 245));
        setMaximumSize(new java.awt.Dimension(1058, 741));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        materialTabbed1.setBackground(new java.awt.Color(255, 255, 255));
        materialTabbed1.setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Thông tin dịch vụ");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        txtServiceCode.setLabelText("Mã dịch vụ");

        txtServiceName.setLabelText("Tên dịch vụ");

        button2.setBackground(new java.awt.Color(204, 255, 255));
        button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-add-24 (1).png"))); // NOI18N
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        txtDuration.setLabelText("Thời gian");
        txtDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDurationActionPerformed(evt);
            }
        });

        txtPriceService.setLabelText("Giá dịch vụ");

        textAreaScroll1.setLabelText("Mô tả dịch vụ");

        txtDescribeService.setColumns(20);
        txtDescribeService.setRows(5);
        textAreaScroll1.setViewportView(txtDescribeService);

        cbbTypeService.setLabeText("Loại dịch vụ");

        cbbTimeUnit.setLabeText("Đơn vị thời gian");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtServiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtServiceName, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(cbbTypeService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(cbbTimeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPriceService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbTimeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtServiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtServiceName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPriceService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(cbbTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 22, Short.MAX_VALUE))
                    .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAdd.setBackground(new java.awt.Color(204, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 255, 204));
        btnEdit.setText("Sửa");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(255, 204, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-restore-20.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        button7.setBackground(new java.awt.Color(255, 204, 204));
        button7.setText("Lịch sử đã xóa");
        button7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button7ActionPerformed(evt);
            }
        });

        btnEditStatus.setBackground(new java.awt.Color(255, 102, 102));
        btnEditStatus.setText("        ");
        btnEditStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        tbService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "STT", "Mã dịch vụ", "Tên dịch vụ", "Loại dịch vụ", "T.gian", "Đơn vị thời gian", "Giá", "Mô tả dịch vụ", "Ngày tạo", "Trạng thái", "Thao tác"
            }
        ));
        tbService.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbServiceMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbService);
        if (tbService.getColumnModel().getColumnCount() > 0) {
            tbService.getColumnModel().getColumn(0).setMinWidth(0);
            tbService.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbService.getColumnModel().getColumn(0).setMaxWidth(0);
            tbService.getColumnModel().getColumn(1).setMinWidth(40);
            tbService.getColumnModel().getColumn(1).setMaxWidth(40);
            tbService.getColumnModel().getColumn(5).setMinWidth(60);
            tbService.getColumnModel().getColumn(5).setMaxWidth(60);
            tbService.getColumnModel().getColumn(8).setMinWidth(200);
            tbService.getColumnModel().getColumn(8).setMaxWidth(200);
        }

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel10.setText("Danh sách dịch vụ");

        cbbSort.setLabeText("Sắp xếp theo");

        txtSearchService.setBackground(new java.awt.Color(250, 250, 250));

        cbbFilterStatus.setLabeText("Trạng thái");

        cbbFilterTypeService.setLabeText("Loại dịch vụ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbbFilterTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbbFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearchService, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSearchService, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Quản lý dịch vụ", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(materialTabbed1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDurationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDurationActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
        popupTypeService();
    }//GEN-LAST:event_button2ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        resetFormService();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận thêm?", () -> {
            insertService();
        });
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditStatusActionPerformed
        // TODO add your handling code here:
        updateStatusService();
    }//GEN-LAST:event_btnEditStatusActionPerformed

    private void tbServiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbServiceMouseClicked
        // TODO add your handling code here:
        showData();
    }//GEN-LAST:event_tbServiceMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        updateService();
    }//GEN-LAST:event_btnEditActionPerformed

    private void button7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button7ActionPerformed
        // TODO add your handling code here:
        showPopUpHistoryDeleted();
    }//GEN-LAST:event_button7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnAdd;
    private com.petshop.swing.Button btnEdit;
    private com.petshop.swing.Button btnEditStatus;
    private com.petshop.swing.Button btnReset;
    private com.petshop.swing.Button button2;
    private com.petshop.swing.Button button7;
    private com.petshop.swing.combobox.Combobox cbbFilterStatus;
    private com.petshop.swing.combobox.Combobox cbbFilterTypeService;
    private com.petshop.swing.combobox.ComboboxRounded cbbSort;
    private com.petshop.swing.combobox.Combobox cbbTimeUnit;
    private com.petshop.swing.combobox.Combobox cbbTypeService;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.table.Table tbService;
    private com.petshop.swing.textarea.TextAreaScroll textAreaScroll1;
    private com.petshop.swing.textarea.TextArea txtDescribeService;
    private com.petshop.swing.textfield.TextFieldRounded txtDuration;
    private com.petshop.swing.textfield.TextFieldRounded txtPriceService;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchService;
    private com.petshop.swing.textfield.TextFieldRounded txtServiceCode;
    private com.petshop.swing.textfield.TextFieldRounded txtServiceName;
    // End of variables declaration//GEN-END:variables
}
