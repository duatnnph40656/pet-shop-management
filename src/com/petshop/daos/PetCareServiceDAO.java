package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.Invoices;
import com.petshop.models.PetCareServices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
import com.petshop.models.TypePets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PetCareServiceDAO {

    private Connection conn;

    public PetCareServiceDAO() {
        conn = DBConnect.getConnection();
    }

    public List<PetCareServices> getListPetCareService() {
        List<PetCareServices> list = new ArrayList<>();
        String sql = "  SELECT pcs.id, pcs.pet_id, pcs.service_id, pcs.service_start, pcs.service_end, "
                + "       pcs.actual_end, pcs.notes, pcs.created_at, pcs.is_status, pcs.id_invoice, "
                + "       p.pet_name, p.pet_code, p.breed, p.owner, p.id_type_pet, p.color, p.weight, p.vaccinated, p.age, p.gender, " // Sửa dấu phẩy
                + "       t.type_pet_name, " // Đưa lên cùng dòng
                + "       s.service_code, s.service_name, "
                + "       i.id AS invoice_id "
                + "FROM pet_care_services pcs "
                + "JOIN pets p ON pcs.pet_id = p.id "
                + "JOIN type_pets t ON p.id_type_pet = t.id  " // Không cần comment ở đây
                + "JOIN service_details s ON pcs.service_id = s.id "
                + "JOIN invoices i ON pcs.id_invoice = i.id "
                + "WHERE i.is_deleted = 0;";
        // Thêm điều kiện is_deleted = 0

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
        String sql = "INSERT INTO pet_care_services (pet_id, service_id, service_start, service_end, notes, is_deleted, is_status,id_invoice) "
                + "VALUES (?, ?, ?, ?, ?, 0, ?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, service.getPet().getId());
            ps.setInt(2, service.getPetS().getId());
            ps.setTimestamp(3, service.getDateStart() != null ? Timestamp.valueOf(service.getDateStart()) : null);
            ps.setTimestamp(4, service.getDateEnd() != null ? Timestamp.valueOf(service.getDateEnd()) : null);
            ps.setString(5, service.getNote());
            ps.setBoolean(6, service.isStatus());
            ps.setInt(7, service.getInvoices().getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateActualEndAndNote(int id, LocalDateTime actualEnd, String note) {
        String sql = "UPDATE pet_care_services "
                + "SET actual_end = ?, notes = ?, is_status = 0"
                + "WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, actualEnd != null ? Timestamp.valueOf(actualEnd) : null);
            ps.setString(2, note);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PetCareServices mapResult(ResultSet rs) throws Exception {
        PetCareServices p = new PetCareServices();
        p.setId(rs.getInt("id"));

        // Map dữ liệu của dịch vụ
        PetServices s = new PetServices();
        s.setServiceCode(rs.getString("service_code"));
        s.setServiceName(rs.getString("service_name"));
        p.setPetS(s);

        // Map dữ liệu của thú cưng
        Pets e = new Pets();
        e.setPetName(rs.getString("pet_name"));
        e.setPetCode(rs.getString("pet_code"));
        e.setBreed(rs.getString("breed"));
        e.setOwner(rs.getString("owner"));
        e.setColor(rs.getString("color"));

        // Chuyển đổi weight từ Decimal -> String
        e.setWeight(rs.getBigDecimal("weight"));

        // Kiểm tra và set giá trị vaccinated
        e.setVaccinated(rs.getBoolean("vaccinated"));

        // Kiểm tra và set tuổi (age)
        e.setAge(rs.getString("age"));

        // Kiểm tra và set giới tính (gender)
        e.setGender(rs.getBoolean("gender"));

        // Map dữ liệu loại thú cưng
        TypePets t = new TypePets();
        t.setTypePetName(rs.getString("type_pet_name"));
        e.setTypePet(t);

        // Set pet vào PetCareServices
        p.setPet(e);

        // Map dữ liệu hóa đơn
        Invoices i = new Invoices();
        i.setId(rs.getInt("invoice_id"));
        p.setInvoices(i);

        // Chuyển đổi DateTime từ SQL sang LocalDateTime
        p.setDateStart(getLocalDateTime(rs, "service_start"));
        p.setDateEnd(getLocalDateTime(rs, "service_end"));
        p.setActualEnd(getLocalDateTime(rs, "actual_end"));
        p.setCreatedAt(rs.getDate("created_at"));

        // Ghi chú và trạng thái
        p.setNote(rs.getString("notes"));
        p.setStatus(rs.getBoolean("is_status"));

        return p;
    }

    private LocalDateTime getLocalDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    public int getServicesUsedToday() {
        String sql = "SELECT COUNT(*) FROM pet_care_services WHERE CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE())";
        return getServiceCount(sql);
    }

    public int getServicesUsedYesterday() {
        String sql = "SELECT COUNT(*) FROM pet_care_services WHERE CONVERT(DATE, created_at) = CONVERT(DATE, DATEADD(DAY, -1, GETDATE()))";
        return getServiceCount(sql);
    }

    public int getServicesUsedThisMonth() {
        String sql = "SELECT COUNT(*) FROM pet_care_services WHERE MONTH(created_at) = MONTH(GETDATE()) AND YEAR(created_at) = YEAR(GETDATE())";
        return getServiceCount(sql);
    }

    public int getServicesUsedLastMonth() {
        String sql = "SELECT COUNT(*) FROM pet_care_services WHERE MONTH(created_at) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(created_at) = YEAR(DATEADD(MONTH, -1, GETDATE()))";
        return getServiceCount(sql);
    }

    // Hàm hỗ trợ lấy số lượng dịch vụ
    private int getServiceCount(String sql) {
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<PetCareServices> searchServices(int daysAgo, Boolean isStatus) {
        List<PetCareServices> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT pcs.id, pcs.pet_id, pcs.service_id, pcs.service_start, pcs.service_end, "
                + "pcs.actual_end, pcs.notes, pcs.created_at, pcs.is_status, pcs.id_invoice, "
                + "p.pet_name, p.pet_code, p.breed, p.owner, p.color, p.weight, p.vaccinated, p.age, p.gender, "
                + "t.type_pet_name, "
                + "s.service_code, s.service_name, "
                + "i.id AS invoice_id "
                + "FROM pet_care_services pcs "
                + "JOIN pets p ON pcs.pet_id = p.id "
                + "JOIN type_pets t ON p.id_type_pet = t.id "
                + "JOIN service_details s ON pcs.service_id = s.id "
                + "JOIN invoices i ON pcs.id_invoice = i.id "
                + "WHERE i.is_deleted = 0 AND i.is_status = 0"
        );

        // Nếu lọc theo ngày (chỉ lấy từ N ngày trước đến ngày hôm qua)
        if (daysAgo > 0) {
            sql.append(" AND CAST(pcs.created_at AS DATE) BETWEEN DATEADD(DAY, -?, CAST(GETDATE() AS DATE)) AND DATEADD(DAY, -1, CAST(GETDATE() AS DATE))");
        }

        // Nếu lọc theo trạng thái
        if (isStatus != null) {
            sql.append(" AND pcs.is_status = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (daysAgo > 0) {
                ps.setInt(paramIndex++, daysAgo);
            }
            if (isStatus != null) {
                ps.setBoolean(paramIndex++, isStatus);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResult(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
