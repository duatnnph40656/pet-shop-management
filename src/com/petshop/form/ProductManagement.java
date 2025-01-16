/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.CategoryProductDAO;
import com.petshop.daos.ProductDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.models.CategoryProduct;
import com.petshop.models.Product;
import com.petshop.popup.PopupCategoryPet;
import com.petshop.popup.PopupCategoryProduct;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.popup.Message;
import com.petshop.swing.table.Action;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class ProductManagement extends javax.swing.JPanel {

    /**
     * Creates new form ProductManagement
     */
    private final ProductDAO productDao;
    private final CategoryProductDAO categoryProductDao;
    private DefaultTableModel model;

    public ProductManagement() {
        initComponents();
        productDao = new ProductDAO();
        categoryProductDao = new CategoryProductDAO();
        model = new DefaultTableModel();
        tbProduct.fixTable(jScrollPane1);
        tbProductDetail.fixTable(jScrollPane2);
        init();
    }

    void init() {
        this.getListProduct(productDao.getListProduct());
        this.loadCBBCategoryProduct(categoryProductDao.getListCategoryProduct());
        this.loadFilterCBBProduct(categoryProductDao.getListCategoryProduct());
        this.loadCBBProduct(productDao.getListProduct());
        this.loadCBBStatus();
        txtProductCode.setText(Ultil.generateRandomProductCode());
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

    public void getListProduct(List<Product> list) {
        int stt = 1;
        tbProduct.setRowCount(0);
        // Xóa tất cả các hàng trước khi thêm mới
        for (Product product : list) {
            ModelAction<Product> actionData = new ModelAction<>(
                    product,
                    new EventAction<Product>() {
                @Override
                public void delete(Product product) {
                    showMessageConfirm("Xác nhận xóa sản phẩm?", () -> {
                        deleteProduct();
                    });
                }

                @Override
                public void update(Product product) {

                }
            }
            );
            // Kiểm tra trạng thái sản phẩm
            if (product.isStatus()) {
                tbProduct.addRow(new Object[]{
                    product.getId(),
                    stt,
                    product.getProductCode(),
                    product.getProductName(),
                    product.getCategoryProduct().getCategoryProductName(),
                    product.getFormattedPriceBase(),
                    product.getFormattedCreatedAt(),
                    "Hoạt động",
                    actionData
                });
                stt++; // Tăng STT
            }
        }
        // Ẩn cột ID
        tbProduct.getColumnModel()
                .getColumn(0).setMinWidth(0); // Giả sử cột ID là cột 1
        tbProduct.getColumnModel()
                .getColumn(0).setMaxWidth(0);
        tbProduct.getColumnModel()
                .getColumn(0).setWidth(0);
    }

    public Product readFormProduct() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String productCode = txtProductCode.getText().trim();
            String productName = txtProductName.getText().trim();
            BigDecimal priceBase = new BigDecimal(txtPriceProduct.getText().trim());

            // Lấy danh mục sản phẩm từ JComboBox
            CategoryProduct categoryProduct = (CategoryProduct) cbbCategoryProduct.getSelectedItem();

            // Lấy trạng thái từ JComboBox
            boolean isStatus;
            String status = (String) cbbStatusProduct.getSelectedItem();
            if (status == null) {
                isStatus = true;
            } else {
                isStatus = status.equalsIgnoreCase("Hoạt động");
            }

            // Tạo đối tượng Product
            Product product = new Product();
            product.setProductCode(productCode);
            product.setProductName(productName);
            product.setCategoryProduct(categoryProduct); // Gán trực tiếp đối tượng CategoryProduct
            product.setPriceBase(priceBase);
            product.setStatus(isStatus);

            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadFormProduct(int index) {
        if (index < 0 || index >= tbProduct.getRowCount()) {
            return;
        }
        txtProductCode.enable(false);
        txtProductCode.setText((String) tbProduct.getValueAt(index, 2));
        txtProductName.setText((String) tbProduct.getValueAt(index, 3));
        String categoryName = (String) tbProduct.getValueAt(index, 4);
        for (int i = 0; i < cbbCategoryProduct.getItemCount(); i++) {
            CategoryProduct category = (CategoryProduct) cbbCategoryProduct.getItemAt(i);
            if (category.getCategoryProductName().equals(categoryName)) {
                cbbCategoryProduct.setSelectedItem(category);
                break;
            }
        }
        String priceText = tbProduct.getValueAt(index, 5).toString();
        priceText = priceText.replace(" VND", "").replace(",", "");
        txtPriceProduct.setText(priceText);
        String status = (String) tbProduct.getValueAt(index, 7);
        cbbStatusProduct.setSelectedItem(status.equals("Hoạt động") ? "Hoạt động" : "Không hoạt động");
    }

    public void resetFormProduct() {
        txtProductCode.enable(true);
        txtProductCode.setText(Ultil.generateRandomProductCode());
        txtProductName.setText("");
        txtPriceProduct.setText("");
        cbbCategoryProduct.setSelectedIndex(-1);
        cbbStatusProduct.setSelectedIndex(-1);
        tbProduct.clearSelection();
       
    }

    public void resetRamdomCode() {
        txtProductCode.setText(Ultil.generateRandomProductCode());
    }

    public void loadCBBCategoryProduct(List<CategoryProduct> categoryList) {
        cbbCategoryProduct.removeAllItems();
        for (CategoryProduct category : categoryList) {
            cbbCategoryProduct.addItem(category);
        }
        // Đặt mục chọn về -1 (không có mục nào được chọn)
        cbbCategoryProduct.setSelectedIndex(-1);
    }

    public void loadFilterCBBProduct(List<CategoryProduct> categoryList) {
        cbbFilterCategory.removeAllItems(); // Xóa tất cả các mục hiện có
        for (CategoryProduct category : categoryList) {
            cbbFilterCategory.addItem(category); // Thêm đối tượng CategoryProduct vào JComboBox
        }
        cbbFilterCategory.setSelectedIndex(-1);

        cbbFilterCategory.addActionListener(e -> {
            if (cbbFilterCategory.getSelectedIndex() == -1) {
                return;
            }
            CategoryProduct selectedCategory = (CategoryProduct) cbbFilterCategory.getSelectedItem();
            if (selectedCategory != null) {
                int categoryId = selectedCategory.getId();
                filterProductByCategory(categoryId);
            }
        });

    }
    
    public void loadCBBProduct(List<Product> productList){
        cbbProduct.removeAllItems();
        for(Product p : productList){
            cbbProduct.addItem(p);
        }
        // Đặt mục chọn về -1 (không có mục nào được chọn)
        cbbProduct.setSelectedIndex(-1);
    }

    public void filterProductByCategory(int categoryId) {
        List<Product> filteredProducts = productDao.selectProductByCategoryId(categoryId); // Lấy sản phẩm từ DAO
        getListProduct(filteredProducts); // Hiển thị sản phẩm trong bảng

    }

    public void loadCBBStatus() {
        cbbStatusProduct.removeAll();
        cbbStatusProduct.addItem("Hoạt động");
        cbbStatusProduct.addItem("Không hoạt động");
        cbbStatusProduct.setSelectedIndex(-1);
    }

    public boolean check() {
        // Kiểm tra mã sản phẩm
        if (txtProductCode.getText().isEmpty()) {
            this.showMessageFail("Mã trống!");
            return false;
        }
        // Kiểm tra mã trùng
        for (int i = 0; i < tbProduct.getRowCount(); i++) {
            if (tbProduct.getValueAt(i, 2).toString().equals(txtProductCode.getText())) {
                this.showMessageFail("Mã sản phẩm đã tồn tại!");
                return false;
            }
        }
        if (!txtProductCode.getText().matches("^[a-zA-Z0-9]+$")) {
            this.showMessageFail("Mã không được chứa ký tự đặc biệt hoặc dấu!");
            return false;
        }
        // Kiểm tra tên sản phẩm
        if (txtProductName.getText().isEmpty()) {
            this.showMessageFail("Tên trống!");
            return false;
        }
        if (txtProductName.getText().matches(".*\\d.*")) {
            this.showMessageFail("Tên không được chứa số!");
            return false;
        }

        // Kiểm tra giá sản phẩm
        if (txtPriceProduct.getText().isEmpty()) {
            this.showMessageFail("Giá trống!");
            return false;
        }
        double price = Double.parseDouble(txtPriceProduct.getText());
        if (price < 0) {
            this.showMessageFail("Giá không được là số âm!");
            return false;
        }
        if (price > 1000000000) {
            this.showMessageFail("Giá không được vượt quá 1 tỷ!");
            return false;
        }

        // Kiểm tra loại sản phẩm
        if (cbbCategoryProduct.getSelectedIndex() == -1) {
            this.showMessageFail("Bạn chưa chọn loại sản phẩm!");
            return false;
        }

        return true;
    }

    public void insertProduct() {
        if (!check()) {
            return;
        }
        if (productDao.addProduct(readFormProduct())) {
            this.showMessageSuccess("Thêm sản phẩm thành công!");
            this.getListProduct(productDao.getListProduct());
            resetFormProduct();
            resetRamdomCode();
        } else {
            this.showMessageFail("Thêm sản phẩm thất bại");
        }
    }

    public void updateProduct() {
        if (!check()) {
            return;
        }
        int selectedRow = tbProduct.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tbProduct.getValueAt(selectedRow, 0);
            Product product = readFormProduct(); // Đọc dữ liệu từ form
            if (productDao.updateProduct(id, product)) {
                this.showMessageSuccess("Update thành công!");
                this.getListProduct(productDao.getListProduct());
            } else {
                this.showMessageFail("Update thất bại!!");
            }
        }
    }

    public void deleteProduct() {
        int selectedRow = tbProduct.getSelectedRow(); // Lấy hàng được chọn
        if (selectedRow != -1) { // Kiểm tra nếu hàng được chọn hợp lệ
            int id = (int) tbProduct.getValueAt(selectedRow, 0); // Lấy ID từ cột 1
            productDao.deleteProduct(id); // Gọi DAO để xóa sản phẩm
            this.getListProduct(productDao.getListProduct()); // Tải lại danh sách sản phẩm
            this.showMessageSuccess("Xóa thành công!");
            resetFormProduct();
            resetRamdomCode();
        } else {
            this.showMessageFail("Vui lòng chọn một sản phẩm để xóa.");
        }
    }

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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnAddProduct = new com.petshop.swing.Button();
        btnUpdateProduct = new com.petshop.swing.Button();
        btnRestProduct = new com.petshop.swing.Button();
        btnHistoryProductDeleted = new com.petshop.swing.Button();
        jPanel7 = new javax.swing.JPanel();
        btnPopupCProduct = new com.petshop.swing.ButtonBadges();
        txtProductCode = new com.petshop.swing.textfield.TextFieldRounded();
        txtProductName = new com.petshop.swing.textfield.TextFieldRounded();
        txtPriceProduct = new com.petshop.swing.textfield.TextFieldRounded();
        cbbCategoryProduct = new com.petshop.swing.combobox.Combobox();
        cbbStatusProduct = new com.petshop.swing.combobox.Combobox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProduct = new com.petshop.swing.table.Table();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterCategory = new com.petshop.swing.combobox.Combobox();
        combobox4 = new com.petshop.swing.combobox.Combobox();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtProductDetailName = new com.petshop.swing.textfield.TextFieldRounded();
        txtFlavor = new com.petshop.swing.textfield.TextFieldRounded();
        textAreaScroll1 = new com.petshop.swing.textarea.TextAreaScroll();
        txtDescribe = new com.petshop.swing.textarea.TextArea();
        btnWeightProductDetail = new com.petshop.swing.textfield.TextFieldRounded();
        txtExpiry = new com.petshop.swing.textfield.TextFieldRounded();
        btnPopupTypePet = new com.petshop.swing.Button();
        pic = new com.petshop.swing.ImageRectangle();
        btnSelectImage = new com.petshop.swing.Button();
        txtPriceProductDetail = new com.petshop.swing.textfield.TextFieldRounded();
        txtQuantityInStock = new com.petshop.swing.textfield.TextFieldRounded();
        cbbProduct = new com.petshop.swing.combobox.Combobox();
        cbbTypePet = new com.petshop.swing.combobox.Combobox();
        jLabel3 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnAddProductDetail = new com.petshop.swing.Button();
        btnUpdateProductDetail = new com.petshop.swing.Button();
        btnRestProductDetail = new com.petshop.swing.Button();
        button9 = new com.petshop.swing.Button();
        btnUpdateStatusProductDetail = new com.petshop.swing.Button();
        btnHisProductDetailDeleted = new com.petshop.swing.Button();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProductDetail = new com.petshop.swing.table.Table();
        button11 = new com.petshop.swing.Button();
        txtSearchProductDetail = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterProduct = new com.petshop.swing.combobox.Combobox();
        cbbFilterTypePet = new com.petshop.swing.combobox.Combobox();
        cbbSort = new com.petshop.swing.combobox.Combobox();

        setMaximumSize(new java.awt.Dimension(1058, 741));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        materialTabbed1.setBackground(new java.awt.Color(243, 243, 243));
        materialTabbed1.setMaximumSize(new java.awt.Dimension(1060, 700));
        materialTabbed1.setPreferredSize(new java.awt.Dimension(1060, 700));

        jPanel1.setMaximumSize(new java.awt.Dimension(1054, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(1054, 600));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÔNG TIN SẢN PHẨM");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAddProduct.setBackground(new java.awt.Color(204, 255, 255));
        btnAddProduct.setText("Thêm");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnUpdateProduct.setBackground(new java.awt.Color(255, 255, 204));
        btnUpdateProduct.setText("Cập nhập");
        btnUpdateProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProductActionPerformed(evt);
            }
        });

        btnRestProduct.setBackground(new java.awt.Color(204, 204, 204));
        btnRestProduct.setText("Làm mới");
        btnRestProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestProductActionPerformed(evt);
            }
        });

        btnHistoryProductDeleted.setBackground(new java.awt.Color(204, 204, 255));
        btnHistoryProductDeleted.setText("Lịch sử đã xóa");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHistoryProductDeleted, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(btnUpdateProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRestProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnHistoryProductDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdateProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRestProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnPopupCProduct.setBackground(new java.awt.Color(204, 255, 255));
        btnPopupCProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-add-24 (1).png"))); // NOI18N
        btnPopupCProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopupCProductActionPerformed(evt);
            }
        });

        txtProductCode.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductCode.setLabelText("Mã sản phẩm");
        txtProductCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductCodeActionPerformed(evt);
            }
        });

        txtProductName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductName.setLabelText("Tên sản phẩm");

        txtPriceProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPriceProduct.setLabelText("Giá");

        cbbCategoryProduct.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbCategoryProduct.setLabeText("Loại sản phẩm");

        cbbStatusProduct.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbStatusProduct.setLabeText("Trạng thái");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(cbbCategoryProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPopupCProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPriceProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addComponent(cbbStatusProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPriceProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPopupCProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbCategoryProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbStatusProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã SP", "Tên SP", "Loại sản phẩm", "Giá", "Ngày tạo", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProduct);
        if (tbProduct.getColumnModel().getColumnCount() > 0) {
            tbProduct.getColumnModel().getColumn(0).setMaxWidth(1);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Danh sách sản phẩm");

        txtSearch.setBackground(new java.awt.Color(250, 250, 250));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        cbbFilterCategory.setLabeText("Loại sản phẩm");
        cbbFilterCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbFilterCategoryActionPerformed(evt);
            }
        });

        combobox4.setLabeText("Sắp xếp theo");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(85, 85, 85)
                        .addComponent(cbbFilterCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(combobox4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel2))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbFilterCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(combobox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        materialTabbed1.addTab("Sản phẩm", jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(1055, 700));
        jPanel2.setPreferredSize(new java.awt.Dimension(1055, 600));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtProductDetailName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductDetailName.setLabelText("Tên sản phẩm chi tiết");

        txtFlavor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFlavor.setLabelText("Hương vị");
        txtFlavor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFlavorActionPerformed(evt);
            }
        });

        textAreaScroll1.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll1.setLabelText("Mô tả");

        txtDescribe.setColumns(20);
        txtDescribe.setRows(5);
        textAreaScroll1.setViewportView(txtDescribe);

        btnWeightProductDetail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnWeightProductDetail.setLabelText("Trọng lượng (gram)");

        txtExpiry.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtExpiry.setLabelText("Hạn sử dụng (tháng)");

        btnPopupTypePet.setBackground(new java.awt.Color(204, 255, 255));
        btnPopupTypePet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-add-24 (1).png"))); // NOI18N
        btnPopupTypePet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopupTypePetActionPerformed(evt);
            }
        });

        pic.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSelectImage.setBackground(new java.awt.Color(204, 255, 255));
        btnSelectImage.setText("Chọn ảnh");

        txtPriceProductDetail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPriceProductDetail.setLabelText("Giá bán (VND)");

        txtQuantityInStock.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtQuantityInStock.setLabelText("Số lượng trong kho");

        cbbProduct.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbProduct.setLabeText("Sản phẩm");

        cbbTypePet.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbTypePet.setLabeText("Loại thú cưng");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(cbbTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPopupTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtFlavor, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPriceProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addComponent(cbbProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnWeightProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(txtProductDetailName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtExpiry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtQuantityInStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSelectImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtProductDetailName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtQuantityInStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnWeightProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPriceProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(btnPopupTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cbbTypePet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtFlavor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addContainerGap())))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("THÔNG TIN SẢN PHẨM CHI TIẾT");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAddProductDetail.setBackground(new java.awt.Color(204, 255, 255));
        btnAddProductDetail.setText("Thêm");

        btnUpdateProductDetail.setBackground(new java.awt.Color(255, 255, 204));
        btnUpdateProductDetail.setText("Cập nhập");

        btnRestProductDetail.setBackground(new java.awt.Color(204, 204, 204));
        btnRestProductDetail.setText("Làm mới");

        button9.setBackground(new java.awt.Color(204, 255, 204));
        button9.setText("Import Excel");

        btnUpdateStatusProductDetail.setBackground(new java.awt.Color(255, 204, 204));
        btnUpdateStatusProductDetail.setText("Ngưng bán");

        btnHisProductDetailDeleted.setBackground(new java.awt.Color(204, 204, 255));
        btnHisProductDetailDeleted.setText("Lịch sử đã xóa");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button9, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(btnUpdateProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnUpdateStatusProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRestProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnHisProductDetailDeleted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHisProductDetailDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUpdateProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateStatusProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRestProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel4.setText("Danh sách sản phẩm chi tiết");

        tbProductDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên SPCT", "Mã SPCT", "Loại thú cưng", "Hạn sử dụng", "Số lượng", "Trọng lượng", "Giá bán", "Mô tả", "Trạng thái", "Thao tác"
            }
        ));
        jScrollPane2.setViewportView(tbProductDetail);

        button11.setBackground(new java.awt.Color(204, 255, 204));
        button11.setText("Export Excel");

        txtSearchProductDetail.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchProductDetail.setMinimumSize(new java.awt.Dimension(64, 42));

        cbbFilterProduct.setLabeText("Lọc theo sản phẩm");

        cbbFilterTypePet.setLabeText("Lọc thưc ăn cho loại thú cưng");

        cbbSort.setLabeText("Sắp xếp theo");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(177, 177, 177))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(cbbSort, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(button11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        materialTabbed1.addTab("Sản phẩm chi tiết", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(materialTabbed1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        showMessageConfirm("Xác nhận thêm sản phẩm?", () -> {
            insertProduct();
        });
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void txtFlavorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFlavorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFlavorActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnPopupCProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupCProductActionPerformed
        // TODO add your handling code here:
        PopupCategoryProduct obj = new PopupCategoryProduct();
        GlassPanePopup.showPopup(obj);
    }//GEN-LAST:event_btnPopupCProductActionPerformed

    private void btnPopupTypePetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupTypePetActionPerformed
        // TODO add your handling code here:
        PopupCategoryPet obj = new PopupCategoryPet();
        GlassPanePopup.showPopup(obj);
    }//GEN-LAST:event_btnPopupTypePetActionPerformed

    private void cbbFilterCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbFilterCategoryActionPerformed

    }//GEN-LAST:event_cbbFilterCategoryActionPerformed

    private void tbProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProductMouseClicked
        // TODO add your handling code here:
        int index = tbProduct.getSelectedRow();
        this.loadFormProduct(index);
    }//GEN-LAST:event_tbProductMouseClicked

    private void btnRestProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestProductActionPerformed
        // TODO add your handling code here:
        resetFormProduct();
    }//GEN-LAST:event_btnRestProductActionPerformed

    private void btnUpdateProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProductActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận cập nhập lại sản phẩm?", () -> {
            updateProduct();
        });
    }//GEN-LAST:event_btnUpdateProductActionPerformed

    private void txtProductCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductCodeActionPerformed

    }//GEN-LAST:event_txtProductCodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnAddProduct;
    private com.petshop.swing.Button btnAddProductDetail;
    private com.petshop.swing.Button btnHisProductDetailDeleted;
    private com.petshop.swing.Button btnHistoryProductDeleted;
    private com.petshop.swing.ButtonBadges btnPopupCProduct;
    private com.petshop.swing.Button btnPopupTypePet;
    private com.petshop.swing.Button btnRestProduct;
    private com.petshop.swing.Button btnRestProductDetail;
    private com.petshop.swing.Button btnSelectImage;
    private com.petshop.swing.Button btnUpdateProduct;
    private com.petshop.swing.Button btnUpdateProductDetail;
    private com.petshop.swing.Button btnUpdateStatusProductDetail;
    private com.petshop.swing.textfield.TextFieldRounded btnWeightProductDetail;
    private com.petshop.swing.Button button11;
    private com.petshop.swing.Button button9;
    private com.petshop.swing.combobox.Combobox cbbCategoryProduct;
    private com.petshop.swing.combobox.Combobox cbbFilterCategory;
    private com.petshop.swing.combobox.Combobox cbbFilterProduct;
    private com.petshop.swing.combobox.Combobox cbbFilterTypePet;
    private com.petshop.swing.combobox.Combobox cbbProduct;
    private com.petshop.swing.combobox.Combobox cbbSort;
    private com.petshop.swing.combobox.Combobox cbbStatusProduct;
    private com.petshop.swing.combobox.Combobox cbbTypePet;
    private com.petshop.swing.combobox.Combobox combobox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.ImageRectangle pic;
    private com.petshop.swing.table.Table tbProduct;
    private com.petshop.swing.table.Table tbProductDetail;
    private com.petshop.swing.textarea.TextAreaScroll textAreaScroll1;
    private com.petshop.swing.textarea.TextArea txtDescribe;
    private com.petshop.swing.textfield.TextFieldRounded txtExpiry;
    private com.petshop.swing.textfield.TextFieldRounded txtFlavor;
    private com.petshop.swing.textfield.TextFieldRounded txtPriceProduct;
    private com.petshop.swing.textfield.TextFieldRounded txtPriceProductDetail;
    private com.petshop.swing.textfield.TextFieldRounded txtProductCode;
    private com.petshop.swing.textfield.TextFieldRounded txtProductDetailName;
    private com.petshop.swing.textfield.TextFieldRounded txtProductName;
    private com.petshop.swing.textfield.TextFieldRounded txtQuantityInStock;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProductDetail;
    // End of variables declaration//GEN-END:variables
}
