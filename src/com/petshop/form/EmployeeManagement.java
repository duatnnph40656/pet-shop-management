/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.EmployeeDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Employees;
import com.petshop.models.Products;
import com.petshop.models.Roles;
import com.petshop.popup.PopupShowHistoryDeleted;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import javax.management.relation.Role;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class EmployeeManagement extends javax.swing.JPanel {

    private final EmployeeDAO employeeDao;

    /**
     * Creates new form EmployeesManagement
     */
    public EmployeeManagement() {
        initComponents();
        employeeDao = new EmployeeDAO();
        tblnhanvien.fixTable(jScrollPane1);
        txtSearch.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchByNameOrCode(txtSearch.getText());
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {

            }
        });

        init();
    }

    public void init() {
        fillTable(employeeDao.getListEmployee());
        resetFields();
        txtMaNhanVien.setEditable(false);
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

    public void searchByNameOrCode(String keyword) {
        List<Employees> list = employeeDao.searchEmployee(keyword);
        fillTable(list);
    }

    private void fillTable(List<Employees> list) {
        int stt = 1;
        tblnhanvien.setRowCount(0);
        // Lấy danh sách nhân viên từ DB
        for (Employees emp : list) {

            tblnhanvien.addRow(new Object[]{
                emp.getId(),
                stt, // 0 - ID
                emp.getEmployeeCode() == null ? "Chua co thong tin" : emp.getEmployeeCode(), // 1 - Mã nhân viên
                emp.getEmployeeName() == null ? "Chua co thong tin" : emp.getEmployeeName(), // 2 - Tên nhân viên
                emp.getEmail() == null ? "Chua co thong tin" : emp.getEmail(), // 4 - Email
                emp.isGender() ? "Nam" : "Nu", // 7 - Giới tính (Nam/Nữ)
                emp.getPhoneNumber() == null ? "Chua co thong tin" : emp.getPhoneNumber(), // 3 - Số điện thoại
                emp.getRole().getId() == 2 ? "Nhan Vien" : "Quan Ly", // 8 - Chức vụ (Nhân viên/Quản lý)
                emp.getCreatedAt() == null ? "Chua co thong tin" : emp.getCreatedAt(), // 6 - Ngày tạo
                emp.getAddress() == null ? "Chua co thong tin" : emp.getAddress(), // 5 - Địa chỉ         
                emp.isStatus() ? "Hoat dong" : "Ngung hoat dong",
                new ModelAction<>(emp, new EventAction<Employees>() {
                    @Override
                    public void delete(Employees e) {
                        showMessageConfirm("Xác nhận xóa nhân viên?", () -> {
                            deleteEmployee();
                        });
                    }

                    @Override
                    public void update(Employees e) {

                    }

                    @Override
                    public void add(Employees e) {
                    }
                })
            });
            stt++;
        }
    }

    private void resetFields() {
        // Xóa các trường nhập liệu
        txtMaNhanVien.setText("NV" + Ultil.generateRandomCode());
        txtTenNhanVien.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNgayTao.setText("");

        rdNam.setSelected(false);
        rdNu.setSelected(false);

        // Đặt lại trạng thái checkbox
        // Nếu có trường hợp khác cần đặt lại, bạn cũng có thể thêm vào đây
    }

    public int getSelectedRowEmployee() {
        return tblnhanvien.getSelectedRow();
    }

    public Integer getIdSelectedEmployee() {
        int selectedRow = getSelectedRowEmployee();
        if (selectedRow == -1) {
            return null; // Không có dòng nào được chọn
        }
        return (Integer) tblnhanvien.getValueAt(selectedRow, 0); // Cột 0 chứa ID hóa đơn
    }

    private Employees readForm() {
        Employees employee = new Employees();
        employee.setEmployeeCode(txtMaNhanVien.getText());
        employee.setEmployeeName(txtTenNhanVien.getText());
        employee.setPhoneNumber(txtSDT.getText());
        employee.setEmail(txtEmail.getText());

        Roles role = new Roles();
        role.setId(2);
        employee.setRole(role);

        employee.setAddress(txtDiaChi.getText());

        if (rdNam.isSelected()) {
            employee.setGender(true);
        } else if (rdNu.isSelected()) {
            employee.setGender(false);
        } else {
            // Trường hợp không chọn gì cả (nếu cần)
        }

        return employee;
    }

    private Employees readFormUpdate() {
        Employees employee = new Employees();
        employee.setEmployeeName(txtTenNhanVien.getText());
        employee.setPhoneNumber(txtSDT.getText());
        employee.setEmail(txtEmail.getText());
        employee.setAddress(txtDiaChi.getText());
        employee.setGender(rdNam.isSelected());
        return employee;
    }

    private void showDataEmployee() {
        int selectedRow = tblnhanvien.getSelectedRow();
        if (selectedRow == -1) {
            return; // Không chọn dòng nào thì thoát
        }

        String statusValue = tblnhanvien.getValueAt(selectedRow, 10).toString();
        System.out.println(statusValue);

        // Lấy dữ liệu từ bảng (Dựa theo vị trí cột)
        String employeeCode = tblnhanvien.getValueAt(selectedRow, 2).toString();
        String employeeName = tblnhanvien.getValueAt(selectedRow, 3).toString();
        String email = tblnhanvien.getValueAt(selectedRow, 4).toString();
        String sexText = tblnhanvien.getValueAt(selectedRow, 5).toString();  // Hiển thị "Nam" hoặc "Nữ"
        String phoneNumber = tblnhanvien.getValueAt(selectedRow, 6).toString();

        String createdAt = tblnhanvien.getValueAt(selectedRow, 8).toString();
        String address = tblnhanvien.getValueAt(selectedRow, 9).toString();

        // Đưa dữ liệu lên form nhập liệu
        txtMaNhanVien.setText(employeeCode);
        txtTenNhanVien.setText(employeeName);
        txtSDT.setText(phoneNumber);
        txtEmail.setText(email);
        txtDiaChi.setText(address);
        txtNgayTao.setText(createdAt);

        if (sexText.equalsIgnoreCase("Nam")) {
            this.rdNam.setSelected(true);
        } else {
            this.rdNu.setSelected(true);
        }

    }

    private void fillDeletedEmployeeTable(List<Employees> list) {
        int stt = 1;
        PopupShowHistoryDeleted popup = new PopupShowHistoryDeleted();
        List<Employees> employee = employeeDao.getListEmployeeDeleted();
        List<Object[]> data = new ArrayList<>();
        for (Employees c : employee) {
            data.add(new Object[]{
                stt,
                c.getEmployeeCode(),
                c.getEmployeeName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.isGender() ? "Nam" : "Nu",
                c.getRole().getRoleName(),
                c.getAddress(),
                c.getCreatedAt(),
                c.isStatus() ? "Hoat Dong" : "Ngung Hoat Dong",
                new ModelAction<>(c, new EventAction<Employees>() {
                    @Override
                    public void delete(Employees employees) {
                        showMessageConfirm("Xác nhận khôi phục khách hàng này?", () -> {
                            restoreCustomer(employees);
                            reloadTableCustomer(popup); // Gọi lại để cập nhật giao diện
                        });
                    }

                    @Override
                    public void update(Employees employees) {
                    }

                    @Override
                    public void add(Employees model) {
                    }

                })
            });
            stt++;
        }
        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Mã KH", "Họ tên", "Email", "SĐT", "Giới tính", "Cong Viec", "Địa chỉ", "Ngày Tạo", "Trạng thái", "Thao tác"};

        // Hiển thị popup
        popup.setLbText("Danh sách khách hàng đã xóa");
        popup.fillTable(data, columnNames); // Đảm bảo bảng có dữ liệu trước khi hiển thị

        popup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                fillTable(employeeDao.getListEmployee());
            }
        });
        GlassPanePopup.showPopup(popup);

