/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.InvoiceDetailDAO;
import com.petshop.daos.ProductDetailDAO;
import com.petshop.daos.ReturnInvoiceDAO;
import com.petshop.daos.ReturnInvoiceDetailDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.models.ProductDetails;
import com.petshop.models.ReturnInvoiceDetail;
import com.petshop.models.ReturnInvoices;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.popup.GlassPanePopup;
import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import com.petshop.ultils.Ultil;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.petshop.swing.table.ModelImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author duat
 */
public class PopupReturn extends javax.swing.JPanel {

    /**
     * Creates new form PopupInvoices
     */
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final InvoiceDAO invoiceDAO;
    private final ProductDetailDAO productDetailDAO;
    private final ReturnInvoiceDetailDAO returnInvoiceDetailDAO;
    private final ReturnInvoiceDAO returnInvoiceDAO;

    public void setInvoiceCode(String code) {
        txtInvoiceCode.setText(code);
        loadFormInvoice();
    }

    public void setReturnInvoiceCode(String code) {
        txtReturnInvoiceCode.setText(code);
        ReturnInvoices c = returnInvoiceDAO.getReturnInvoiceByCode(code);
        calculateTotals();
        showReturnInvoiceDetail(c.getId());
    }

    public void getListRinvoiceD() {
        ReturnInvoices c = returnInvoiceDAO.getReturnInvoiceByCode(txtReturnInvoiceCode.getText());
        showReturnInvoiceDetail(c.getId());
    }

    public int getIdRinvoice() {
        ReturnInvoices c = returnInvoiceDAO.getReturnInvoiceByCode(txtReturnInvoiceCode.getText());
        return c.getId();
    }

    public PopupReturn() {
        initComponents();
        setOpaque(false);
        invoiceDAO = new InvoiceDAO();
        invoiceDetailDAO = new InvoiceDetailDAO();
        productDetailDAO = new ProductDetailDAO();
        returnInvoiceDetailDAO = new ReturnInvoiceDetailDAO();
        returnInvoiceDAO = new ReturnInvoiceDAO();
        txtInvoiceCode.setEditable(false);
        tbReturnInvoiceDetail.fixTable(jScrollPane1);
        tbProductDetail.fixTable(jScrollPane3);
        tbInvoiceDetail.fixTable(jScrollPane4);
        getListProductDetail(productDetailDAO.getListProductDetail());

    }

    //<editor-fold defaultstate="collapsed" desc="{Message...">
    private void showMessageSuccess(String message) {
        DialogMessageSuccess success = new DialogMessageSuccess(message);
        raven.glasspanepopup.GlassPanePopup.showPopup(success);
    }

    private void showMessageError(String message) {
        DialogMessageError error = new DialogMessageError(message);
        raven.glasspanepopup.GlassPanePopup.showPopup(error);
    }

    private void showMessageFail(String message) {
        DialogMessageFail fail = new DialogMessageFail(message);
        raven.glasspanepopup.GlassPanePopup.showPopup(fail);
    }

    public void showMessageConfirm(String message, Runnable onConfirmAction) {
        DialogConfirm confirm = new DialogConfirm(message);
        confirm.setConfirmListener(new ConfirmListener() {
            @Override
            public void onConfirm() {
                if (onConfirmAction != null) {
                    onConfirmAction.run(); // Thực hiện hành động truyền vào
                }
                raven.glasspanepopup.GlassPanePopup.closePopup("confirm");
            }

            @Override
            public void onCancel() {

            }
        });
        raven.glasspanepopup.GlassPanePopup.showPopup(confirm, "confirm"); // Hiển thị popup
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
        raven.glasspanepopup.GlassPanePopup.showPopup(input, "input");
    }
    //</editor-fold>

    private void loadFormInvoice() {
        Invoices invoice = invoiceDAO.searchInvoiceByCodeResultModel(txtInvoiceCode.getText());
        if (invoice != null) {
            List<InvoiceDetails> list = invoiceDetailDAO.getInvoiceDetailsByInvoiceId(invoice.getId());
            getListInvoiceDetail(list);
            lblTienGocMua.setText(Ultil.formatCurrency(invoice.getTotalPrice()));
            lblTienHangTra.setText("0 đ");
            lblCanTra.setText("0 đ");
            lblKhachTraThem.setText("0 đ");
        }
    }

