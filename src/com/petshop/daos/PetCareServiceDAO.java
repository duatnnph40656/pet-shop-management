/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.PetCareServices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class PetCareServiceDAO {

    private Connection conn;

    public PetCareServiceDAO() {
        conn = DBConnect.getConnection();
    }

    public List<PetCareServices> getListPetCareService() {
    List<PetCareServices> list = new ArrayList<>();
    String sql = "SELECT pcs.id, pcs.pet_id, pcs.service_id, pcs.service_start, pcs.service_end, "
               + "pcs.actual_end, pcs.notes, pcs.created_at, pcs.is_status, "
               + "p.pet_name, p.pet_code, p.breed, "
               + "s.service_code, s.service_name "
               + "FROM pet_care_services pcs "
               + "JOIN pets p ON pcs.pet_id = p.id "
               + "JOIN service_details s ON pcs.service_id = s.id";

    try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            list.add(mapResult(rs));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


    public boolean insertPetCareService(PetCareServices service) {
        String sql = "INSERT INTO pet_care_services (pet_id, service_id, service_start, service_end, notes, created_at, is_deleted,is_status) VALUES (?, ?, ?, ?, ?, ?, 0,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, service.getPet().getId());
            ps.setInt(2, service.getPetS().getId());
            ps.setDate(3, service.getDateStart());
            ps.setDate(4, service.getDateEnd());
            ps.setString(5, service.getNote());
            ps.setDate(6, service.getCreatedAt());
            ps.setBoolean(7, service.isStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PetCareServices mapResult(ResultSet rs) throws Exception {
        PetCareServices p = new PetCareServices();
        p.setId(rs.getInt("id"));

        PetServices s = new PetServices();
        s.setServiceCode(rs.getString("service_code"));
        s.setServiceName(rs.getString("service_name"));
        p.setPetS(s);

        Pets e = new Pets();
        e.setPetName(rs.getString("pet_name"));
        e.setPetCode(rs.getString("pet_code"));
        e.setBreed(rs.getString("breed"));
        p.setPet(e);

        p.setDateStart(rs.getDate("service_start"));
        p.setDateEnd(rs.getDate("service_end"));

        p.setActualEnd(rs.getDate("actual_end"));

        p.setNote(rs.getString("notes"));

        p.setCreatedAt(rs.getDate("created_at"));
        p.setStatus(rs.getBoolean("is_status"));

        return p;
    }
}
