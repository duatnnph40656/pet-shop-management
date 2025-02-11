/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.PetServiceDAO;
import com.petshop.daos.TypeServiceDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.PetService;
import com.petshop.models.TypeService;
import com.petshop.popup.PopupTypeService;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
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

    public ServiceManagerment() {
        initComponents();
        table1.fixTable(jScrollPane1);
        table2.fixTable(jScrollPane2);
        table3.fixTable(jScrollPane3);
        tbService.fixTable(jScrollPane4);
        petServiceDAO = new PetServiceDAO();
        typeServiceDAO = new TypeServiceDAO();
        init();
    }

    public void init() {
        txtServiceCode.setText("SV" + Ultil.generateRandomCode());
        getListService(petServiceDAO.getList());
        loadCBBTypeService(typeServiceDAO.getListTypeS());
        loadCbbFilterTypeService(typeServiceDAO.getListTypeS());
        loadCBBTimeUnit();
        loadCbbFilterStatus();
        searchEvent();
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

    //<editor-fold defaultstate="collapsed" desc="Popup...">
    public void popupTypeService() {
        PopupTypeService tPopup = new PopupTypeService();
        tPopup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
        GlassPanePopup.showPopup(tPopup, "tPopup");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadCBB...">
    public void loadCBBTypeService(List<TypeService> list) {
        cbbTypeService.removeAllItems();
        for (TypeService p : list) {
            cbbTypeService.addItem(p);
        }
        cbbTypeService.setSelectedIndex(-1);
    }

    public void loadCbbFilterTypeService(List<TypeService> list) {
        cbbFilterTypeService.removeAllItems();
        for (TypeService p : list) {
            cbbFilterTypeService.addItem(p);
        }
        cbbFilterTypeService.setSelectedIndex(-1);

        cbbFilterTypeService.addActionListener(e -> getListServiceByFilter());

    }

    public void loadCbbFilterStatus() {
        cbbFilterStatus.removeAllItems();
        cbbFilterStatus.addItem("Hoạt động");
        cbbFilterStatus.addItem("Ngưng hoạt động");
        cbbFilterStatus.setSelectedIndex(0);

        cbbFilterStatus.addActionListener(e -> getListServiceByFilter());
    }

    public void getListServiceByFilter() {
        TypeService t = (TypeService) cbbFilterTypeService.getSelectedItem();
        boolean status = cbbFilterStatus.getSelectedItem().equals("Hoạt động");

        System.out.println(status);
        Integer typeServiceId = (t != null) ? t.getId() : null;
        
        List<PetService> filteredList;
        if (typeServiceId == null || status == true) {
            filteredList = petServiceDAO.getList();
        } else {
            filteredList = petServiceDAO.filterServiceByIdTypeService(typeServiceId, status);
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
        cbbFilterStatus.setSelectedIndex(-1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Service...">
    public void getListService(List<PetService> list) {
        tbService.setRowCount(0);
        int stt = 1;
        for (PetService p : list) {
            tbService.addRow(new Object[]{
                p.getId(),
                stt,
                p.getSerivce_code(),
                p.getServce_name(),
                p.getTypeService().getTypeServiceName(),
                p.getDuration(),
                p.getTime_unit(),
                p.getFormattedPriceService(),
                p.getDescribe_service(),
                p.getFormattedCreatedAt(),
                p.isStatus() ? "Hoạt động" : "Tạm ngưng",
                new ModelAction<>(p, new EventAction<PetService>() {
                    @Override
                    public void delete(PetService p) {
                        showMessageConfirm("Xác nhận xóa", () -> {
                            deleteService(p);
                        });
                    }

                    @Override
                    public void update(PetService p) {

                    }
                })
            });
            stt++;
        }
    }

    private PetService readForm() {
        PetService p = new PetService();
        p.setSerivce_code(txtServiceCode.getText());
        p.setServce_name(txtServiceName.getText());
        p.setDescribe_service(txtDescribeService.getText());
        p.setDuration(Integer.parseInt(txtDuration.getText()));
        BigDecimal priceService = new BigDecimal(txtPriceService.getText().trim());
        p.setPriceService(priceService);

        TypeService t = (TypeService) cbbTypeService.getSelectedItem();
        p.setTypeService(t);

        String timeDuration = cbbTimeUnit.getSelectedItem().toString();
        p.setTime_unit(timeDuration);

        p.setDeleted(false);
        p.setStatus(true);

        return p;
    }

    private PetService readFormUpdate() {
        PetService p = new PetService();
        p.setServce_name(txtServiceName.getText());
        p.setDescribe_service(txtDescribeService.getText());
        p.setDuration(Integer.parseInt(txtDuration.getText()));
        BigDecimal priceService = new BigDecimal(txtPriceService.getText().trim());
        p.setPriceService(priceService);

        TypeService t = (TypeService) cbbTypeService.getSelectedItem();
        p.setTypeService(t);

        String timeDuration = cbbTimeUnit.getSelectedItem().toString();
        p.setTime_unit(timeDuration);
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

        String priceStr = tbService.getValueAt(selectedRow, 7).toString().trim(); // Chỉnh lại cột nếu cần

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
            btnEditStatus.setText("Tạm ngưng dịch vụ");

        } else {
            btnEditStatus.setText("Tiếp tục DV");

        }

        // Đặt giá trị cho cbbTypeService dựa vào tên loại dịch vụ
        for (int i = 0; i < cbbTypeService.getItemCount(); i++) {
            TypeService type = (TypeService) cbbTypeService.getItemAt(i);
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
            getListService(petServiceDAO.getList());
            resetFormService();
        } else {
            showMessageFail("Thêm thất bại!!");
        }
    }

    public void deleteService(PetService p) {
        int selectedRow = tbService.getSelectedRow();
        if (selectedRow != -1) {
            int id = p.getId();
            petServiceDAO.deletePetService(id);
            showMessageSuccess("Xóa thành công!");
            getListService(petServiceDAO.getList());
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
            getListService(petServiceDAO.getList());
        } else {
            showMessageFail("Cập nhập thất bại!!!");
        }
    }

    public void updateStatusService() {
        // TODO add your handling code here:
        int selectRow = tbService.getSelectedRow();
        int id = (int) tbService.getValueAt(selectRow, 0);
        boolean status = tbService.getValueAt(selectRow, 10).equals("Tạm ngưng");
        petServiceDAO.updateStatusService(id, status);
        getListService(petServiceDAO.getList());
    }

    public void searchServiceByName(String keyword) {
        List<PetService> list = petServiceDAO.searchByServiceNameOrCode(keyword);
        if (list.isEmpty()) {
            showMessageFail("Không tìm thấy!");
        } else {
            getListService(list);
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

        materialTabbed1 = new com.petshop.swing.tabbed.MaterialTabbed();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new com.petshop.swing.table.Table();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table2 = new com.petshop.swing.table.Table();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table3 = new com.petshop.swing.table.Table();
        jLabel8 = new javax.swing.JLabel();
        textFieldAnimation1 = new com.petshop.swing.textfield.TextFieldAnimation();
        comboboxRounded1 = new com.petshop.swing.combobox.ComboboxRounded();
        comboboxRounded2 = new com.petshop.swing.combobox.ComboboxRounded();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        button1 = new com.petshop.swing.Button();
        jLabel3 = new javax.swing.JLabel();
        textFieldSuggestion1 = new com.petshop.swing.textfield_suggestion.TextFieldSuggestion();
        button6 = new com.petshop.swing.Button();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        button8 = new com.petshop.swing.Button();
        button9 = new com.petshop.swing.Button();
        button10 = new com.petshop.swing.Button();
        comboboxRounded7 = new com.petshop.swing.combobox.ComboboxRounded();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
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
        comboboxRounded6 = new com.petshop.swing.combobox.ComboboxRounded();
        txtSearchService = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterStatus = new com.petshop.swing.combobox.Combobox();
        cbbFilterTypeService = new com.petshop.swing.combobox.Combobox();

        setMaximumSize(new java.awt.Dimension(1058, 741));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        materialTabbed1.setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel1.setText("HÓA ĐƠN CHỜ");

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã hóa đơn", "Tên khách hàng", "Số điện thoại", "Tên nhân viên", "Trạng thái"
            }
        ));
        jScrollPane1.setViewportView(table1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên dịch vụ", "Tên thú cưng", "Thời gian", "Đơn vị thời gian", "Số lượng", "Đơn giá", "Thao tác"
            }
        ));
        jScrollPane2.setViewportView(table2);

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel7.setText("HÓA ĐƠN CHI TIẾT");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        table3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã dịch vụ", "Tên dịch vụ", "Loại dịch vụ", "Thời gian", "Đơn vị thời gian", "Giá", "Mô tả", "Trạng thái", "Thao tác"
            }
        ));
        jScrollPane3.setViewportView(table3);

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel8.setText("DANH SÁCH DỊCH VỤ");

        textFieldAnimation1.setBackground(new java.awt.Color(250, 250, 250));

        comboboxRounded1.setLabeText("Sắp xếp theo");

        comboboxRounded2.setLabeText("Loại dịch vụ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(126, 126, 126)
                        .addComponent(comboboxRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(comboboxRounded1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldAnimation1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(textFieldAnimation1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboboxRounded1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboboxRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Hóa đơn");

        button1.setBackground(new java.awt.Color(0, 255, 255));
        button1.setText("Thanh toán");

        jLabel3.setText("Nhập số điện thại khách hàng:");

        button6.setBackground(new java.awt.Color(204, 255, 255));
        button6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-search-15.png"))); // NOI18N

        jLabel4.setText("Mã kh  :");

        jLabel5.setText("Tên kh : ");

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("KH01");

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Nguyễn Nông Duật");

        button8.setBackground(new java.awt.Color(204, 255, 255));
        button8.setText("Tạo hóa đơn");

        button9.setBackground(new java.awt.Color(255, 204, 204));
        button9.setText("Hủy hóa đơn");

        button10.setBackground(new java.awt.Color(255, 255, 204));
        button10.setText("Làm mới");

        comboboxRounded7.setLabeText("Hình thức thanh toán");
        comboboxRounded7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxRounded7ActionPerformed(evt);
            }
        });

        jLabel12.setText("Tổng tiền :");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("VND");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Thành tiền:");

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 51, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("VND");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboboxRounded7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textFieldSuggestion1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldSuggestion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboboxRounded7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(26, 26, 26)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Dịch vụ", jPanel1);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Thông tin dịch vụ");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtServiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtServiceName, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(cbbTimeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(cbbTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(txtPriceService, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
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
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtPriceService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbbTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
        btnReset.setText("Làm mới");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        button7.setText("Lịch sử đã xóa");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
        }

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel10.setText("Danh sách dịch vụ");

        comboboxRounded6.setLabeText("Sắp xếp theo");

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
                        .addComponent(cbbFilterTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbbFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboboxRounded6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(comboboxRounded6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterTypeService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSearchService, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 2, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addContainerGap(30, Short.MAX_VALUE))
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

    private void comboboxRounded7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxRounded7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboboxRounded7ActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnAdd;
    private com.petshop.swing.Button btnEdit;
    private com.petshop.swing.Button btnEditStatus;
    private com.petshop.swing.Button btnReset;
    private com.petshop.swing.Button button1;
    private com.petshop.swing.Button button10;
    private com.petshop.swing.Button button2;
    private com.petshop.swing.Button button6;
    private com.petshop.swing.Button button7;
    private com.petshop.swing.Button button8;
    private com.petshop.swing.Button button9;
    private com.petshop.swing.combobox.Combobox cbbFilterStatus;
    private com.petshop.swing.combobox.Combobox cbbFilterTypeService;
    private com.petshop.swing.combobox.Combobox cbbTimeUnit;
    private com.petshop.swing.combobox.Combobox cbbTypeService;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded1;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded2;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded6;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.table.Table table1;
    private com.petshop.swing.table.Table table2;
    private com.petshop.swing.table.Table table3;
    private com.petshop.swing.table.Table tbService;
    private com.petshop.swing.textarea.TextAreaScroll textAreaScroll1;
    private com.petshop.swing.textfield.TextFieldAnimation textFieldAnimation1;
    private com.petshop.swing.textfield_suggestion.TextFieldSuggestion textFieldSuggestion1;
    private com.petshop.swing.textarea.TextArea txtDescribeService;
    private com.petshop.swing.textfield.TextFieldRounded txtDuration;
    private com.petshop.swing.textfield.TextFieldRounded txtPriceService;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchService;
    private com.petshop.swing.textfield.TextFieldRounded txtServiceCode;
    private com.petshop.swing.textfield.TextFieldRounded txtServiceName;
    // End of variables declaration//GEN-END:variables
}
