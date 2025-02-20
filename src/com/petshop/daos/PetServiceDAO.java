/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.MostUsedService;
import com.petshop.models.PetServices;
import com.petshop.models.TypeServices;
import java.security.Provider;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class PetServiceDAO {

    private Connection conn;

    public PetServiceDAO() {
        conn = DBConnect.getConnection();
    }

    public List<PetServices> getListService() {
        String sql = "SELECT\n"
                + "    sd.id,\n"
                + "    sd.service_code,\n"
                + "    sd.service_name,\n"
                + "    ts.type_service_name,\n"
                + "    sd.price_service,\n"
                + "    sd.describe_service,\n"
                + "    sd.created_at,\n"
                + "    sd.is_deleted,\n"
                + "    sd.is_status,\n"
                + "    sd.duration,\n"
                + "    sd.time_unit\n"
                + "FROM service_details sd\n"
                + "JOIN type_services ts ON sd.id_service_type = ts.id\n"
                + "WHERE sd.is_deleted = 0;";
        List<PetServices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapPetService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<PetServices> getListServiceDeleted() {
        String sql = "SELECT\n"
                + "    sd.id,\n"
                + "    sd.service_code,\n"
                + "    sd.service_name,\n"
                + "    ts.type_service_name,\n"
                + "    sd.price_service,\n"
                + "    sd.describe_service,\n"
                + "    sd.created_at,\n"
                + "    sd.is_deleted,\n"
                + "    sd.is_status,\n"
                + "    sd.duration,\n"
                + "    sd.time_unit\n"
                + "FROM service_details sd\n"
                + "JOIN type_services ts ON sd.id_service_type = ts.id\n"
                + "WHERE sd.is_deleted = 1;";
        List<PetServices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapPetService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertPetService(PetServices petService) {
        String sql = "INSERT INTO service_details (service_code, service_name, id_service_type, price_service, describe_service,  is_deleted, is_status, duration, time_unit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, petService.getServiceCode());
            ps.setString(2, petService.getServiceName());
            ps.setInt(3, petService.getTypeService().getId());
            ps.setBigDecimal(4, petService.getPriceService());
            ps.setString(5, petService.getDescribeService());
            ps.setBoolean(6, petService.isDeleted());
            ps.setBoolean(7, petService.isStatus());
            ps.setInt(8, petService.getDuration());
            ps.setString(9, petService.getTimeUnit());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePetService(int id, PetServices petService) {
        String sql = "UPDATE service_details SET service_name = ?, id_service_type = ?, price_service = ?, describe_service = ?, duration = ?, time_unit = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, petService.getServiceName());
            ps.setInt(2, petService.getTypeService().getId());
            ps.setBigDecimal(3, petService.getPriceService());
            ps.setString(4, petService.getDescribeService());
            ps.setInt(5, petService.getDuration());
            ps.setString(6, petService.getTimeUnit());
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePetService(int id) {
        String sql = "UPDATE service_details SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PetServices> searchByServiceNameOrCode(String keyword) {
        String sql = "SELECT\n"
                + "    sd.id,\n"
                + "    sd.service_code,\n"
                + "    sd.service_name,\n"
                + "    ts.type_service_name,\n"
                + "    sd.price_service,\n"
                + "    sd.describe_service,\n"
                + "    sd.created_at,\n"
                + "    sd.is_deleted,\n"
                + "    sd.is_status,\n"
                + "    sd.duration,\n"
                + "    sd.time_unit\n"
                + "FROM service_details sd\n"
                + "JOIN type_services ts ON sd.id_service_type = ts.id\n"
                + "WHERE (sd.service_name LIKE ? OR sd.service_code LIKE ?) AND sd.is_deleted = 0";

        List<PetServices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPetService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public PetServices getPetServiceByCode(String code) {
        String sql = "SELECT id, service_code, service_name FROM service_details WHERE service_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code); // Đặt giá trị tham số trước khi thực thi truy vấn
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Sử dụng if thay vì while vì chỉ cần lấy 1 kết quả
                    PetServices p = new PetServices();
                    p.setId(rs.getInt("id"));
                    p.setServiceCode(rs.getString("service_code"));
                    p.setServiceName(rs.getString("service_name"));
                    return p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PetServices> searchByTypeServiceId(int typeServiceId) {
        String sql = "SELECT * FROM service_details WHERE id_service_type = ? AND is_deleted = 0";
        List<PetServices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeServiceId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPetService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatusService(int id, boolean status) {
        String sql = "UPDATE service_details SET is_status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setBoolean(1, status);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean restoreService(int id) {
        String sql = "UPDATE service_details SET is_deleted = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PetServices> filterServiceByIdTypeService(int typeServiceId, boolean status) {
        String sql = "SELECT "
                + "    sd.id, "
                + "    sd.service_code, "
                + "    sd.service_name, "
                + "    ts.type_service_name, "
                + "    sd.price_service, "
                + "    sd.describe_service, "
                + "    sd.created_at, "
                + "    sd.is_deleted, "
                + "    sd.is_status, "
                + "    sd.duration, "
                + "    sd.time_unit "
                + "FROM service_details sd "
                + "JOIN type_services ts ON sd.id_service_type = ts.id "
                + "WHERE sd.id_service_type = ? AND sd.is_status = ? AND sd.is_deleted = 0";

        List<PetServices> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeServiceId);
            ps.setBoolean(2, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPetService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isServiceCodeExists(String serviceCode) {
        String sql = "SELECT COUNT(*) FROM service_details WHERE service_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serviceCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0, tức là đã tồn tại
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public PetServices mapPetService(ResultSet rs) throws Exception {
        PetServices p = new PetServices();
        p.setId(rs.getInt("id"));
        p.setServiceCode(rs.getString("service_code"));
        p.setServiceName(rs.getString("service_name"));

        TypeServices t = new TypeServices();
        t.setTypeServiceName(rs.getString("type_service_name"));
        p.setTypeService(t);

        p.setPriceService(rs.getBigDecimal("price_service"));
        p.setDuration(rs.getInt("duration"));
        p.setTimeUnit(rs.getString("time_unit"));
        p.setDescribeService(rs.getString("describe_service"));
        p.setCreatedAt(rs.getDate("created_at"));
        p.setDeleted(rs.getBoolean("is_deleted"));
        p.setStatus(rs.getBoolean("is_status"));
        return p;
    }

    public List<MostUsedService> getMostUsedServices(Date startDate, Date endDate, int limit) {
        List<MostUsedService> list = new ArrayList<>();
        String sql = "{CALL GetMostUsedServices(?, ?, ?)}"; // Gọi stored procedure

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MostUsedService service = new MostUsedService(
                            rs.getInt("service_id"),
                            rs.getString("service_code"),
                            rs.getString("service_name"),
                            rs.getInt("total_used")
                    );
                    list.add(service);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
