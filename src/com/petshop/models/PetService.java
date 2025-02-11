/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author dut
 */
public class PetService {
    private int id;
    private String serivce_code;
    private String servce_name;
    private TypeService typeService;
    private BigDecimal priceService;
    private String describe_service;
    private int duration;
    private String time_unit;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public PetService() {
    }

    public PetService(int id, String serivce_code, String servce_name, TypeService typeService, BigDecimal priceService, String describe_service, int duration, String time_unit, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.serivce_code = serivce_code;
        this.servce_name = servce_name;
        this.typeService = typeService;
        this.priceService = priceService;
        this.describe_service = describe_service;
        this.duration = duration;
        this.time_unit = time_unit;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public PetService(String serivce_code, String servce_name, TypeService typeService, BigDecimal priceService, String describe_service, boolean deleted, boolean status) {
        this.serivce_code = serivce_code;
        this.servce_name = servce_name;
        this.typeService = typeService;
        this.priceService = priceService;
        this.describe_service = describe_service;
        this.deleted = deleted;
        this.status = status;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerivce_code() {
        return serivce_code;
    }

    public void setSerivce_code(String serivce_code) {
        this.serivce_code = serivce_code;
    }

    public String getServce_name() {
        return servce_name;
    }

    public void setServce_name(String servce_name) {
        this.servce_name = servce_name;
    }

    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    public BigDecimal getPriceService() {
        return priceService;
    }

    public void setPriceService(BigDecimal priceService) {
        this.priceService = priceService;
    }

    public String getDescribe_service() {
        return describe_service;
    }

    public void setDescribe_service(String describe_service) {
        this.describe_service = describe_service;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTime_unit() {
        return time_unit;
    }

    public void setTime_unit(String time_unit) {
        this.time_unit = time_unit;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "N/A"; // Nếu ngày tạo là null, trả về chuỗi mặc định
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(createdAt);
    }

    // Getter tùy chỉnh: Định dạng giá theo tiền tệ VND
    public String getFormattedPriceService() {
        if (priceService == null) {
            return "N/A"; // Nếu giá là null, trả về chuỗi mặc định
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Định dạng giá thành 1,000
        return decimalFormat.format(priceService) + " VND"; // Thêm đơn vị "VND"
    }
    
    
}
