/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Customers;
import com.petshop.models.Pets;
import com.petshop.models.TypePets;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author duat
 */
public class PetDAO1 {

    private Connection conn;

    public PetDAO1() {
        conn = DBConnect.getConnection();
    }

    public List<Pets> getListPet() {
        List<Pets> list = new ArrayList<>();
        String sql = """
        SELECT p.id, p.pet_code, p.pet_name, p.breed,p.weight, p.color,p.age,p.gender, p.owner, p.id_type_pet, p.vaccinated, p.is_deleted, p.is_status, p.created_at, 
               c.customer_name, t.type_pet_name
        FROM pets p
        LEFT JOIN customers c ON p.id_customer = c.id
        LEFT JOIN type_pets t ON p.id_type_pet = t.id
        WHERE p.is_deleted = 0
        ORDER BY p.created_at DESC
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc log lỗi
        }

        return list;
    }

    public List<Pets> getListPetSortId() {
        List<Pets> list = new ArrayList<>();
        String sql = """
        SELECT p.id, p.pet_code, p.pet_name, p.breed, p.weight, p.color, p.age, p.gender, 
               p.owner, p.id_type_pet, p.vaccinated, p.is_deleted, p.is_status, p.created_at, 
               c.customer_name, t.type_pet_name
        FROM pets p
        LEFT JOIN customers c ON p.id_customer = c.id
        LEFT JOIN type_pets t ON p.id_type_pet = t.id
        WHERE p.is_deleted = 0
        ORDER BY p.id DESC
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultPet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc log lỗi
        }

        return list;
    }

    public boolean insertPet(Pets p) {
        String sql = """
        INSERT INTO pets (pet_code, pet_name, breed, color,gender, owner, id_type_pet,vaccinated, is_deleted,is_status)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 1)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getPetCode());
            ps.setString(2, p.getPetName());
            ps.setString(3, p.getBreed());
            ps.setString(4, p.getColor());
            ps.setBoolean(5, p.isGender());
            ps.setString(6, p.getOwner());
            ps.setInt(7, p.getTypePet().getId());
            ps.setBoolean(8, p.isVaccinated());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc log lỗi
        }
        return false;
    }

    public List<Pets> loadCboGiong() {
        List<Pets> list = new ArrayList<>();
        return list;
    }

    public Pets getPetByCode(String petCode) {
        String sql = "SELECT id,pet_code,pet_name FROM pets WHERE pet_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, petCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pets p = new Pets();
                    p.setId(rs.getInt("id"));
                    p.setPetCode(rs.getString("pet_code"));
                    p.setPetName(rs.getString("pet_name"));
                    return p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Pets p, int id) {

    }

    private Pets mapResultPet(ResultSet rs) throws SQLException {
        Pets p = new Pets();
        p.setId(rs.getInt("id"));
        p.setPetCode(rs.getString("pet_code"));
        p.setPetName(rs.getString("pet_name"));
        p.setBreed(rs.getString("breed"));
        p.setColor(rs.getString("color"));
        p.setGender(rs.getBoolean("gender"));
        p.setWeight(rs.getBigDecimal("weight"));
        p.setOwner(rs.getString("owner"));
        p.setVaccinated(rs.getBoolean("vaccinated"));
        p.setCreatedAt(rs.getDate("created_at"));
        p.setStatus(rs.getBoolean("is_status"));
        p.setAge(rs.getString("age"));
        Customers c = new Customers();
        c.setCustomerName(rs.getString("customer_name"));
        p.setCustomer(c);

        TypePets t = new TypePets();
        t.setTypePetName(rs.getString("type_pet_name"));
        p.setTypePet(t);

        return p;
    }
}
