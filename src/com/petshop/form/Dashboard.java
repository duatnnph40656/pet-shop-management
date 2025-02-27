/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.form;

import com.petshop.btnfloating.FloatingButtonUI;
import com.petshop.daos.CustomerDAO;
import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.PetCareServiceDAO;
import com.petshop.daos.PetServiceDAO;
import com.petshop.daos.ProductDetailDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.main.Main;
import com.petshop.models.BestSellingProduct;
import com.petshop.models.MostUsedService;
import com.petshop.popup.PopupExport;
import com.petshop.swing.model.ModelCard;
import com.petshop.swing.model.ModelStudent;
import com.petshop.swing.icon.GoogleMaterialDesignIcons;
import com.petshop.swing.icon.IconFontSwing;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.noticeboard.ModelNoticeBoard;
import com.petshop.swing.table.EventAction;
import com.petshop.ultils.Ultil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayer;
import javax.swing.JOptionPane;
import org.bridj.objc.ObjCBlock;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author dut
 */
public class Dashboard extends javax.swing.JPanel {

    /**
     * Creates new form Dashboard
     */
    private final InvoiceDAO invoiceDAO;
    private final CustomerDAO customerDAO;
    private final PetCareServiceDAO careServiceDAO;
    private final ProductDetailDAO productDetailDAO;
    private final PetServiceDAO petServiceDAO;
    private boolean isDailyMode = true;
    private boolean isShowingDailyRevenue = true;
    private boolean isShowingDailyNewCustomers = true;
    private boolean isShowingDailyServices = true;// Biến trạng thái hiển thị

    public Dashboard() {
        initComponents();
        tbProduct.fixTable(jScrollPane4);
        tbService.fixTable(jScrollPane5);
        invoiceDAO = new InvoiceDAO();
        customerDAO = new CustomerDAO();
        careServiceDAO = new PetCareServiceDAO();
        productDetailDAO = new ProductDetailDAO();
        petServiceDAO = new PetServiceDAO();
        cacularTotalOrders();
        cacularTotalRevenue();
        cacularTotalNewCustomer();
        cacularTotalServicesUsed();
        init();
        restTextProduct();
        restTextService();
    }

    public void onFloatingButtonClick() {
        PopupExport pEx = new PopupExport();
        GlassPanePopup.showPopup(pEx, "pExport");
    }

    private void init() {
        getListBestSaleProduct();
        getListMostServiceUsed();
    }

    private void restTextProduct() {
        txtDateEndProduct.setText("Chọn ngày");
        txtDateStartProduct.setText("Chọn ngày");
    }

    private void restTextService() {
        txtDateEndService.setText("Chọn ngày");
        txtDateStartService.setText("Chọn ngày");
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

    private void cacularTotalRevenue() {
        int todayRevenue = invoiceDAO.getTodayRevenue();
        int yesterdayRevenue = invoiceDAO.getYesterdayRevenue();
        int currentMonthRevenue = invoiceDAO.getCurrentMonthRevenue();
        int lastMonthRevenue = invoiceDAO.getLastMonthRevenue();

        // Tính % thay đổi so với hôm qua
        double dailyChange = (yesterdayRevenue == 0) ? 100.0 : ((double) (todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 100;

        // Tính % thay đổi so với tháng trước
        double monthlyChange = (lastMonthRevenue == 0) ? 100.0 : ((double) (currentMonthRevenue - lastMonthRevenue) / lastMonthRevenue) * 100;

        Icon icon1 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ATTACH_MONEY, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));

        // Chuyển đổi tiền tệ trước khi hiển thị
        String formattedTodayRevenue = Ultil.formatCurrencyDouble(todayRevenue);
        String formattedMonthRevenue = Ultil.formatCurrencyDouble(currentMonthRevenue);

        // Hiển thị mặc định theo ngày
        card5.setData(new ModelCard("Doanh Thu (Today)", (double) todayRevenue, dailyChange, icon1));

        // Xử lý khi click vào card
        card5.setOnClickListener(e -> {
            isShowingDailyRevenue = !isShowingDailyRevenue; // Chuyển trạng thái
            if (isShowingDailyRevenue) {
                card5.setData(new ModelCard("Doanh Thu (Today)", (double) todayRevenue, dailyChange, icon1));
            } else {
                card5.setData(new ModelCard("Doanh Thu (Month)", (double) currentMonthRevenue, monthlyChange, icon1));
            }
        });
    }

    private void cacularTotalOrders() {
        int todayOrders = invoiceDAO.getTodayOrders();
        int yesterdayOrders = invoiceDAO.getYesterdayOrders();
        int currentMonthOrders = invoiceDAO.getCurrentMonthOrders();
        int lastMonthOrders = invoiceDAO.getLastMonthOrders();

        // Tính phần trăm thay đổi
        double dailyChange = (yesterdayOrders == 0) ? 100.0 : ((double) (todayOrders - yesterdayOrders) / yesterdayOrders) * 100;
        double monthlyChange = (lastMonthOrders == 0) ? 100.0 : ((double) (currentMonthOrders - lastMonthOrders) / lastMonthOrders) * 100;

        Icon icon2 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHOPPING_CART, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));

