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
import java.util.Objects;

/**
 *
 * @author duat
 */
public class PetDAO {

    private Connection conn;

    public PetDAO() {
        conn = DBConnect.getConnection();
    }

    public List<Pets> getList() {
        List<Pets> list = new ArrayList<>();
        String sql = """
                    SELECT 
                        p.id, 
                        p.pet_code, 
                        p.pet_name, 
                        tp.type_pet_name, 
                        p.breed, 
                        p.age, 
                        p.weight, 
                        p.color, 
                        p.gender, 
                        p.vaccinated, 
                        p.owner, 
                        p.created_at, 
                        p.is_status, 
                        p.id_type_pet, 
                        p.id_customer, 
                        c.customer_name
                    FROM pets p
                    INNER JOIN type_pets tp ON tp.id = p.id_type_pet
                    INNER JOIN customers c ON c.id = p.id_customer 
                    WHERE p.is_deleted = 1;
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pets pet = new Pets();
                pet.setId(rs.getInt("id"));
                pet.setPetCode(rs.getString("pet_code"));
                pet.setPetName(rs.getString("pet_name"));
                pet.setBreed(rs.getString("breed"));
                pet.setAge(rs.getString("age"));
                pet.setWeight(rs.getBigDecimal("weight"));
                pet.setColor(rs.getString("color"));
                pet.setGender(rs.getBoolean("gender"));
                pet.setVaccinated(rs.getBoolean("vaccinated"));
                pet.setOwner(rs.getString("owner"));
                pet.setCreatedAt(rs.getTimestamp("created_at"));
                pet.setStatus(rs.getBoolean("is_status"));

                // Gán TypePets
                TypePets typePet = new TypePets();
                typePet.setId(rs.getInt("id_type_pet"));
                typePet.setTypePetName(rs.getString("type_pet_name"));
                pet.setTypePet(typePet);

                // Gán Customers
                Customers customer = new Customers();
                customer.setId(rs.getInt("id_customer"));
                customer.setCustomerName(rs.getString("customer_name"));
                pet.setCustomer(customer);

                list.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // History delete
    public List<Pets> getListHistoryDelete() {
        List<Pets> list = new ArrayList<>();
        String sql = """
                    SELECT 
                        p.id, 
                        p.pet_code, 
                        p.pet_name, 
                        tp.type_pet_name, 
                        p.breed, 
                        p.age, 
                        p.weight, 
                        p.color, 
                        p.gender, 
                        p.vaccinated, 
                        p.owner, 
                        p.created_at, 
                        p.is_status, 
                        p.id_type_pet, 
                        p.id_customer, 
                        c.customer_name
                    FROM pets p
                    INNER JOIN type_pets tp ON tp.id = p.id_type_pet
                    INNER JOIN customers c ON c.id = p.id_customer 
                    WHERE p.is_deleted = 0;
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pets pet = new Pets();
                pet.setId(rs.getInt("id"));
                pet.setPetCode(rs.getString("pet_code"));
                pet.setPetName(rs.getString("pet_name"));
                pet.setBreed(rs.getString("breed"));
                pet.setAge(rs.getString("age"));
                pet.setWeight(rs.getBigDecimal("weight"));
                pet.setColor(rs.getString("color"));
                pet.setGender(rs.getBoolean("gender"));
                pet.setVaccinated(rs.getBoolean("vaccinated"));
                pet.setOwner(rs.getString("owner"));
                pet.setCreatedAt(rs.getTimestamp("created_at"));
                pet.setStatus(rs.getBoolean("is_status"));

                // Gán TypePets
                TypePets typePet = new TypePets();
                typePet.setId(rs.getInt("id_type_pet"));
                typePet.setTypePetName(rs.getString("type_pet_name"));
                pet.setTypePet(typePet);

                // Gán Customers
                Customers customer = new Customers();
                customer.setId(rs.getInt("id_customer"));
                customer.setCustomerName(rs.getString("customer_name"));
                pet.setCustomer(customer);

                list.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add new Pet
    public boolean insertPet(Pets pet) {
        String sql = """
                     INSERT INTO pets (
                        pet_code,
                        pet_name,
                        id_type_pet,
                        breed,
                        age,
                        weight,
                        color,
                        gender,
                        vaccinated,
                        created_at,
                        is_deleted,
                        is_status,
                        id_customer
                     )
                     VALUES 
                        (?,?,?,?,?,?,?,?,?,GETDATE(),1,1,?)
                     """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pet.getPetCode());
            ps.setString(2, pet.getPetName());
            ps.setInt(3, pet.getTypePet().getId()); // Lấy ID từ TypePets
            ps.setString(4, pet.getBreed());
            ps.setString(5, pet.getAge());
            ps.setBigDecimal(6, pet.getWeight());
            ps.setString(7, pet.getColor());
            ps.setBoolean(8, pet.isGender());
            ps.setBoolean(9, pet.isVaccinated());
            ps.setInt(10, pet.getCustomer().getId()); // Lấy ID từ Customers
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get id
    public Pets getById(int id) {
        Pets pet = null;
        String sql = """
        SELECT p.id, p.pet_code, p.pet_name, p.breed, p.age, p.weight, p.color, p.gender, p.vaccinated, 
               p.created_at, p.is_status, p.id_type_pet, p.id_customer, p.owner, 
               c.id as customer_id, c.customer_name, 
               t.id as type_id, t.type_pet_name 
        FROM pets p
        LEFT JOIN customers c ON p.id_customer = c.id
        LEFT JOIN type_pets t ON p.id_type_pet = t.id
        WHERE p.id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Chỉ lấy 1 dòng duy nhất
                    pet = new Pets();
                    pet.setId(rs.getInt("id"));
                    pet.setPetCode(rs.getString("pet_code"));
                    pet.setPetName(rs.getString("pet_name"));
                    pet.setBreed(rs.getString("breed"));
                    pet.setAge(rs.getString("age"));
                    pet.setWeight(rs.getBigDecimal("weight"));
                    pet.setColor(rs.getString("color"));
                    pet.setGender(rs.getBoolean("gender"));
                    pet.setVaccinated(rs.getBoolean("vaccinated"));
                    pet.setCreatedAt(rs.getDate("created_at"));
                    pet.setStatus(rs.getBoolean("is_status"));
                    pet.setOwner(rs.getString("owner"));

                    // Gán khách hàng
                    Customers customer = new Customers();
                    customer.setId(rs.getInt("customer_id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    pet.setCustomer(customer);

                    // Gán loại thú cưng
                    TypePets typePets = new TypePets();
                    typePets.setId(rs.getInt("type_id"));
                    typePets.setTypePetName(rs.getString("type_pet_name"));
                    pet.setTypePet(typePets);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pet;
    }

    // Update
    public boolean update(Pets pet, int id) {
        String sql = """
                     UPDATE pets
                           SET 
                               id_customer = ?
                              ,id_type_pet = ?
                              ,breed = ?
                              ,age = ?
                              ,weight = ? 
                              ,color = ?
                              ,gender = ?
                              ,vaccinated = ?
                              ,pet_name = ?
                         WHERE id = ?
                     """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pet.getCustomer().getId());
            ps.setInt(2, pet.getTypePet().getId());
            ps.setString(3, pet.getBreed());
            ps.setString(4, pet.getAge());
            ps.setBigDecimal(5, pet.getWeight());
            ps.setString(6, pet.getColor());
            ps.setBoolean(7, pet.isGender());
            ps.setBoolean(8, pet.isVaccinated());
            ps.setString(9, pet.getPetName());
            ps.setInt(10, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Khoi phuc
    public boolean restore(String ma) {
        String sql = "UPDATE pets SET is_deleted = 1  WHERE pet_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete
    public boolean delete(String ma) {

        String sql = "UPDATE pets SET is_deleted = 0 ,created_at = GETDATE() WHERE pet_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Filter
    public ArrayList<Pets> filterPet(String loai, String giong) {
        List<Pets> allPets = getList();
        ArrayList<Pets> filteredPets = new ArrayList<>();

        for (Pets pet : allPets) {
            boolean matchBreed = (loai == null || Objects.equals(pet.getTypePet().getTypePetName(), loai));
            boolean matchType = (giong == null || Objects.equals(pet.getBreed(), giong));

            if (matchBreed && matchType) {
                filteredPets.add(pet);
            }
        }
        return filteredPets;
    }

    public Customers searchCustomer(String customer) {
        String sql = """
        SELECT id, customer_code, customer_name
        FROM customers
        WHERE phone_number LIKE ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + customer + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customers customers = new Customers();
                    customers.setId(rs.getInt("id"));
                    customers.setCustomerCode(rs.getString("customer_code"));
                    customers.setCustomerName(rs.getString("customer_name"));
                    return customers;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customers searchCustomerId(String customer_name) {
        String sql = """
        SELECT id
        FROM customers
        WHERE customer_name = ?
    """;

        Customers customers = new Customers();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer_name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    customers.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

}
