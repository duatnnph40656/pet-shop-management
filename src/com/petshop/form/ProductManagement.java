/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.daos.CategoryProductDAO;
import com.petshop.daos.ProductDAO;
import com.petshop.daos.ProductDetailDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.CategoryProduct;
import com.petshop.models.Product;
import com.petshop.models.ProductDetail;
import com.petshop.models.TypePet;
import com.petshop.popup.PopupCategoryPet;
import com.petshop.popup.PopupCategoryProduct;
import com.petshop.popup.PopupScanBarCode;
import com.petshop.swing.datechooser.EventDateChooser;
import com.petshop.swing.datechooser.SelectedAction;
import com.petshop.swing.datechooser.SelectedDate;
import com.petshop.swing.jnafilechooser.api.JnaFileChooser;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.popup.Message;
import com.petshop.swing.table.Action;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.swing.table.ModelImage;
import com.petshop.swing.table.ModelProfile;
import com.petshop.ultils.Ultil;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private final TypePetDAO typePetDAO;
    private final ProductDetailDAO productDetailDao;

    public ProductManagement() {
        initComponents();
        productDao = new ProductDAO();
        categoryProductDao = new CategoryProductDAO();
        productDetailDao = new ProductDetailDAO();
        typePetDAO = new TypePetDAO();
        tbProduct.fixTable(jScrollPane1);
        tbProductDetail.fixTable(jScrollPane2);
        init();
        this.getListProductDetail(productDetailDao.getListProductDetail());
        this.loadCBBStatus();
    }

    void init() {
        resetRamdomCode();
        addTableEvent();
        searchEvent();
        dateChooserEvent();
        this.getListProduct(productDao.getListProduct());
        this.getListProductDetail(productDetailDao.getListProductDetail());
        this.loadCBBCategoryProduct(categoryProductDao.getListCategoryProduct());
        this.loadCBBTypePet(typePetDAO.getList());
        this.loadFilterCBBProduct(categoryProductDao.getListCategoryProduct());
        this.loadCBBProduct(productDao.getListProduct());
        this.loadCbbFilterTypePet(typePetDAO.getList());
        this.loadCbbFilterProduct(productDao.getListProduct());
    }

    private void addTableEvent() {
        tbProductDetail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDataD();
            }
        });

        tbProductDetail.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    showDataD();
                }
            }
        });
    }

    private void searchEvent() {
        txtSearchProductDetail.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchProductD(txtSearchProductDetail.getText()); // tìm kiếm sau khi ấn, lấy text của thanh search
                    call.done();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            @Override
            public void onCancel() {

            }
        });
        txtSearchProduct.addEvent(new EventTextField() {
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    searchProduct(txtSearchProduct.getText()); // tìm kiếm sau khi ấn, lấy text của thanh search
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

    private void dateChooserEvent() {
        dateChooser.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    dateChooser.hidePopup();
                }
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

    //<editor-fold defaultstate="collapsed" desc="{Popup...">
    public void showPopupTypePet() {
        PopupCategoryPet tPopup = new PopupCategoryPet();
        tPopup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                loadCBBTypePet(typePetDAO.getList());
            }

            @Override
            public void onCancel() {
                loadCBBTypePet(typePetDAO.getList());
            }
        });
        GlassPanePopup.showPopup(tPopup, "tPopup");
    }

    public void showPopupCategoryProduct() {
        PopupCategoryProduct pPopup = new PopupCategoryProduct();
        pPopup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                loadCBBCategoryProduct(categoryProductDao.getListCategoryProduct());
            }

            @Override
            public void onCancel() {
                loadCBBCategoryProduct(categoryProductDao.getListCategoryProduct());
            }
        });
        GlassPanePopup.showPopup(pPopup, "pPopup");
    }

    public void showPopupWebcam() {
        PopupScanBarCode pWebCam = new PopupScanBarCode();

        pWebCam.setBarcodeListener((String barcode) -> {
            List<ProductDetail> product = productDetailDao.searchByBarCode(barcode);
            if (product != null) {
                getListProductDetail(product); // Hiển thị thông tin sản phẩm lên giao diện
            } else {
                showMessageFail("Không tìm thấy sản phẩm có mã vạch: " + barcode);
            }
        });

        GlassPanePopup.showPopup(pWebCam, "pWebCam");
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="{Loadcbb...">
    public void loadCBBCategoryProduct(List<CategoryProduct> categoryList) {
        cbbCategoryProduct.removeAllItems();
        for (CategoryProduct category : categoryList) {
            cbbCategoryProduct.addItem(category);
        }
        // Đặt mục chọn về -1 (không có mục nào được chọn)
        cbbCategoryProduct.setSelectedIndex(-1);
    }

    public void loadCBBTypePet(List<TypePet> list) {
        cbbTypePet.removeAllItems();
        cbbFilterTypePet.removeAllItems();
        for (TypePet t : list) {
            cbbTypePet.addItem(t);
            cbbFilterTypePet.addItem(t);
        }
        cbbFilterTypePet.setSelectedIndex(-1);
        cbbTypePet.setSelectedIndex(-1);

        cbbFilterTypePet.addActionListener(e -> {
            if (cbbFilterTypePet.getSelectedIndex() == -1) {
                return;
            }
            TypePet t = (TypePet) cbbFilterTypePet.getSelectedItem();
            if (t != null) {
                int typePetId = t.getId();
                filterProductDetailByTypePet(typePetId);
            }
        });
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

    public void loadCBBProduct(List<Product> productList) {
        cbbProduct.removeAllItems();
        for (Product p : productList) {
            cbbProduct.addItem(p);
        }
        // Đặt mục chọn về -1 (không có mục nào được chọn)
        cbbProduct.setSelectedIndex(-1);

    }

    private void loadCbbFilterTypePet(List<TypePet> list) {
        cbbFilterTypePet.removeAllItems();
        for (TypePet t : list) {
            cbbFilterTypePet.addItem(t);
        }
        cbbFilterTypePet.setSelectedIndex(-1);

        cbbFilterTypePet.addActionListener(e -> filterProductDetails());
    }

    private void loadCbbFilterProduct(List<Product> productList) {
        cbbFilterProduct.removeAllItems();
        for (Product p : productList) {
            cbbFilterProduct.addItem(p);
        }
        cbbFilterProduct.setSelectedIndex(-1);
        cbbFilterProduct.addActionListener(e -> filterProductDetails());
    }

    private void filterProductDetails() {
        TypePet selectedTypePet = (TypePet) cbbFilterTypePet.getSelectedItem();
        Product selectedProduct = (Product) cbbFilterProduct.getSelectedItem();

        Integer typePetId = (selectedTypePet != null) ? selectedTypePet.getId() : null;
        Integer productId = (selectedProduct != null) ? selectedProduct.getId() : null;

        // Nếu cả hai đều null, lấy toàn bộ danh sách
        List<ProductDetail> filteredList;
        if (typePetId == null && productId == null) {
            filteredList = productDetailDao.getListProductDetail();
        } else {
            filteredList = productDetailDao.searchProductDetails(productId, typePetId);
        }

        getListProductDetail(filteredList); // Cập nhật giao diện với danh sách lọc
    }

    public void filterProductDetailByProduct(int productId) {
        List<ProductDetail> list = productDetailDao.searchByProductId(productId);
        getListProductDetail(list);
    }

    public void filterProductByCategory(int categoryId) {
        List<Product> filteredProducts = productDao.selectProductByCategoryId(categoryId); // Lấy sản phẩm từ DAO
        getListProduct(filteredProducts); // Hiển thị sản phẩm trong bảng

    }

    public void filterProductDetailByTypePet(int typePetId) {
        List<ProductDetail> filteredProducts = productDetailDao.findByTypePetId(typePetId);
        getListProductDetail(filteredProducts);
    }

    public void loadCBBStatus() {
        cbbStatusProduct.removeAll();
        cbbStatusProduct.addItem("Hoạt động");
        cbbStatusProduct.addItem("Không hoạt động");
        cbbStatusProduct.setSelectedIndex(-1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{crud product...">
    public void getListProduct(List<Product> list) {
        int stt = 1;
        tbProduct.setRowCount(0); // Xóa dữ liệu cũ
        for (Product product : list) {
            if (!product.isDeleted()) {
                tbProduct.addRow(new Object[]{
                    product.getId(),
                    stt,
                    product.getProductCode(),
                    product.getProductName(),
                    product.getCategoryProduct().getCategoryProductName(),
                    product.getFormattedPriceBase(),
                    product.getFormattedCreatedAt(),
                    product.isStatus() ? "Hoạt động" : "Ngưng hoạt động",
                    new ModelAction<>(product, new EventAction<Product>() {
                        @Override
                        public void delete(Product product) {
                            showMessageConfirm("Xác nhận xóa sản phẩm?", () -> {
                                deleteProduct(product);
                            });
                        }

                        @Override
                        public void update(Product product) {

                        }
                    })
                });
                stt++;
            }
        }
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
        resetRamdomCode();
        txtProductName.setText("");
        txtPriceProduct.setText("");
        txtSearchProduct.setText("");
        cbbCategoryProduct.setSelectedIndex(-1);
        cbbStatusProduct.setSelectedIndex(-1);
        tbProduct.clearSelection();
        init();
    }

    public void resetRamdomCode() {
        txtProductCode.setText("SP" + Ultil.generateRandomCode());
    }

    public boolean checkProduct() {
        // Kiểm tra mã sản phẩm
        if (txtProductCode.getText().isEmpty()) {
            this.showMessageFail("Mã trống!");
            return false;
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
        if (!checkProduct()) {
            return;
        }
        if (productDao.addProduct(readFormProduct())) {
            this.showMessageSuccess("Thêm sản phẩm thành công!");
            init();
            resetFormProduct();
            resetRamdomCode();
        } else {
            this.showMessageFail("Thêm sản phẩm thất bại");
        }
    }

    public void updateProduct() {
        if (!checkProduct()) {
            return;
        }
        int selectedRow = tbProduct.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tbProduct.getValueAt(selectedRow, 0);
            Product product = readFormProduct(); // Đọc dữ liệu từ form
            if (productDao.updateProduct(id, product)) {
                this.showMessageSuccess("Update thành công!");
                init();
            } else {
                this.showMessageFail("Update thất bại!!");
            }
        }
    }

    public void deleteProduct(Product p) {
        int selectedRow = tbProduct.getSelectedRow(); // Lấy hàng được chọn
        if (selectedRow != -1) { // Kiểm tra nếu có hàng được chọn
            int id = p.getId(); // Lấy ID từ cột đầu tiên
            productDao.deleteProduct(id); // Xóa sản phẩm trong DB

            init();
            // Kiểm tra nếu bảng còn dữ liệu, chọn lại dòng gần nhất
            if (tbProduct.getRowCount() > 0) {
                int newRow = Math.min(selectedRow, tbProduct.getRowCount() - 1);
                tbProduct.setRowSelectionInterval(newRow, newRow);
            }

            this.showMessageSuccess("Xóa thành công!");
            resetFormProduct();
            resetRamdomCode();
        } else {
            this.showMessageFail("Vui lòng chọn một sản phẩm để xóa.");
        }
    }

    public void searchProduct(String keyword) {
        List<Product> list = productDao.searchProduct(keyword);
        if (list.isEmpty()) {
            showMessageFail("Không tìm thấy sản phẩm nào!");
        } else {
            getListProduct(list); // Gọi hàm cập nhật bảng
        }
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="{crud product detail...">
    public void getListProductDetail(List<ProductDetail> list) {
        int stt = 1;
        tbProductDetail.setRowCount(0);

        for (ProductDetail productDetail : list) {
            tbProductDetail.addRow(new Object[]{
                productDetail.getId(),
                productDetail.getImagePath(),
                productDetail.getProduct().getProductName(),
                stt,
                new ModelImage(productDetail.getIcon().toString(), productDetail.getProductDetailName()),
                productDetail.getProductDetailCode(),
                productDetail.getBarCode(),
                productDetail.getTypePet().getTypePetName(),
                productDetail.getFlavor(),
                productDetail.getQuantityInStock(),
                productDetail.getWeight() + "KG",
                productDetail.getFormattedProductionDate(),
                productDetail.getExpirydate() + " Tháng",
                productDetail.getFormattedPriceBase(),
                productDetail.getDescription(),
                productDetail.isStatus() ? "Còn hàng" : "Hết hàng",
                new ModelAction<>(productDetail, new EventAction<ProductDetail>() {
                    @Override
                    public void delete(ProductDetail p) {
                        showMessageConfirm("Xác nhận xóa sản phẩm?", () -> {
                            deleteProductD(p);
                        });
                    }

                    @Override
                    public void update(ProductDetail p) {

                    }
                })
            }
            );
            stt++; // Tăng STT
//            if (tbProductDetail.getColumnModel().getColumnCount() > 0) {
//                tbProductDetail.getColumnModel().getColumn(0).setMinWidth(0);
//                tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(0);
//                tbProductDetail.getColumnModel().getColumn(0).setWidth(0);
//                tbProductDetail.getColumnModel().getColumn(0).setMinWidth(1);
//                tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(1);
//                tbProductDetail.getColumnModel().getColumn(0).setWidth(1);
//                tbProductDetail.getColumnModel().getColumn(0).setMinWidth(2);
//                tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(2);
//                tbProductDetail.getColumnModel().getColumn(0).setWidth(2);
//            }
        }
    }

    public void resetFormProductD() {
        txtProductCode.setText("");
        txtProductDetailName.setText("");
        txtPriceProductDetail.setText("");
        txtDescribe.setText("");
        txtQuantityInStock.setText("");
        txtPriceProductDetail.setText("");
        txtFlavor.setText("");
        txtExpiry.setText("");
        txtProductDate.setText("");
        txtWeightProductDetail.setText("");
        txtSearchProductDetail.setText("");

        pic.clearImage();
        pic.putClientProperty("imagePath", null);
//        System.out.println(pic.getClientProperty("imagePath"));// Lưu đường dẫn vào thuộc tính của pic

        cbbProduct.setSelectedIndex(-1);
        cbbTypePet.setSelectedIndex(-1);
        cbbFilterProduct.setSelectedIndex(-1);

        tbProductDetail.clearSelection();
        init();
    }

    public void showDataD() {
        int selectedRow = tbProductDetail.getSelectedRow(); // Lấy chỉ số dòng được chọn
        if (selectedRow == -1) {
            return; // Nếu không có dòng nào được chọn, thoát khỏi hàm
        }

        // Lấy dữ liệu từ bảng
        String productName = tbProductDetail.getValueAt(selectedRow, 2).toString();
        ModelImage modelImage = (ModelImage) tbProductDetail.getValueAt(selectedRow, 4);
        String productDetailName = modelImage.getNameProduct();
        String barCode = tbProductDetail.getValueAt(selectedRow, 5).toString(); // Mã vạch
        String typePet = tbProductDetail.getValueAt(selectedRow, 7).toString(); // Loại thú cưng
        String flavor = tbProductDetail.getValueAt(selectedRow, 8).toString(); // Hương vị
        String quantityInStock = tbProductDetail.getValueAt(selectedRow, 9).toString(); // Số lượng tồn kho
        String weight = tbProductDetail.getValueAt(selectedRow, 10).toString().replace("KG", ""); // Cân nặng (loại bỏ "KG")
        String productionDate = tbProductDetail.getValueAt(selectedRow, 11).toString(); // Ngày sản xuất
        String expiryDate = tbProductDetail.getValueAt(selectedRow, 12).toString().replace(" Tháng", ""); // Hạn sử dụng (loại bỏ "Tháng")
        String price = tbProductDetail.getValueAt(selectedRow, 13).toString().replace("VND", "").replace(",", ""); // Giá sản phẩm
        String description = tbProductDetail.getValueAt(selectedRow, 14).toString(); // Mô tả sản phẩm
        String imagePath = tbProductDetail.getValueAt(selectedRow, 1).toString(); // Đường dẫn ảnh
        boolean status = tbProductDetail.getValueAt(selectedRow, 15).equals("Hết hàng");
        if (status) {
            btnUpdateStatusProductDetail.setText("Tiếp tục bán");
        } else {
            btnUpdateStatusProductDetail.setText("Ngưng bán");
        }
        // Set dữ liệu vào các ô nhập
        txtProductDetailName.setText(productDetailName);
        txtFlavor.setText(flavor);
        txtQuantityInStock.setText(quantityInStock);
        txtWeightProductDetail.setText(weight);
        txtProductDate.setText(productionDate);
        txtExpiry.setText(expiryDate);
        txtPriceProductDetail.setText(price);
        txtDescribe.setText(description);

        // Hiển thị ảnh sản phẩm
        pic.putClientProperty("imagePath", imagePath);
        pic.setImage("D:\\FPT\\DA1\\Project1\\pet-shop\\src\\com\\petshop\\images\\" + imagePath);

        for (int i = 0; i < cbbProduct.getItemCount(); i++) {
            if (cbbProduct.getItemAt(i).toString().equalsIgnoreCase(productName)) {
                cbbProduct.setSelectedIndex(i);
                break;
            }
        }

        // Chọn item trong combobox dựa trên tên loại thú cưng
        for (int i = 0; i < cbbTypePet.getItemCount(); i++) {
            if (cbbTypePet.getItemAt(i).toString().equalsIgnoreCase(typePet)) {
                cbbTypePet.setSelectedIndex(i);
                break;
            }
        }
    }

    public ProductDetail readFormProductDetail() {
        String nameProductDetail = txtProductDetailName.getText().trim();
        String productDetailCode = "SPCT" + Ultil.generateRandomCode().trim();
        Product p = (Product) cbbProduct.getSelectedItem();
        TypePet typePet = (TypePet) cbbTypePet.getSelectedItem();

        // Tạo barcode tự động
        String barCode = Ultil.generateEAN13Barcode();
        Ultil.generateBarcodeImage(barCode); // gen img barcode

        // Chuyển đổi weight từ String sang BigDecimal (kiểm tra tránh lỗi)
        BigDecimal weight = BigDecimal.ZERO;
        try {
            String weightStr = txtWeightProductDetail.getText().trim();
            if (weightStr != null && !weightStr.trim().isEmpty()) {
                weight = new BigDecimal(weightStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Chuyển đổi quantity từ String sang int
        int amount = 0;
        try {
            amount = Integer.parseInt(txtQuantityInStock.getText().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Chuyển đổi expirydate từ String sang int
        int expirydate = 0;
        try {
            expirydate = Integer.parseInt(txtExpiry.getText().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Date sqlProductDate = null;
        try {
            String productDateStr = txtProductDate.getText().trim();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            if (!productDateStr.isEmpty()) {
                java.util.Date utilDate = df.parse(productDateStr); // Chuyển String -> java.util.Date
                sqlProductDate = new java.sql.Date(utilDate.getTime()); // Chuyển java.util.Date -> java.sql.Date
            } else {
                sqlProductDate = new java.sql.Date(System.currentTimeMillis()); // Nếu trống, lấy ngày hiện tại
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Lấy giá sản phẩm
        BigDecimal price = BigDecimal.ZERO;
        try {
            String priceStr = txtPriceProductDetail.getText().trim();
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = new BigDecimal(priceStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Lấy hương vị (flavor)
        String flavor = txtFlavor.getText().trim();
        String description = txtDescribe.getText().trim();
        String imagePath = (String) pic.getClientProperty("imagePath");
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = "01.jpg";
        }

        boolean statusP = amount > 0;

        // Trả về đối tượng ProductDetail
        return new ProductDetail(productDetailCode, nameProductDetail, barCode, p, typePet, expirydate, sqlProductDate, weight, amount, flavor, description, price, imagePath, statusP);
    }

    public ProductDetail readFormProductDetailForUpdate() {
        String nameProductDetail = txtProductDetailName.getText().trim();
        Product p = (Product) cbbProduct.getSelectedItem();
        TypePet typePet = (TypePet) cbbTypePet.getSelectedItem();

        int expirydate = 0;
        try {
            expirydate = Integer.parseInt(txtExpiry.getText().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Date sqlProductDate = null;
        try {
            String productDateStr = txtProductDate.getText().trim();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            if (!productDateStr.isEmpty()) {
                java.util.Date utilDate = df.parse(productDateStr); // Chuyển String -> java.util.Date
                sqlProductDate = new java.sql.Date(utilDate.getTime()); // Chuyển java.util.Date -> java.sql.Date
            } else {
                sqlProductDate = new java.sql.Date(System.currentTimeMillis()); // Nếu trống, lấy ngày hiện tại
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Chuyển đổi weight từ String sang BigDecimal
        BigDecimal weight = BigDecimal.ZERO;
        try {
            String weightStr = txtWeightProductDetail.getText().trim();
            if (!weightStr.isEmpty()) {
                weight = new BigDecimal(weightStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Chuyển đổi quantity từ String sang int
        int amount = 0;
        try {
            amount = Integer.parseInt(txtQuantityInStock.getText().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Lấy giá sản phẩm
        BigDecimal price = BigDecimal.ZERO;
        try {
            String priceStr = txtPriceProductDetail.getText().trim();
            if (!priceStr.isEmpty()) {
                price = new BigDecimal(priceStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Date productDate = null;
        try {
            String productDateStr = txtProductDate.getText().trim();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            if (!productDateStr.isEmpty()) {
                java.util.Date utilDate = df.parse(productDateStr); // Chuyển String -> java.util.Date
                productDate = new java.sql.Date(utilDate.getTime()); // Chuyển java.util.Date -> java.sql.Date
            } else {
                sqlProductDate = new java.sql.Date(System.currentTimeMillis()); // Nếu trống, lấy ngày hiện tại
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Lấy hương vị (flavor)
        String flavor = txtFlavor.getText().trim();
        String description = txtDescribe.getText().trim();
        String imagePath = (String) pic.getClientProperty("imagePath");

        boolean statusP = amount > 0;

        // Trả về đối tượng ProductDetail
        return new ProductDetail(nameProductDetail, p, typePet, expirydate, productDate, weight, amount, flavor, description, price, imagePath, statusP);
    }

    public boolean checkProductD() {
        if (txtProductDetailName.getText().isEmpty()) {
            return false;
        } else if (txtPriceProductDetail.getText().isEmpty()) {
            return false;
        } else if (txtWeightProductDetail.getText().isEmpty()) {
            return false;
        } else if (txtFlavor.getText().isEmpty()) {
            return false;
        } else if (txtQuantityInStock.getText().isEmpty()) {
            return false;
        } else if (txtDescribe.getText().isEmpty()) {
            return false;
        } else if (txtExpiry.getText().isEmpty()) {
            return false;
        } else if (txtProductDate.getText().isEmpty()) {
            return false;
        }

        try {
            // Kiểm tra giá không âm và không vượt quá 1 tỷ
            double price = Double.parseDouble(txtPriceProductDetail.getText());
            if (price < 0) {
                this.showMessageFail("Giá không được là số âm!");
                return false;
            }
            if (price > 1_000_000_000) {
                this.showMessageFail("Giá không được vượt quá 1 tỷ!");
                return false;
            }

            // Kiểm tra số lượng không âm
            int quantity = Integer.parseInt(txtQuantityInStock.getText());
            if (quantity < 0) {
                this.showMessageFail("Số lượng không được là số âm!");
                return false;
            }

            // Kiểm tra hương vị không chứa số
            if (!txtFlavor.getText().matches("^[^0-9]*$")) {
                this.showMessageFail("Hương vị không được chứa số!");
                return false;
            }

            // Kiểm tra trọng lượng không âm và không quá 100kg
            double weight = Double.parseDouble(txtWeightProductDetail.getText());
            if (weight < 0) {
                this.showMessageFail("Trọng lượng không được là số âm!");
                return false;
            }
            if (weight > 100) {
                this.showMessageFail("Trọng lượng không được vượt quá 100kg!");
                return false;
            }

            // Kiểm tra ngày hết hạn không âm
            int expiry = Integer.parseInt(txtExpiry.getText());
            if (expiry < 0) {
                this.showMessageFail("Hạn sử dụng không được là số âm!");
                return false;
            }

            // Kiểm tra ngày sản xuất không lớn hơn ngày hiện tại
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate productDate = LocalDate.parse(txtProductDate.getText(), formatter);
            if (productDate.isAfter(LocalDate.now())) {
                this.showMessageFail("Ngày sản xuất không được lớn hơn ngày hiện tại!");
                return false;
            }

            // Kiểm tra combobox
            if (cbbProduct.getSelectedIndex() == -1) {
                return false;
            } else if (cbbTypePet.getSelectedIndex() == -1) {
                return false;
            }

        } catch (NumberFormatException e) {
            this.showMessageFail("Định dạng số không hợp lệ!");
            return false;
        } catch (DateTimeParseException e) {
            this.showMessageFail("Định dạng ngày không hợp lệ! (yyyy-MM-dd)");
            return false;
        }

        return true;
    }

    public void insertProductD() {
        if (!checkProductD()) {
            return;
        }
        if (productDetailDao.addProductDetail(readFormProductDetail())) {
            showMessageSuccess("Thêm thành công!");
            init();
            this.resetFormProductD();
        } else {
            showMessageFail("Thêm thất bại!");
        }

    }

    public void deleteProductD(ProductDetail p) {
        try {
            int selectRow = tbProductDetail.getSelectedRow();
            if (selectRow != -1) {
                int id = p.getId();
                productDetailDao.deleteProductDetail(id);
                this.showMessageSuccess("Xóa thành công!!");
                init();
                resetFormProductD();
            } else {
                this.showMessageFail("Vui lòng chọn thông tin để xóa!");
            }
        } catch (Exception e) {
            showMessageError("Có lỗi sảy ra!!");
            e.printStackTrace();
        }
    }

    public void updateProductD() {
        try {
            if (!checkProductD()) {
                return;
            }
            int selectedRow = tbProductDetail.getSelectedRow();
            if (selectedRow == -1) {
                showMessageFail("Vui lòng chọn một sản phẩm để cập nhật!");
                return;
            }
            int id = (int) tbProductDetail.getValueAt(selectedRow, 0);
            if (productDetailDao.updateProductDetail(id, readFormProductDetailForUpdate())) {
                showMessageSuccess("Cập nhập thành công!");
                init();
            } else {
                showMessageFail("Cập nhập thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để dễ debug
            showMessageError("Đã xảy ra lỗi khi cập nhật!");
        }
    }

    public void updateStatusProductD() {
        int selectedRow = tbProductDetail.getSelectedRow();
        if (selectedRow != -1) {
            boolean status = tbProductDetail.getValueAt(selectedRow, 15).equals("Hết hàng");
            productDetailDao.updateStatusProductDetail(status, (int) tbProductDetail.getValueAt(selectedRow, 0));
            init();
            showMessageSuccess("Thay đổi trạng thái thành công!");
        } else {
            showMessageFail("Vui lòng chọn sản phẩm");
        }

    }

    public void searchProductD(String keyword) {
        List<ProductDetail> productsByNameOrFlavor = productDetailDao.searchByNameOrFlavor(keyword);
        if (productsByNameOrFlavor.isEmpty()) {
            showMessageFail("Không tìm thấy sản phẩm nào!");
        } else {
            getListProductDetail(productsByNameOrFlavor); // Gọi hàm cập nhật bảng
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

        dateChooser = new com.petshop.swing.datechooser.DateChooser();
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
        txtSearchProduct = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterCategory = new com.petshop.swing.combobox.Combobox();
        combobox4 = new com.petshop.swing.combobox.Combobox();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtProductDetailName = new com.petshop.swing.textfield.TextFieldRounded();
        txtProductDate = new com.petshop.swing.textfield.TextFieldRounded();
        textAreaScroll1 = new com.petshop.swing.textarea.TextAreaScroll();
        txtDescribe = new com.petshop.swing.textarea.TextArea();
        txtWeightProductDetail = new com.petshop.swing.textfield.TextFieldRounded();
        txtExpiry = new com.petshop.swing.textfield.TextFieldRounded();
        btnPopupTypePet = new com.petshop.swing.Button();
        pic = new com.petshop.swing.ImageRectangle();
        btnSelectImage = new com.petshop.swing.Button();
        txtPriceProductDetail = new com.petshop.swing.textfield.TextFieldRounded();
        txtQuantityInStock = new com.petshop.swing.textfield.TextFieldRounded();
        cbbProduct = new com.petshop.swing.combobox.Combobox();
        cbbTypePet = new com.petshop.swing.combobox.Combobox();
        txtFlavor = new com.petshop.swing.textfield.TextFieldRounded();
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
        txtSearchProductDetail = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterProduct = new com.petshop.swing.combobox.Combobox();
        cbbFilterTypePet = new com.petshop.swing.combobox.Combobox();
        cbbSort = new com.petshop.swing.combobox.Combobox();

        dateChooser.setTextRefernce(txtProductDate);

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
            tbProduct.getColumnModel().getColumn(0).setMinWidth(0);
            tbProduct.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Danh sách sản phẩm");

        txtSearchProduct.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchProductActionPerformed(evt);
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
                        .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        txtProductDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductDate.setLabelText("NSX(dd/MM/yyy)");
        txtProductDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductDateActionPerformed(evt);
            }
        });

        textAreaScroll1.setBackground(new java.awt.Color(255, 255, 255));
        textAreaScroll1.setLabelText("Mô tả");

        txtDescribe.setColumns(20);
        txtDescribe.setRows(5);
        textAreaScroll1.setViewportView(txtDescribe);

        txtWeightProductDetail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtWeightProductDetail.setLabelText("Trọng lượng (gram)");

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
        btnSelectImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectImageActionPerformed(evt);
            }
        });

        txtPriceProductDetail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPriceProductDetail.setLabelText("Giá bán (VND)");

        txtQuantityInStock.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtQuantityInStock.setLabelText("Số lượng");

        cbbProduct.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbProduct.setLabeText("Sản phẩm");

        cbbTypePet.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbTypePet.setLabeText("Loại thú cưng");

        txtFlavor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFlavor.setLabelText("Hương vị");
        txtFlavor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFlavorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbTypePet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPriceProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(cbbProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtWeightProductDetail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addComponent(txtProductDetailName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnPopupTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFlavor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtExpiry, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(txtProductDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtQuantityInStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtProductDetailName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtQuantityInStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtWeightProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPriceProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(btnPopupTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtProductDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtFlavor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(cbbTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap())
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("THÔNG TIN SẢN PHẨM CHI TIẾT");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAddProductDetail.setBackground(new java.awt.Color(204, 255, 255));
        btnAddProductDetail.setText("Thêm");
        btnAddProductDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductDetailActionPerformed(evt);
            }
        });

        btnUpdateProductDetail.setBackground(new java.awt.Color(255, 255, 204));
        btnUpdateProductDetail.setText("Cập nhập");
        btnUpdateProductDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProductDetailActionPerformed(evt);
            }
        });

        btnRestProductDetail.setBackground(new java.awt.Color(204, 204, 204));
        btnRestProductDetail.setText("Làm mới");
        btnRestProductDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestProductDetailActionPerformed(evt);
            }
        });

        button9.setBackground(new java.awt.Color(204, 255, 204));
        button9.setText("Quét BarCode");
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });

        btnUpdateStatusProductDetail.setBackground(new java.awt.Color(255, 204, 204));
        btnUpdateStatusProductDetail.setText("             ");
        btnUpdateStatusProductDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateStatusProductDetailActionPerformed(evt);
            }
        });

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
                        .addComponent(btnUpdateStatusProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRestProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                "", "", "", "STT", "Tên SPCT", "Mã SPCT", "BarCode", "Dành cho", "Hương vị", "SL", "TL", "NSX", "HSD", "Giá bán", "Mô tả", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbProductDetail);
        if (tbProductDetail.getColumnModel().getColumnCount() > 0) {
            tbProductDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbProductDetail.getColumnModel().getColumn(1).setMinWidth(0);
            tbProductDetail.getColumnModel().getColumn(1).setMaxWidth(0);
            tbProductDetail.getColumnModel().getColumn(2).setMinWidth(0);
            tbProductDetail.getColumnModel().getColumn(2).setMaxWidth(0);
            tbProductDetail.getColumnModel().getColumn(3).setMaxWidth(40);
            tbProductDetail.getColumnModel().getColumn(4).setMinWidth(180);
            tbProductDetail.getColumnModel().getColumn(5).setMinWidth(80);
            tbProductDetail.getColumnModel().getColumn(7).setMaxWidth(70);
            tbProductDetail.getColumnModel().getColumn(8).setMaxWidth(70);
            tbProductDetail.getColumnModel().getColumn(9).setMaxWidth(40);
            tbProductDetail.getColumnModel().getColumn(10).setMaxWidth(60);
            tbProductDetail.getColumnModel().getColumn(12).setMaxWidth(70);
        }

        txtSearchProductDetail.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchProductDetail.setMinimumSize(new java.awt.Dimension(64, 42));

        cbbFilterProduct.setLabeText("Lọc theo sản phẩm");

        cbbFilterTypePet.setLabeText("Lọc thưc ăn cho loại thú cưng");

        cbbSort.setLabeText("Sắp xếp theo");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
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

    private void txtProductDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductDateActionPerformed

    private void txtSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchProductActionPerformed

    private void btnPopupCProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupCProductActionPerformed
        // TODO add your handling code here:
        showPopupCategoryProduct();
    }//GEN-LAST:event_btnPopupCProductActionPerformed

    private void btnPopupTypePetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupTypePetActionPerformed
        // TODO add your handling code here:
        this.showPopupTypePet();
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

    private void btnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectImageActionPerformed

        Window window = SwingUtilities.getWindowAncestor(this);
        JnaFileChooser jnaCh = new JnaFileChooser();

        // Thiết lập thư mục mặc định
        String defaultDirectory = new String("D:\\FPT\\DA1\\Project1\\pet-shop\\src\\com\\petshop\\images");
        jnaCh.setCurrentDirectory(defaultDirectory);

        // Mở hộp thoại chọn file
        boolean save = jnaCh.showOpenDialog(window); // Sử dụng showOpenDialog để chọn ảnh
        if (save) {
            File selectedFile = jnaCh.getSelectedFile();
            if (selectedFile != null) {
                // Lấy tên file ảnh
                String fileName = selectedFile.getName();

                // Đường dẫn cố định
                String fixedPath = "D:\\FPT\\DA1\\Project1\\pet-shop\\src\\com\\petshop\\images\\";

                // Tạo đường dẫn ảnh hoàn chỉnh
                String imagePath = fixedPath + fileName;

                System.out.println("Đã chọn ảnh: " + imagePath);
                pic.setImage(imagePath);
                pic.putClientProperty("imagePath", fileName); // Lưu đường dẫn vào thuộc tính của pic
//                System.out.println(pic.getClientProperty("imagePath"));
            }
        }

    }//GEN-LAST:event_btnSelectImageActionPerformed

    private void txtFlavorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFlavorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFlavorActionPerformed

    private void btnAddProductDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductDetailActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận thêm", ()
                -> insertProductD());
    }//GEN-LAST:event_btnAddProductDetailActionPerformed

    private void btnRestProductDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestProductDetailActionPerformed
        // TODO add your handling code here:
        resetFormProductD();
    }//GEN-LAST:event_btnRestProductDetailActionPerformed

    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
        // TODO add your handling code here:
        showPopupWebcam();
    }//GEN-LAST:event_button9ActionPerformed

    private void btnUpdateProductDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProductDetailActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận?", () -> {
            updateProductD();
        });
    }//GEN-LAST:event_btnUpdateProductDetailActionPerformed

    private void btnUpdateStatusProductDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateStatusProductDetailActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận?", () -> {
            updateStatusProductD();
        }
        );
    }//GEN-LAST:event_btnUpdateStatusProductDetailActionPerformed


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
    private com.petshop.swing.datechooser.DateChooser dateChooser;
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
    private com.petshop.swing.textfield.TextFieldRounded txtProductDate;
    private com.petshop.swing.textfield.TextFieldRounded txtProductDetailName;
    private com.petshop.swing.textfield.TextFieldRounded txtProductName;
    private com.petshop.swing.textfield.TextFieldRounded txtQuantityInStock;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProduct;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProductDetail;
    private com.petshop.swing.textfield.TextFieldRounded txtWeightProductDetail;
    // End of variables declaration//GEN-END:variables
}
