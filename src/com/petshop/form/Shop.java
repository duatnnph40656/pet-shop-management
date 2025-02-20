/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.component.Header;
import com.petshop.daos.CustomerDAO;
import com.petshop.daos.EmployeeDAO;
import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.daos.PetDAO;
import com.petshop.daos.PetServiceDAO;
import com.petshop.daos.ProductDAO;
import com.petshop.daos.ProductDetailDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.main.Main;
import com.petshop.models.Customers;
import com.petshop.models.Employees;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
import com.petshop.models.ProductDetails;
import com.petshop.models.Products;
import com.petshop.models.TypePets;
import com.petshop.popup.PopupCustomer;
import com.petshop.popup.PopupScanBarCode;
import com.petshop.popup.PopupService;
import com.petshop.services.RememberMeService;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.swing.table.ModelImage;
import com.petshop.ultils.Ultil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class Shop extends javax.swing.JPanel {

    /**
     * Creates new form Shop
     */
    private final ProductDetailDAO productDetailDAO;
    private final ProductDAO productDAO;
    private final TypePetDAO typePetDAO;
    private final InvoiceDAO invoiceDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private final PetServiceDAO petServiceDAO;
    private final PetDAO petDAO;
    private RememberMeService rememberMeService;

    public Shop() {
        initComponents();
        tbInvoice.fixTable(jScrollPane1);
        tbInvoiceDetail.fixTable(jScrollPane2);
        tbProductDetail.fixTable(jScrollPane3);
        tbService.fixTable(jScrollPane4);
        productDetailDAO = new ProductDetailDAO();
        productDAO = new ProductDAO();
        invoiceDAO = new InvoiceDAO();
        invoiceDetailDAO = new InvoiceDetailDAO();
        customerDAO = new CustomerDAO();
        employeeDAO = new EmployeeDAO();
        petServiceDAO = new PetServiceDAO();
        petDAO = new PetDAO();
        typePetDAO = new TypePetDAO();
        rememberMeService = new RememberMeService();
        loadCbbPaymentMethod();
        loadCbbSort();
        init();
    }

    public void init() {
        getListProductDetail(productDetailDAO.getListProductDetail());
        getListService(petServiceDAO.getListService());
        getListInvoice(invoiceDAO.getListInvoice());
        loadCbbFilterTypePet(typePetDAO.getListTypePet());
        loadCbbFilterProduct(productDAO.getListProduct());
        resetForm();
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

    public void showInputDialog(int amount, Consumer<Integer> onConfirmAction) {
        DialogInput dialog = new DialogInput(amount);
        dialog.setConfirmListener(new ConfirmListenerInput() {
            @Override
            public void onConfirm(int inputAmount) {
                if (onConfirmAction != null) {
                    onConfirmAction.accept(inputAmount); // Truyền số lượng nhập vào
                }
            }

            @Override
            public void onCancel() {
                // Đóng popup nếu người dùng hủy
                GlassPanePopup.closePopupLast();
            }
        });
        GlassPanePopup.showPopup(dialog, "pInput");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{Popup...">
    private void showPopupService(PetServices p) {
        if (getSelectedRowInvoice() == -1) {
            showMessageFail("Vui lòng chọn hóa đơn!!");
            return;
        }
        PopupService poup = new PopupService();
        poup.setServiceCode(p.getServiceCode());
        poup.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                addServiceToInvoiceD(p, 1, poup.getPetCode());
            }

            @Override
            public void onCancel() {

            }
        });
        GlassPanePopup.showPopup(poup, "pPet");
    }

    public void showPopupWebcam() {
        PopupScanBarCode pWebCam = new PopupScanBarCode();

        pWebCam.setBarcodeListener((String barcode) -> {
            ProductDetails p = productDetailDAO.searchByBarCodeResultModel(barcode);
            if (p != null) {
                insertPToInvoiceD(p, 1); // Hiển thị thông tin sản phẩm lên giao diện
                showMessageSuccess("Đã thêm sản phẩm");
                GlassPanePopup.closePopup("pWebCam");
            } else {
                showMessageFail("Không tìm thấy sản phẩm có mã vạch: " + barcode);
                GlassPanePopup.closePopup("pWebCam");
            }
        });

        GlassPanePopup.showPopup(pWebCam, "pWebCam");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{CBB...">
    public void loadCbbPaymentMethod() {
        cbbPayment.removeAllItems();
        cbbPayment.addItem("Tiền mặt");
        cbbPayment.addItem("Thanh toán qua banking");
        cbbPayment.setSelectedIndex(-1);
    }

    private void loadCbbFilterTypePet(List<TypePets> list) {
        cbbFilterTypePet.removeAllItems();

        if (list == null || list.isEmpty()) {
            return; // Nếu danh sách null hoặc rỗng, thoát khỏi phương thức
        }

        for (TypePets t : list) {
            cbbFilterTypePet.addItem(t);
        }
        cbbFilterTypePet.setSelectedIndex(-1);

        // Thêm sự kiện cho JComboBox
        cbbFilterTypePet.addActionListener(e -> filterAndSortProductDetails());
    }

    private void loadCbbFilterProduct(List<Products> productList) {
        cbbFilterProduct.removeAllItems();

        if (productList == null || productList.isEmpty()) {
            return; // Nếu danh sách null hoặc rỗng, thoát khỏi phương thức
        }

        for (Products p : productList) {
            cbbFilterProduct.addItem(p);
        }
        cbbFilterProduct.setSelectedIndex(-1);

        // Thêm sự kiện cho JComboBox
        cbbFilterProduct.addActionListener(e -> filterAndSortProductDetails());
    }

    private void loadCbbSort() {
        cbbSort.removeAllItems();
        cbbSort.addItem("Theo giá tăng dần");
        cbbSort.addItem("Theo giá giảm dần");
        cbbSort.setSelectedIndex(-1); // Không chọn mục nào mặc định

        // Thêm sự kiện cho JComboBox
        cbbSort.addActionListener(e -> filterAndSortProductDetails());
    }

    private void filterAndSortProductDetails() {
        // Lấy giá trị được chọn từ các JComboBox
        Products selectedProduct = (Products) cbbFilterProduct.getSelectedItem();
        TypePets selectedTypePet = (TypePets) cbbFilterTypePet.getSelectedItem();
        String selectedSort = (String) cbbSort.getSelectedItem();

        // Xác định các tham số lọc
        Integer productId = (selectedProduct != null) ? selectedProduct.getId() : null;
        Integer typePetId = (selectedTypePet != null) ? selectedTypePet.getId() : null;

        // Lấy danh sách sản phẩm chi tiết dựa trên bộ lọc
        List<ProductDetails> filteredList;
        if (productId == null && typePetId == null) {
            filteredList = productDetailDAO.getListProductDetail(); // Lấy toàn bộ danh sách nếu không có bộ lọc
        } else {
            filteredList = productDetailDAO.searchProductDetails(productId, typePetId); // Lọc theo sản phẩm và loại thú cưng
        }

        // Sắp xếp danh sách nếu có yêu cầu sắp xếp
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Theo giá tăng dần":
                    filteredList.sort(Comparator.comparing(ProductDetails::getPrice)); // Sắp xếp tăng dần theo giá
                    break;
                case "Theo giá giảm dần":
                    filteredList.sort(Comparator.comparing(ProductDetails::getPrice).reversed()); // Sắp xếp giảm dần theo giá
                    break;
                default:
                    break;
            }
        }

        // Hiển thị danh sách đã lọc và sắp xếp
        getListProductDetail(filteredList);
    }

//    private void filterProductDetails() {
//        TypePets selectedTypePet = (TypePets) cbbFilterTypePet.getSelectedItem();
//        Products selectedProduct = (Products) cbbFilterProduct.getSelectedItem();
//
//        Integer typePetId = (selectedTypePet != null) ? selectedTypePet.getId() : null;
//        Integer productId = (selectedProduct != null) ? selectedProduct.getId() : null;
//
//        // Nếu cả hai đều null, lấy toàn bộ danh sách
//        List<ProductDetails> filteredList;
//        if (typePetId == null && productId == null) {
//            filteredList = productDetailDAO.getListProductDetail();
//        } else {
//            filteredList = productDetailDAO.searchProductDetails(productId, typePetId);
//        }
//
//        getListProductDetail(filteredList); // Cập nhật giao diện với danh sách lọc
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="{ProductDetail...">
    public void getListProductDetail(List<ProductDetails> list) {
        int stt = 1;
        tbProductDetail.setRowCount(0);
        for (ProductDetails p : list) {
            if (p.isStatus()) {
                tbProductDetail.addRow(new Object[]{
                    p.getId(),
                    stt,
                    new ModelImage(p.getImageName(), p.getProductDetailName()),
                    p.getProductDetailCode(),
                    p.getBarCode(),
                    p.getTypePet().getTypePetName(),
                    p.getFlavor(),
                    p.getQuantityInStock(),
                    p.getWeight() + "KG",
                    p.getFormattedProductionDate(),
                    p.getExpirydate() + " Tháng",
                    Ultil.formatCurrency(p.getPrice()),
                    p.isStatus() ? "Còn hàng" : "Hết hàng",
                    new ModelAction<>(p, new EventAction<ProductDetails>() {
                        @Override
                        public void delete(ProductDetails p) {

                        }

                        @Override
                        public void update(ProductDetails p) {

                        }

                        @Override
                        public void add(ProductDetails model) {
                            showInputDialog(1, inputAmount -> { // Gọi form nhập số lượng, mặc định là 1
                                insertPToInvoiceD(model, inputAmount); // Thêm sản phẩm với số lượng nhập vào
                            });
                        }
                    })

                });
                stt++;
            }

        }
    }

    private int getSelectedProductD() {
        return tbInvoiceDetail.getSelectedRow();
    }

    private Integer getIdProductD() {
        int selectedRow = getSelectedProductD();
        if (selectedRow == -1) {
            return null; // Không có dòng nào được chọn
        }
        return (Integer) tbInvoiceDetail.getValueAt(selectedRow, 1);
    }

    private void searchByBarcode(String barcode) {
        ProductDetails p = productDetailDAO.searchByBarCodeResultModel(barcode);
        insertPToInvoiceD(p, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{Invoice...">
    public void getListInvoice(List<Invoices> list) {
        int stt = 1;
        tbInvoice.setRowCount(0);
        for (Invoices i : list) {
            tbInvoice.addRow(new Object[]{
                i.getId(),
                i.getCustomer().getCustomerCode(),
                stt,
                i.getInvoiceCode(),
                i.getCustomer().getCustomerName(),
                i.getEmployee().getEmployeeName(),
                Ultil.formatCurrency(i.getTotalPrice()),
                i.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán",});
            stt++;
        }
    }

    public int getSelectedRowInvoice() {
        return tbInvoice.getSelectedRow();
    }

    public Integer getIdSelectedInvoice() {
        int selectedRow = getSelectedRowInvoice();
        if (selectedRow == -1) {
            return null; // Không có dòng nào được chọn
        }
        return (Integer) tbInvoice.getValueAt(selectedRow, 0); // Cột 0 chứa ID hóa đơn
    }

    public void showDataInvoice() {
        lbInvoiceCode.setText((String) tbInvoice.getValueAt(getSelectedRowInvoice(), 3));
        lbCustomerName.setText((String) tbInvoice.getValueAt(getSelectedRowInvoice(), 4));
        lbTotalPrice.setText((String) tbInvoice.getValueAt(getSelectedRowInvoice(), 6));
        lbTotalPrice1.setText((String) tbInvoice.getValueAt(getSelectedRowInvoice(), 6));

    }

    public void resetForm() {
        Customers c = customerDAO.searchCustomerById(1);
        lbCustomerName.setText(c.getCustomerName());
        lbCustomerCode.setText(c.getCustomerCode());

        lbTotalPrice.setText("₫");
        lbTotalPrice1.setText("₫");
        checkBoxPaymStatus.setSelected(false);
        cbbPayment.setSelectedIndex(0);

        tbInvoice.clearSelection();
        clearInvoiceDetailTable();

        cbbFilterProduct.setSelectedIndex(-1);
        cbbFilterTypePet.setSelectedIndex(-1);
        cbbSort.setSelectedIndex(-1);
    }

    public Invoices readFormInsert() {
        Invoices i = new Invoices();

        String invoiceCode = "HD" + Ultil.generateRandomCode();
//        Ultil.generateInvoice(invoiceCode, invoiceCode, new ArrayList<>(), invoiceCode);

        Customers c = customerDAO.searchCustomerByCustomerCode(lbCustomerCode.getText());
        if (c == null) {
            showMessageFail("Có lỗi sảy ra");
        }
        // Lấy username của nhân viên đang đăng nhập
        int idEmployee = rememberMeService.getEmployeeId();

        Employees e = employeeDAO.findEmployeeById(idEmployee);

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal costsIncurred = BigDecimal.ZERO;
//        boolean paymentMethod = "Tiền mặt".equals(cbbPayment.getSelectedItem());
//        boolean paymentStatus = checkBoxPaymStatus.isSelected();
        String note = "";
//        boolean deleted = false;
        boolean status = true; // Mặc định hóa đơn hoạt động

        i.setInvoiceCode(invoiceCode);
        i.setTotalPrice(totalPrice);
        i.setCostsIncurred(costsIncurred);
//        i.setNote(note);
        i.setCustomer(c);
        i.setEmployee(e);
        i.setStatus(status);
        return i;
    }

    public Invoices readFormUpdate() {
        Invoices i = new Invoices();
        boolean paymentMethod = cbbPayment.getSelectedItem().equals("Tiền mặt");
        i.setPaymentMethod(paymentMethod);
        i.setPaymentStatus(checkBoxPaymStatus.isSelected());
        i.setNote(null);
        i.setCostsIncurred(new BigDecimal(0));
        return i;
    }

    public void insertInvoice() {
        // Kiểm tra xem khách hàng có hóa đơn chờ hay không
        Customers c = customerDAO.searchCustomerByCustomerCode(lbCustomerCode.getText());
        if (c == null) {
            showMessageFail("Có lỗi sảy ra");
        }

        if (invoiceDAO.hasPendingInvoice(c.getId())) {
            showMessageFail("Khách hàng này đã có hóa đơn chờ!");
            return;
        }

        // Nếu không có hóa đơn chờ, tiếp tục tạo hóa đơn mới
        Invoices invoice = readFormInsert();
        if (invoice == null) {
            showMessageFail("Tạo hóa đơn thất bại!!");
            return;
        }

        invoiceDAO.createPendingInvoice(invoice);
        getListInvoice(invoiceDAO.getListInvoice());
        showMessageSuccess("Tạo hóa đơn thành công");
    }

    public void deleteInvoice() {
        int id = getIdSelectedInvoice();
        invoiceDAO.updateDeletedStatus(id, true);
        getListInvoice(invoiceDAO.getListInvoice());
        clearInvoiceDetailTable();
        resetForm();
    }

    public void updateToTalPriceInvoice(int id) {
        int selectedRow = getSelectedRowInvoice(); // Lưu dòng đang chọn
        Integer selectedId = null;
        BigDecimal totalPrice = null;

        if (selectedRow >= 0) {
            selectedId = getIdSelectedInvoice(); // Lưu ID hóa đơn
            Object priceObj = tbInvoice.getValueAt(selectedRow, 6); // Lấy giá trị từ cột 5 (total price)

            if (priceObj instanceof BigDecimal) {
                totalPrice = (BigDecimal) priceObj;
            } else if (priceObj instanceof Number) {
                totalPrice = BigDecimal.valueOf(((Number) priceObj).doubleValue());
            } else if (priceObj instanceof String) {
                try {
                    // Xóa mọi ký tự không phải số
                    String priceStr = ((String) priceObj).replaceAll("[^\\d]", "").trim();
                    totalPrice = new BigDecimal(priceStr);
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi chuyển đổi totalPrice: " + priceObj);
                }
            } else {
                System.out.println("Giá trị totalPrice không hợp lệ: " + priceObj);
            }
        }

        // Cập nhật giá trị total price trong DB
        invoiceDAO.updateTotalPrice(id);

        // Set lb total price
        // Cập nhật lại danh sách hóa đơn trên bảng
        getListInvoice(invoiceDAO.getListInvoice());

        Object priceObj = tbInvoice.getValueAt(selectedRow, 5);

        lbTotalPrice.setText(priceObj.toString());
        lbTotalPrice1.setText(priceObj.toString());

        if (selectedId != null) {
            for (int i = 0; i < tbInvoice.getRowCount(); i++) {
                if (tbInvoice.getValueAt(i, 0).equals(selectedId)) {
                    tbInvoice.setRowSelectionInterval(i, i);
                    tbInvoice.scrollRectToVisible(tbInvoice.getCellRect(i, 0, true));
                    return;
                }
            }
        }

        if (tbInvoice.getRowCount() > 0) {
            tbInvoice.setRowSelectionInterval(0, 0);
            tbInvoice.scrollRectToVisible(tbInvoice.getCellRect(0, 0, true));
        }
    }

    public void updateInvoice() {
        if (getSelectedRowInvoice() == -1) {
            showMessageFail("Vui lòng chọn hóa đơn");
            return;
        }
        if (!invoiceDAO.isValidInvoiceTotal(getIdSelectedInvoice())) {
            showMessageFail("Không thể hoàn thành hóa đơn 0đ");
            return;
        }
        if (invoiceDAO.updateInvoice(getIdSelectedInvoice(), readFormUpdate())) {
            List<InvoiceDetails> list = invoiceDetailDAO.getInvoiceDetails(getIdSelectedInvoice());
            updateRemoveQuantityInProductDetail(list);
            showMessageSuccess("Thành công");
            getListInvoice(invoiceDAO.getListInvoice());
            clearInvoiceDetailTable();
        } else {
            showMessageFail("Thất bại!!");
        }
    }

    private void updateRemoveQuantityInProductDetail(List<InvoiceDetails> list) {
        productDetailDAO.deductStockFromInvoice(list);
        getListProductDetail(productDetailDAO.getListProductDetail());
    }

    private void printInvoice(String invoiceId, String employeeName, String customerName, String totalAmount) {
        List<InvoiceDetails> list = invoiceDetailDAO.getInvoiceDetailsByInvoiceId(getIdSelectedInvoice());
        List<String[]> items = new ArrayList<>();

        for (InvoiceDetails i : list) {
            String itemName = (i.getProductDetail() != null && i.getProductDetail().getProductDetailName() != null)
                    ? i.getProductDetail().getProductDetailName()
                    : (i.getPetService() != null ? i.getPetService().getServiceName() : "N/A");

            String price = (i.getProductDetail() != null && i.getProductDetail().getPrice() != null)
                    ? i.getProductDetail().getPrice().toString()
                    : (i.getPetService() != null ? i.getPetService().getPriceService().toString() : "0");

            String quantity = String.valueOf(i.getUsageOrQuantity());
            String total = String.valueOf(i.getTotalPrice());

            items.add(new String[]{itemName, quantity, price, total});
        }

        Ultil.generateInvoice(invoiceId, employeeName, customerName, employeeName, items, totalAmount);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="{InvoiceDetail...">
    public void getListInvoiceDetail(List<InvoiceDetails> list) {
        int stt = 1;
        tbInvoiceDetail.setRowCount(0);
        for (InvoiceDetails i : list) {
            String name = (i.getProductDetail() != null && i.getProductDetail().getProductDetailName() != null)
                    ? i.getProductDetail().getProductDetailName()
                    : (i.getPetService() != null ? i.getPetService().getServiceName() : "N/A");

            String code = (i.getProductDetail() != null && i.getProductDetail().getProductDetailCode() != null)
                    ? i.getProductDetail().getProductDetailCode()
                    : (i.getPetService() != null ? i.getPetService().getServiceCode() : "N/A");
            String petName = (i.getPet() != null) ? i.getPet().getPetName() : "N/A";

            BigDecimal price = (i.getProductDetail() != null && i.getProductDetail().getPrice() != null)
                    ? i.getProductDetail().getPrice()
                    : (i.getPetService() != null && i.getPetService().getPriceService() != null)
                    ? i.getPetService().getPriceService()
                    : BigDecimal.ZERO; // Nếu không có giá thì để là 0

            tbInvoiceDetail.addRow(new Object[]{
                i.getId(),
                i.getProductDetail().getId(),
                stt,
                i.getInvoiceDetailCode(),
                code,
                name,
                i.getUsageOrQuantity(),
                Ultil.formatCurrency(price),
                Ultil.formatCurrency(i.getTotalPrice()),
                petName == null ? "N/A" : petName,
                new ModelAction<>(i, new EventAction<InvoiceDetails>() {
                    @Override
                    public void delete(InvoiceDetails i) {
                        showMessageConfirm("Xác nhận xóa?", () -> {
                            deleleInvoiceDetail(i);
                        });
                    }

                    @Override
                    public void update(InvoiceDetails i) {
                        showInputDialog(1, inputAmount -> {
                            updateQuantityInvoiceDetail(i, inputAmount);
                        });
                    }

                    @Override
                    public void add(InvoiceDetails i) {

                    }
                })
            });
            stt++;
        }
    }

    private int getSelectedInvoiceD() {
        return tbInvoiceDetail.getSelectedRow();
    }

    private Integer getIdInvoiceD() {
        int selectedRow = getSelectedInvoiceD();
        if (selectedRow == -1) {
            return null; // Không có dòng nào được chọn
        }
        return (Integer) tbInvoiceDetail.getValueAt(selectedRow, 0);
    }

    public void updateQuantityInvoiceDetail(InvoiceDetails detail, int amount) {
        if (amount < 1) {
            showMessageFail("Số lượng không được nhỏ hơn 1");
            GlassPanePopup.closePopup("pInput");
            return;
        }

        if (!productDetailDAO.isEnoughStock(getIdProductD(), amount)) {
            showMessageFail("Số lượng nhập lớn hơn số lượng SP!!");
            return;
        }

        int id = detail.getId();
        BigDecimal totalPrice = BigDecimal.ZERO; // Đặt giá trị mặc định là 0
        if (detail.getProductDetail() != null && detail.getProductDetail().getPrice() != null) {
            totalPrice = detail.getProductDetail().getPrice().multiply(BigDecimal.valueOf(amount));
        } else if (detail.getPetService() != null && detail.getPetService().getPriceService() != null) {
            totalPrice = detail.getPetService().getPriceService().multiply(BigDecimal.valueOf(amount));
        }

        invoiceDetailDAO.updateUsageOrQuantityAndTprice(id, amount, totalPrice);

        this.updateToTalPriceInvoice(getIdSelectedInvoice());

        showInvoiceDetailByIdInvoice();
        GlassPanePopup.closePopup("pInput");
    }

    public void deleleInvoiceDetail(InvoiceDetails i) {
        // Xóa chi tiết hóa đơn trước
        invoiceDetailDAO.deleteInvoiceDetail(i.getId());

        // Lưu dòng đang chọn
        Integer selectedId = null;

        if (getSelectedInvoiceD() >= 0) {
            selectedId = getIdSelectedInvoice(); // Lưu ID hóa đơn
        }

        // Sau đó cập nhật tổng tiền của hóa đơn chính xác
        this.updateToTalPriceInvoice(selectedId);

        // Hiển thị lại danh sách chi tiết hóa đơn
        showInvoiceDetailByIdInvoice();
    }

    public void showInvoiceDetailByIdInvoice() {
        if (getSelectedRowInvoice() == -1) {
            showMessageFail("Bạn chưa chọn hóa đơn!!");// Kiểm tra xem có hàng nào được chọn không
            return;
        }

        int id = getIdSelectedInvoice(); // Lấy ID hóa đơn

        List<InvoiceDetails> list = invoiceDetailDAO.getInvoiceDetailsByInvoiceId(id);
        getListInvoiceDetail(list); // Load dữ liệu lên bảng
    }

    public InvoiceDetails readDataInvoiceDetail(ProductDetails p) {
        InvoiceDetails detail = new InvoiceDetails();

        detail.setInvoiceDetailCode("HDCT" + Ultil.generateRandomCode()); // Tạo mã hóa đơn chi tiết tự động

        int id = getIdSelectedInvoice(); // Lấy ID hóa đơn
        Invoices invoice = invoiceDAO.getInvoiceById(id);

        detail.setInvoice(invoice);
        detail.setTotalPrice(p.getPrice()); // Lấy giá từ sản phẩm
        detail.setProductDetail(p);
        detail.setPetService(null); // Không phải dịch vụ
        detail.setPet(null); // Không phải thú cưng
        detail.setStatus(true); // Mặc định là true

        return detail;
    }

    public void insertPToInvoiceD(ProductDetails p, int inputAmount) {
        if (getSelectedRowInvoice() == -1) {
            showMessageFail("Vui lòng chọn hóa đơn!!");
            GlassPanePopup.closePopup("pInput");
            return;
        }

        if (!productDetailDAO.isEnoughStock(p.getId(), inputAmount)) {
            showMessageFail("Số lượng nhập lớn hơn số lượng trong kho!!");
            return;
        }

        int id = getIdSelectedInvoice(); // Lấy ID hóa đơn
        InvoiceDetails existingDetail = invoiceDetailDAO.getInvoiceDetailByProductDetailId(id, p.getId());

        boolean isUpdatedOrInserted = false;

        if (existingDetail != null) { // Nếu sản phẩm đã tồn tại trong hóa đơn
            int newQuantity = existingDetail.getUsageOrQuantity() + inputAmount;
            BigDecimal totalPrice = p.getPrice().multiply(BigDecimal.valueOf(newQuantity));

            // Cập nhật số lượng và tổng tiền trước
            isUpdatedOrInserted = invoiceDetailDAO.updateUsageOrQuantityAndTprice(existingDetail.getId(), newQuantity, totalPrice);
        } else { // Nếu chưa tồn tại, thêm mới
            InvoiceDetails detail = new InvoiceDetails();
            detail.setInvoiceDetailCode("HDCT" + Ultil.generateRandomCode());
            detail.setTotalPrice(p.getPrice().multiply(BigDecimal.valueOf(inputAmount)));

            Invoices i = new Invoices();
            i.setId(id);
            detail.setInvoice(i);
            detail.setProductDetail(p);
            detail.setUsageOrQuantity(inputAmount);

            // Chèn dữ liệu trước
            isUpdatedOrInserted = invoiceDetailDAO.insertInvoiceDetailProduct(detail);
        }

        if (isUpdatedOrInserted) {
            // Sau khi cập nhật hoặc thêm sản phẩm, mới cập nhật tổng tiền
            this.updateToTalPriceInvoice(id);
            showInvoiceDetailByIdInvoice();
            GlassPanePopup.closePopup("pInput");
        } else {
            System.out.println("Thêm sản phẩm thất bại!");
        }
    }

    public void clearInvoiceDetailTable() {
        tbInvoiceDetail.setRowCount(0); // Xóa toàn bộ dữ liệu trong bảng invoiceDetail
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{Customers...">
    public Customers readFormCustomer() {

        return new Customers();
    }

    public void insertCustomer() {

    }

    public void showPopupInsertCustomer() {
        PopupCustomer pC = new PopupCustomer();

        GlassPanePopup.showPopup(pC, "pCustomer");
    }

    public void searchCustomer(String keyword) {
        Customers t = customerDAO.searchCustomerByPhoneNumber(keyword);
        if (t != null) {
            txtSearchCustomer.setText(t.getPhoneNumber());
            lbCustomerCode.setText(t.getCustomerCode());
            lbCustomerName.setText(t.getCustomerName());
        } else {
            showMessageConfirm("Không tìm thấy khách hàng \nbạn có muốn thêm mới khách hàng", () -> {
                showPopupInsertCustomer();
            });
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="{PetService...">
    private void getListService(List<PetServices> list) {
        int stt = 1;
        tbService.setRowCount(0);
        for (PetServices p : list) {
            tbService.addRow(new Object[]{
                p.getId(),
                stt,
                p.getServiceCode(),
                p.getServiceName(),
                ">= " + p.getDuration(),
                p.getTimeUnit(),
                Ultil.formatCurrency(p.getPriceService()),
                p.isStatus() ? "Hoạt động" : "Ngưng nhận",
                new ModelAction<>(p, new EventAction<PetServices>() {
                    @Override
                    public void delete(PetServices p) {

                    }

                    @Override
                    public void update(PetServices p) {

                    }

                    @Override
                    public void add(PetServices model) {
                        showPopupService(model);
                    }
                })
            });
            stt++;
        }
    }

    public void addServiceToInvoiceD(PetServices p, int inputAmount, String petCode) {
        if (getSelectedRowInvoice() == -1) {
            showMessageFail("Vui lòng chọn hóa đơn!!");
            GlassPanePopup.closePopup("pInput");
            return;
        }

        int id = getIdSelectedInvoice(); // Lấy ID hóa đơn

        Pets c = petDAO.getPetByCode(petCode);

        if (c == null) {
            showMessageFail("Bạn chưa chọn thông tin pet");
            return;
        }

        InvoiceDetails existingDetail = invoiceDetailDAO.getInvoiceDetailByServiceId(id, p.getId(), c.getId());

        boolean isUpdatedOrInserted = false;

        if (existingDetail != null) {
            int newQuantity = existingDetail.getUsageOrQuantity() + inputAmount;
            BigDecimal totalPrice = p.getPriceService().multiply(BigDecimal.valueOf(newQuantity));

            // Cập nhật số lượng và tổng tiền trước
            isUpdatedOrInserted = invoiceDetailDAO.updateUsageOrQuantityAndTprice(existingDetail.getId(), newQuantity, totalPrice);

        } else { // Nếu chưa tồn tại, thêm mới
            InvoiceDetails detail = new InvoiceDetails();
            detail.setInvoiceDetailCode("HDCT" + Ultil.generateRandomCode());
            detail.setTotalPrice(p.getPriceService().multiply(BigDecimal.valueOf(inputAmount)));

            Invoices i = new Invoices();
            i.setId(id);
            detail.setInvoice(i);
            detail.setPetService(p);
            detail.setPet(c);
            detail.setUsageOrQuantity(inputAmount);

            // Chèn dữ liệu trước
            isUpdatedOrInserted = invoiceDetailDAO.insertInvoiceDetailService(detail);
        }

        if (isUpdatedOrInserted) {
            // Sau khi cập nhật hoặc thêm sản phẩm, mới cập nhật tổng tiền
            this.updateToTalPriceInvoice(id);
            showInvoiceDetailByIdInvoice();
            GlassPanePopup.closePopup("pPet");
        } else {
            System.out.println("Thêm dịch vụ thất bại!");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInvoice = new com.petshop.swing.table.Table();
        jPanel5 = new javax.swing.JPanel();
        button12 = new com.petshop.swing.Button1();
        jPanel13 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnConfirmInvoice = new com.petshop.swing.Button();
        jLabel33 = new javax.swing.JLabel();
        btnSearchCustomer = new com.petshop.swing.Button();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        lbInvoiceCode = new javax.swing.JLabel();
        lbCustomerName = new javax.swing.JLabel();
        btnAddInvoice = new com.petshop.swing.Button();
        btnDeleteInvoice = new com.petshop.swing.Button();
        btnResetInvoice = new com.petshop.swing.Button();
        jLabel38 = new javax.swing.JLabel();
        lbTotalPrice = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        lbTotalPrice1 = new javax.swing.JLabel();
        checkBoxPaymStatus = new com.petshop.swing.checkbox.JCheckBoxCustom();
        cbbPayment = new com.petshop.swing.combobox.Combobox();
        txtSearchCustomer = new com.petshop.swing.textfield.TextField1();
        lbCustomerCode = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbInvoiceDetail = new com.petshop.swing.tableMore.TableMore1();
        materialTabbed1 = new com.petshop.swing.tabbed.MaterialTabbed();
        jPanel2 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txtSearchProductDetail = new com.petshop.swing.textfield.TextFieldAnimation();
        cbbSort = new com.petshop.swing.combobox.ComboboxRounded();
        cbbFilterTypePet = new com.petshop.swing.combobox.ComboboxRounded();
        cbbFilterProduct = new com.petshop.swing.combobox.ComboboxRounded();
        btnScanBarcode = new com.petshop.swing.Button1();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbProductDetail = new com.petshop.swing.tableMore.TableMore();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        textFieldAnimation1 = new com.petshop.swing.textfield.TextFieldAnimation();
        comboboxRounded1 = new com.petshop.swing.combobox.ComboboxRounded();
        comboboxRounded3 = new com.petshop.swing.combobox.ComboboxRounded();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbService = new com.petshop.swing.tableMore.TableMore();

        setMaximumSize(new java.awt.Dimension(1058, 741));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel1.setText("HÓA ĐƠN CHỜ");

        tbInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "STT", "Mã HD", "Tên khách hàng", "Tên nhân viên", "Tổng giá", "Trạng thái thanh toán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInvoiceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbInvoice);
        if (tbInvoice.getColumnModel().getColumnCount() > 0) {
            tbInvoice.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoice.getColumnModel().getColumn(0).setMaxWidth(0);
            tbInvoice.getColumnModel().getColumn(1).setMinWidth(0);
            tbInvoice.getColumnModel().getColumn(1).setMaxWidth(0);
            tbInvoice.getColumnModel().getColumn(2).setMinWidth(40);
            tbInvoice.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        button12.setText("Quét hóa đơn");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button12, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Hóa đơn");

        btnConfirmInvoice.setBackground(new java.awt.Color(0, 255, 255));
        btnConfirmInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-yes-30.png"))); // NOI18N
        btnConfirmInvoice.setText("Xác nhận");
        btnConfirmInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmInvoiceActionPerformed(evt);
            }
        });

        jLabel33.setText("Nhập số điện thại khách hàng:");

        btnSearchCustomer.setBackground(new java.awt.Color(204, 255, 255));
        btnSearchCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-search-15.png"))); // NOI18N
        btnSearchCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCustomerActionPerformed(evt);
            }
        });

        jLabel34.setText("HD:");

        jLabel35.setText("Tên KH:");

        lbInvoiceCode.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbInvoiceCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbInvoiceCode.setText("HD");

        lbCustomerName.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCustomerName.setText("Chưa có thông tin");

        btnAddInvoice.setBackground(new java.awt.Color(204, 255, 255));
        btnAddInvoice.setText("Tạo hóa đơn");
        btnAddInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddInvoiceActionPerformed(evt);
            }
        });

        btnDeleteInvoice.setBackground(new java.awt.Color(255, 204, 204));
        btnDeleteInvoice.setText("Hủy hóa đơn");
        btnDeleteInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteInvoiceActionPerformed(evt);
            }
        });

        btnResetInvoice.setBackground(new java.awt.Color(255, 255, 204));
        btnResetInvoice.setText("Làm mới");
        btnResetInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetInvoiceActionPerformed(evt);
            }
        });

        jLabel38.setText("Tổng tiền :");

        lbTotalPrice.setBackground(new java.awt.Color(255, 255, 255));
        lbTotalPrice.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbTotalPrice.setForeground(new java.awt.Color(255, 0, 0));
        lbTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTotalPrice.setText("VND");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setText("Thành tiền:");

        lbTotalPrice1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbTotalPrice1.setForeground(new java.awt.Color(255, 51, 0));
        lbTotalPrice1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTotalPrice1.setText("VND");

        checkBoxPaymStatus.setBackground(new java.awt.Color(255, 204, 204));
        checkBoxPaymStatus.setText("Đã thanh toán");

        cbbPayment.setLabeText("Phương thức thanh toán");
        cbbPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbPaymentActionPerformed(evt);
            }
        });

        lbCustomerCode.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbCustomerCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCustomerCode.setText("KH");

        jLabel3.setText("KH:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnConfirmInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbTotalPrice1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbCustomerCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(btnResetInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55)
                                        .addComponent(btnDeleteInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtSearchCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(checkBoxPaymStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 2, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel32)
                .addGap(0, 0, 0)
                .addComponent(jLabel33)
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(lbInvoiceCode)
                    .addComponent(lbCustomerCode)
                    .addComponent(jLabel3))
                .addGap(5, 5, 5)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(lbCustomerName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(lbTotalPrice))
                .addGap(10, 10, 10)
                .addComponent(cbbPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(lbTotalPrice1))
                .addGap(5, 5, 5)
                .addComponent(checkBoxPaymStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfirmInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel42.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel42.setText("HÓA ĐƠN CHI TIẾT");

        tbInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "STT", "Mã HDCT", "Mã SP or SV", "Tên SP or SV", "SL", "Giá bán or Giá SV", "Tổng giá", "Thông tin khác", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbInvoiceDetail);
        if (tbInvoiceDetail.getColumnModel().getColumnCount() > 0) {
            tbInvoiceDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(1).setMinWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(1).setMaxWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(2).setMinWidth(40);
            tbInvoiceDetail.getColumnModel().getColumn(2).setMaxWidth(40);
            tbInvoiceDetail.getColumnModel().getColumn(3).setMinWidth(80);
            tbInvoiceDetail.getColumnModel().getColumn(3).setMaxWidth(80);
            tbInvoiceDetail.getColumnModel().getColumn(4).setMinWidth(80);
            tbInvoiceDetail.getColumnModel().getColumn(4).setMaxWidth(80);
            tbInvoiceDetail.getColumnModel().getColumn(5).setMinWidth(200);
            tbInvoiceDetail.getColumnModel().getColumn(5).setMaxWidth(200);
            tbInvoiceDetail.getColumnModel().getColumn(6).setMinWidth(40);
            tbInvoiceDetail.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel42)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        materialTabbed1.setBackground(new java.awt.Color(245, 245, 245));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel43.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel43.setText("DANH SÁCH SẢN PHẨM");

        txtSearchProductDetail.setBackground(new java.awt.Color(250, 250, 250));

        cbbSort.setLabeText("Sắp xếp theo");

        cbbFilterTypePet.setLabeText("Loại thú cưng");

        cbbFilterProduct.setLabeText("Sản phẩm");

        btnScanBarcode.setText("Quét barcode");
        btnScanBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanBarcodeActionPerformed(evt);
            }
        });

        tbProductDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Tên SP", "Mã SP", "BarCode", "Dành cho", "Hương vị", "SL", "TL", "NSX", "HSD", "Giá bán", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbProductDetail);
        if (tbProductDetail.getColumnModel().getColumnCount() > 0) {
            tbProductDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbProductDetail.getColumnModel().getColumn(1).setMinWidth(40);
            tbProductDetail.getColumnModel().getColumn(1).setMaxWidth(40);
            tbProductDetail.getColumnModel().getColumn(2).setMinWidth(230);
            tbProductDetail.getColumnModel().getColumn(2).setMaxWidth(230);
            tbProductDetail.getColumnModel().getColumn(7).setMinWidth(40);
            tbProductDetail.getColumnModel().getColumn(7).setMaxWidth(40);
            tbProductDetail.getColumnModel().getColumn(8).setMinWidth(50);
            tbProductDetail.getColumnModel().getColumn(8).setMaxWidth(50);
            tbProductDetail.getColumnModel().getColumn(10).setMinWidth(60);
            tbProductDetail.getColumnModel().getColumn(10).setMaxWidth(60);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addGap(18, 18, 18)
                .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(btnScanBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnScanBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbFilterProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        materialTabbed1.addTab("Sản phẩm", jPanel2);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel8.setText("DANH SÁCH DỊCH VỤ");

        textFieldAnimation1.setBackground(new java.awt.Color(250, 250, 250));

        comboboxRounded1.setLabeText("Sắp xếp theo");

        comboboxRounded3.setLabeText("Loại dịch vụ");

        tbService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã DV", "Tên DV", "Thời gian", "Đơn vị thời gian", "Giá DV", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbService);
        if (tbService.getColumnModel().getColumnCount() > 0) {
            tbService.getColumnModel().getColumn(0).setMinWidth(0);
            tbService.getColumnModel().getColumn(0).setMaxWidth(0);
            tbService.getColumnModel().getColumn(4).setMinWidth(80);
            tbService.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)
                        .addGap(126, 126, 126)
                        .addComponent(comboboxRounded3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(comboboxRounded1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldAnimation1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addGap(0, 0, 0))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(textFieldAnimation1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboboxRounded1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboboxRounded3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1041, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Dịch vụ", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetInvoiceActionPerformed
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_btnResetInvoiceActionPerformed

    private void cbbPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbPaymentActionPerformed

    private void btnAddInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddInvoiceActionPerformed
        // TODO add your handling code here:
        insertInvoice();
    }//GEN-LAST:event_btnAddInvoiceActionPerformed

    private void btnSearchCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCustomerActionPerformed
        // TODO add your handling code here:
        searchCustomer(txtSearchCustomer.getText());
    }//GEN-LAST:event_btnSearchCustomerActionPerformed

    private void btnDeleteInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteInvoiceActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận hủy hóa đơn", () -> {
            deleteInvoice();
        });
    }//GEN-LAST:event_btnDeleteInvoiceActionPerformed

    private void tbInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInvoiceMouseClicked
        // TODO add your handling code here:
        showDataInvoice();
        showInvoiceDetailByIdInvoice();
    }//GEN-LAST:event_tbInvoiceMouseClicked

    private void btnConfirmInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmInvoiceActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Bạn có muốn in hóa đơn không?", () -> {
            updateInvoice();
        });
    }//GEN-LAST:event_btnConfirmInvoiceActionPerformed

    private void btnScanBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScanBarcodeActionPerformed
        // TODO add your handling code here:
        showPopupWebcam();
    }//GEN-LAST:event_btnScanBarcodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button btnAddInvoice;
    private com.petshop.swing.Button btnConfirmInvoice;
    private com.petshop.swing.Button btnDeleteInvoice;
    private com.petshop.swing.Button btnResetInvoice;
    private com.petshop.swing.Button1 btnScanBarcode;
    private com.petshop.swing.Button btnSearchCustomer;
    private com.petshop.swing.Button1 button12;
    private com.petshop.swing.combobox.ComboboxRounded cbbFilterProduct;
    private com.petshop.swing.combobox.ComboboxRounded cbbFilterTypePet;
    private com.petshop.swing.combobox.Combobox cbbPayment;
    private com.petshop.swing.combobox.ComboboxRounded cbbSort;
    private com.petshop.swing.checkbox.JCheckBoxCustom checkBoxPaymStatus;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded1;
    private com.petshop.swing.combobox.ComboboxRounded comboboxRounded3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbCustomerCode;
    private javax.swing.JLabel lbCustomerName;
    private javax.swing.JLabel lbInvoiceCode;
    private javax.swing.JLabel lbTotalPrice;
    private javax.swing.JLabel lbTotalPrice1;
    private com.petshop.swing.tabbed.MaterialTabbed materialTabbed1;
    private com.petshop.swing.table.Table tbInvoice;
    private com.petshop.swing.tableMore.TableMore1 tbInvoiceDetail;
    private com.petshop.swing.tableMore.TableMore tbProductDetail;
    private com.petshop.swing.tableMore.TableMore tbService;
    private com.petshop.swing.textfield.TextFieldAnimation textFieldAnimation1;
    private com.petshop.swing.textfield.TextField1 txtSearchCustomer;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchProductDetail;
    // End of variables declaration//GEN-END:variables
}
