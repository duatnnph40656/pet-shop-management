/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.CategoryProduct;
import com.petshop.models.Product;
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

    public List<Product> getListProduct() {
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
                + "    [categories] c\n"
                + "ON \n"
                + "    p.id_category = c.id\n"
                + "WHERE \n"
                + "    p.is_deleted = 0 AND p.is_status = 1;";

        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setProductCode(rs.getString("product_code"));
                product.setProductName(rs.getString("product_name"));
                product.setPriceBase(rs.getBigDecimal("price_base"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setDeleted(rs.getBoolean("is_deleted"));
                product.setStatus(rs.getBoolean("is_status"));

                CategoryProduct categoryProduct = new CategoryProduct();
                categoryProduct.setCategoryProductName(rs.getString("category_name"));

                product.setCategoryProduct(categoryProduct);

                list.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addProduct(Product p) {
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

    public boolean updateProduct(int id, Product p) {
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
        String sql = "UPDATE products SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }

    public List<Product> searchProduct(String keyword) {
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
                + "    [categories] c\n"
                + "ON \n"
                + "    p.id_category = c.id\n"
                + "WHERE \n"
                + "    p.is_deleted = 0 \n"
                + "    AND p.is_status = 1\n"
                + "    AND p.product_name LIKE ?;";
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Product Code: " + rs.getString("product_code"));
                System.out.println("Product Name: " + rs.getString("product_name"));
                System.out.println("Category Name: " + rs.getString("category_name"));
                System.out.println("Price Base: " + rs.getBigDecimal("price_base"));
                System.out.println("Created At: " + rs.getTimestamp("created_at"));
                System.out.println("Is Deleted: " + rs.getBoolean("is_deleted"));
                System.out.println("Is Status: " + rs.getBoolean("is_status"));
                System.out.println("-----------------------------");
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> selectProductByCategoryId(int categoryId) {
        String sql = "SELECT * FROM products "
                + "WHERE id_category = ? AND is_status = 1 AND is_deleted = 0";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId); // Gán tham số categoryId
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setProductCode(rs.getString("product_code"));
                    product.setProductName(rs.getString("product_name"));

                    // Gán đối tượng CategoryProduct
                    CategoryProduct category = new CategoryProduct();
                    category.setId(rs.getInt("id_category"));
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
}
