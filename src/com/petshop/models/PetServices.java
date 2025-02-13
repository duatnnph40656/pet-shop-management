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
public class PetServices {
    private int id;
    private String serviceCode;
    private String serviceName;
    private TypeServices typeService;
    private BigDecimal priceService;
    private String describeService;
    private int duration;
    private String time_unit;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public PetServices() {
    }

    public PetServices(int id, String serivce_code, String servce_name, TypeServices typeService, BigDecimal priceService, String describe_service, int duration, String time_unit, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.serviceCode = serivce_code;
        this.serviceName = servce_name;
        this.typeService = typeService;
        this.priceService = priceService;
        this.describeService = describe_service;
        this.duration = duration;
        this.time_unit = time_unit;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public PetServices(String serivce_code, String servce_name, TypeServices typeService, BigDecimal priceService, String describe_service, boolean deleted, boolean status) {
        this.serviceCode = serivce_code;
        this.serviceName = servce_name;
        this.typeService = typeService;
        this.priceService = priceService;
        this.describeService = describe_service;
        this.deleted = deleted;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public TypeServices getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeServices typeService) {
        this.typeService = typeService;
    }

    public BigDecimal getPriceService() {
        return priceService;
    }

    public void setPriceService(BigDecimal priceService) {
        this.priceService = priceService;
    }

    public String getDescribeService() {
        return describeService;
    }

    public void setDescribeService(String describeService) {
        this.describeService = describeService;
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
