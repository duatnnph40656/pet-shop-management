/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.petshop.popup;

import com.petshop.daos.CustomerDAO;
import com.petshop.daos.InvoiceDAO;
import com.petshop.daos.PetCareServiceDAO;
import com.petshop.daos.PetDAO;
import com.petshop.daos.PetServiceDAO;
import com.petshop.daos.TypePetDAO;
import com.petshop.event.ConfirmListener;
import com.petshop.event.ConfirmListenerInput;
import com.petshop.event.EventCallBack;
import com.petshop.event.EventTextField;
import com.petshop.models.Customers;
import com.petshop.models.Invoices;
import com.petshop.models.PetCareServices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
import com.petshop.models.TypePets;
import com.petshop.swing.datechooser.EventDateChooser;
import com.petshop.swing.datechooser.SelectedAction;
import com.petshop.swing.datechooser.SelectedDate;
import com.petshop.swing.message.DialogConfirm;
import com.petshop.swing.message.DialogInput;
import com.petshop.swing.message.DialogMessageError;
import com.petshop.swing.message.DialogMessageFail;
import com.petshop.swing.message.DialogMessageSuccess;
import com.petshop.swing.popup.GlassPanePopup;
import com.petshop.ultils.Ultil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author duat
 */
public class PopupService extends javax.swing.JPanel {

    /**
     * Creates new form PopupService
     */
    private final PetDAO petDAO;
    private final TypePetDAO typePetDAO;
    private final PetServiceDAO petServiceDAO;
    private final PetCareServiceDAO careServices;
    private final CustomerDAO customerDAO;
    private final InvoiceDAO invoiceDAO;
    private ConfirmListener listener;

    // Đăng ký ConfirmListener
    public void setConfirmListener(ConfirmListener listener) {
        this.listener = listener;
    }

    public PopupService() {
        initComponents();
        setOpaque(false);
        cbbTypePet.setLightWeightPopupEnabled(false);
        tbPet.fixTable(jScrollPane1);
        petDAO = new PetDAO();
        typePetDAO = new TypePetDAO();
        petServiceDAO = new PetServiceDAO();
        careServices = new PetCareServiceDAO();
        customerDAO = new CustomerDAO();
        invoiceDAO = new InvoiceDAO();
        btnCancel.addActionListener(evt -> {
            if (listener != null) {
                listener.onCancel();
            }
            raven.glasspanepopup.GlassPanePopup.closePopupLast();
        });
        btnConfirm.addActionListener(evt -> {
            if (listener != null) {
                listener.onConfirm();
                insertPetCareService();
            }
        });
        txtServiceStart.setEditable(false);
        this.init();
    }

    public void init() {
        txtPetCode.setText("PET" + Ultil.generateRandomCode());
//        getListPet(petDAO.getListPetSortId());
        loadCBBTypePet(typePetDAO.getListTypePet());
        eventDateChooser();
        eventTable();
        eventSearch();
    }

