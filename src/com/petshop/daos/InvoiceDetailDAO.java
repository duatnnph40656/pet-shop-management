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

    public List<InvoiceDetails> getListInvoiceDetailProduct() {
    String sql = "SELECT \n"
            + "       id.id, \n"
            + "       id.invoice_detail_code, \n"
            + "       COALESCE(pd.product_detail_name, sd.service_name) AS product_or_service_name, \n"
            + "       COALESCE(pd.product_detail_code, sd.service_code) AS product_or_service_code, \n"
            + "       id.id_pet, \n"
            + "       id.usage_or_quantity, \n"
            + "       id.price, \n"
            + "       (id.usage_or_quantity * id.price) AS total_price, \n"
            + "       id.duration, \n"
            + "       id.created_at, \n"
            + "       iv.invoice_code \n"  
            + "FROM invoice_details id \n"
            + "LEFT JOIN invoices iv ON id.id_invoice = iv.id \n"  
            + "LEFT JOIN product_details pd ON id.id_product_detail = pd.id \n"
            + "LEFT JOIN service_details sd ON id.id_service_detail = sd.id \n"
            + "LEFT JOIN pets p ON id.id_pet = p.id \n"
            + "WHERE id.is_status = 1;";

    List<InvoiceDetails> list = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(mapInvoiceDetail(rs)); // Ánh xạ dữ liệu vào danh sách hóa đơn chi tiết
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


    public boolean insertInvoiceDetail(InvoiceDetails invoiceDetail) {
        String sql = """
        INSERT INTO invoice_details (invoice_detail_code, invoice_id, usage_or_quantity, price, 
                                     product_detail_id, service_id, pet_id,is_status)
        VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try ( PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public InvoiceDetails mapInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetails i = new InvoiceDetails();

        // Ánh xạ dữ liệu từ ResultSet vào InvoiceDetails
        i.setId(rs.getInt("id"));
        i.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
        i.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
        i.setPrice(rs.getBigDecimal("price"));
        i.setTotal(rs.getBigDecimal("total_price")); // Thành tiền

        // Lấy mã hóa đơn từ bảng invoices
        Invoices ic = new Invoices();
        ic.setInvoiceCode(rs.getString("invoice_code"));
        i.setInvoice(ic);

        // Lấy tên sản phẩm hoặc dịch vụ
        i.setProductOrServiceName(rs.getString("product_or_service_name"));

        // Lấy mã sản phẩm hoặc mã dịch vụ
        i.setProductOrServiceCode(rs.getString("product_or_service_code"));

        // Lấy thông tin thú cưng (nếu có)
        Pets pet = new Pets();
        pet.setPetCode(rs.getString("id_pet"));  // Hiển thị mã thú cưng, không phải ID
        i.setPet(pet);

        i.setDuration(rs.getString("duration"));
        i.setCreatedAt(rs.getDate("created_at"));

        return i;
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
    public List<InvoiceDetails> getInvoiceDetailsByInvoiceId(int invoiceId) {
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

}
