/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Product;
import com.petshop.models.ProductDetail;
import com.petshop.models.TypePet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class ProductDetailDAO {

    private Connection conn = null;

    public ProductDetailDAO() {
        conn = DBConnect.getConnection();
    }

    public List<ProductDetail> getListProductDetail() {
        String sql = "SELECT\n"
                + "    pd.id,\n"
                + "    p.product_name, \n"
                + "    pd.product_detail_code,\n"
                + "    pd.product_detail_name,\n"
                + "    t.type_pet_name,   \n"
                + "    pd.expirydate,\n"
                + "    pd.weight,\n"
                + "    pd.quantity_in_stock,\n"
                + "    pd.flavor,\n"
                + "    pd.describe,\n"
                + "    pd.price,\n"
                + "    pd.image_path,\n"
                + "    pd.created_at,\n"
                + "    pd.is_deleted,\n"
                + "    pd.is_status,\n"
                + "    pd.id_product,\n"
                + "    pd.bar_code,\n" // Thêm dấu phẩy ở đây
                + "    pd.production_date\n"
                + "FROM product_details pd\n"
                + "JOIN products p ON pd.id_product = p.id    \n"
                + "JOIN type_pets t ON pd.id_type_pet = t.id    \n"
                + "WHERE pd.is_deleted = 0 \n"
                + "AND pd.is_status = 1;";

        List<ProductDetail> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductDetail proD = new ProductDetail();
                proD.setId(rs.getInt("id"));
                proD.setProductDetailCode(rs.getString("product_detail_code"));
                proD.setProductDetailName(rs.getString("product_detail_name"));
                proD.setBarCode(rs.getString("bar_code"));

                // Set Product
                Product product = new Product();
                product.setProductName(rs.getString("product_name"));
                proD.setProduct(product);

                // Set TypePet
                TypePet typePet = new TypePet();
                typePet.setTypePetName(rs.getString("type_pet_name"));
                proD.setTypePet(typePet);

                // Nếu `expirydate` là kiểu DATE trong DB
                proD.setExpirydate(rs.getInt("expirydate"));
                proD.setProductionDate(rs.getDate("production_date"));

                proD.setWeight(rs.getBigDecimal("weight"));
                proD.setQuantityInStock(rs.getInt("quantity_in_stock"));
                proD.setFlavor(rs.getString("flavor"));
                proD.setDescription(rs.getString("describe"));
                proD.setPrice(rs.getBigDecimal("price"));
                proD.setImagePath(rs.getString("image_path"));
                proD.setCreatedAt(rs.getDate("created_at"));
                proD.setDeleted(rs.getBoolean("is_deleted"));
                proD.setStatus(rs.getBoolean("is_status"));

                list.add(proD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addProductDetail(ProductDetail productDetail) {
        String sql = "INSERT INTO product_details ("
                + "product_detail_code, product_detail_name, bar_code, id_product, id_type_pet, "
                + "production_date,expirydate, weight, quantity_in_stock, flavor, describe, "
                + "price, image_path, is_deleted, is_status) "
                + "VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productDetail.getProductDetailCode());
            ps.setString(2, productDetail.getProductDetailName());
            ps.setString(3, productDetail.getBarCode());
            ps.setInt(4, productDetail.getProduct().getId());
            ps.setInt(5, productDetail.getTypePet().getId());
            ps.setDate(6, new java.sql.Date(productDetail.getProductionDate().getTime()));
            ps.setInt(7, productDetail.getExpirydate());
            ps.setBigDecimal(8, productDetail.getWeight());
            ps.setInt(9, productDetail.getQuantityInStock());
            ps.setString(10, productDetail.getFlavor());
            ps.setString(11, productDetail.getDescription());
            ps.setBigDecimal(12, productDetail.getPrice());
            ps.setString(13, productDetail.getImagePath());
            ps.setBoolean(14, false); // Mặc định chưa bị xóa
            ps.setBoolean(15, true);  // Mặc định đang hoạt động

            return ps.executeUpdate() > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProductDetail(int id){
        String sql = "UPDATE product_details SET is_deleted = 1 WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
