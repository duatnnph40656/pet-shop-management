/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.CategoryProducts;
import com.petshop.models.Products;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author duat
 */
public class ProductDAO {
    
    private Connection conn = null;
    
    public ProductDAO() {
        conn = DBConnect.getConnection();
    }
    
    public List<Products> getListProduct() {
        String sql = "SELECT \n"
                + "    p.id,\n"
                + "    p.product_code,\n"
                + "    p.product_name,\n"
                + "    c.category_name,\n"
                + "    p.price_base,\n"
                + "    p.created_at,\n"
                + "    p.is_deleted,\n"
                + "    p.is_status\n"
                + "FROM \n"
                + "    [products] p\n"
                + "JOIN \n"
                + "    [category_products] c\n"
                + "ON \n"
                + "    p.id_category = c.id\n"
                + "WHERE \n"
                + "    p.is_deleted = 0;";
        
        List<Products> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public List<Products> getListProductDeleted() {
        String sql = "SELECT \n"
                + "    p.id,\n"
                + "    p.product_code,\n"
                + "    p.product_name,\n"
                + "    c.category_name,\n"
                + "    p.price_base,\n"
                + "    p.created_at,\n"
                + "    p.is_deleted,\n"
                + "    p.is_status\n"
                + "FROM \n"
                + "    [products] p\n"
                + "JOIN \n"
                + "    [category_products] c\n"
                + "ON \n"
                + "    p.id_category = c.id\n"
                + "WHERE \n"
                + "    p.is_deleted = 1;";
        
        List<Products> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public boolean addProduct(Products p) {
        String sql = "INSERT INTO products (product_code, product_name, id_category, price_base, is_deleted ,is_status) VALUES (?,?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Thiết lập các giá trị cho câu lệnh SQL
            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getProductName());
            ps.setInt(3, p.getCategoryProduct().getId());
            ps.setBigDecimal(4, p.getPriceBase());
            ps.setBoolean(5, p.isDeleted());
            ps.setBoolean(6, p.isStatus());

//            // Thực thi câu lệnh và kiểm tra kết quả
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thêm thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }
    
    public boolean updateProduct(int id, Products p) {
        String sql = "UPDATE products SET product_code = ?, product_name = ?, price_base = ?,id_category = ?, is_status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho các tham số
            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getProductName());
            ps.setBigDecimal(3, p.getPriceBase());
            ps.setInt(4, p.getCategoryProduct().getId());
            ps.setBoolean(5, p.isStatus());
            ps.setInt(6, id); // Tham số id của sản phẩm cần cập nhật
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteProduct(int id) {
        String sql = "UPDATE products SET is_deleted = 1, is_status = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }
    
    public boolean restoreProduct(int id) {
        String sql = "UPDATE products SET is_deleted = 0, is_status = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }
    
    public boolean updateStatusProduct(int id, boolean status) {
        String sql = "UPDATE products SET is_status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setBoolean(1, status);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Products> searchProduct(String keyword) {
        String sql = "SELECT "
                + "    p.id, "
                + "    p.product_code, "
                + "    p.product_name, "
                + "    c.category_name, "
                + "    p.price_base, "
                + "    p.created_at, "
                + "    p.is_deleted, "
                + "    p.is_status "
                + "FROM "
                + "    [products] p "
                + "JOIN "
                + "    [category_products] c "
                + "ON "
                + "    p.id_category = c.id "
                + "WHERE "
                + "    p.is_deleted = 0 "
                + "    AND (p.product_name LIKE ? OR p.product_code LIKE ?)";
        List<Products> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Products> selectProductByCategoryId(int categoryId) {
        String sql = "SELECT p.*, c.category_name " // Lấy cả category_name
                + "FROM products p "
                + "JOIN category_products c ON p.id_category = c.id " // JOIN bảng categories
                + "WHERE p.id_category = ? AND p.is_deleted = 0";
        
        List<Products> products = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId); // Gán tham số categoryId
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products();
                    product.setId(rs.getInt("id"));
                    product.setProductCode(rs.getString("product_code"));
                    product.setProductName(rs.getString("product_name"));

                    // Gán đối tượng CategoryProduct
                    CategoryProducts category = new CategoryProducts();
                    category.setId(rs.getInt("id_category"));
                    category.setCategoryProductName(rs.getString("category_name")); // Gán typeProductName
                    product.setCategoryProduct(category);
                    
                    product.setPriceBase(rs.getBigDecimal("price_base"));
                    product.setCreatedAt(rs.getDate("created_at"));
                    product.setDeleted(rs.getBoolean("is_deleted"));
                    product.setStatus(rs.getBoolean("is_status"));
                    
                    products.add(product); // Thêm sản phẩm vào danh sách
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
    
    private Products mapProduct(ResultSet rs) throws SQLException {
        Products product = new Products();
        product.setId(rs.getInt("id"));
        product.setProductCode(rs.getString("product_code"));
        product.setProductName(rs.getString("product_name"));
        product.setPriceBase(rs.getBigDecimal("price_base"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setDeleted(rs.getBoolean("is_deleted"));
        product.setStatus(rs.getBoolean("is_status"));
        
        CategoryProducts categoryProduct = new CategoryProducts();
        categoryProduct.setCategoryProductName(rs.getString("category_name"));
        
        product.setCategoryProduct(categoryProduct);
        return product;
    }
}
