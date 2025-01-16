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
    private boolean isDeleted;
    private boolean isStatus;

    // Biến đếm tĩnh để sinh mã duy nhất cho mỗi đối tượng
    private static int counter = 1;

    public CategoryProduct() {
    }

    public CategoryProduct(String categoryProductCode, String categoryProductName, boolean Status) {
        this.categoryProductCode = categoryProductCode;
        this.categoryProductName = categoryProductName;
        this.isStatus = Status;
    }

    public CategoryProduct(String categoryProductCode, String categoryProductName, Date createdAt, boolean isStatus) {
        this.categoryProductCode = categoryProductCode;
        this.categoryProductName = categoryProductName;
        this.createdAt = createdAt;
        this.isStatus = isStatus;
    }
    
    public CategoryProduct(int id, String categoryProductCode, String categoryProductName, Date createdAt, boolean isDeleted, boolean isStatus) {
        this.id = id;
        this.categoryProductCode = categoryProductCode;
        this.categoryProductName = categoryProductName;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.isStatus = isStatus;
    }
    
    
    // Constructor
    public CategoryProduct(String categoryProductName, boolean isStatus) {
        this.categoryProductName = categoryProductName;
        this.isStatus = isStatus;
        this.isDeleted = false; // Khởi tạo mặc định là chưa xóa
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

    public void setCategoryProductCode(String categoryProductCode) {
        this.categoryProductCode = categoryProductCode;
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
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean isStatus) {
        this.isStatus = isStatus;
    }

    @Override
    public String toString() {
        return categoryProductName; // Hiển thị tên danh mục trong JComboBox
    }

    
}
