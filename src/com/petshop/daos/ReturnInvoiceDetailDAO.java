/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.ProductDetails;
import com.petshop.models.ReturnInvoiceDetail;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class ReturnInvoiceDetailDAO {

    private Connection conn;

    public ReturnInvoiceDetailDAO() {
        conn = DBConnect.getConnection();
    }

    public ReturnInvoiceDetail getReturnInvoiceDetailByProductDetailId(int invoiceId, int productDetailId) {
        String sql = """
        SELECT id, return_invoice_detail_code, id_return_invoice, id_product_detail, 
               usage_or_quantity, total_price, type_invoice_detail, created_at, is_deleted
        FROM return_invoice_details
        WHERE id_product_detail = ? AND id_return_invoice = ? AND is_deleted = 0
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productDetailId);
            ps.setInt(2, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Nếu tìm thấy, trả về object ReturnInvoiceDetail
                    ReturnInvoiceDetail detail = new ReturnInvoiceDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setReturnInvoiceDetailCode(rs.getString("return_invoice_detail_code"));
                    detail.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
                    detail.setTotalPrice(rs.getBigDecimal("total_price"));
                    detail.setTypeInvoiceDetail(rs.getBoolean("type_invoice_detail"));
                    detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    detail.setDeleted(rs.getBoolean("is_deleted"));
                    return detail;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy, trả về null
    }

    public boolean updateUsageOrQuantityAndTprice(int id, int newQuantity, BigDecimal totalPrice) {
        String sql = "UPDATE return_invoice_details SET usage_or_quantity = ?, total_price = ? WHERE id = ?";
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
//

    public boolean isValidProductQuantityOfRinvoiceD(int idProduct, int quantity) {
        String sql = """
        SELECT pd.quantity_in_stock 
        FROM product_details pd
        WHERE pd.id = ?;
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduct);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int stockQuantity = rs.getInt("quantity_in_stock");
                    return quantity <= stockQuantity; // Kiểm tra số lượng nhập vào có lớn hơn số lượng tồn kho không
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy dữ liệu
    }

//fix
    public boolean isValidReturnQuantity(int idReturnInvoiceDetail, int idInvoiceDetail) {
        String sql = """
        SELECT rid.usage_or_quantity AS return_quantity, 
               id.usage_or_quantity AS invoice_quantity
        FROM return_invoice_details rid
        JOIN invoice_details id ON rid.id_invoice_detail = id.id
        WHERE rid.id = ? AND id.id = ?;
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReturnInvoiceDetail);
            ps.setInt(2, idInvoiceDetail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int returnQuantity = rs.getInt("return_quantity");
                    int invoiceQuantity = rs.getInt("invoice_quantity");

                    if (returnQuantity > invoiceQuantity) {
                        System.err.println("Lỗi: Số lượng trả hàng không thể vượt quá số lượng mua!");
                        return false;
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
        }
        return false; // Trả về false nếu không tìm thấy dữ liệu
    }

    public List<ReturnInvoiceDetail> getReturnInvoiceDetailsByReturnInvoiceId(int returnInvoiceId) {
        String sql = """
        SELECT rid.id, rid.return_invoice_detail_code, rid.id_return_invoice, 
               rid.id_product_detail, rid.usage_or_quantity, rid.total_price, 
               rid.type_invoice_detail, rid.created_at, rid.is_deleted,
               pd.product_detail_code, pd.product_detail_name, pd.price
        FROM return_invoice_details rid
        JOIN product_details pd ON rid.id_product_detail = pd.id
        WHERE rid.id_return_invoice = ? AND rid.is_deleted = 0
        ORDER BY rid.created_at DESC
    """;

        List<ReturnInvoiceDetail> details = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, returnInvoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { // Lặp qua từng dòng kết quả
                    ReturnInvoiceDetail detail = new ReturnInvoiceDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setReturnInvoiceDetailCode(rs.getString("return_invoice_detail_code"));
                    detail.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
                    detail.setTotalPrice(rs.getBigDecimal("total_price"));
                    detail.setTypeInvoiceDetail(rs.getBoolean("type_invoice_detail"));
                    detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    detail.setDeleted(rs.getBoolean("is_deleted"));

                    // Tạo đối tượng ProductDetails và ánh xạ dữ liệu
                    ProductDetails productDetail = new ProductDetails();
                    productDetail.setId(rs.getInt("id_product_detail"));
                    productDetail.setProductDetailCode(rs.getString("product_detail_code"));
                    productDetail.setProductDetailName(rs.getString("product_detail_name"));
                    productDetail.setPrice(rs.getBigDecimal("price"));

                    detail.setProductDetails(productDetail); // Gán vào ReturnInvoiceDetail

                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details; // Trả về danh sách ReturnInvoiceDetail
    }

    public boolean insertReturnInvoiceDetailProduct(ReturnInvoiceDetail returnInvoiceDetail) {
        String sql = """
        INSERT INTO return_invoice_details(return_invoice_detail_code, id_return_invoice, usage_or_quantity, total_price, 
                                     id_product_detail, is_deleted,type_invoice_detail,id_invoice_detail)
        VALUES ( ?, ?, ?, ?, ?, 0,?,?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, returnInvoiceDetail.getReturnInvoiceDetailCode());
            ps.setInt(2, returnInvoiceDetail.getReturnsInvoices().getId()); // Đảm bảo invoice không null
            ps.setInt(3, returnInvoiceDetail.getUsageOrQuantity());
            ps.setBigDecimal(4, returnInvoiceDetail.getTotalPrice());

            // Kiểm tra null trước khi set giá trị
            if (returnInvoiceDetail.getProductDetails() != null) {
                ps.setInt(5, returnInvoiceDetail.getProductDetails().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setBoolean(6, returnInvoiceDetail.isTypeInvoiceDetail());
//            ps.setInt(7, returnInvoiceDetail.getInvoiceDetails().getId());
            if (returnInvoiceDetail.getInvoiceDetails() != null) {
                ps.setInt(7, returnInvoiceDetail.getInvoiceDetails().getId());
            } else {
                ps.setNull(7, Types.INTEGER); // Nếu không có hóa đơn chi tiết, đặt giá trị NULL
            }

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDeletedRInvoiceD(int id) {
        String sql = "UPDATE return_invoice_details SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalReturnAmountByInvoiceId(int invoiceId) {
        String sql = """
        SELECT COALESCE(SUM(rid.usage_or_quantity * pd.price), 0) 
        FROM return_invoice_details rid
        JOIN product_details pd ON rid.id_product_detail = pd.id
        WHERE rid.id_return_invoice = ? 
              AND rid.type_invoice_detail = 1 
              AND rid.is_deleted = 0;
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalExchangeAmountByInvoiceId(int invoiceId) {
        String sql = """
        SELECT COALESCE(SUM(rid.usage_or_quantity * pd.price), 0) 
        FROM return_invoice_details rid
        JOIN product_details pd ON rid.id_product_detail = pd.id
        WHERE rid.id_return_invoice = ? 
              AND rid.type_invoice_detail = 0 
              AND rid.is_deleted = 0;
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
