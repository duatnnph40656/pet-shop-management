/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.util.Date;

/**
 *
 * @author dut
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class CategoryProduct {

    private int id;
    private String categoryProductCode;
    private String categoryProductName;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    // Biến đếm tĩnh để sinh mã duy nhất cho mỗi đối tượng
    private static int counter = 1;

    public CategoryProduct(String categoryProductCode, String categoryProductName) {
        this.categoryProductCode = categoryProductCode;
        this.categoryProductName = categoryProductName;
    }

    public CategoryProduct(String categoryProductCode, String categoryProductName, Date createdAt, boolean status) {
        this.categoryProductCode = categoryProductCode;
        this.categoryProductName = categoryProductName;
        this.createdAt = createdAt;
        this.status = status;
    }
    
    public CategoryProduct(int id, String categoryProductCode, String categoryProductName, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.categoryProductCode = categoryProductCode;
        this.categoryProductName = categoryProductName;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }
    
    
    // Constructor
    public CategoryProduct(String categoryProductName, boolean status) {
        this.categoryProductName = categoryProductName;
        this.status = status;
        this.deleted = false; // Khởi tạo mặc định là chưa xóa
        this.createdAt = new Date(); // Ngày tạo mặc định là ngày hiện tại
        this.categoryProductCode = generateCategoryProductCode(); // Gán mã tự động
    }

    // Hàm sinh mã danh mục sản phẩm
    private String generateCategoryProductCode() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String serial = String.format("%04d", counter);
        String code = "CP" + date + serial;
        counter++;
        return code;
    }

    // Getter và setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryProductCode() {
        return categoryProductCode;
    }

    public String getCategoryProductName() {
        return categoryProductName;
    }

    public void setCategoryProductName(String categoryProductName) {
        this.categoryProductName = categoryProductName;
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

}
