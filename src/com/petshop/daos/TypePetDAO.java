/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.TypePets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class TypePetDAO {

    private Connection conn = null;

    public TypePetDAO() {
        conn = DBConnect.getConnection();
    }

    public List<TypePets> getList() {
        String sql = "SELECT id, type_pet_code, type_pet_name, created_at, is_deleted, is_status FROM type_pets WHERE is_deleted = 0 AND is_status = 1";

        List<TypePets> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TypePets t = new TypePets();
                t.setId(rs.getInt("id"));
                t.setTypePetCode(rs.getString("type_pet_code"));
                t.setTypePetName(rs.getString("type_pet_name"));
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

    public boolean insertTypePet(TypePets t) {
        String sql = "INSERT INTO type_pets(type_pet_code,type_pet_name,is_deleted,is_status) VALUES (?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTypePetCode());
            ps.setString(2, t.getTypePetName());
            ps.setBoolean(3, t.isDeleted());
            ps.setBoolean(4, t.isStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public TypePets getTypePetByName(String typePetName) {
        String sql = "SELECT id, type_pet_code, type_pet_name, created_at, is_deleted, is_status FROM type_pets WHERE type_pet_name = ? AND is_deleted = 0 AND is_status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typePetName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TypePets t = new TypePets();
                    t.setId(rs.getInt("id"));
                    t.setTypePetCode(rs.getString("type_pet_code"));
                    t.setTypePetName(rs.getString("type_pet_name"));
                    t.setCreatedAt(rs.getDate("created_at"));
                    t.setDeleted(rs.getBoolean("is_deleted"));
                    t.setStatus(rs.getBoolean("is_status"));
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }

    public boolean deleteTypePet(int id) {
        String sql = "UPDATE type_pets SET is_deleted = 1 WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTypePetNameExists(String typePetName) {
        String sql = "SELECT COUNT(*) FROM type_pets WHERE type_pet_name = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typePetName);
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
