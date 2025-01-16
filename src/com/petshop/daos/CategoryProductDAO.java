/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.CategoryProduct;
import com.petshop.models.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class CategoryProductDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql = "";

    public CategoryProductDAO() {
        conn = DBConnect.getConnection();
    }

    public List<CategoryProduct> getListCategoryProduct() {
        sql = "	SELECT \n"
                + "    c.id,\n"
                + "    c.category_code,\n"
                + "    c.category_name,\n"
                + "    c.created_at,\n"
                + "    c.is_deleted,\n"
                + "    c.is_status\n"
                + "FROM \n"
                + "    [categories] c\n"
                + "WHERE \n"
                + "    c.is_deleted = 0 AND c.is_status = 1";
        List<CategoryProduct> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoryProduct c = new CategoryProduct();
                c.setId(rs.getInt("id"));
                c.setCategoryProductCode(rs.getString("category_code"));
                c.setCategoryProductName(rs.getString("category_name"));
                c.setCreatedAt(rs.getDate("created_at"));
                c.setDeleted(rs.getBoolean("is_deleted"));
                c.setStatus(rs.getBoolean("is_status"));
                list.add(c);
            }
            return list;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }

    public boolean addCategoryProduct(CategoryProduct c) {
        sql = "INSERT INTO categories(category_code, category_name, is_deleted, is_status) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCategoryProductCode());
            ps.setString(2, c.getCategoryProductName());
            ps.setBoolean(3, c.isDeleted());
            ps.setBoolean(4, c.isStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    
}
