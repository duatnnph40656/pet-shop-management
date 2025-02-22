package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.PetCareServices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
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
        String sql = "INSERT INTO pet_care_services (pet_id, service_id, service_start, service_end, notes, is_deleted, is_status) "
                + "VALUES (?, ?, ?, ?, ?, 0, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, service.getPet().getId());
            ps.setInt(2, service.getPetS().getId());
            ps.setTimestamp(3, service.getDateStart() != null ? Timestamp.valueOf(service.getDateStart()) : null);
            ps.setTimestamp(4, service.getDateEnd() != null ? Timestamp.valueOf(service.getDateEnd()) : null);
            ps.setString(5, service.getNote());
            ps.setBoolean(6, service.isStatus());
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

        // Chuyển đổi DATETIME từ SQL sang LocalDateTime
        p.setDateStart(getLocalDateTime(rs, "service_start"));
        p.setDateEnd(getLocalDateTime(rs, "service_end"));
        p.setActualEnd(getLocalDateTime(rs, "actual_end"));
        p.setCreatedAt(rs.getDate("created_at"));

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

}
