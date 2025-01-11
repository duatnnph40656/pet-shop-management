/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.CategoryProduct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dut
 */
public class CategoryProductDAO {

    private Connection conn;
    private String sql = null;

    public CategoryProductDAO() {
        conn = DBConnect.getConnection();
    }

    public List<CategoryProduct> findAll() {
        List<CategoryProduct> list = new ArrayList<>();
        sql = "SELECT * FROM category_products WHERE is_deleted = 0;";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Create a new CategoryProduct with the retrieved fields
                CategoryProduct cProduct = new CategoryProduct(
                        rs.getString("category_product_code"),
                        rs.getString("category_product_name"),
                        rs.getDate("created_at"),
                        rs.getBoolean("is_status")
                );
                // Set additional fields not covered by the constructor
                list.add(cProduct);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return list;
    }

    ;
    
     public boolean insert(CategoryProduct cProduct) {
        sql = "INSERT INTO category_products(category_product_code, category_product_name) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Set các giá trị cho câu lệnh INSERT
            ps.setString(1, cProduct.getCategoryProductCode());
            ps.setString(2, cProduct.getCategoryProductName());
            // Thực thi câu lệnh và kiểm tra xem có thành công không
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi nếu có
            return false; // Trả về false nếu có lỗi
        }
    }
}
