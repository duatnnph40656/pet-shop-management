/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.daos;

import com.petshop.connect.DBConnect;
import com.petshop.models.InvoiceDetails;
import com.petshop.models.Invoices;
import com.petshop.models.PetServices;
import com.petshop.models.Pets;
import com.petshop.models.ProductDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duat
 */
public class InvoiceDetailDAO {

    private Connection conn;

    public InvoiceDetailDAO() {
        conn = DBConnect.getConnection();
    }

    public List<InvoiceDetails> getListInvoiceDetailProduct() {
        String sql = "SELECT \n"
                + "    id.id, \n"
                + "    id.invoice_detail_code, \n"
                + "    id.usage_or_quantity, \n"
                + "    id.price, \n"
                + "    id.created_at, \n"
                + "    id.is_status,\n"
                + "    iv.invoice_code, \n"
                + "    pd.product_detail_name, \n"
                + "    sd.service_name, \n"
                + "    p.pet_name\n"
                + "FROM invoice_details id\n"
                + "LEFT JOIN invoices iv ON id.id = iv.id\n"
                + "LEFT JOIN product_details pd ON id.id = pd.id\n"
                + "LEFT JOIN service_details sd ON id.id = sd.id\n"
                + "LEFT JOIN pets p ON id.id = p.id;";

        List<InvoiceDetails> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapInvoiceDetail(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertInvoiceDetail(InvoiceDetails invoiceDetail) {
        String sql = """
        INSERT INTO invoice_details (invoice_detail_code, invoice_id, usage_or_quantity, price, 
                                     product_detail_id, service_id, pet_id,is_status)
        VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, invoiceDetail.getInvoiceDetailCode());
            ps.setInt(2, invoiceDetail.getInvoice().getId()); // Đảm bảo invoice không null
            ps.setInt(3, invoiceDetail.getUsageOrQuantity());
            ps.setBigDecimal(4, invoiceDetail.getPrice());

            // Kiểm tra null trước khi set giá trị
            if (invoiceDetail.getProductDetail() != null) {
                ps.setInt(5, invoiceDetail.getProductDetail().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (invoiceDetail.getPetService() != null) {
                ps.setInt(6, invoiceDetail.getPetService().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (invoiceDetail.getPet() != null) {
                ps.setInt(7, invoiceDetail.getPet().getId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setBoolean(9, invoiceDetail.isStatus());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsageOrQuantity(int id, int newQuantity) {
        String sql = "UPDATE invoice_details SET usage_or_quantity = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public InvoiceDetails mapInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetails i = new InvoiceDetails();
        i.setId(rs.getInt("id"));
        i.setInvoiceDetailCode(rs.getString("invoice_detail_code"));
        i.setUsageOrQuantity(rs.getInt("usage_or_quantity"));
        i.setPrice(rs.getBigDecimal("price"));

        Invoices ic = new Invoices();
        ic.setId(rs.getInt("id_invoice"));
        i.setInvoice(ic);

        ProductDetails p = new ProductDetails();
        p.setProductDetailName("product_detail_name");
        i.setProductDetail(p);

        PetServices ps = new PetServices();
        ps.setServiceName(rs.getString("service_name"));
        i.setPetService(ps);

        Pets pet = new Pets();
        pet.setPetName(rs.getString("pet_name"));
        i.setPet(pet);

        i.setCreatedAt(rs.getDate("created_at"));
        i.setStatus(rs.getBoolean("is_status"));

        return i;
    }

}