        // Kiểm tra chế độ hiển thị
        String title;
        int orders;
        double percentage;
        if (isDailyMode) {
            title = "Đơn Hàng (Today)";
            orders = todayOrders;
            percentage = dailyChange;
        } else {
            title = "Đơn Hàng (Month)";
            orders = currentMonthOrders;  // LẤY SỐ ĐƠN HÀNG TRONG THÁNG
            percentage = monthlyChange;
        }

        // Gán dữ liệu vào card
        card6.setData(new ModelCard(title, orders, percentage, icon2));

        // Thêm sự kiện click
        card6.setOnClickListener(e -> {
            isDailyMode = !isDailyMode;  // Đổi trạng thái
            cacularTotalOrders();  // Cập nhật lại dữ liệu
        });
    }

    private void cacularTotalNewCustomer() {
        int todayNewCustomers = customerDAO.getTodayNewCustomers();
        int currentMonthNewCustomers = customerDAO.getCurrentMonthNewCustomers();
        int lastMonthNewCustomers = customerDAO.getLastMonthNewCustomers();

        // Tính % thay đổi so với tháng trước
        double monthlyChange = (lastMonthNewCustomers == 0) ? 100.0
                : ((double) (currentMonthNewCustomers - lastMonthNewCustomers) / lastMonthNewCustomers) * 100;

        Icon icon3 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PERSON_ADD, 60,
                new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));

        // Hiển thị mặc định theo ngày
        card7.setData(new ModelCard("Khách Hàng Mới(Today)", todayNewCustomers, 0, icon3));

        // Xử lý khi click vào card
        card7.setOnClickListener(e -> {
            isShowingDailyNewCustomers = !isShowingDailyNewCustomers; // Chuyển trạng thái
            if (isShowingDailyNewCustomers) {
                card7.setData(new ModelCard("Khách Hàng Mới(Today)", todayNewCustomers, 0, icon3));
            } else {
                card7.setData(new ModelCard("Khách Hàng Mới(Month)", currentMonthNewCustomers, monthlyChange, icon3));
            }
        });
    }

    private void cacularTotalServicesUsed() {
        int todayServices = careServiceDAO.getServicesUsedToday();
        int yesterdayServices = careServiceDAO.getServicesUsedYesterday();
        int currentMonthServices = careServiceDAO.getServicesUsedThisMonth();
        int lastMonthServices = careServiceDAO.getServicesUsedLastMonth();

        // Tính % thay đổi
        double dailyChange = (yesterdayServices == 0) ? 100.0 : ((double) (todayServices - yesterdayServices) / yesterdayServices) * 100;
        double monthlyChange = (lastMonthServices == 0) ? 100.0 : ((double) (currentMonthServices - lastMonthServices) / lastMonthServices) * 100;

        Icon iconService = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BUILD, 60,
                new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));

        // Hiển thị mặc định theo ngày
        card8.setData(new ModelCard("Dịch vụ (Today)", (double) todayServices, dailyChange, iconService));

        // Xử lý khi click để chuyển đổi giữa ngày và tháng
        card8.setOnClickListener(e -> {
            isShowingDailyServices = !isShowingDailyServices;
            if (isShowingDailyServices) {
                card8.setData(new ModelCard("Dịch vụ (Today)", (double) todayServices, dailyChange, iconService));
            } else {
                card8.setData(new ModelCard("Dịch vụ (Month)", (double) currentMonthServices, monthlyChange, iconService));
            }
        });
    }

    private void getListBestSaleProduct() {
        try {
            // Ngày hiện tại (cuối ngày)
            LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

            // Ngày cách đây 7 ngày (đầu ngày)
            LocalDateTime startDate = endDate.minusDays(7).withHour(0).withMinute(0).withSecond(0);

            // Gọi DAO lấy danh sách sản phẩm bán chạy trong 7 ngày gần nhất
            List<BestSellingProduct> list = productDetailDAO.getBestSellingProducts(startDate, endDate, 10);
            loadTableBestProduct(list);

        } catch (Exception e) {
            e.printStackTrace();
            showMessageFail("Lỗi khi lấy danh sách sản phẩm bán chạy!");
        }
    }

    private void getListBestSaleProductRangeDate() {
        try {

            if (txtDateStartProduct.getText().equals("Chọn ngày")) {
                showMessageFail("Vui lòng chọn này!!");
                return;
            }
            if (txtDateEndProduct.getText().equals("Chọn ngày")) {
                showMessageFail("Vui lòng chọn này!!");
                return;
            }

            // Định dạng của ngày nhập vào
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Chuyển đổi từ chuỗi ngày sang LocalDateTime
            LocalDateTime startDate = LocalDateTime.of(
                    LocalDate.parse(txtDateStartProduct.getText(), formatter),
                    LocalDateTime.MIN.toLocalTime()
            );

            LocalDateTime endDate = LocalDateTime.of(
                    LocalDate.parse(txtDateEndProduct.getText(), formatter),
                    LocalDateTime.MAX.toLocalTime()
            );

            // Gọi DAO lấy danh sách sản phẩm bán chạy
            List<BestSellingProduct> list = productDetailDAO.getBestSellingProducts(startDate, endDate, 10);

            loadTableBestProduct(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTableBestProduct(List<BestSellingProduct> list) {
        int stt = 1;
        tbProduct.setRowCount(0);
        for (BestSellingProduct p : list) {
            tbProduct.addRow(new Object[]{
                stt,
                p.getProductCode(),
                p.getProductName(),
                p.getTotalSold() + " lần",
                p.getSaleCount() + " lượt"
            });
            stt++;
        }
    }

    private void getListMostServiceUsed() {
        try {
            // Lấy thời điểm hiện tại (LocalDateTime)
            LocalDateTime endDateTime = LocalDateTime.now();

            // Lấy thời điểm cách đây 7 ngày
            LocalDateTime startDateTime = endDateTime.minusDays(7);

            // Gọi DAO lấy danh sách dịch vụ được sử dụng nhiều nhất trong 7 ngày gần nhất
            List<MostUsedService> list = petServiceDAO.getMostUsedServices(startDateTime, endDateTime, 10);

            // Hiển thị danh sách trên giao diện
            loadMostServiceUsed(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getListMostServiceUsedDateRanger() {
        try {
            // Định dạng ngày theo "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Lấy ngày từ ô nhập liệu
            String startDateStr = txtDateStartService.getText().trim();
            String endDateStr = txtDateEndService.getText().trim();

            // Kiểm tra nếu ô nhập liệu rỗng
            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                showMessageFail("Vui lòng nhập đầy đủ ngày bắt đầu và ngày kết thúc!");
                return;
            }

            // Chuyển đổi chuỗi ngày sang LocalDateTime
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;

            try {
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                // Gán giờ mặc định: Bắt đầu từ 00:00:00 và kết thúc 23:59:59
                startDateTime = startDate.atStartOfDay();
                endDateTime = endDate.atTime(23, 59, 59);
            } catch (DateTimeParseException e) {
                showMessageFail("Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy.");
                return;
            }

            // Gọi DAO lấy danh sách dịch vụ sử dụng nhiều nhất trong khoảng thời gian đã chọn
            List<MostUsedService> list = petServiceDAO.getMostUsedServices(startDateTime, endDateTime, 10);
            loadMostServiceUsed(list);

        } catch (Exception e) {
            e.printStackTrace();
            showMessageFail("Lỗi khi lấy danh sách dịch vụ được sử dụng nhiều nhất!");
        }
    }

    private void loadMostServiceUsed(List<MostUsedService> list) {
        int stt = 1;
        tbService.setRowCount(0);
        for (MostUsedService m : list) {
            tbService.addRow(new Object[]{
                stt,
                m.getServiceCode(),
                m.getServiceName(),
                m.getTotalUsed() + " lần"
            });
            stt++;
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

        dateChooser1 = new com.petshop.swing.datechooser.DateChooser();
        dateChooser2 = new com.petshop.swing.datechooser.DateChooser();
        dateChooser3 = new com.petshop.swing.datechooser.DateChooser();
        dateChooser4 = new com.petshop.swing.datechooser.DateChooser();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbProduct = new com.petshop.swing.table.Table();
        jLabel2 = new javax.swing.JLabel();
        txtDateStartProduct = new com.petshop.swing.textfield.TextField1();
        txtDateEndProduct = new com.petshop.swing.textfield.TextField1();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        button1 = new com.petshop.swing.Button();
        button2 = new com.petshop.swing.Button();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbService = new com.petshop.swing.table.Table();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDateEndService = new com.petshop.swing.textfield.TextField1();
        jLabel8 = new javax.swing.JLabel();
        txtDateStartService = new com.petshop.swing.textfield.TextField1();
        button3 = new com.petshop.swing.Button();
        button4 = new com.petshop.swing.Button();
        jLabel4 = new javax.swing.JLabel();
        card5 = new com.petshop.component.Card();
        card6 = new com.petshop.component.Card();
        card7 = new com.petshop.component.Card();
        card8 = new com.petshop.component.Card();

        dateChooser1.setTextRefernce(txtDateStartProduct);

        dateChooser2.setTextRefernce(txtDateEndProduct);

        dateChooser3.setTextRefernce(txtDateStartService);

        dateChooser4.setTextRefernce(txtDateEndService);

        setBackground(new java.awt.Color(245, 245, 245));
        setPreferredSize(new java.awt.Dimension(1058, 741));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setText("Dashboard");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tbProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "SL bán", "Số lần"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbProduct);
        if (tbProduct.getColumnModel().getColumnCount() > 0) {
            tbProduct.getColumnModel().getColumn(0).setMinWidth(35);
            tbProduct.getColumnModel().getColumn(0).setMaxWidth(35);
            tbProduct.getColumnModel().getColumn(1).setMinWidth(80);
            tbProduct.getColumnModel().getColumn(1).setMaxWidth(80);
            tbProduct.getColumnModel().getColumn(2).setMinWidth(300);
            tbProduct.getColumnModel().getColumn(2).setMaxWidth(300);
        }

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 102, 102));
        jLabel2.setText("Danh sách sản phẩm bán chạy nhất");

        jLabel5.setText("Từ ngày:");

        jLabel6.setText("Đến ngày:");

        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-eye-20.png"))); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-restore-20.png"))); // NOI18N
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDateStartProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDateEndProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDateStartProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDateEndProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tbService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "Mã DV", "Tên DV", "SL sử dụng"
            }
        ));
        jScrollPane5.setViewportView(tbService);
        if (tbService.getColumnModel().getColumnCount() > 0) {
            tbService.getColumnModel().getColumn(0).setMinWidth(35);
            tbService.getColumnModel().getColumn(0).setMaxWidth(35);
            tbService.getColumnModel().getColumn(1).setMinWidth(80);
            tbService.getColumnModel().getColumn(1).setMaxWidth(80);
            tbService.getColumnModel().getColumn(2).setMinWidth(250);
            tbService.getColumnModel().getColumn(2).setMaxWidth(250);
        }

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 204, 0));
        jLabel3.setText("Danh sách dịch vụ được sử dụng nhiều nhất");

        jLabel7.setText("Đến ngày:");

        jLabel8.setText("Từ ngày:");

        txtDateStartService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateStartServiceActionPerformed(evt);
            }
        });

        button3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-eye-20.png"))); // NOI18N
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-restore-20.png"))); // NOI18N
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, 0)
                                .addComponent(txtDateStartService, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addGap(0, 0, 0)
                                .addComponent(txtDateEndService, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDateEndService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)
                        .addComponent(txtDateStartService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(4, 72, 210));
        jLabel4.setText("Dashboard / Home");

        card5.setBackground(new java.awt.Color(76, 175, 80));
        card5.setColorGradient(new java.awt.Color(56, 211, 6));

        card6.setBackground(new java.awt.Color(13, 71, 161));
        card6.setColorGradient(new java.awt.Color(33, 150, 243));

        card7.setBackground(new java.awt.Color(103, 58, 183));
        card7.setColorGradient(new java.awt.Color(156, 39, 176));

        card8.setBackground(new java.awt.Color(230, 81, 0));
        card8.setColorGradient(new java.awt.Color(255, 152, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(card6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(card7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(card8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel4)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(card5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        getListBestSaleProductRangeDate();
    }//GEN-LAST:event_button1ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
        getListBestSaleProduct();
        restTextProduct();
    }//GEN-LAST:event_button2ActionPerformed

    private void txtDateStartServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateStartServiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateStartServiceActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        // TODO add your handling code here:
        getListMostServiceUsedDateRanger();
    }//GEN-LAST:event_button3ActionPerformed

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        // TODO add your handling code here:
        getListMostServiceUsed();
        restTextService();
    }//GEN-LAST:event_button4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button button1;
    private com.petshop.swing.Button button2;
    private com.petshop.swing.Button button3;
    private com.petshop.swing.Button button4;
    private com.petshop.component.Card card5;
    private com.petshop.component.Card card6;
    private com.petshop.component.Card card7;
    private com.petshop.component.Card card8;
    private com.petshop.swing.datechooser.DateChooser dateChooser1;
    private com.petshop.swing.datechooser.DateChooser dateChooser2;
    private com.petshop.swing.datechooser.DateChooser dateChooser3;
    private com.petshop.swing.datechooser.DateChooser dateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private com.petshop.swing.table.Table tbProduct;
    private com.petshop.swing.table.Table tbService;
    private com.petshop.swing.textfield.TextField1 txtDateEndProduct;
    private com.petshop.swing.textfield.TextField1 txtDateEndService;
    private com.petshop.swing.textfield.TextField1 txtDateStartProduct;
    private com.petshop.swing.textfield.TextField1 txtDateStartService;
    // End of variables declaration//GEN-END:variables
}
