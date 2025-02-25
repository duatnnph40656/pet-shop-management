/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.AccountDAO;
import com.petshop.daos.EmployeeDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.models.Account;
import com.petshop.models.Employees;
import com.petshop.models.Products;
import com.petshop.models.Roles;
import com.petshop.popup.PopupChangePassword;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import java.util.List;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class AccountManagement extends javax.swing.JPanel {

    /**
     * Creates new form AccountManagement
     */
    private final EmployeeDAO employeeDAO;
    private final AccountDAO accountDAO;

    public AccountManagement() {
        initComponents();
        tbAccount.fixTable(jScrollPane3);
        tbEmployee.fixTable(jScrollPane2);
        employeeDAO = new EmployeeDAO();
        accountDAO = new AccountDAO();
        getListEmployee(employeeDAO.getListEmployee());
        getListAccount(accountDAO.getListAccount());
    }

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

    private void getListEmployee(List<Employees> list) {
        int stt = 1;
        tbEmployee.setRowCount(0);
        for (Employees e : list) {
            tbEmployee.addRow(new Object[]{
                e.getId(),
                stt,
                checkEmpty(e.getEmployeeCode()),
                checkEmpty(e.getEmployeeName()),
                e.isGender() ? "Nam" : "Nữ",
                e.getRole() != null ? (e.getRole().getId() == 1 ? "Quản lý" : "Nhân viên") : "Chưa có thông tin",
                checkEmpty(e.getPhoneNumber()),
                checkEmpty(e.getEmail()),
                checkEmpty(e.getAddress()),
                e.isStatus() ? "Đang làm" : "Nghỉ việc",
                e.getRole() != null ? e.getRole().getId() : null // Thêm cột ID_ROLE vào bảng
            });
            stt++;
        }
    }

    private String checkEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? "Chưa có thông tin" : value;
    }

    private int getSelectedRowEmployee() {
        return tbEmployee.getSelectedRow();
    }

    private Account readF() {
        Account c = new Account();
        c.setUserName(txtUsername.getText());
        c.setPassword(txtPassword.getText());

        int selectedRow = getSelectedRowEmployee();
        if (selectedRow == -1) {
            return null;
        }

        // Lấy ID nhân viên từ bảng
        int idEmployee = (int) tbEmployee.getValueAt(selectedRow, 0);

        // Lấy ID Role từ cột 10 (đã thêm vào getListEmployee)
        Object roleIdObj = tbEmployee.getValueAt(selectedRow, 10);
        int idRole = roleIdObj != null ? (int) roleIdObj : 0; // Tránh lỗi nếu null

        System.out.println(idRole);
        // Gán dữ liệu
        Employees e = new Employees();
        e.setId(idEmployee);

        Roles r = new Roles();
        r.setId(idRole);
        e.setRole(r);

        c.setEmployee(e);

        c.setStatus(true);

        return c;
    }

    private void insertAccount() {
        if (getSelectedRowEmployee() == -1) {
            showMessageFail("Vui lòng chọn nhân viên!!");
            return;
        }
        if (!check()) {
            return;
        }
        if (accountDAO.createAccount(readF())) {
            showMessageSuccess("Tạo tài khoản thành công");
            getListAccount(accountDAO.getListAccount());
        } else {
            showMessageSuccess("Tạo tài khoản thất bại!!");
        }
    }

    private boolean check() {
        if (txtUsername.getText().isEmpty()) {
            showMessageFail("Username trống!!");
            return false;
        }
        if (txtPassword.getText().isEmpty()) {
            showMessageFail("Mật khẩu trống!!");
            return false;
        }
        if (!accountDAO.isUsernameExists(txtUsername.getText())) {
            showMessageFail("Tài khoản đã tồn tại!!");
            return false;
        }
        return true;
    }

    private void getListAccount(List<Account> list) {
        int stt = 1;
        tbAccount.setRowCount(0);
        for (Account a : list) {
            tbAccount.addRow(new Object[]{
                a.getId(),
                stt,
                a.getUserName(),
                a.getPassword(),
                a.getEmployee().getEmployeeCode(),
                a.getEmployee().getEmployeeName(),
                a.getEmployee().getPhoneNumber() == null ? a.getEmployee().getEmail() : a.getEmployee().getPhoneNumber(),
                a.getFormattedCreatedAt(),
                a.getRole().getId() == 1 ? "Tất cả" : "Giới hạn",
                a.isStatus() ? "Chưa khóa" : "Đã khóa",
                new ModelAction<>(a, new EventAction<Account>() {
                    @Override
                    public void delete(Account a) {
                        showMessageConfirm("Xác nhân xóa tài khoản này?", () -> {
                            deleteAccount(a.getId());
                        });
                    }

                    @Override
                    public void update(Account a) {
                        if (a.isStatus()) {
                            showMessageConfirm("Xác nhận khóa tài khoản này?", () -> {
                                banAccount(a.getId(), false);
                            });
                        } else {
                            showMessageConfirm("Xác nhận mở khóa tài khoản này?", () -> {
                                banAccount(a.getId(), true);
                            });
                        }
                    }

                    @Override
                    public void add(Account a) {
                        showPopupChangePassword(a);
                    }
                })
            });
            stt++;
        }
    }

    public void deleteAccount(int id) {
        if (accountDAO.updateIsDeleted(id)) {
            showMessageSuccess("Xóa tài khoản thành công!!");
            getListAccount(accountDAO.getListAccount());
        } else {
            showMessageFail("Xóa tài khoản thất bại!!");
        }
    }

    public void banAccount(int id, boolean status) {
        if (accountDAO.updateIsStatus(id, status)) {
            showMessageSuccess("Khóa tài khoản thành công!!");
            getListAccount(accountDAO.getListAccount());
        } else {
            showMessageFail("Khóa tài khoản thất bại!!");
        }
    }

    public void showPopupChangePassword(Account a) {
        PopupChangePassword pop = new PopupChangePassword();

        pop.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                String passOld = pop.getTxtPassOld();
                String passNew = pop.getTxtPassNew();

                // Kiểm tra nếu mật khẩu cũ trống
                if (passOld.isEmpty()) {
                    pop.setShowMessageFail("Vui lòng nhập mật khẩu cũ!");
                    return;
                }

                // Kiểm tra nếu mật khẩu mới trống
                if (passNew.isEmpty()) {
                    pop.setShowMessageFail("Vui lòng nhập mật khẩu mới!");
                    return;
                }

                // Hiển thị hộp thoại xác nhận trước khi đổi mật khẩu
                pop.showMessageConfirm("Bạn có chắc chắn muốn đổi mật khẩu?", () -> {
                    if (updatePasswordAccount(a, passOld, passNew)) {
                        GlassPanePopup.closePopup("pPass");
                        DialogMessageSuccess success = new DialogMessageSuccess("Mật khẩu đã được thay đổi!");
                        GlassPanePopup.showPopup(success);
                        getListAccount(accountDAO.getListAccount());
                    } else {
                        pop.setShowMessageFail("Mật khẩu cũ không đúng!");
                    }
                });
            }

            @Override
            public void onCancel() {
                GlassPanePopup.closePopup("pPass");
            }
        });

        GlassPanePopup.showPopup(pop, "pPass");
    }

    public boolean updatePasswordAccount(Account a, String passOld, String passNew) {
        // Kiểm tra mật khẩu cũ có đúng không
        if (!accountDAO.checkOldPassword(a.getId(), passOld)) {
            return false; // Sai mật khẩu cũ
        }

        // Cập nhật mật khẩu mới
        return accountDAO.changePassword(a.getId(), passNew);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEmployee = new com.petshop.swing.tableMore.TableMore4();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbAccount = new com.petshop.swing.tableMore.TableMore4();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtUsername = new com.petshop.swing.textfield.TextField1();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPassword = new com.petshop.swing.textfield.PasswordField1();
        btnAdd = new com.petshop.swing.Button1();

        setBackground(new java.awt.Color(245, 245, 245));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ TÀI KHOẢN");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Danh sách nhân viên");

        tbEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã NV", "Tên NV", "Giới tính", "Chức vụ", "SDT", "Email", "Dịa chỉ", "Trạng thái", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbEmployee);
        if (tbEmployee.getColumnModel().getColumnCount() > 0) {
            tbEmployee.getColumnModel().getColumn(0).setMinWidth(0);
            tbEmployee.getColumnModel().getColumn(0).setMaxWidth(0);
            tbEmployee.getColumnModel().getColumn(1).setMinWidth(35);
            tbEmployee.getColumnModel().getColumn(1).setMaxWidth(35);
            tbEmployee.getColumnModel().getColumn(10).setMinWidth(0);
            tbEmployee.getColumnModel().getColumn(10).setMaxWidth(0);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tbAccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Tên đăng nhập", "Mật khẩu", "Mã NV", "Họ tên NV", "Thông tin NV", "Ngày đăng kí", "Quyền hạn", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbAccount);
        if (tbAccount.getColumnModel().getColumnCount() > 0) {
            tbAccount.getColumnModel().getColumn(0).setMinWidth(0);
            tbAccount.getColumnModel().getColumn(0).setMaxWidth(0);
            tbAccount.getColumnModel().getColumn(1).setMinWidth(35);
            tbAccount.getColumnModel().getColumn(1).setMaxWidth(35);
        }

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel5.setText("Danh sách tài khoản");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setText("Tài khoản:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel4.setText("Mật khẩu");

        btnAdd.setBackground(new java.awt.Color(204, 255, 204));
        btnAdd.setText("Tạo");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 91, Short.MAX_VALUE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận thêm tài khoản?", () -> {
            insertAccount();
        });
    }//GEN-LAST:event_btnAddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button1 btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.petshop.swing.tableMore.TableMore4 tbAccount;
    private com.petshop.swing.tableMore.TableMore4 tbEmployee;
    private com.petshop.swing.textfield.PasswordField1 txtPassword;
    private com.petshop.swing.textfield.TextField1 txtUsername;
    // End of variables declaration//GEN-END:variables
}
