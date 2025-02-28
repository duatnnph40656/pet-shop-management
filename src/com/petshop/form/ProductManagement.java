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
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.CategoryProducts;
import com.petshop.models.Products;
import com.petshop.models.ProductDetails;
import com.petshop.models.TypePets;
import com.petshop.popup.PopupCategoryPet;
import com.petshop.popup.PopupCategoryProduct;
import com.petshop.popup.PopupScan;
import com.petshop.popup.PopupShowHistoryDeleted;
import com.petshop.services.RememberMeService;
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
import java.awt.TextArea;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
    private RememberMeService rememberMeService;

    public ProductManagement() {
        initComponents();
        productDao = new ProductDAO();
        categoryProductDao = new CategoryProductDAO();
        productDetailDao = new ProductDetailDAO();
        typePetDAO = new TypePetDAO();
        rememberMeService = new RememberMeService();
        checkPermission();
        tbProduct.fixTable(jScrollPane1);
        tbProductDetail.fixTable(jScrollPane2);
        loadCbbSort();
        init();
    }

    private void checkPermission() {
        if (rememberMeService.getEmployeeId() != 1) {
            btnAddProduct.setEnabled(false);
            btnAddProductDetail.setEnabled(false);
            btnUpdateProductDetail.setEnabled(false);
            btnUpdateProduct.setEnabled(false);

        }

    }

    void init() {
        resetRamdomCode();
        addTableEvent();
        searchEvent();
        dateChooserEvent();
        this.getListProduct(productDao.getListProduct());
        this.getListProductDetail(productDetailDao.getListProductDetail());
        this.loadCBBCategoryProduct(categoryProductDao.getListCategoryProduct());
        this.loadCBBTypePet(typePetDAO.getListTypePet());
        this.loadFilterCBBProduct(categoryProductDao.getListCategoryProduct());
        this.loadCBBProduct(productDao.getListProduct());
        this.loadCbbFilterTypePet(typePetDAO.getListTypePet());
        this.loadCbbFilterProduct(productDao.getListProduct());
        txtProductCode.setEditable(false);
        txtProductDate.setText("Chọn ngày");
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
                    if (txtSearchProductDetail.getText().isEmpty()) {
                        showMessageFail("Vui lòng nhập thông tin");
                        call.done();
                        return;
                    }
                    searchProductD(txtSearchProductDetail.getText());
                    txtSearchProductDetail.setText("");// tìm kiếm sau khi ấn, lấy text của thanh search
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
                    if (txtSearchProduct.getText().isEmpty()) {
                        showMessageFail("Vui lòng nhập thông tin");
                        call.done();
                        return;
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

    //<editor-fold defaultstate="collapsed" desc="{Popup...">
    public void showPopupTypePet() {
        PopupCategoryPet tPopup = new PopupCategoryPet();
        tPopup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                loadCBBTypePet(typePetDAO.getListTypePet());
            }

            @Override
            public void onCancel() {
                loadCBBTypePet(typePetDAO.getListTypePet());
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
        PopupScan pWebCam = new PopupScan();

        pWebCam.setCodeListener((String barcode) -> {
            List<ProductDetails> product = productDetailDao.searchByBarCode(barcode);
            if (product != null) {
                getListProductDetail(product); // Hiển thị thông tin sản phẩm lên giao diện
            } else {
                showMessageFail("Không tìm thấy sản phẩm có mã vạch: " + barcode);
            }
        });

        GlassPanePopup.showPopup(pWebCam, "pWebCam");
    }

    public void showPopUpHistoryDeletedProducts() {
        int stt = 1;
        PopupShowHistoryDeleted popup = new PopupShowHistoryDeleted();
        List<Products> products = productDao.getListProductDeleted();
        // Chuyển đổi danh sách sản phẩm thành List<Object[]>
        List<Object[]> data = new ArrayList<>();
        for (Products p : products) {
            data.add(new Object[]{
                stt,
                p.getProductCode(),
                p.getProductName(),
                p.getCategoryProduct().getCategoryProductName(),
                p.getFormattedPriceBase(),
                p.getFormattedCreatedAt(),
                p.isStatus() ? "Hoạt động" : "Ngưng hoạt động",
                new ModelAction<>(p, new EventAction<Products>() {
                    @Override
                    public void delete(Products product) {
                        showMessageConfirm("Xác nhận khôi phục sản phẩm này", () -> {
                            restoreProduct(product);
                            reloadTableProduct(popup);
                        });
                    }

                    @Override
                    public void update(Products product) {

                    }

                    @Override
                    public void add(Products model) {

                    }
                })
            });
            stt++;
        }

        // Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Mã SP", "Tên SP", "Danh Mục", "Giá", "Ngày Tạo", "Trạng thái", "Thao tác"};

        // Hiển thị popup
        popup.setLbText("Danh sách sản phẩm đã xóa");
        popup.fillTable(data, columnNames); // Đảm bảo bảng có dữ liệu trước khi hiển thị

        popup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                getListProduct(productDao.getListProduct());
            }
        });
        GlassPanePopup.showPopup(popup);
    }

    private void reloadTableProduct(PopupShowHistoryDeleted popup) {
        int stt = 1;
        List<Products> products = productDao.getListProductDeleted();
        List<Object[]> data = new ArrayList<>();

        for (Products p : products) {
            data.add(new Object[]{
                stt,
                p.getProductCode(),
                p.getProductName(),
                p.getCategoryProduct().getCategoryProductName(),
                p.getFormattedPriceBase(),
                p.getFormattedCreatedAt(),
                p.isStatus() ? "Hoạt động" : "Ngưng hoạt động",
                new ModelAction<>(p, new EventAction<Products>() {
                    @Override
                    public void delete(Products product) {
                        showMessageConfirm("Xác nhận khôi phục sản phẩm này", () -> {
                            restoreProduct(product);
                            reloadTableProduct(popup); // Gọi lại sau khi khôi phục
                        });
                    }

                    @Override
                    public void update(Products product) {
                    }

                    @Override
                    public void add(Products model) {
                    }
                })
            });
            stt++;
        }

        // Cập nhật lại bảng
        popup.fillTable(data, new String[]{"STT", "Mã SP", "Tên SP", "Danh Mục", "Giá", "Ngày Tạo", "Trạng thái", "Thao tác"});
    }

    public void showPopupHistoryDeletedProductD() {
        int stt = 1;
        PopupShowHistoryDeleted popup = new PopupShowHistoryDeleted();
        List<ProductDetails> list = productDetailDao.getListProductDetailDeleted();
        List<Object[]> data = new ArrayList<>();
        for (ProductDetails productDetail : list) {
            data.add(new Object[]{
                stt,
                new ModelImage(productDetail.getImageName(), productDetail.getProductDetailName()),
                productDetail.getProductDetailCode(),
                productDetail.getBarCode(),
                productDetail.getTypePet().getTypePetName(),
                productDetail.getFlavor(),
                productDetail.getWeight() + "KG",
                productDetail.getFormattedPriceBase(),
                productDetail.isStatus() ? "Còn hàng" : "Hết hàng",
                new ModelAction<>(productDetail, new EventAction<ProductDetails>() {
                    @Override
                    public void delete(ProductDetails p) {
                        showMessageConfirm("Xác nhận khôi phục sản phẩm?", () -> {
                            restoreProductDetail(p);
                            reloadTableProductD(popup);
                        });
                    }

                    @Override
                    public void update(ProductDetails p) {

                    }

                    @Override
                    public void add(ProductDetails model) {

                    }
                })

            });
            stt++;
        }
// Định nghĩa tiêu đề cột
        String[] columnNames = {"STT", "Tên SPCT", "Mã SPCT", "BarCode", "Dành cho", "Hương vị", "TL", "Giá bán", "Trạng thái", "Thao tác"};

        // Hiển thị popup
        popup.setLbText("Danh sách sản phẩm chi tiết đã xóa");
        popup.fillTable(data, columnNames); // Đảm bảo bảng có dữ liệu trước khi hiển thị

        popup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                getListProductDetail(productDetailDao.getListProductDetail());
            }
        });
        GlassPanePopup.showPopup(popup);
    }

    private void reloadTableProductD(PopupShowHistoryDeleted popup) {
        int stt = 1;
        List<ProductDetails> list = productDetailDao.getListProductDetailDeleted();
        List<Object[]> data = new ArrayList<>();
        for (ProductDetails productDetail : list) {
            data.add(new Object[]{
                stt,
                new ModelImage(productDetail.getImageName(), productDetail.getProductDetailName()),
                productDetail.getProductDetailCode(),
                productDetail.getBarCode(),
                productDetail.getTypePet().getTypePetName(),
                productDetail.getFlavor(),
                productDetail.getWeight() + "KG",
                productDetail.getFormattedPriceBase(),
                productDetail.isStatus() ? "Còn hàng" : "Hết hàng",
                new ModelAction<>(productDetail, new EventAction<ProductDetails>() {
                    @Override
                    public void delete(ProductDetails p) {
                        showMessageConfirm("Xác nhận khôi phục sản phẩm?", () -> {
                            restoreProductDetail(p);
                            reloadTableProductD(popup);
                        });
                    }

                    @Override
                    public void update(ProductDetails p) {

                    }

                    @Override
                    public void add(ProductDetails model) {
                    }
                })

            });
            stt++;
        }
        String[] columnNames = {"STT", "Tên SPCT", "Mã SPCT", "BarCode", "Dành cho", "Hương vị", "TL", "Giá bán", "Trạng thái", "Thao tác"};
        popup.fillTable(data, columnNames);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{Loadcbb...">
    //<editor-fold defaultstate="collapsed" desc="{LoadcbbForProduct...">
    public void loadCBBCategoryProduct(List<CategoryProducts> categoryList) {
        cbbCategoryProduct.removeAllItems();

        if (categoryList == null || categoryList.isEmpty()) {
            return; // Nếu danh sách null hoặc rỗng, thoát khỏi phương thức
        }

        for (CategoryProducts category : categoryList) {
            cbbCategoryProduct.addItem(category);
        }

        // Đặt mục chọn về -1 (không có mục nào được chọn)
        cbbCategoryProduct.setSelectedIndex(-1);
    }

    public void loadCBBTypePet(List<TypePets> list) {
        cbbTypePet.removeAllItems();
        cbbFilterTypePet.removeAllItems();

        if (list == null || list.isEmpty()) {
            return; // Nếu danh sách null hoặc rỗng, thoát khỏi phương thức
        }

        for (TypePets t : list) {
            cbbTypePet.addItem(t);
            cbbFilterTypePet.addItem(t);
        }
        cbbFilterTypePet.setSelectedIndex(-1);
        cbbTypePet.setSelectedIndex(-1);

        cbbFilterTypePet.addActionListener(e -> {
            if (cbbFilterTypePet.getSelectedIndex() == -1) {
                return;
            }
            TypePets t = (TypePets) cbbFilterTypePet.getSelectedItem();
            if (t != null) {
                int typePetId = t.getId();
//                filterProductDetailByTypePet(typePetId);
            }
        });
    }

    public void loadFilterCBBProduct(List<CategoryProducts> categoryList) {
        cbbFilterCategory.removeAllItems(); // Xóa tất cả các mục hiện có

        if (categoryList == null || categoryList.isEmpty()) {
            return; // Nếu danh sách null hoặc rỗng, thoát khỏi phương thức
        }

        for (CategoryProducts category : categoryList) {
            cbbFilterCategory.addItem(category); // Thêm đối tượng CategoryProduct vào JComboBox
        }
        cbbFilterCategory.setSelectedIndex(-1);

        cbbFilterCategory.addActionListener(e -> {
            if (cbbFilterCategory.getSelectedIndex() == -1) {
                return;
            }
            CategoryProducts selectedCategory = (CategoryProducts) cbbFilterCategory.getSelectedItem();
            if (selectedCategory != null) {
                int categoryId = selectedCategory.getId();
//                filterProductByCategory(categoryId);
            }
        });

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{LoadcbbForProductDetail...">
    public void loadCBBProduct(List<Products> productList) {
        cbbProduct.removeAllItems();

        if (productList == null || productList.isEmpty()) {
            return;
        }

        for (Products p : productList) {
            cbbProduct.addItem(p);
        }

        cbbProduct.setSelectedIndex(0); // Mặc định chọn "Tất cả"
    }

    private void loadCbbFilterTypePet(List<TypePets> list) {
        cbbFilterTypePet.removeAllItems();
        // Thêm một đối tượng đặc biệt đại diện cho "Tất cả"
        TypePets allPetsOption = new TypePets();
        allPetsOption.setId(0); // ID null để biểu thị không lọc
        allPetsOption.setTypePetName("Tất cả");
        cbbFilterTypePet.addItem(allPetsOption);// Thêm tùy chọn "Tất cả"

        if (list == null || list.isEmpty()) {
            return;
        }

        for (TypePets t : list) {
            cbbFilterTypePet.addItem(t);
        }

        cbbFilterTypePet.setSelectedIndex(0);
        cbbFilterTypePet.addActionListener(e -> filterAndSortProductDetails());
    }

    private void loadCbbFilterProduct(List<Products> productList) {
        cbbFilterProduct.removeAllItems();
        cbbFilterProduct.addItem("Tất cả"); // Thêm tùy chọn "Tất cả"

        if (productList == null || productList.isEmpty()) {
            return;
        }

        for (Products p : productList) {
            cbbFilterProduct.addItem(p);
        }

        cbbFilterProduct.setSelectedIndex(0);
        cbbFilterProduct.addActionListener(e -> filterAndSortProductDetails());
    }

    private void loadCbbSort() {
        cbbSort.removeAllItems();
        cbbSort.addItem("Tất cả"); // Thêm tùy chọn "Tất cả"
        cbbSort.addItem("Theo giá tăng dần");
        cbbSort.addItem("Theo giá giảm dần");

        cbbSort.setSelectedIndex(0);
        cbbSort.addActionListener(e -> filterAndSortProductDetails());
    }

    private void filterAndSortProductDetails() {
        // Lấy giá trị được chọn từ các JComboBox
        Object selectedProduct = cbbFilterProduct.getSelectedItem();
        Object selectedTypePet = cbbFilterTypePet.getSelectedItem();
        String selectedSort = (String) cbbSort.getSelectedItem();

        // Kiểm tra nếu chọn "Tất cả", đặt ID thành null để bỏ qua bộ lọc
        Integer productId = (selectedProduct instanceof Products) ? ((Products) selectedProduct).getId() : null;
        Integer typePetId = (selectedTypePet instanceof TypePets) ? ((TypePets) selectedTypePet).getId() : null;

        // Lấy danh sách sản phẩm chi tiết dựa trên bộ lọc
        List<ProductDetails> filteredList;
        if (productId == null && typePetId == null) {
            filteredList = productDetailDao.getListProductDetail(); // Lấy toàn bộ danh sách
        } else {
            filteredList = productDetailDao.searchProductDetails(productId, typePetId);
        }

        // Sắp xếp danh sách nếu có yêu cầu sắp xếp
        if (selectedSort != null && !"Tất cả".equals(selectedSort)) {
            switch (selectedSort) {
                case "Theo giá tăng dần":
                    filteredList.sort(Comparator.comparing(ProductDetails::getPrice));
                    break;
                case "Theo giá giảm dần":
                    filteredList.sort(Comparator.comparing(ProductDetails::getPrice).reversed());
                    break;
            }
        }

        // Hiển thị danh sách đã lọc và sắp xếp
        getListProductDetail(filteredList);
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{crud product...">
    public void getListProduct(List<Products> list) {
        int stt = 1;
        tbProduct.setRowCount(0); // Xóa dữ liệu cũ
        for (Products product : list) {
            if (!product.isDeleted()) {
                tbProduct.addRow(new Object[]{
                    product.getId(),
                    stt,
                    product.getProductCode(),
                    product.getProductName(),
                    product.getCategoryProduct().getCategoryProductName(),
                    Ultil.formatCurrency(product.getPriceBase()),
                    product.getFormattedCreatedAt(),
                    product.isStatus() ? "Hoạt động" : "Ngưng hoạt động",
                    new ModelAction<>(product, new EventAction<Products>() {
                        @Override
                        public void delete(Products product) {
                            showMessageConfirm("Xác nhận xóa sản phẩm?", () -> {
                                deleteProduct(product);
                            });
                        }

                        @Override
                        public void update(Products product) {

                        }

                        @Override
                        public void add(Products model) {
                        }
                    })
                });
                stt++;
            }
        }
    }

    public int getSelectedRowProduct() {
        return tbProduct.getSelectedRow();
    }

    public Integer getIdSelectedProduct() {
        int selectedRow = getSelectedRowProduct();
        if (selectedRow == -1) {
            return null; // Không có dòng nào được chọn
        }
        return (Integer) tbProduct.getValueAt(selectedRow, 0); // Cột 0 chứa ID hóa đơn
    }

    public Products readFormProduct() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String productCode = "SP" + Ultil.generateRandomCode();
            String productName = txtProductName.getText().trim();
            BigDecimal priceBase = new BigDecimal(txtPriceProduct.getText().trim());

            // Lấy danh mục sản phẩm từ JComboBox
            CategoryProducts categoryProduct = (CategoryProducts) cbbCategoryProduct.getSelectedItem();

            // Tạo đối tượng Product
            Products product = new Products();
            product.setProductCode(productCode);
            product.setProductName(productName);
            product.setCategoryProduct(categoryProduct); // Gán trực tiếp đối tượng CategoryProduct
            product.setPriceBase(priceBase);
            product.setStatus(true);

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
        txtProductCode.setText((String) tbProduct.getValueAt(index, 2));
        txtProductName.setText((String) tbProduct.getValueAt(index, 3));
        String categoryName = (String) tbProduct.getValueAt(index, 4);
        for (int i = 0; i < cbbCategoryProduct.getItemCount(); i++) {
            CategoryProducts category = (CategoryProducts) cbbCategoryProduct.getItemAt(i);
            if (category.getCategoryProductName().equals(categoryName)) {
                cbbCategoryProduct.setSelectedItem(category);
                break;
            }
        }
        String priceText = tbProduct.getValueAt(index, 5).toString();
        priceText = priceText.replace("₫", "").replace(".", "").replace("\u00A0", "").replaceAll("\\s+", "").trim();
        txtPriceProduct.setText(priceText);

        boolean status = tbProduct.getValueAt(getSelectedRowProduct(), 7).equals("Ngưng hoạt động");
        if (status) {
            btnUpdateStatusP.setText("Hoạt động");
        } else {
            btnUpdateStatusP.setText("Ngưng hoạt động");
        }
    }

    public void resetFormProduct() {
        txtProductCode.enable(true);
        resetRamdomCode();
        txtProductName.setText("");
        txtPriceProduct.setText("");
        txtSearchProduct.setText("");
        cbbCategoryProduct.setSelectedIndex(-1);
        tbProduct.clearSelection();
        btnUpdateStatusP.setText("");
        btnUpdateStatusProductDetail.setText("");
//        init();
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
        if (productDao.isProductNameExists(txtProductName.getText())) {
            showMessageFail("Sản phẩm đã tồn tại!!");
            return;
        }
        if (productDao.addProduct(readFormProduct())) {
            this.showMessageSuccess("Thêm sản phẩm thành công!");
            getListProduct(productDao.getListProduct());
            resetFormProduct();
            resetRamdomCode();
        } else {
            this.showMessageFail("Thêm sản phẩm thất bại");
        }
    }

    public void updateProduct() {
        if (getSelectedRowProduct() == -1) {
            showMessageFail("Vui lòng chọn sản phẩm!!");
            return;
        }
        if (!checkProduct()) {
            return;
        }
        int selectedRow = tbProduct.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tbProduct.getValueAt(selectedRow, 0);
            Products product = readFormProduct(); // Đọc dữ liệu từ form
            if (productDao.updateProduct(id, product)) {
                this.showMessageSuccess("Update thành công!");
                getListProduct(productDao.getListProduct());
            } else {
                this.showMessageFail("Update thất bại!!");
            }
        }
    }

    public void updateStatusProduct() {
        if (getSelectedRowProduct() == -1) {
            showMessageFail("Vui lòng chọn thông tin sản phẩm!!");
            return;
        }
        boolean status = tbProduct.getValueAt(getSelectedRowProduct(), 7).equals("Ngưng hoạt động");
        productDao.updateStatusProduct(getIdSelectedProduct(), status);
        getListProduct(productDao.getListProduct());
    }

    public void restoreProduct(Products p) {
        productDao.restoreProduct(p.getId());
        getListProduct(productDao.getListProduct());
    }

    public void deleteProduct(Products p) {
        if (rememberMeService.getEmployeeId() != 1) {
            showMessageFail("Bạn không có quyền xóa!!!");
            return;
        }

        int selectedRow = tbProduct.getSelectedRow(); // Lấy hàng được chọn
        if (selectedRow != -1) { // Kiểm tra nếu có hàng được chọn
            int id = p.getId(); // Lấy ID từ cột đầu tiên
            productDao.deleteProduct(id); // Xóa sản phẩm trong DB
            getListProduct(productDao.getListProduct());
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
        List<Products> list = productDao.searchProduct(keyword);
        if (list.isEmpty()) {
            showMessageFail("Không tìm thấy sản phẩm nào!");
        } else {
            getListProduct(list); // Gọi hàm cập nhật bảng
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{crud product detail...">
    public void getListProductDetail(List<ProductDetails> list) {
        int stt = 1;
        tbProductDetail.setRowCount(0);

        for (ProductDetails productDetail : list) {
            tbProductDetail.addRow(new Object[]{
                productDetail.getId(),
                productDetail.getImagePath(),
                productDetail.getProduct().getProductName(),
                stt,
                new ModelImage(productDetail.getImageName(), productDetail.getProductDetailName()),
                productDetail.getProductDetailCode(),
                productDetail.getBarCode(),
                productDetail.getTypePet().getTypePetName(),
                productDetail.getFlavor(),
                productDetail.getQuantityInStock(),
                Ultil.formatWeight(productDetail.getWeight()),
                productDetail.getFormattedProductionDate(),
                productDetail.getExpirydate() + " Tháng",
                Ultil.formatCurrency(productDetail.getPrice()),
                productDetail.getDescription(),
                productDetail.isStatus() ? "Còn hàng" : "Hết hàng",
                new ModelAction<>(productDetail, new EventAction<ProductDetails>() {
                    @Override
                    public void delete(ProductDetails p) {
                        showMessageConfirm("Xác nhận xóa sản phẩm?", () -> {
                            deleteProductD(p);
                        });
                    }

                    @Override
                    public void update(ProductDetails p) {

                    }

                    @Override
                    public void add(ProductDetails model) {
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

        btnUpdateStatusP.setText("");
        btnUpdateStatusProductDetail.setText("");

        tbProductDetail.clearSelection();
//        init();
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
        String weightStr = tbProductDetail.getValueAt(selectedRow, 10).toString().trim(); // Cân nặng
        String productionDate = tbProductDetail.getValueAt(selectedRow, 11).toString(); // Ngày sản xuất
        String expiryDate = tbProductDetail.getValueAt(selectedRow, 12).toString().replace("Tháng", "").trim(); // Hạn sử dụng
        String price = tbProductDetail.getValueAt(selectedRow, 13).toString().replace("₫", "").replace(".", "").replace("\u00A0", "").replaceAll("\\s+", "").trim(); // Giá sản phẩm
        String description = tbProductDetail.getValueAt(selectedRow, 14).toString(); // Mô tả sản phẩm
        String imagePath = tbProductDetail.getValueAt(selectedRow, 1).toString(); // Đường dẫn ảnh
        boolean status = tbProductDetail.getValueAt(selectedRow, 15).equals("Hết hàng");

        if (status) {
            btnUpdateStatusProductDetail.setText("Tiếp tục bán");
        } else {
            btnUpdateStatusProductDetail.setText("Ngưng bán");
        }

        // Chuyển đổi weight về BigDecimal
        BigDecimal weight = Ultil.convertWeightToKg(weightStr);

        // Set dữ liệu vào các ô nhập
        txtProductDetailName.setText(productDetailName);
        txtFlavor.setText(flavor);
        txtQuantityInStock.setText(quantityInStock);
        txtWeightProductDetail.setText(weight.toString()); // Hiển thị weight dưới dạng số thực (kg)
        txtProductDate.setText(productionDate);
        txtExpiry.setText(expiryDate);
        txtPriceProductDetail.setText(price);
        txtDescribe.setText(description);

        // Hiển thị ảnh sản phẩm
        pic.putClientProperty("imagePath", imagePath);
        pic.setImage(imagePath);

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

    public ProductDetails readFormProductDetail() {
        String nameProductDetail = txtProductDetailName.getText().trim();
        String productDetailCode = "SPCT" + Ultil.generateRandomCode().trim();
        Products p = (Products) cbbProduct.getSelectedItem();
        TypePets typePet = (TypePets) cbbTypePet.getSelectedItem();

        // Tạo barcode tự động
        String barCode = Ultil.generateEAN13Barcode();
        Ultil.generateBarcodeImage(barCode); // gen img barcode

        // Chuyển đổi weight từ String sang BigDecimal (kiểm tra tránh lỗi)
        BigDecimal weight = BigDecimal.ZERO;
        try {
            String weightStr = txtWeightProductDetail.getText().trim();
            if (weightStr != null && !weightStr.isEmpty()) {
                weightStr = weightStr.replaceAll("[^0-9.]", ""); // Xóa ký tự không hợp lệ
                weight = new BigDecimal(weightStr);

                // Nếu người dùng nhập trên 100 thì hiểu là kg → đổi sang gram
                if (weight.compareTo(BigDecimal.valueOf(100)) > 0) {
                    weight = weight.multiply(BigDecimal.valueOf(1000)); // Chuyển thành gram
                }
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
            imagePath = "default.jpg";
        }

        boolean statusP = amount > 0;

        // Trả về đối tượng ProductDetail
        return new ProductDetails(productDetailCode, nameProductDetail, barCode, p, typePet, expirydate, sqlProductDate, weight, amount, flavor, description, price, imagePath, statusP);
    }

    public ProductDetails readFormProductDetailForUpdate() {
        String nameProductDetail = txtProductDetailName.getText().trim();
        Products p = (Products) cbbProduct.getSelectedItem();
        TypePets typePet = (TypePets) cbbTypePet.getSelectedItem();

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

        // Chuyển đổi weight từ String sang BigDecimal (kiểm tra tránh lỗi)
        BigDecimal weight = BigDecimal.ZERO;
        try {
            String weightStr = txtWeightProductDetail.getText().trim();
            if (weightStr != null && !weightStr.isEmpty()) {
                weightStr = weightStr.replaceAll("[^0-9.]", ""); // Xóa ký tự không hợp lệ
                weight = new BigDecimal(weightStr);

                // Nếu người dùng nhập trên 100 thì hiểu là kg → đổi sang gram
                if (weight.compareTo(BigDecimal.valueOf(100)) > 0) {
                    weight = weight.multiply(BigDecimal.valueOf(1000)); // Chuyển thành gram
                }
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
        return new ProductDetails(nameProductDetail, p, typePet, expirydate, productDate, weight, amount, flavor, description, price, imagePath, statusP);
    }

    public boolean checkProductD() {
        try {
            // Kiểm tra các trường không được để trống
            if (checkEmpty(txtProductDetailName, "Tên sản phẩm")
                    || checkEmpty(txtPriceProductDetail, "Giá sản phẩm")
                    || checkEmpty(txtWeightProductDetail, "Trọng lượng")
                    || checkEmpty(txtFlavor, "Hương vị")
                    || checkEmpty(txtQuantityInStock, "Số lượng tồn")
                    || checkEmptyTextArea(txtDescribe, "Mô tả")
                    || checkEmpty(txtExpiry, "Hạn sử dụng")
                    || checkEmpty(txtProductDate, "Ngày sản xuất")) {
                return false;
            }

            // Kiểm tra giá sản phẩm (>= 0 và <= 1 tỷ)
            double price = checkNumber(txtPriceProductDetail, "Giá sản phẩm", 0, 1_000_000_000, true);

            // Kiểm tra số lượng sản phẩm (>= 0)
            int quantity = (int) checkNumber(txtQuantityInStock, "Số lượng tồn", 0, Integer.MAX_VALUE, false);

            // Kiểm tra trọng lượng (>= 0 và <= 100kg)
            double weight = checkNumber(txtWeightProductDetail, "Trọng lượng", 0, 10000, true);

            // Kiểm tra hạn sử dụng (>= 0)
            int expiry = (int) checkNumber(txtExpiry, "Hạn sử dụng", 0, Integer.MAX_VALUE, false);

            // Kiểm tra hương vị không chứa số
            if (!txtFlavor.getText().matches("^[^0-9]*$")) {
                showMessageFail("Hương vị không được chứa số!");
                return false;
            }

            // Kiểm tra ngày sản xuất không lớn hơn ngày hiện tại
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate productDate = LocalDate.parse(txtProductDate.getText().trim(), formatter);
            if (productDate.isAfter(LocalDate.now())) {
                showMessageFail("Ngày sản xuất không được lớn hơn ngày hiện tại!");
                return false;
            }

            // Kiểm tra combobox
            if (cbbProduct.getSelectedIndex() == -1 || cbbTypePet.getSelectedIndex() == -1) {
                showMessageFail("Vui lòng chọn loại sản phẩm và loại thú cưng!");
                return false;
            }

        } catch (NumberFormatException e) {
            showMessageFail("Giá trị nhập vào không hợp lệ!");
            return false;
        } catch (DateTimeParseException e) {
            showMessageFail("Định dạng ngày không hợp lệ! (dd/MM/yyyy)");
            return false;
        }

        return true;
    }

    private boolean checkEmpty(JTextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            showMessageFail(fieldName + " không được để trống!");
            return true;
        }
        return false;
    }

    private boolean checkEmptyTextArea(com.petshop.swing.textarea.TextArea field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            showMessageFail(fieldName + " không được để trống!");
            return true;
        }
        return false;
    }

    private double checkNumber(JTextField field, String fieldName, double min, double max, boolean isDouble) throws NumberFormatException {
        String text = field.getText().replaceAll("[^\\d.]", "").trim(); // Loại bỏ ký tự không mong muốn
        double value = isDouble ? Double.parseDouble(text) : Integer.parseInt(text);

        if (value < min || value > max) {
            showMessageFail(fieldName + " phải từ " + min + " đến " + max + "!");
            throw new NumberFormatException();
        }
        return value;
    }

    public void insertProductD() {
        if (!checkProductD()) {
            return;
        }
        if (productDetailDao.addProductDetail(readFormProductDetail())) {
            showMessageSuccess("Thêm thành công!");
            getListProductDetail(productDetailDao.getListProductDetail());
            this.resetFormProductD();
        } else {
            showMessageFail("Thêm thất bại!");
        }

    }

    public void deleteProductD(ProductDetails p) {
        if (rememberMeService.getEmployeeId() != 1) {
            showMessageFail("Bạn không có quyền xóa!!!");
            return;
        }
        try {
            int selectRow = tbProductDetail.getSelectedRow();
            if (selectRow != -1) {
                int id = p.getId();
                productDetailDao.deleteProductDetail(id);
                this.showMessageSuccess("Xóa thành công!!");
                getListProductDetail(productDetailDao.getListProductDetail());
                resetFormProductD();
            } else {
                this.showMessageFail("Vui lòng chọn thông tin để xóa!");
            }
        } catch (Exception e) {
            showMessageError("Có lỗi sảy ra!!");
            e.printStackTrace();
        }
    }

    public void restoreProductDetail(ProductDetails p) {
        productDetailDao.restoreProductDetail(p.getId());
        getListProductDetail(productDetailDao.getListProductDetailDeleted());
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
                getListProductDetail(productDetailDao.getListProductDetail());
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
            getListProductDetail(productDetailDao.getListProductDetail());
            showMessageSuccess("Thay đổi trạng thái thành công!");
        } else {
            showMessageFail("Vui lòng chọn sản phẩm");
        }
    }

    public void searchProductD(String keyword) {
        List<ProductDetails> productsByNameOrFlavor = productDetailDao.searchByNameOrFlavor(keyword);
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
        jPanel7 = new javax.swing.JPanel();
        btnPopupCProduct = new com.petshop.swing.ButtonBadges();
        txtProductName = new com.petshop.swing.textfield.TextFieldRounded();
        txtPriceProduct = new com.petshop.swing.textfield.TextFieldRounded();
        cbbCategoryProduct = new com.petshop.swing.combobox.Combobox();
        txtProductCode = new com.petshop.swing.textfield.TextField();
        jPanel6 = new javax.swing.JPanel();
        btnAddProduct = new com.petshop.swing.Button();
        btnRestProduct = new com.petshop.swing.Button();
        btnHistoryProductDeleted = new com.petshop.swing.Button();
        btnUpdateProduct = new com.petshop.swing.Button();
        btnUpdateStatusP = new com.petshop.swing.Button();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProduct = new com.petshop.swing.table.Table();
        jLabel2 = new javax.swing.JLabel();
        txtSearchProduct = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbFilterCategory = new com.petshop.swing.combobox.Combobox();
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

        setBackground(new java.awt.Color(245, 245, 245));
        setMaximumSize(new java.awt.Dimension(1058, 741));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        materialTabbed1.setMaximumSize(new java.awt.Dimension(1060, 700));
        materialTabbed1.setPreferredSize(new java.awt.Dimension(1060, 700));

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));
        jPanel1.setMaximumSize(new java.awt.Dimension(1054, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(1054, 600));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÔNG TIN SẢN PHẨM");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        btnPopupCProduct.setBackground(new java.awt.Color(204, 255, 255));
        btnPopupCProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-add-24 (1).png"))); // NOI18N
        btnPopupCProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPopupCProductActionPerformed(evt);
            }
        });

        txtProductName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductName.setLabelText("Tên sản phẩm");

        txtPriceProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPriceProduct.setLabelText("Giá");

        cbbCategoryProduct.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cbbCategoryProduct.setLabeText("Loại sản phẩm");

        txtProductCode.setLabelText("Mã SP");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductName, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPriceProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(cbbCategoryProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPopupCProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPriceProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPopupCProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbCategoryProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAddProduct.setBackground(new java.awt.Color(204, 255, 255));
        btnAddProduct.setText("Thêm");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnRestProduct.setBackground(new java.awt.Color(204, 204, 204));
        btnRestProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-restore-20.png"))); // NOI18N
        btnRestProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestProductActionPerformed(evt);
            }
        });

        btnHistoryProductDeleted.setBackground(new java.awt.Color(204, 204, 255));
        btnHistoryProductDeleted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-activity-history-30.png"))); // NOI18N
        btnHistoryProductDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryProductDeletedActionPerformed(evt);
            }
        });

        btnUpdateProduct.setBackground(new java.awt.Color(255, 255, 204));
        btnUpdateProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-update-20.png"))); // NOI18N
        btnUpdateProduct.setText("Cập nhập");
        btnUpdateProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProductActionPerformed(evt);
            }
        });

        btnUpdateStatusP.setBackground(new java.awt.Color(255, 153, 153));
        btnUpdateStatusP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateStatusPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUpdateProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdateStatusP, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnHistoryProductDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRestProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRestProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHistoryProductDeleted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdateStatusP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnUpdateProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbbFilterCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbFilterCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        materialTabbed1.addTab("Sản phẩm", jPanel1);

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));
        jPanel2.setMaximumSize(new java.awt.Dimension(1055, 700));
        jPanel2.setPreferredSize(new java.awt.Dimension(1055, 600));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        txtProductDetailName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductDetailName.setLabelText("Tên sản phẩm chi tiết");

        txtProductDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductDate.setLabelText("NSX");
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
        txtWeightProductDetail.setLabelText("TL (KG hoặc Gram)");

        txtExpiry.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtExpiry.setLabelText("HSD(tháng)");
        txtExpiry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExpiryActionPerformed(evt);
            }
        });

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
                    .addComponent(cbbTypePet, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(cbbProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPriceProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(btnPopupTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtWeightProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(txtFlavor, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQuantityInStock, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProductDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(txtProductDetailName, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                    .addComponent(cbbProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtWeightProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPriceProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtFlavor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtQuantityInStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(26, 26, 26)
                                        .addComponent(btnPopupTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(86, 86, 86)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtProductDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(cbbTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap())
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 32, Short.MAX_VALUE))))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("THÔNG TIN SẢN PHẨM CHI TIẾT");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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

        btnRestProductDetail.setBackground(new java.awt.Color(204, 204, 255));
        btnRestProductDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-restore-20.png"))); // NOI18N
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
        btnHisProductDetailDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHisProductDetailDeletedActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHisProductDetailDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdateProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateStatusProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRestProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

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
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true
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
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                        .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGap(0, 0, 0))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
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

    private void txtProductDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductDateActionPerformed

    private void txtSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchProductActionPerformed

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

    private void btnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectImageActionPerformed
        Window window = SwingUtilities.getWindowAncestor(this);
        JnaFileChooser jnaCh = new JnaFileChooser();

        URL resourceUrl = getClass().getClassLoader().getResource("com/images");
        if (resourceUrl != null) {
            File resourceFolder = new File(resourceUrl.getFile());
            jnaCh.setCurrentDirectory(resourceFolder.toString()); // Đặt thư mục mặc định
        }

        boolean save = jnaCh.showOpenDialog(window);
        if (save) {
            File selectedFile = jnaCh.getSelectedFile();
            if (selectedFile != null) {
                String fileName = selectedFile.getName(); // Chỉ lấy tên file ảnh

                System.out.println("Ảnh đã chọn: " + fileName);
                pic.putClientProperty("imagePath", fileName);
                pic.setImage(fileName);// Lưu tên ảnh, không lưu cả đường dẫn
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

    private void txtExpiryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExpiryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExpiryActionPerformed

    private void btnHisProductDetailDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHisProductDetailDeletedActionPerformed
        // TODO add your handling code here:
        showPopupHistoryDeletedProductD();
    }//GEN-LAST:event_btnHisProductDetailDeletedActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        showMessageConfirm("Xác nhận thêm sản phẩm?", () -> {
            insertProduct();
        });
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void btnRestProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestProductActionPerformed
        // TODO add your handling code here:
        resetFormProduct();
    }//GEN-LAST:event_btnRestProductActionPerformed

    private void btnHistoryProductDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryProductDeletedActionPerformed
        // TODO add your handling code here:
        showPopUpHistoryDeletedProducts();
    }//GEN-LAST:event_btnHistoryProductDeletedActionPerformed

    private void btnUpdateProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProductActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận cập nhập lại sản phẩm?", () -> {
            updateProduct();
        });
    }//GEN-LAST:event_btnUpdateProductActionPerformed

    private void btnUpdateStatusPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateStatusPActionPerformed
        // TODO add your handling code here:
        updateStatusProduct();
    }//GEN-LAST:event_btnUpdateStatusPActionPerformed

    private void btnPopupCProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopupCProductActionPerformed
        // TODO add your handling code here:
        showPopupCategoryProduct();
    }//GEN-LAST:event_btnPopupCProductActionPerformed


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
    private com.petshop.swing.Button btnUpdateStatusP;
    private com.petshop.swing.Button btnUpdateStatusProductDetail;
    private com.petshop.swing.Button button9;
    private com.petshop.swing.combobox.Combobox cbbCategoryProduct;
    private com.petshop.swing.combobox.Combobox cbbFilterCategory;
    private com.petshop.swing.combobox.Combobox cbbFilterProduct;
    private com.petshop.swing.combobox.Combobox cbbFilterTypePet;
    private com.petshop.swing.combobox.Combobox cbbProduct;
    private com.petshop.swing.combobox.Combobox cbbSort;
    private com.petshop.swing.combobox.Combobox cbbTypePet;
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
    private com.petshop.swing.textfield.TextField txtProductCode;
    private com.petshop.swing.textfield.TextFieldRounded txtProductDate;
    private com.petshop.swing.textfield.TextFieldRounded txtProductDetailName;
    private com.petshop.swing.textfield.TextFieldRounded txtProductName;
    private com.petshop.swing.textfield.TextFieldRounded txtQuantityInStock;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProduct;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProductDetail;
    private com.petshop.swing.textfield.TextFieldRounded txtWeightProductDetail;
    // End of variables declaration//GEN-END:variables
}
