/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.TypePet;
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

    public List<TypePet> getList() {
        String sql = "SELECT id, type_pet_code, type_pet_name, created_at, is_deleted, is_status FROM type_pets WHERE is_deleted = 0 AND is_status = 1";

        List<TypePet> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TypePet t = new TypePet();
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
    
    public boolean insertTypePet(TypePet t){
        String sql = "INSERT INTO type_pets(type_pet_code,type_pet_name,is_deleted,is_status) VALUES (?,?,?,?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)){
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
    
    public boolean deleteTypePet(int id){
        String sql = "UPDATE type_pets SET is_deleted = 1 WHERE id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
