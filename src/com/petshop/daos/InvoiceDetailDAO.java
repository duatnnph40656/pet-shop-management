/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
import com.petshop.models.ProductDetails;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class InvoiceDetailDAO {

    private Connection conn;

    public InvoiceDetailDAO() {
        conn = DBConnect.getConnection();
    }

//    public List<InvoiceDetails> getListInvoiceDetailProduct() {
//        String sql = "SELECT \n"
//                + "       id.id, \n"
//                + "       id.invoice_detail_code, \n"
//                + "       COALESCE(pd.product_detail_name, sd.service_name) AS product_or_service_name, \n"
//                + "       COALESCE(pd.product_detail_code, sd.service_code) AS product_or_service_code, \n"
//                + "       id.id_pet, \n"
//                + "       id.usage_or_quantity, \n"
//                + "       id.price, \n"
//                + "       (id.usage_or_quantity * id.price) AS total_price, \n"
//                + "       id.duration, \n"
//                + "       id.created_at, \n"
//                + "       iv.invoice_code \n"
//                + "FROM invoice_details id \n"
//                + "LEFT JOIN invoices iv ON id.id_invoice = iv.id \n"
//                + "LEFT JOIN product_details pd ON id.id_product_detail = pd.id \n"
//                + "LEFT JOIN service_details sd ON id.id_service_detail = sd.id \n"
//                + "LEFT JOIN pets p ON id.id_pet = p.id \n"
//                + "WHERE id.is_status = 1;";
//
//        List<InvoiceDetails> list = new ArrayList<>();
//        try (PreparedStatement ps = conn.prepareStatement(sql);
//                ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                list.add(mapInvoiceDetail(rs)); // Ánh xạ dữ liệu vào danh sách hóa đơn chi tiết
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
    public List<InvoiceDetails> getListInvoiceDetailProduct() {
        String sql = "SELECT \n"
                + "    id.id, \n"
                + "    id.invoice_detail_code, \n"
                + "    id.usage_or_quantity, \n"
                + "    id.total_price, \n"
                + "    id.created_at, \n"
                + "    id.is_status,\n"
                + "    iv.invoice_code, \n"
                + "    pd.product_detail_name, \n"
                + "    sd.service_name, \n"
                + "    p.pet_name\n"
                + "FROM invoice_details id\n"
                + "LEFT JOIN invoices iv ON id.id = iv.id\n"
                + "LEFT JOIN product_details pd ON id.id = pd.id\n"
                + "LEFT JOIN service_details sd ON id.id = sd.id\n"
                + "LEFT JOIN pets p ON id.id = p.id;";

        List<InvoiceDetails> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapInvoiceDetail(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertInvoiceDetail(InvoiceDetails invoiceDetail) {
        String sql = """
        INSERT INTO invoice_details (invoice_detail_code, id_invoice, usage_or_quantity, total_price, 
                                     id_product_detail, is_deleted,is_status,type_invoice_detail)
        VALUES ( ?, ?, ?, ?, ?, 0,?,0)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, invoiceDetail.getInvoiceDetailCode());
            ps.setInt(2, invoiceDetail.getInvoice().getId()); // Đảm bảo invoice không null
            ps.setInt(3, invoiceDetail.getUsageOrQuantity());
            ps.setBigDecimal(4, invoiceDetail.getPrice());

            // Kiểm tra null trước khi set giá trị
            if (invoiceDetail.getProductDetail() != null) {
                ps.setInt(5, invoiceDetail.getProductDetail().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (invoiceDetail.getPetService() != null) {
                ps.setInt(6, invoiceDetail.getPetService().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (invoiceDetail.getPet() != null) {
                ps.setInt(7, invoiceDetail.getPet().getId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setBoolean(9, invoiceDetail.isStatus());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsageOrQuantity(int id, int newQuantity) {
        String sql = "UPDATE invoice_details SET usage_or_quantity = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsageOrQuantityAndTprice(int id, int newQuantity, BigDecimal totalPrice) {
        String sql = "UPDATE invoice_details SET usage_or_quantity = ?, total_price = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(3, id);
            ps.setBigDecimal(2, totalPrice);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInvoiceDetail(int id) {
        String sql = "UPDATE invoice_details SET is_deleted = 1 WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
        }

        return false;
    }

    public InvoiceDetails getInvoiceDetailByProductDetailId(int invoiceId, int productDetailId) {
        String sql = """
        SELECT id, invoice_detail_code, id_invoice, usage_or_quantity, total_price, created_at, is_status
        FROM invoice_details
        WHERE id_product_detail = ? AND id_invoice = ? AND is_deleted = 0
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productDetailId);
            ps.setInt(2, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Nếu tìm thấy, trả về object InvoiceDetails
                    InvoiceDetails detail = new InvoiceDetails();
                    detail.setId(rs.getInt("id"));
                    detail.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
                    detail.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
                    detail.setTotalPrice(rs.getBigDecimal("total_price"));
                    detail.setCreatedAt(rs.getDate("created_at"));
                    detail.setStatus(rs.getBoolean("is_status"));
                    return detail;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy, trả về null
    }

    public boolean insertInvoiceDetailService(InvoiceDetails invoiceDetail) {
        String sql = """
        INSERT INTO invoice_details (invoice_detail_code, id_invoice, usage_or_quantity, total_price, 
                                     id_service_detail,id_pet, is_deleted,is_status,type_invoice_detail)
        VALUES ( ?, ?, ?, ?,?, ?, 0,?,1)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, invoiceDetail.getInvoiceDetailCode());
            ps.setInt(2, invoiceDetail.getInvoice().getId()); // Đảm bảo invoice không null
            ps.setInt(3, invoiceDetail.getUsageOrQuantity());
            ps.setBigDecimal(4, invoiceDetail.getTotalPrice());

            // Kiểm tra null trước khi set giá trị
            if (invoiceDetail.getPetService() != null) {
                ps.setInt(5, invoiceDetail.getPetService().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            if (invoiceDetail.getPet() != null) {
                ps.setInt(6, invoiceDetail.getPet().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setBoolean(7, invoiceDetail.isStatus());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<InvoiceDetails> getInvoiceDetailsByInvoiceId1(int invoiceId) {
        String sql = """
        SELECT 
            id.id, 
            id.invoice_detail_code, 
            id.usage_or_quantity, 
            id.total_price, 
            pd.product_detail_code,
            pd.product_detail_name,
            pd.price
        FROM invoice_details id
        LEFT JOIN invoices iv ON id.id_invoice = iv.id
        LEFT JOIN product_details pd ON id.id_product_detail = pd.id
        WHERE id.id_invoice = ? AND id.is_deleted = 0;
    """;

        List<InvoiceDetails> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapInvoiceDetailProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm lấy danh sách sản phẩm từ invoice_details
    public List<InvoiceDetails> getInvoiceDetails(int invoiceId) {
        List<InvoiceDetails> productList = new ArrayList<>();
        String query = "SELECT id_product_detail, usage_or_quantity FROM invoice_details WHERE id_invoice = ? AND type_invoice_detail = 0 AND is_deleted = 0";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("id_product_detail");
                int quantity = rs.getInt("usage_or_quantity");
                ProductDetails p = new ProductDetails();
                p.setId(productId);
                productList.add(new InvoiceDetails(p, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public InvoiceDetails getInvoiceDetailByServiceId(int invoiceId, int serviceId, int petId) {
        String sql = """
        SELECT id, invoice_detail_code, id_invoice, usage_or_quantity, total_price, created_at, is_status
        FROM invoice_details
        WHERE id_service_detail = ? AND id_invoice = ? AND id_pet = ? AND is_deleted = 0
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setInt(2, invoiceId);
            ps.setInt(3, petId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Nếu tìm thấy, trả về object InvoiceDetails
                    InvoiceDetails detail = new InvoiceDetails();
                    detail.setId(rs.getInt("id"));
                    detail.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
                    detail.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
                    detail.setTotalPrice(rs.getBigDecimal("total_price"));
                    detail.setCreatedAt(rs.getDate("created_at"));
                    detail.setStatus(rs.getBoolean("is_status"));
                    return detail;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy, trả về null
    }

    public InvoiceDetails mapInvoiceDetail1(ResultSet rs) throws SQLException {
        InvoiceDetails i = new InvoiceDetails();
        i.setId(rs.getInt("id"));
        i.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
        i.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
        i.setTotalPrice(rs.getBigDecimal("total_price"));

        Invoices ic = new Invoices();
        ic.setId(rs.getInt("id_invoice"));
        i.setInvoice(ic);

        ProductDetails p = new ProductDetails();
        p.setProductDetailName("product_detail_name");
        i.setProductDetail(p);

        PetServices ps = new PetServices();
        ps.setServiceName(rs.getString("service_name"));
        i.setPetService(ps);

        Pets pet = new Pets();
        pet.setPetName(rs.getString("pet_name"));
        i.setPet(pet);

        i.setCreatedAt(rs.getDate("created_at"));
        i.setStatus(rs.getBoolean("is_status"));

        return i;
    }

    public InvoiceDetails mapInvoiceDetailProduct(ResultSet rs) throws SQLException {
        InvoiceDetails i = new InvoiceDetails();
        i.setId(rs.getInt("id"));
        i.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
        i.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
        i.setTotalPrice(rs.getBigDecimal("total_price"));

        // Lấy thông tin sản phẩm
        ProductDetails p = new ProductDetails();
        p.setProductDetailCode(rs.getString("product_detail_code"));
        p.setProductDetailName(rs.getString("product_detail_name"));
        p.setPrice(rs.getBigDecimal("price"));
        i.setProductDetail(p);

        return i;
    }

    public boolean insertInvoiceDetailProduct(InvoiceDetails invoiceDetail) {
        String sql = """
        INSERT INTO invoice_details (invoice_detail_code, id_invoice, usage_or_quantity, total_price, 
                                     id_product_detail, is_deleted,is_status)
        VALUES ( ?, ?, ?, ?, ?, 0,?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, invoiceDetail.getInvoiceDetailCode());
            ps.setInt(2, invoiceDetail.getInvoice().getId()); // Đảm bảo invoice không null
            ps.setInt(3, invoiceDetail.getUsageOrQuantity());
            ps.setBigDecimal(4, invoiceDetail.getTotalPrice());

            // Kiểm tra null trước khi set giá trị
            if (invoiceDetail.getProductDetail() != null) {
                ps.setInt(5, invoiceDetail.getProductDetail().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setBoolean(6, invoiceDetail.isStatus());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<InvoiceDetails> getInvoiceDetailsByInvoiceId(int invoiceId) {
        String sql = """
        SELECT 
            id.id, 
            id.invoice_detail_code, 
            id.usage_or_quantity, 
            id.total_price, 
            id.created_at,
            id.type_invoice_detail,
            id.is_status,
            id.id_invoice,
            pd.id AS id_product_detail,
            pd.product_detail_code,
            pd.product_detail_name,
            pd.price AS product_price,
            ps.service_code,
            ps.service_name,
            ps.price_service AS service_price,
            pet.pet_name
        FROM invoice_details id
        LEFT JOIN invoices iv ON id.id_invoice = iv.id
        LEFT JOIN product_details pd ON id.id_product_detail = pd.id
        LEFT JOIN service_details ps ON id.id_service_detail = ps.id
        LEFT JOIN pets pet ON id.id_pet = pet.id
        WHERE id.id_invoice = ? AND id.is_deleted = 0;
    """;

        List<InvoiceDetails> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapInvoiceDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public InvoiceDetails mapInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetails i = new InvoiceDetails();
        i.setId(rs.getInt("id"));
        i.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
        i.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
        i.setTotalPrice(rs.getBigDecimal("total_price"));
        i.setCreatedAt(rs.getDate("created_at"));
        i.setStatus(rs.getBoolean("is_status"));
        i.setTypeInvoiceDetail(rs.getBoolean("type_invoice_detail"));

        // Set Invoice
        Invoices ic = new Invoices();
        ic.setId(rs.getInt("id_invoice"));
        i.setInvoice(ic);

        // Set ProductDetails (nếu có)
        ProductDetails p = new ProductDetails();
        if (hasColumn(rs, "id_product_detail")) {
            p.setId(rs.getInt("id_product_detail"));
        }
        if (hasColumn(rs, "product_detail_code")) {
            p.setProductDetailCode(rs.getString("product_detail_code"));
        }
        if (hasColumn(rs, "product_detail_name")) {
            p.setProductDetailName(rs.getString("product_detail_name"));
        }
        if (hasColumn(rs, "product_price")) {
            p.setPrice(rs.getBigDecimal("product_price"));
        }
        i.setProductDetail(p);

        // Set PetServices (nếu có)
        PetServices ps = new PetServices();
        if (hasColumn(rs, "service_code")) {
            ps.setServiceCode(rs.getString("service_code"));
        }
        if (hasColumn(rs, "service_name")) {
            ps.setServiceName(rs.getString("service_name"));
        }
        if (hasColumn(rs, "service_price")) {
            ps.setPriceService(rs.getBigDecimal("service_price"));
        }
        i.setPetService(ps);

        // Set Pets (nếu có)
        Pets pet = new Pets();
        if (hasColumn(rs, "pet_name")) {
            pet.setPetName(rs.getString("pet_name"));
        }
        i.setPet(pet);

        return i;
    }

// Kiểm tra cột có tồn tại trong ResultSet không (tránh lỗi khi truy xuất dữ liệu không có)
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
