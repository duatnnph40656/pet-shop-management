/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.TypeService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class TypeServiceDAO {

    private Connection conn = null;

    public TypeServiceDAO() {
        conn = DBConnect.getConnection();
    }

    // Lấy danh sách loại dịch vụ
    public List<TypeService> getListTypeS() {
        String sql = "SELECT * FROM type_services WHERE is_deleted = 0 AND is_status = 1";
        List<TypeService> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TypeService t = new TypeService();
                t.setId(rs.getInt("id"));
                t.setTypeServiceCode(rs.getString("type_service_code"));
                t.setTypeServiceName(rs.getString("type_service_name"));
                t.setCreatedAt(rs.getDate("created_at"));
                t.setDeleted(rs.getBoolean("is_deleted"));
                t.setStatus(rs.getBoolean("is_status"));
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm mới loại dịch vụ
    public boolean insertTypeService(TypeService typeService) {
        String sql = "INSERT INTO type_services (type_service_code, type_service_name, is_deleted, is_status) VALUES (?, ?, 0, 1)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeService.getTypeServiceCode());
            ps.setString(2, typeService.getTypeServiceName());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin loại dịch vụ
    public boolean updateTypeService(TypeService typeService) {
        String sql = "UPDATE type_services SET type_service_code = ?, type_service_name = ?, is_status = ? WHERE id = ? AND is_deleted = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeService.getTypeServiceCode());
            ps.setString(2, typeService.getTypeServiceName());
            ps.setBoolean(3, typeService.isStatus());
            ps.setInt(4, typeService.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa mềm loại dịch vụ (đánh dấu is_deleted = 1)
    public boolean deleteTypeService(int id) {
        String sql = "UPDATE type_services SET is_deleted = 1 WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isTypeServiceNameExists(String typeServiceName) {
    String sql = "SELECT COUNT(*) FROM type_services WHERE type_service_name = ? AND is_deleted = 0";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, typeServiceName);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0, tức là tên đã tồn tại
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

}