    private void eventSearch() {
        txtSearch.addEvent(new EventTextField() { // là tên của cái search
            @Override
            public void onPressed(EventCallBack call) {
                //  Test
                try {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(5); // time sleep
                    }
                    if (txtSearch.getText().isEmpty()) {
                        showMessageFail("Vui lòng nhập thông tin!!");
                        call.done();
                        return;
                    }
                    searchPetsByOwner(txtSearch.getText());
                    txtSearch.setText("");
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

    private void eventDateChooser() {
        dateChooser1.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    dateChooser1.hidePopup();
                }
            }
        });
        dateChooser2.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                    dateChooser2.hidePopup();
                }
            }
        });
    }

    public void setServiceCode(String serviceCode) {
        txtServiceCode.setText(serviceCode);
    }

    public void setCustomerCode(String customers) {
        txtCustomerCode.setText(customers);
        fillTableByCustomerCode(customers);
    }

    public void setInvoiceCode(String invoice) {
        txtInvoiceCode.setText(invoice);
    }

    public String getPetCode() {
        return txtPetCode.getText();
    }

    public void setPhoneNumber(String phoneNumber) {
        txtOwner.setText(phoneNumber);
    }

    public int getPetId() {
        return (int) tbPet.getValueAt(getSeletedRowTable(), 0);
    }

    private void eventTable() {
        tbPet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDataPet();
            }
        });

        tbPet.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    showDataPet();
                }
            }
        });
    }

    private int getSeletedRowTable() {
        return tbPet.getSelectedRow();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
        g2.dispose();
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

    private void loadCBBTypePet(List<TypePets> list) {
        cbbTypePet.removeAllItems();
        for (TypePets t : list) {
            cbbTypePet.addItem(t);

        }
        cbbTypePet.setSelectedIndex(-1);
    }

    private void getListPet(List<Pets> list) {
        int stt = 1;
        tbPet.setRowCount(0);
        for (Pets p : list) {
            tbPet.addRow(new Object[]{
                p.getId(),
                stt,
                checkNullOrEmpty(p.getPetCode()), // Kiểm tra mã thú cưng
                checkNullOrEmpty(p.getPetName()), // Kiểm tra tên thú cưng
                (p.getTypePet() != null && !isNullOrEmpty(p.getTypePet().getTypePetName()))
                ? p.getTypePet().getTypePetName() : "Chưa có thông tin", // Kiểm tra loại thú cưng
                (p.getAge() != null) ? p.getAge() + " Tháng" : "Chưa có thông tin", // Kiểm tra tuổi
                checkNullOrEmpty(p.getBreed()), // Kiểm tra giống loài
                (p.getWeight() != null) ? p.getWeight().toString() : "Chưa có thông tin", // Kiểm tra cân nặng
                p.isGender() ? "Đực" : "Cái", // Kiểm tra giới tính
                checkNullOrEmpty(p.getColor()), // Kiểm tra màu sắc
                p.isVaccinated() ? "Đã tiêm" : "Chưa tiêm", // Kiểm tra tiêm phòng
                checkNullOrEmpty(p.getOwner()), // Kiểm tra chủ sở hữu
                p.isStatus() ? "Hoạt động" : "Không hoạt động", // Kiểm tra trạng thái
            });
            stt++;
        }
    }

    private String checkNullOrEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? "Chưa có thông tin" : value;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void resetForm() {
        txtPetCode.setText("PET" + Ultil.generateRandomCode());
        txtPetName.setText("");
        txtOwner.setText("");
        cbbTypePet.setSelectedIndex(-1);
    }

    private boolean check() {
        if (txtPetName.getText().isEmpty()) {
            showMessageFail("Tên trống!");
            return false;
        }
        if (txtBreed.getText().isEmpty()) {
            showMessageFail("Giống loài trống!");
            return false;
        }
        if (txtOwner.getText().isEmpty()) {
            showMessageFail("Thông tin chủ trống!");
            return false;
        }
        if (txtColor.getText().isEmpty()) {
            showMessageFail("Màu sắc trống!");
            return false;
        }
        if (cbbTypePet.getSelectedIndex() == -1) {
            showMessageFail("Chưa chọn loại thú cưng!");
            return false;
        }
        return true;
    }

    private Pets readForm() {
        Pets p = new Pets();
        p.setPetCode("PET" + Ultil.generateRandomCode());
        p.setPetName(txtPetName.getText());
        p.setBreed(txtBreed.getText());
        p.setOwner(txtOwner.getText());
        p.setColor(txtColor.getText());

        // Lấy loại thú cưng từ ComboBox
        TypePets t = (TypePets) cbbTypePet.getSelectedItem();
        p.setTypePet(t);

        // Kiểm tra checkbox tiêm phòng & giới tính
        p.setVaccinated(cbVaccina.isSelected());
        p.setGender(rdMale.isSelected());

        // Xử lý cân nặng (cho phép null)
        String weightText = txtWeight.getText().trim();
        if (!weightText.isEmpty()) {
            try {
                p.setWeight(new BigDecimal(weightText));
            } catch (NumberFormatException e) {
                showMessageFail("Cân nặng không hợp lệ!");
                return null; // Trả về null nếu có lỗi
            }
        } else {
            p.setWeight(null); // Nếu trống thì đặt null
        }

        // Xử lý tuổi (cho phép null)
        String ageText = txtAge.getText().trim();
        if (!ageText.isEmpty()) {
            p.setAge(ageText);
        } else {
            p.setAge(null);
        }

        // Lấy thông tin khách hàng từ mã khách hàng
        Customers c = customerDAO.searchCustomerByCustomerCode(txtCustomerCode.getText());
        p.setCustomer(c);

        return p;
    }

    private void showDataPet() {
        txtPetCode.setText((String) tbPet.getValueAt(getSeletedRowTable(), 2));
        txtPetName.setText((String) tbPet.getValueAt(getSeletedRowTable(), 3));

        String typetName = (String) tbPet.getValueAt(getSeletedRowTable(), 4);
        for (int i = 0; i < cbbTypePet.getItemCount(); i++) {
            if (cbbTypePet.getItemAt(i).toString().equalsIgnoreCase(typetName)) {
                cbbTypePet.setSelectedIndex(i);
                break;
            }
        }

        txtBreed.setText((String) tbPet.getValueAt(getSeletedRowTable(), 6));
        txtWeight.setText((String) tbPet.getValueAt(getSeletedRowTable(), 7));
        txtAge.setText((String) tbPet.getValueAt(getSeletedRowTable(), 5));
        txtColor.setText((String) tbPet.getValueAt(getSeletedRowTable(), 9));

        boolean gender = tbPet.getValueAt(getSeletedRowTable(), 8).equals("Đực");
        if (gender) {
            rdMale.isSelected();
        } else {
            rdFemale.isSelected();
        }
        txtOwner.setText((String) tbPet.getValueAt(getSeletedRowTable(), 11));
    }

    private void insertPet() {
        if (!check()) {
            return;
        }
        if (petDAO.insertPetNew(readForm())) {
            showMessageSuccess("Thêm thành công");
            fillTableByCustomerCode(txtCustomerCode.getText());;
            resetForm();
            selectFirstRow();
        } else {
            showMessageFail("Thêm thất bại");
        }
    }

    private PetCareServices readFormPetC() {
        PetCareServices c = new PetCareServices();

        // Ngày bắt đầu: Luôn lấy thời gian hiện tại
        LocalDateTime dateStart = LocalDateTime.now();

        // Ngày kết thúc mặc định: Cuối ngày hôm nay
        LocalDateTime dateEnd = dateStart.withHour(23).withMinute(59).withSecond(59);

        // Kiểm tra nếu người dùng có nhập ngày kết thúc
        String dateEndStr = txtServiceEnd.getText().trim();
        if (!dateEndStr.isEmpty()) {
            try {
                // Chuyển đổi từ chuỗi "dd/MM/yyyy" sang LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate parsedDate = LocalDate.parse(dateEndStr, formatter);

                // Kết hợp với 23:59:59 để tạo LocalDateTime
                dateEnd = parsedDate.atTime(23, 59, 59);
            } catch (DateTimeParseException e) {
                showMessageFail("Sai định dạng");
                return null; // Trả về null nếu có lỗi nhập ngày
            }
        }

        c.setDateStart(dateStart);
        c.setDateEnd(dateEnd);
        c.setNote(txtNote.getText());

        // Lấy thông tin dịch vụ
        PetServices p = petServiceDAO.getPetServiceByCode(txtServiceCode.getText());
        c.setPetS(p);

        // Lấy thông tin thú cưng từ bảng
        Pets e = new Pets();
        e.setId((int) tbPet.getValueAt(getSeletedRowTable(), 0));
        c.setPet(e);

        Invoices i = invoiceDAO.getInvoiceByCode(txtInvoiceCode.getText());
        c.setInvoices(i);

        c.setStatus(true);

        return c;
    }

    private void insertPetCareService() {
        if (getSeletedRowTable() == -1) {
            return;
        }
        if (careServices.insertPetCareService(readFormPetC())) {
            showMessageSuccess("Đã lưu vào quản lý dịch vụ!");
        } else {
            showMessageFail("Lưu thất bại!!!");
        }
    }

    private void selectFirstRow() {
        if (tbPet.getRowCount() > 0) { // Kiểm tra nếu bảng có dữ liệu
            tbPet.setRowSelectionInterval(0, 0); // Chọn dòng đầu tiên
            showDataPet(); // Gọi hàm để lấy dữ liệu dòng đã chọn
        }
    }

    private void searchPetsByOwner(String keyword) {
        List<Pets> list = petDAO.searchPetsByOwner(keyword);
        if (list.isEmpty()) {
            showMessageFail("không tìm thấy thông tin!!");
        } else {
            getListPet(list);
        }

    }

    private void fillTableByCustomerCode(String customerCode) {
        List<Pets> list = petDAO.getListPetByCustomerCode(customerCode);
        getListPet(list);
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPet = new com.petshop.swing.table.Table();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPetCode = new com.petshop.swing.textfield.TextField1();
        jLabel4 = new javax.swing.JLabel();
        txtPetName = new com.petshop.swing.textfield.TextField1();
        jLabel5 = new javax.swing.JLabel();
        txtBreed = new com.petshop.swing.textfield.TextField1();
        jLabel6 = new javax.swing.JLabel();
        txtOwner = new com.petshop.swing.textfield.TextField1();
        jLabel7 = new javax.swing.JLabel();
        btnAddPet = new com.petshop.swing.Button1();
        cbVaccina = new com.petshop.swing.checkbox.JCheckBoxCustom();
        txtColor = new com.petshop.swing.textfield.TextField1();
        jLabel9 = new javax.swing.JLabel();
        txtAge = new com.petshop.swing.textfield.TextField1();
        jLabel10 = new javax.swing.JLabel();
        rdMale = new com.petshop.swing.radio_button.RadioButtonCustom();
        rdFemale = new com.petshop.swing.radio_button.RadioButtonCustom();
        cbbTypePet = new com.petshop.swing.combobox.Combobox();
        btnCancel = new com.petshop.swing.Button();
        btnResetFormPet = new com.petshop.swing.Button();
        jLabel12 = new javax.swing.JLabel();
        txtWeight = new com.petshop.swing.textfield.TextField1();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtServiceCode = new com.petshop.swing.textfield.TextField();
        txtServiceStart = new com.petshop.swing.textfield.TextField();
        txtServiceEnd = new com.petshop.swing.textfield.TextField();
        textAreaScroll1 = new com.petshop.swing.textarea.TextAreaScroll();
        txtNote = new com.petshop.swing.textarea.TextArea();
        btnConfirm = new com.petshop.swing.Button1();
        txtSearch = new com.petshop.swing.textfield.TextFieldAnimation();
        txtInvoiceCode = new com.petshop.swing.textfield.TextField1();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCustomerCode = new com.petshop.swing.textfield.TextField1();

        dateChooser1.setTextRefernce(txtServiceStart);

        dateChooser2.setTextRefernce(txtServiceEnd);

        setBackground(new java.awt.Color(245, 245, 245));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tbPet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "STT", "Mã Pet", "Tên Pet", "Loại Pet", "Tuổi", "Giống loài", "Cân nặng", "Giới tính", "Màu sắc", "Vaccinated", "Thông tin chủ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbPet);
        if (tbPet.getColumnModel().getColumnCount() > 0) {
            tbPet.getColumnModel().getColumn(0).setMinWidth(0);
            tbPet.getColumnModel().getColumn(0).setMaxWidth(0);
            tbPet.getColumnModel().getColumn(1).setMinWidth(40);
            tbPet.getColumnModel().getColumn(1).setMaxWidth(40);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));

        jLabel2.setText("Mã thú cưng:");

        txtPetCode.setEnabled(false);
        txtPetCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPetCodeActionPerformed(evt);
            }
        });

        jLabel4.setText("Tên thú cưng:");

        jLabel5.setText("Giống loài:");

        jLabel7.setText("Thông tin chủ sở hữu:");

        btnAddPet.setBackground(new java.awt.Color(102, 255, 204));
        btnAddPet.setText("Thêm mới");
        btnAddPet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPetActionPerformed(evt);
            }
        });

        cbVaccina.setBackground(new java.awt.Color(255, 0, 0));
        cbVaccina.setText("Đã tiêm vaccin");

        jLabel9.setText("Màu sắc:");

        txtAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgeActionPerformed(evt);
            }
        });

        jLabel10.setText("Tuổi:");

        rdMale.setBackground(new java.awt.Color(255, 102, 102));
        buttonGroup1.add(rdMale);
        rdMale.setText("Giống đực");

        rdFemale.setBackground(new java.awt.Color(255, 102, 153));
        buttonGroup1.add(rdFemale);
        rdFemale.setText("Giống cái");

        cbbTypePet.setLabeText("Loại thú cưng");
        cbbTypePet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbTypePetActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(245, 245, 245));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-close-15.png"))); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnResetFormPet.setBackground(new java.awt.Color(245, 245, 245));
        btnResetFormPet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-restore-20.png"))); // NOI18N

        jLabel12.setText("Cân nặng:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPetCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtPetName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtOwner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtAge, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rdMale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(rdFemale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(cbVaccina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbbTypePet, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnResetFormPet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddPet, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(txtBreed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtWeight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel2))
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(txtPetCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(0, 0, 0)
                .addComponent(txtPetName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cbbTypePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addComponent(txtBreed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel9)
                .addGap(0, 0, 0)
                .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel10)
                .addGap(0, 0, 0)
                .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel7)
                .addGap(0, 0, 0)
                .addComponent(txtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdMale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdFemale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbVaccina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddPet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetFormPet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("DANH SÁCH THÚ CƯNG ĐÃ LƯU");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("QUẢN LÝ DỊCH VỤ CHĂM SÓC THÚ CƯNG");

        txtServiceCode.setLabelText("Mã dịch vụ");

        txtServiceStart.setLabelText("Ngày bắt đầu");

        txtServiceEnd.setLabelText("Ngày kết thúc");

        textAreaScroll1.setLabelText("Ghi chú");

        txtNote.setColumns(20);
        txtNote.setRows(5);
        textAreaScroll1.setViewportView(txtNote);

        btnConfirm.setBackground(new java.awt.Color(51, 255, 255));
        btnConfirm.setText("Hoàn tất");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(txtServiceCode, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtServiceStart, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtServiceEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textAreaScroll1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtServiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtServiceStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtServiceEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        txtSearch.setBackground(new java.awt.Color(204, 255, 255));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        txtInvoiceCode.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Mã KH:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Mã HD:");

        txtCustomerCode.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(0, 0, 0)
                        .addComponent(txtInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addGap(0, 0, 0)
                        .addComponent(txtCustomerCode, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtInvoiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel11)
                            .addComponent(txtCustomerCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtPetCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPetCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPetCodeActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnAddPetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPetActionPerformed
        // TODO add your handling code here:
        showMessageConfirm("Xác nhận thêm mới?", () -> {
            insertPet();
        });
    }//GEN-LAST:event_btnAddPetActionPerformed

    private void cbbTypePetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTypePetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbTypePetActionPerformed

    private void txtAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button1 btnAddPet;
    private com.petshop.swing.Button btnCancel;
    private com.petshop.swing.Button1 btnConfirm;
    private com.petshop.swing.Button btnResetFormPet;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.petshop.swing.checkbox.JCheckBoxCustom cbVaccina;
    private com.petshop.swing.combobox.Combobox cbbTypePet;
    private com.petshop.swing.datechooser.DateChooser dateChooser1;
    private com.petshop.swing.datechooser.DateChooser dateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.petshop.swing.radio_button.RadioButtonCustom rdFemale;
    private com.petshop.swing.radio_button.RadioButtonCustom rdMale;
    private com.petshop.swing.table.Table tbPet;
    private com.petshop.swing.textarea.TextAreaScroll textAreaScroll1;
    private com.petshop.swing.textfield.TextField1 txtAge;
    private com.petshop.swing.textfield.TextField1 txtBreed;
    private com.petshop.swing.textfield.TextField1 txtColor;
    private com.petshop.swing.textfield.TextField1 txtCustomerCode;
    private com.petshop.swing.textfield.TextField1 txtInvoiceCode;
    private com.petshop.swing.textarea.TextArea txtNote;
    private com.petshop.swing.textfield.TextField1 txtOwner;
    private com.petshop.swing.textfield.TextField1 txtPetCode;
    private com.petshop.swing.textfield.TextField1 txtPetName;
    private com.petshop.swing.textfield.TextFieldAnimation txtSearch;
    private com.petshop.swing.textfield.TextField txtServiceCode;
    private com.petshop.swing.textfield.TextField txtServiceEnd;
    private com.petshop.swing.textfield.TextField txtServiceStart;
    private com.petshop.swing.textfield.TextField1 txtWeight;
    // End of variables declaration//GEN-END:variables
}