//        for (Employees emp : list) {
//            tblnhanvien.addRow(new Object[]{
//                emp.getId(),
//                stt++, // Số thứ tự
//                emp.getEmployeeCode(),
//                emp.getEmployeeName(),
//                emp.getEmail(),
//                emp.getPhoneNumber(),
//                emp.isGender() ? "Nam" : "Nữ",
//                emp.getRole().getRoleName(),
//                emp.getAddress(),
//                emp.getCreatedAt(),
//                emp.isStatus() ? "Hoạt động" : "Ngừng hoạt động",
//                "Đã Xóa" // Trạng thái để dễ nhận diện
//            });
//        }
    }

    private void restoreCustomer(Employees employees) {
        employeeDao.restoreEmployee(employees.getId());
        fillTable(employeeDao.getListEmployee());
    }

    private void reloadTableCustomer(PopupShowHistoryDeleted popup) {
        int stt = 1;
        List<Employees> cus = employeeDao.getListEmployeeDeleted();
        List<Object[]> data = new ArrayList<>();

        for (Employees customer : cus) {
            data.add(new Object[]{
                stt,
                customer.getEmployeeCode(),
                customer.getEmployeeName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.isGender() ? "Nam" : "Nữ",
                customer.isGender() ? "Nam" : "Nu",
                customer.getAddress(),
                customer.getCreatedAt(),
                customer.isStatus() ? "Đã kích hoạt" : "Chưa kích hoạt",
                new ModelAction<>(customer, new EventAction<Employees>() {
                    @Override
                    public void delete(Employees customer) {
                        showMessageConfirm("Xác nhận khôi phục khách hàng này?", () -> {
                            restoreCustomer(customer);
                            reloadTableCustomer(popup); // Gọi lại để cập nhật giao diện
                        });
                    }

                    @Override
                    public void update(Employees customer) {
                    }

                    @Override
                    public void add(Employees model) {
                    }
                })
            });
            stt++;
        }

        String[] columnNames = {"STT", "Mã KH", "Họ tên", "Email", "SĐT", "Giới tính", "Cong Viec", "Địa chỉ", "Ngày Tạo", "Trạng thái", "Thao tác"};

        popup.fillTable(data, columnNames); // Cập nhật lại dữ liệu bảng
    }

    private void restoreEmployees() {
        int selectedRow = tblnhanvien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để khôi phục!");
            return;
        }

        int id = (int) tblnhanvien.getValueAt(selectedRow, 0);

        boolean success = employeeDao.restoreEmployee(id);

        if (success) {
            JOptionPane.showMessageDialog(this, "Khôi phục nhân viên thành công!");
            fillDeletedEmployeeTable(employeeDao.getListEmployeeDeleted());
        } else {
            JOptionPane.showMessageDialog(this, "Khôi phục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployeesDataToTable(List<Employees> list) {
        tblnhanvien.setRowCount(0); // Xóa dữ liệu cũ trên bảng
        int stt = 1;
        for (Employees emp : list) {
            tblnhanvien.addRow(new Object[]{
                stt++,
                emp.getEmployeeCode(),
                emp.getEmployeeName(),
                emp.getEmail(),
                emp.isGender() ? "Nam" : "Nữ",
                emp.getPhoneNumber(),
                emp.getRole().getRoleName(),
                emp.getCreatedAt(),
                emp.getAddress(),
                emp.isStatus()
            });
        }
    }

    private boolean check() {
        // Kiểm tra trống
        if (txtMaNhanVien.getText().trim().isEmpty()) {
            showMessageFail("Mã nhân viên không được để trống!");
            return false;
        }
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            showMessageFail("Tên nhân viên không được để trống!");
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            showMessageFail("Email không được để trống!");
            return false;
        }
        if (txtSDT.getText().trim().isEmpty()) {
            showMessageFail("Số điện thoại không được để trống!");
            return false;
        }
        if (txtDiaChi.getText().trim().isEmpty()) {
            showMessageFail("Địa chỉ không được để trống!");
            return false;
        }
        if (!rdNam.isSelected() && !rdNu.isSelected()) {
            showMessageFail("Vui lòng chọn giới tính!");
            return false;
        }

        // Kiểm tra tên không chứa số hoặc ký tự đặc biệt
        String tenNV = txtTenNhanVien.getText().trim();
        String tenRegex = "^[a-zA-ZÀ-Ỹà-ỹ\\s]+$"; // Chỉ chứa chữ cái và khoảng trắng
        if (!tenNV.matches(tenRegex)) {
            showMessageFail("Tên nhân viên không hợp lệ! (Chỉ chứa chữ cái và khoảng trắng)");
            return false;
        }

        // Kiểm tra email đúng định dạng
        String email = txtEmail.getText().trim();
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            showMessageFail("Email không hợp lệ!");
            return false;
        }

        // Kiểm tra số điện thoại đúng định dạng (10 số, bắt đầu bằng 03, 05, 07, 08, 09)
        String sdt = txtSDT.getText().trim();
        String sdtRegex = "^(03|05|07|08|09)[0-9]{8}$";
        if (!sdt.matches(sdtRegex)) {
            showMessageFail("Số điện thoại không hợp lệ! (VD: 0365190926)");
            return false;
        }

        return true; // Nếu tất cả hợp lệ
    }

    public void insertEmployee() {
        if (!check()) {
            return;
        }
        if (employeeDao.insertEmployee(readForm())) {
            showMessageSuccess("Thêm thành công!");
            fillTable(employeeDao.getListEmployee());
        } else {
            showMessageFail("Thêm thất bại");
        }
    }

    public void updateEmployee() {
        if (!check()) {
            return;
        }
        if (employeeDao.updateEmployee(getIdSelectedEmployee(), readFormUpdate())) {
            showMessageSuccess("Update nhân viên thành công!!");
            fillTable(employeeDao.getListEmployee());
        } else {
            showMessageFail("Update thất bại");
        }
    }

    public void deleteEmployee() {
        
        if (getSelectedRowEmployee() == -1) {
            showMessageFail("Vui lòng chọn nhân viên để xóa");
            return;
        }
        employeeDao.deletedEmployee(getIdSelectedEmployee());
        fillTable(employeeDao.getListEmployee());
        showMessageSuccess("Xóa thành công!");
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
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblnhanvien = new com.petshop.swing.table.Table();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        combobox3 = new com.petshop.swing.combobox.Combobox();
        combobox4 = new com.petshop.swing.combobox.Combobox();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtMaNhanVien = new com.petshop.swing.textfield.TextFieldRounded();
        txtNgayTao = new com.petshop.swing.textfield.TextFieldRounded();
        txtEmail = new com.petshop.swing.textfield.TextFieldRounded();
        txtTenNhanVien = new com.petshop.swing.textfield.TextFieldRounded();
        txtSDT = new com.petshop.swing.textfield.TextFieldRounded();
        txt = new com.petshop.swing.textarea.TextAreaScroll();
        txtDiaChi = new com.petshop.swing.textarea.TextArea();
        rdNam = new com.petshop.swing.radio_button.RadioButtonCustom();
        rdNu = new com.petshop.swing.radio_button.RadioButtonCustom();
        jLabel3 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        btnAdd = new com.petshop.swing.Button();
        btnUpdate = new com.petshop.swing.Button();
        btnReset = new com.petshop.swing.Button();
        button69 = new com.petshop.swing.Button();
        btnRestore = new com.petshop.swing.Button();

        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblnhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã NV", "Tên NV", "Email", "Giới Tính", "SĐT", "Cong Viec", "Ngày tạo", "Địa Chỉ", "Trạng Thái", "Thao Tac"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblnhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblnhanvienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblnhanvien);
        if (tblnhanvien.getColumnModel().getColumnCount() > 0) {
            tblnhanvien.getColumnModel().getColumn(0).setMinWidth(0);
            tblnhanvien.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Danh sách nhân viên\n");

        txtSearch.setBackground(new java.awt.Color(250, 250, 250));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        combobox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A-Z", "Z-A" }));
        combobox3.setLabeText("Sắp xếp theo");
        combobox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox3ActionPerformed(evt);
            }
        });

        combobox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nam", "Nu" }));
        combobox4.setLabeText("Lọc theo giới tính");
        combobox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(combobox4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(combobox3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(combobox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combobox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addComponent(jLabel2))
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtMaNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaNhanVien.setLabelText("Mã Nhân Viên");

        txtNgayTao.setEditable(false);
        txtNgayTao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNgayTao.setLabelText("Ngày Tạo");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setLabelText("Email");

        txtTenNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenNhanVien.setLabelText("Tên Nhân Viên");

        txtSDT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSDT.setLabelText("Số Điện Thoại");

        txt.setLabelText("Địa chỉ");

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        txt.setViewportView(txtDiaChi);

        buttonGroup1.add(rdNam);
        rdNam.setText("Nam");

        buttonGroup1.add(rdNu);
        rdNu.setText("Nu");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTenNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(rdNu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(txt, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdNu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("THÔNG TIN NHÂN VIÊN");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAdd.setBackground(new java.awt.Color(204, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(255, 255, 204));
        btnUpdate.setText("Cập nhập");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(204, 204, 204));
        btnReset.setText("Làm mới");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        button69.setBackground(new java.awt.Color(204, 204, 255));
        button69.setText("Danh sách nhân viên đã xóa");
        button69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button69ActionPerformed(evt);
            }
        });

        btnRestore.setBackground(new java.awt.Color(255, 255, 204));
        btnRestore.setText("Restore");
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRestore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button69, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        String keyword = txtSearch.getText().trim(); // Lấy dữ liệu từ ô tìm kiếm

        if (!keyword.isEmpty()) {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            List<Employees> employeesList = employeeDAO.searchEmployee(keyword); // Tìm kiếm theo tên hoặc mã

            if (employeesList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
            } else {
                fillTable(employeesList); // Hiển thị danh sách nhân viên tìm được lên bảng
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
        }
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        resetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:

        showMessageConfirm("Xác nhận thêm mới!", () -> {
            insertEmployee();
        });
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblnhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnhanvienMouseClicked
        showDataEmployee();
    }//GEN-LAST:event_tblnhanvienMouseClicked

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        showMessageConfirm("Xác nhận update nhân viên?", () -> {
            updateEmployee();
        });
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void button69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button69ActionPerformed
        fillDeletedEmployeeTable(employeeDao.getListEmployeeDeleted());
    }//GEN-LAST:event_button69ActionPerformed

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
        // TODO add your handling code here:
        restoreEmployees();
    }//GEN-LAST:event_btnRestoreActionPerformed

    private void combobox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox3ActionPerformed
        // TODO add your handling code here:
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employees> sortedEmployees = employeeDAO.getSortedEmployeesByName(true); // Sắp xếp từ bé đến lớn
        int stt = 1;
        // Cập nhật dữ liệu lên bảng (ví dụ JTable)
        DefaultTableModel model = (DefaultTableModel) tblnhanvien.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        for (Employees emp : sortedEmployees) {
            model.addRow(new Object[]{
                emp.getId(),
                stt, // 0 - ID
                emp.getEmployeeCode() == null ? "Chua co thong tin" : emp.getEmployeeCode(), // 1 - Mã nhân viên
                emp.getEmployeeName() == null ? "Chua co thong tin" : emp.getEmployeeName(), // 2 - Tên nhân viên
                emp.getEmail() == null ? "Chua co thong tin" : emp.getEmail(), // 4 - Email
                emp.isGender() ? "Nam" : "Nu", // 7 - Giới tính (Nam/Nữ)
                emp.getPhoneNumber() == null ? "Chua co thong tin" : emp.getPhoneNumber(), // 3 - Số điện thoại
                emp.getRole().getId() == 2 ? "Nhan Vien" : "Quan Ly", // 8 - Chức vụ (Nhân viên/Quản lý)
                emp.getCreatedAt() == null ? "Chua co thong tin" : emp.getCreatedAt(), // 6 - Ngày tạo
                emp.getAddress() == null ? "Chua co thong tin" : emp.getAddress(), // 5 - Địa chỉ         
                emp.isStatus() ? "Hoat dong" : "Ngung hoat dong",});
        }
        stt++;
    }//GEN-LAST:event_combobox3ActionPerformed

    private void combobox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox4ActionPerformed
        // TODO add your handling code here:
        EmployeeDAO employeeDAO = new EmployeeDAO();
        String selectedGender = combobox4.getSelectedItem().toString(); // Lấy giá trị đã chọn
        Boolean gender = null; // Mặc định null để lấy tất cả nhân viên

        if (selectedGender.equals("Nam")) {
            gender = true;
        } else if (selectedGender.equals("Nu")) {
            gender = false;
        }

        List<Employees> filteredEmployees = employeeDAO.getListEmployeeByGender(gender);
        int stt = 1;
        // Cập nhật dữ liệu lên bảng (JTable)
        DefaultTableModel model = (DefaultTableModel) tblnhanvien.getModel();
        model.setRowCount(0); // Xóa dữ liệu c

        for (Employees emp : filteredEmployees) {
            model.addRow(new Object[]{
                emp.getId(),
                stt, // 0 - ID
                emp.getEmployeeCode() == null ? "Chua co thong tin" : emp.getEmployeeCode(), // 1 - Mã nhân viên
                emp.getEmployeeName() == null ? "Chua co thong tin" : emp.getEmployeeName(), // 2 - Tên nhân viên
                emp.getEmail() == null ? "Chua co thong tin" : emp.getEmail(), // 4 - Email
                emp.isGender() ? "Nam" : "Nu", // 7 - Giới tính (Nam/Nữ)
                emp.getPhoneNumber() == null ? "Chua co thong tin" : emp.getPhoneNumber(), // 3 - Số điện thoại
                emp.getRole().getId() == 2 ? "Nhan Vien" : "Quan Ly", // 8 - Chức vụ (Nhân viên/Quản lý)
                emp.getCreatedAt() == null ? "Chua co thong tin" : emp.getCreatedAt(), // 6 - Ngày tạo
                emp.getAddress() == null ? "Chua co thong tin" : emp.getAddress(), // 5 - Địa chỉ         
                emp.isStatus() ? "Hoat dong" : "Ngung hoat dong",});
        }
        stt++;
    }//GEN-LAST:event_combobox4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnAdd;
    private com.petshop.swing.Button btnReset;
    private com.petshop.swing.Button btnRestore;
    private com.petshop.swing.Button btnUpdate;
    private com.petshop.swing.Button button69;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.petshop.swing.combobox.Combobox combobox3;
    private com.petshop.swing.combobox.Combobox combobox4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private com.petshop.swing.radio_button.RadioButtonCustom rdNam;
    private com.petshop.swing.radio_button.RadioButtonCustom rdNu;
    private com.petshop.swing.table.Table tblnhanvien;
    private com.petshop.swing.textarea.TextAreaScroll txt;
    private com.petshop.swing.textarea.TextArea txtDiaChi;
    private com.petshop.swing.textfield.TextFieldRounded txtEmail;
    private com.petshop.swing.textfield.TextFieldRounded txtMaNhanVien;
    private com.petshop.swing.textfield.TextFieldRounded txtNgayTao;
    private com.petshop.swing.textfield.TextFieldRounded txtSDT;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextFieldRounded txtTenNhanVien;
    // End of variables declaration//GEN-END:variables
}
