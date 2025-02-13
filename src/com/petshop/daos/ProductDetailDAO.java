/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Products;
import com.petshop.models.ProductDetails;
import com.petshop.models.TypePets;
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

    public List<ProductDetails> getListProductDetail() {
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
                + "WHERE pd.is_deleted = 0;";

        List<ProductDetails> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
//                ProductDetail proD = new ProductDetail();
//                proD.setId(rs.getInt("id"));
//                proD.setProductDetailCode(rs.getString("product_detail_code"));
//                proD.setProductDetailName(rs.getString("product_detail_name"));
//                proD.setBarCode(rs.getString("bar_code"));
//
//                // Set Product
//                Product product = new Product();
//                product.setProductName(rs.getString("product_name"));
//                proD.setProduct(product);
//
//                // Set TypePet
//                TypePet typePet = new TypePet();
//                typePet.setTypePetName(rs.getString("type_pet_name"));
//                proD.setTypePet(typePet);
//
//                // Nếu `expirydate` là kiểu DATE trong DB
//                proD.setExpirydate(rs.getInt("expirydate"));
//                proD.setProductionDate(rs.getDate("production_date"));
//
//                proD.setWeight(rs.getBigDecimal("weight"));
//                proD.setQuantityInStock(rs.getInt("quantity_in_stock"));
//                proD.setFlavor(rs.getString("flavor"));
//                proD.setDescription(rs.getString("describe"));
//                proD.setPrice(rs.getBigDecimal("price"));
//                proD.setImagePath(rs.getString("image_path"));
//                proD.setCreatedAt(rs.getDate("created_at"));
//                proD.setDeleted(rs.getBoolean("is_deleted"));
//                proD.setStatus(rs.getBoolean("is_status"));

                list.add(mapResultSetToProductDetail(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addProductDetail(ProductDetails productDetail) {
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
            ps.setDate(6, productDetail.getProductionDate());
            ps.setInt(7, productDetail.getExpirydate());
            ps.setBigDecimal(8, productDetail.getWeight());
            ps.setInt(9, productDetail.getQuantityInStock());
            ps.setString(10, productDetail.getFlavor());
            ps.setString(11, productDetail.getDescription());
            ps.setBigDecimal(12, productDetail.getPrice());
            ps.setString(13, productDetail.getImagePath());
            ps.setBoolean(14, false); // Mặc định chưa bị xóa
            ps.setBoolean(15, productDetail.isStatus());  // Mặc định đang hoạt động

            return ps.executeUpdate() > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProductDetail(int id) {
        String sql = "UPDATE product_details SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProductDetail(int id, ProductDetails productDetail) {
        String sql = "UPDATE product_details SET "
                + "product_detail_name = ?, quantity_in_stock = ?, "
                + "flavor = ?, describe = ?, image_path = ?, "
                + "id_type_pet = ?, price = ?, "
                + "expirydate = ?, production_date = ?, weight = ?, "
                + "id_product = ? ,is_status = ?"
                + "WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productDetail.getProductDetailName());
            ps.setInt(2, productDetail.getQuantityInStock());
            ps.setString(3, productDetail.getFlavor());
            ps.setString(4, productDetail.getDescription());
            ps.setString(5, productDetail.getImagePath());
            ps.setInt(6, productDetail.getTypePet().getId());
            ps.setBigDecimal(7, productDetail.getPrice());
            ps.setInt(8, productDetail.getExpirydate());
            ps.setDate(9, productDetail.getProductionDate()); // Cập nhật ngày sản xuất
            ps.setBigDecimal(10, productDetail.getWeight());
            ps.setInt(11, productDetail.getProduct().getId());
            ps.setBoolean(12, productDetail.isStatus());// Cập nhật id sản phẩm
            ps.setInt(13, id); // Điều kiện WHERE

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductDetails> searchByNameOrFlavor(String keyword) {
        String sql = "SELECT pd.id, p.product_name, pd.product_detail_code, pd.product_detail_name, "
                + "t.type_pet_name, pd.expirydate, pd.weight, pd.quantity_in_stock, pd.flavor, "
                + "pd.describe, pd.price, pd.image_path, pd.created_at, pd.is_deleted, pd.is_status, "
                + "pd.id_product, pd.bar_code, pd.production_date "
                + "FROM product_details pd "
                + "JOIN products p ON pd.id_product = p.id "
                + "JOIN type_pets t ON pd.id_type_pet = t.id "
                + "WHERE (pd.product_detail_name LIKE ? OR pd.flavor LIKE ?) "
                + "AND pd.is_deleted = 0 AND pd.is_status = 1";

        List<ProductDetails> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProductDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductDetails> searchByBarCode(String barCode) {
        List<ProductDetails> list = new ArrayList<>();
        String sql = "SELECT pd.id, p.product_name, pd.product_detail_code, pd.product_detail_name, "
                + "t.type_pet_name, pd.expirydate, pd.weight, pd.quantity_in_stock, pd.flavor, "
                + "pd.describe, pd.price, pd.image_path, pd.created_at, pd.is_deleted, pd.is_status, "
                + "pd.id_product, pd.bar_code, pd.production_date "
                + "FROM product_details pd "
                + "JOIN products p ON pd.id_product = p.id "
                + "JOIN type_pets t ON pd.id_type_pet = t.id "
                + "WHERE pd.bar_code = ? AND pd.is_deleted = 0 AND pd.is_status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, barCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProductDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductDetails> searchByProductId(int productId) {
        String sql = "SELECT pd.id, p.product_name, pd.product_detail_code, pd.product_detail_name, "
                + "t.type_pet_name, pd.expirydate, pd.weight, pd.quantity_in_stock, pd.flavor, "
                + "pd.describe, pd.price, pd.image_path, pd.created_at, pd.is_deleted, pd.is_status, "
                + "pd.id_product, pd.bar_code, pd.production_date "
                + "FROM product_details pd "
                + "JOIN products p ON pd.id_product = p.id "
                + "JOIN type_pets t ON pd.id_type_pet = t.id "
                + "WHERE pd.id_product = ? AND pd.is_deleted = 0";

        List<ProductDetails> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProductDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductDetails> findByTypePetId(int typePetId) {
        List<ProductDetails> list = new ArrayList<>();
        String sql = "SELECT "
                + "    pd.id, "
                + "    p.product_name, "
                + "    pd.product_detail_code, "
                + "    pd.product_detail_name, "
                + "    t.type_pet_name, "
                + "    pd.expirydate, "
                + "    pd.weight, "
                + "    pd.quantity_in_stock, "
                + "    pd.flavor, "
                + "    pd.describe, "
                + "    pd.price, "
                + "    pd.image_path, "
                + "    pd.created_at, "
                + "    pd.is_deleted, "
                + "    pd.is_status, "
                + "    pd.id_product, "
                + "    pd.bar_code, "
                + "    pd.production_date "
                + "FROM product_details pd "
                + "JOIN products p ON pd.id_product = p.id "
                + "JOIN type_pets t ON pd.id_type_pet = t.id "
                + "WHERE pd.id_type_pet = ? "
                + "AND pd.is_deleted = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typePetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProductDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ProductDetails> searchProductDetails(Integer productId, Integer typePetId) {
        List<ProductDetails> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT pd.id, p.product_name, pd.product_detail_code, pd.product_detail_name, "
                + "t.type_pet_name, pd.expirydate, pd.weight, pd.quantity_in_stock, pd.flavor, "
                + "pd.describe, pd.price, pd.image_path, pd.created_at, pd.is_deleted, pd.is_status, "
                + "pd.id_product, pd.bar_code, pd.production_date "
                + "FROM product_details pd "
                + "JOIN products p ON pd.id_product = p.id "
                + "JOIN type_pets t ON pd.id_type_pet = t.id "
                + "WHERE pd.is_deleted = 0 ");

        // Thêm điều kiện nếu có productId
        if (productId != null && productId > 0) {
            sql.append(" AND pd.id_product = ? ");
        }

        // Thêm điều kiện nếu có typePetId
        if (typePetId != null && typePetId > 0) {
            sql.append(" AND pd.id_type_pet = ? ");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;

            // Gán tham số nếu có
            if (productId != null && productId > 0) {
                ps.setInt(index++, productId);
            }
            if (typePetId != null && typePetId > 0) {
                ps.setInt(index++, typePetId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProductDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatusProductDetail(boolean status, int id) {
        String sql = "UPDATE product_details SET is_status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ProductDetails mapResultSetToProductDetail(ResultSet rs) throws SQLException {
        ProductDetails proD = new ProductDetails();
        proD.setId(rs.getInt("id"));
        proD.setProductDetailCode(rs.getString("product_detail_code"));
        proD.setProductDetailName(rs.getString("product_detail_name"));
        proD.setBarCode(rs.getString("bar_code"));

        // Set Product
        Products product = new Products();
        product.setProductName(rs.getString("product_name"));
        proD.setProduct(product);

        // Set TypePet
        TypePets typePet = new TypePets();
        typePet.setTypePetName(rs.getString("type_pet_name"));
        proD.setTypePet(typePet);

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

        return proD;
    }

}