    private int getIdInvoice() {
        Invoices invoice = invoiceDAO.searchInvoiceByCodeResultModel(txtInvoiceCode.getText());
        return invoice.getId();
    }

    public void getListInvoiceDetail(List<InvoiceDetails> list) {
        int stt = 1;
        tbInvoiceDetail.setRowCount(0);

        for (InvoiceDetails detail : list) {
            if (!detail.isTypeInvoiceDetail()) {
                String name = (detail.getProductDetail() != null) ? detail.getProductDetail().getProductDetailName() : "N/A";
                String code = (detail.getProductDetail() != null) ? detail.getProductDetail().getProductDetailCode() : "N/A";
                BigDecimal price = (detail.getProductDetail() != null) ? detail.getProductDetail().getPrice() : BigDecimal.ZERO;

                // Lấy số lượng mua ban đầu
                int soLuongMua = detail.getUsageOrQuantity();

                // Thêm dữ liệu vào bảng, số lượng trả sẽ hiển thị dưới dạng số
                tbInvoiceDetail.addRow(new Object[]{
                    detail.getId(),
                    stt++, // STT
                    detail.getInvoiceDetailCode(),
                    code, // Mã SP
                    name, // Tên SP
                    soLuongMua, // Số lượng mua ban đầu
                    Ultil.formatCurrency(price), // Đơn giá
                    Ultil.formatCurrency(detail.getTotalPrice()),
                    new ModelAction<>(detail, new EventAction<InvoiceDetails>() {
                        @Override
                        public void delete(InvoiceDetails p) {

                        }

                        @Override
                        public void update(InvoiceDetails p) {

                        }

                        @Override
                        public void add(InvoiceDetails model) {
                            showInputDialog(1, inputAmount -> {
                                if (model.getUsageOrQuantity() < inputAmount) {
                                    showMessageFail("Số lượng nhập lớn hơn số lượng trong hóa đơn!!");
                                    return;
                                }
                                insertPinInvoiceDToInvoiceD(model.getProductDetail(), inputAmount, model.getId());
                            });
                        }
                    })// Thành tiền
                });
            }
        }
    }

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
                    p.getTypePet().getTypePetName(),
                    p.getQuantityInStock(),
                    p.getWeight() + "KG",
                    p.getFlavor(),
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
                            showInputDialog(1, inputAmount -> {
                                insertPToReturnInvoiceD(model, inputAmount); // Thêm sản phẩm với số lượng nhập vào
                            });
                        }
                    })

                });
                stt++;
            }

        }
    }

    public void insertPinInvoiceDToInvoiceD(ProductDetails p, int inputAmount, int idInvoiceDetail) {
        if (!productDetailDAO.isEnoughStock(p.getId(), inputAmount)) {
            showMessageFail("Số lượng nhập lớn hơn số lượng trong kho!!");
            return;
        }

        ReturnInvoiceDetail existingDetail = returnInvoiceDetailDAO.getReturnInvoiceDetailByProductDetailId(getIdRinvoice(), p.getId());
        int totalReturnQuantity = inputAmount;

        if (existingDetail != null) {
            totalReturnQuantity += existingDetail.getUsageOrQuantity();
        }

        int purchasedQuantity = invoiceDetailDAO.getPurchasedQuantityById(idInvoiceDetail);
        if (totalReturnQuantity > purchasedQuantity) {
            showMessageFail("Số lượng trả hàng quá số lượng đã mua!");
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
            return;
        }

        boolean isUpdatedOrInserted = false;
        if (existingDetail != null) {
            BigDecimal totalPrice = p.getPrice().multiply(BigDecimal.valueOf(totalReturnQuantity));
            isUpdatedOrInserted = returnInvoiceDetailDAO.updateUsageOrQuantityAndTprice(existingDetail.getId(), totalReturnQuantity, totalPrice);
        } else {
            ReturnInvoiceDetail detail = new ReturnInvoiceDetail();
            detail.setReturnInvoiceDetailCode("HDCT" + Ultil.generateRandomCode());
            detail.setTotalPrice(p.getPrice().multiply(BigDecimal.valueOf(inputAmount)));

            ReturnInvoices i = new ReturnInvoices();
            i.setId(getIdRinvoice());
            detail.setReturnsInvoices(i);
            detail.setProductDetails(p);
            detail.setUsageOrQuantity(inputAmount);
            detail.setTypeInvoiceDetail(true);

            InvoiceDetails l = new InvoiceDetails();
            l.setId(idInvoiceDetail);
            detail.setInvoiceDetails(l);

            isUpdatedOrInserted = returnInvoiceDetailDAO.insertReturnInvoiceDetailProduct(detail);
        }

        if (isUpdatedOrInserted) {
            showReturnInvoiceDetail(getIdRinvoice());
            calculateTotals();
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
        } else {
            showMessageError("Có lỗi xảy ra");
        }
    }

    public void insertPToReturnInvoiceD(ProductDetails p, int inputAmount) {
        if (!productDetailDAO.isEnoughStock(p.getId(), inputAmount)) {
            showMessageFail("Số lượng nhập lớn hơn số lượng trong kho!!");
            return;
        }

        ReturnInvoices c = returnInvoiceDAO.getReturnInvoiceByCode(txtReturnInvoiceCode.getText());

        // Kiểm tra nếu sản phẩm đã có trong hóa đơn trả hàng
        ReturnInvoiceDetail existingDetail = returnInvoiceDetailDAO.getReturnInvoiceDetailByProductDetailId(c.getId(), p.getId());

        int totalReturnQuantity = inputAmount;
        if (existingDetail != null) {
            totalReturnQuantity += existingDetail.getUsageOrQuantity();
        }

        // Kiểm tra số lượng nhập vào không vượt quá số lượng tồn kho
        if (totalReturnQuantity > p.getQuantityInStock()) {
            showMessageFail("Số lượng đổi hàng vượt quá số lượng tồn kho!");
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
            return;
        }

        boolean isUpdatedOrInserted = false;

        if (existingDetail != null) { // Nếu sản phẩm đã tồn tại trong hóa đơn đổi
            BigDecimal totalPrice = p.getPrice().multiply(BigDecimal.valueOf(totalReturnQuantity));

            // Cập nhật số lượng và tổng tiền
            isUpdatedOrInserted = returnInvoiceDetailDAO.updateUsageOrQuantityAndTprice(existingDetail.getId(), totalReturnQuantity, totalPrice);
        } else { // Nếu chưa tồn tại, thêm mới
            ReturnInvoiceDetail detail = new ReturnInvoiceDetail();
            detail.setReturnInvoiceDetailCode("HDCT" + Ultil.generateRandomCode());
            detail.setTotalPrice(p.getPrice().multiply(BigDecimal.valueOf(inputAmount)));

            ReturnInvoices i = new ReturnInvoices();
            i.setId(c.getId());
            detail.setReturnsInvoices(i);
            detail.setProductDetails(p);
            detail.setUsageOrQuantity(inputAmount);
            detail.setTypeInvoiceDetail(false); // Hàng đổi

            isUpdatedOrInserted = returnInvoiceDetailDAO.insertReturnInvoiceDetailProduct(detail);
        }

        if (isUpdatedOrInserted) {
            showReturnInvoiceDetail(c.getId());
            calculateTotals();
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
        } else {
            showMessageError("Có lỗi xảy ra khi đổi hàng");
        }
    }

    private void updateToTalPriceRInvoice(int id) {
        returnInvoiceDAO.updateTotalPriceForExchange(id);
        returnInvoiceDAO.updateTotalPriceReturnForExchange(id);
    }

    private void deletedReturnInvoiceD(ReturnInvoiceDetail i) {
        if (i == null) {
            return;
        }
        returnInvoiceDetailDAO.updateDeletedRInvoiceD(i.getId());
        calculateTotals();
        getListRinvoiceD();
    }

    private void showReturnInvoiceDetail(int id) {
        List<ReturnInvoiceDetail> list = returnInvoiceDetailDAO.getReturnInvoiceDetailsByReturnInvoiceId(id);
        getListReturnInvoiceDetail(list);
    }

    public void getListReturnInvoiceDetail(List<ReturnInvoiceDetail> list) {
        int stt = 1;
        tbReturnInvoiceDetail.setRowCount(0);
        for (ReturnInvoiceDetail i : list) {
            tbReturnInvoiceDetail.addRow(new Object[]{
                i.getId(),
                i.getProductDetails().getId(),
                stt,
                i.getReturnInvoiceDetailCode(),
                i.getProductDetails().getProductDetailCode(),
                i.getProductDetails().getProductDetailName(),
                i.getUsageOrQuantity(),
                Ultil.formatCurrency(i.getProductDetails().getPrice()),
                Ultil.formatCurrency(i.getTotalPrice()),
                i.isTypeInvoiceDetail() ? "Hàng trả" : "Hàng đổi", // ✅ Hiển thị đúng trạng thái
                new ModelAction<>(i, new EventAction<ReturnInvoiceDetail>() {
                    @Override
                    public void delete(ReturnInvoiceDetail i) {
                        showMessageConfirm("Xác nhận xóa?", () -> {
                            deletedReturnInvoiceD(i);
                        });
                    }

                    @Override
                    public void update(ReturnInvoiceDetail i) {
                        if (i.isTypeInvoiceDetail()) {
                            showInputDialog(1, inputAmount -> {
                                updateQuantityReturnInvoiceDetailOfInvoiceD(i, inputAmount, i.getId());
                            });
                        } else {
                            showInputDialog(1, inputAmount -> {
                                updateQuantityReturnInvoiceDetailOfProductD(i, inputAmount);
                            });
                        }
                    }

                    @Override

                    public void add(ReturnInvoiceDetail i) {
                    }
                })
            });
            stt++;
        }
    }

    private int getSelectedRowReturnInvoiceD() {
        return tbReturnInvoiceDetail.getSelectedRow();
    }

    public Integer getIdReturnInvoiceD() {
        return (Integer) tbReturnInvoiceDetail.getValueAt(getSelectedRowReturnInvoiceD(), 1);
    }

    public Integer getIdProductDetail() {
        return (Integer) tbReturnInvoiceDetail.getValueAt(getSelectedRowReturnInvoiceD(), 1);
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
                raven.glasspanepopup.GlassPanePopup.closePopupLast();
            }
        });
        raven.glasspanepopup.GlassPanePopup.showPopup(dialog, "pInput");
    }

    public void updateQuantityReturnInvoiceDetailOfProductD(ReturnInvoiceDetail detail, int amount) {
        if (amount < 1) {
            showMessageFail("Số lượng không được nhỏ hơn 1");
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
            return;
        }

        BigDecimal totalPrice = BigDecimal.ZERO; // Giá trị mặc định là 0

        if (!returnInvoiceDetailDAO.isValidProductQuantityOfRinvoiceD(getIdProductDetail(), amount)) {
            showMessageFail("Số lượng nhập lớn hơn số lượng trong kho!!");
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
            return;
        }

        if (detail.getProductDetails() != null && detail.getProductDetails().getPrice() != null) {
            totalPrice = detail.getProductDetails().getPrice().multiply(BigDecimal.valueOf(amount));
        }

        returnInvoiceDetailDAO.updateUsageOrQuantityAndTprice(detail.getId(), amount, totalPrice);


        showReturnInvoiceDetail(getIdRinvoice());
        calculateTotals();
        raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
    }

    public void updateQuantityReturnInvoiceDetailOfInvoiceD(ReturnInvoiceDetail detail, int amount, int invoiceDetail) {
        if (amount < 1) {
            showMessageFail("Số lượng không được nhỏ hơn 1");
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
            return;
        }

        int id = detail.getId();
        BigDecimal totalPrice = BigDecimal.ZERO; // Giá trị mặc định là 0

        int totalReturnQuantity = amount;

        int purchasedQuantity = invoiceDetailDAO.getPurchasedQuantityById(invoiceDetail);
        if (totalReturnQuantity > purchasedQuantity) {
            showMessageFail("Số lượng trả vượt quá số lượng đã mua!");
            raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
            return;
        }

        if (detail.getProductDetails() != null && detail.getProductDetails().getPrice() != null) {
            totalPrice = detail.getProductDetails().getPrice().multiply(BigDecimal.valueOf(amount));
        }

        invoiceDetailDAO.updateUsageOrQuantityAndTprice(id, amount, totalPrice);

        showReturnInvoiceDetail(getIdRinvoice());
        calculateTotals();
        raven.glasspanepopup.GlassPanePopup.closePopup("pInput");
    }

    public void calculateTotals() {
        // Lấy hóa đơn trả hàng từ mã hóa đơn
        ReturnInvoices returnInvoice = returnInvoiceDAO.getReturnInvoiceByCode(txtReturnInvoiceCode.getText());
        if (returnInvoice == null) {
            lblTienHangTra.setText("0 đ");
            lblTienHangDoi.setText("0 đ");
            lblCanTra.setText("0 đ");
            lblKhachTraThem.setText("0 đ");
            return;
        }

        int returnInvoiceId = returnInvoice.getId();

        // Lấy tổng tiền hàng trả từ SQL
        double totalReturnPrice = returnInvoiceDetailDAO.getTotalReturnAmountByInvoiceId(returnInvoiceId);

        // Lấy tổng tiền hàng đổi từ SQL
        double totalExchangePrice = returnInvoiceDetailDAO.getTotalExchangeAmountByInvoiceId(returnInvoiceId);

        // Trường hợp tổng tiền đổi lớn hơn tổng tiền trả
        if (totalExchangePrice > totalReturnPrice) {
            lblCanTra.setText("0 đ");
            lblKhachTraThem.setText(Ultil.formatCurrency(BigDecimal.valueOf(totalExchangePrice - totalReturnPrice)));
        } else {
            lblKhachTraThem.setText("0 đ");
            lblCanTra.setText(Ultil.formatCurrency(BigDecimal.valueOf(totalReturnPrice - totalExchangePrice)));
        }

        lblTienHangTra.setText(Ultil.formatCurrency(BigDecimal.valueOf(totalReturnPrice)));
        lblTienHangDoi.setText(Ultil.formatCurrency(BigDecimal.valueOf(totalExchangePrice)));
    }
    
    
    private void updateReturnInvoice(){
        
        
        
        updateToTalPriceRInvoice(getIdRinvoice());
        
        raven.glasspanepopup.GlassPanePopup.closePopup("pInvoice");
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
        g2.dispose();
        super.paintComponent(grphcs);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbReturnInvoiceDetail = new com.petshop.swing.tableMore.TableMore1();
        jLabel1 = new javax.swing.JLabel();
        txtSearchHangDoi = new com.petshop.swing.textfield.TextFieldAnimation();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbProductDetail = new com.petshop.swing.tableMore.TableMore();
        jPanel1 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        lblTienGocMua = new javax.swing.JLabel();
        lblCanTra = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lblKhachTraThem = new javax.swing.JLabel();
        button11 = new com.petshop.swing.Button1();
        button12 = new com.petshop.swing.Button1();
        txtInvoiceCode = new com.petshop.swing.textfield.TextField1();
        lblTienHangTra = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        textAreaScroll1 = new com.petshop.swing.textarea.TextAreaScroll();
        txtNotes = new com.petshop.swing.textarea.TextArea();
        txtReturnInvoiceCode = new com.petshop.swing.textfield.TextField1();
        jLabel4 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lblTienHangDoi = new javax.swing.JLabel();
        button1 = new com.petshop.swing.Button();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbInvoiceDetail = new com.petshop.swing.tableMore.TableMore();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        tbReturnInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "null", "", "STT", "Mã SP", "Tên SP", "Số lượng mua", "Số lượng trả", "Đơn giá ", "Thành tiền", "Trạng thái ", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbReturnInvoiceDetail);
        if (tbReturnInvoiceDetail.getColumnModel().getColumnCount() > 0) {
            tbReturnInvoiceDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbReturnInvoiceDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbReturnInvoiceDetail.getColumnModel().getColumn(1).setMinWidth(0);
            tbReturnInvoiceDetail.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel1.setText("Hóa đơn chi tiết");

        txtSearchHangDoi.setBackground(new java.awt.Color(250, 250, 250));
        txtSearchHangDoi.setHintText("Tìm hàng đổi ");

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel5.setText("Tìm hàng đổi ");

        tbProductDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Tên SP", "Mã SP", "Dành cho", "SL", "Trọng lượng", "Hương vị", "Giá bán", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbProductDetail);
        if (tbProductDetail.getColumnModel().getColumnCount() > 0) {
            tbProductDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbProductDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbProductDetail.getColumnModel().getColumn(1).setMinWidth(34);
            tbProductDetail.getColumnModel().getColumn(1).setMaxWidth(35);
        }

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel40.setText("Cần trả khách: ");

        lblTienGocMua.setBackground(new java.awt.Color(255, 255, 255));
        lblTienGocMua.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblTienGocMua.setForeground(new java.awt.Color(255, 0, 0));
        lblTienGocMua.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTienGocMua.setText("VND");

        lblCanTra.setBackground(new java.awt.Color(255, 255, 255));
        lblCanTra.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblCanTra.setForeground(new java.awt.Color(255, 0, 0));
        lblCanTra.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCanTra.setText("VND");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel39.setText("Tổng tiền hàng trả  :");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel41.setText("Khách cần trả thêm: ");

        lblKhachTraThem.setBackground(new java.awt.Color(255, 255, 255));
        lblKhachTraThem.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblKhachTraThem.setForeground(new java.awt.Color(255, 0, 0));
        lblKhachTraThem.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKhachTraThem.setText("VND");

        button11.setBackground(new java.awt.Color(204, 204, 204));
        button11.setText("In hoá đơn");

        button12.setBackground(new java.awt.Color(153, 255, 153));
        button12.setText("Trả hàng");
        button12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button12ActionPerformed(evt);
            }
        });

        lblTienHangTra.setBackground(new java.awt.Color(255, 255, 255));
        lblTienHangTra.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblTienHangTra.setForeground(new java.awt.Color(255, 0, 0));
        lblTienHangTra.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTienHangTra.setText("VND");

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setText("Mã HD:");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel38.setText("Tổng tien hoa don gốc  :");

        textAreaScroll1.setLabelText("Ghi chú");

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        textAreaScroll1.setViewportView(txtNotes);

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel4.setText("Mã HD trả hàng:");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel42.setText("Tổng tiền hàng đổi :");

        lblTienHangDoi.setBackground(new java.awt.Color(255, 255, 255));
        lblTienHangDoi.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblTienHangDoi.setForeground(new java.awt.Color(255, 0, 0));
        lblTienHangDoi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTienHangDoi.setText("VND");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(button11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button12, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtReturnInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachTraThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCanTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(33, 33, 33)
                        .addComponent(lblTienHangDoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel38)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblTienGocMua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel39)
                            .addGap(33, 33, 33)
                            .addComponent(lblTienHangTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(txtInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 166, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(4, 4, 4)
                .addComponent(txtReturnInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(lblTienHangDoi))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(lblCanTra))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(lblKhachTraThem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(txtInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(lblTienGocMua))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel39)
                        .addComponent(lblTienHangTra))
                    .addGap(107, 107, 107)
                    .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(51, Short.MAX_VALUE)))
        );

        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-close-15.png"))); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("Hóa đơn chi tiết trả hàng");

        tbInvoiceDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "STT", "Mã HDCT", "Mã SP", "Tên SP", "SL mua", "Giá sản phẩm", "Tổng tiền", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbInvoiceDetail);
        if (tbInvoiceDetail.getColumnModel().getColumnCount() > 0) {
            tbInvoiceDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tbInvoiceDetail.getColumnModel().getColumn(1).setMinWidth(34);
            tbInvoiceDetail.getColumnModel().getColumn(1).setMaxWidth(34);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSearchHangDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSearchHangDoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        ReturnInvoices c = returnInvoiceDAO.getReturnInvoiceByCode(txtReturnInvoiceCode.getText());
        returnInvoiceDAO.deletedReturnInvoice(c.getId());
        raven.glasspanepopup.GlassPanePopup.closePopup("pInvoice");
    }//GEN-LAST:event_button1ActionPerformed

    private void button12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button12ActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận trả hàng?", ()->{updateReturnInvoice();});
        
    }//GEN-LAST:event_button12ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button button1;
    private com.petshop.swing.Button1 button11;
    private com.petshop.swing.Button1 button12;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblCanTra;
    private javax.swing.JLabel lblKhachTraThem;
    private javax.swing.JLabel lblTienGocMua;
    private javax.swing.JLabel lblTienHangDoi;
    private javax.swing.JLabel lblTienHangTra;
    private com.petshop.swing.tableMore.TableMore tbInvoiceDetail;
    private com.petshop.swing.tableMore.TableMore tbProductDetail;
    private com.petshop.swing.tableMore.TableMore1 tbReturnInvoiceDetail;
    private com.petshop.swing.textarea.TextAreaScroll textAreaScroll1;
    private com.petshop.swing.textfield.TextField1 txtInvoiceCode;
    private com.petshop.swing.textarea.TextArea txtNotes;
    private com.petshop.swing.textfield.TextField1 txtReturnInvoiceCode;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearchHangDoi;
    // End of variables declaration//GEN-END:variables
}
