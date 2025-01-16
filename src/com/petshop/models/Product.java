/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import com.petshop.swing.table.EventAction;
import com.petshop.swing.table.ModelAction;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author dut
 */
public class Product {

    private int id;
    private String productCode;
    private String productName;
    private CategoryProduct categoryProduct;
    private BigDecimal priceBase;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public Product() {
    }

    public Product(int id, String productCode, String productName, CategoryProduct categoryProduct, BigDecimal priceBase, Date createdAt, boolean isDeleted, boolean isStatus) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.categoryProduct = categoryProduct;
        this.priceBase = priceBase;
        this.createdAt = createdAt;
        this.deleted = isDeleted;
        this.status = isStatus;
    }

    public Product(String productCode, String productName, CategoryProduct categoryProduct, BigDecimal priceBase, boolean isDeleted, boolean isStatus) {
        this.productCode = productCode;
        this.productName = productName;
        this.categoryProduct = categoryProduct;
        this.priceBase = priceBase;
        this.deleted = isDeleted;
        this.status = isStatus;
    }

    public int getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public CategoryProduct getCategoryProduct() {
        return categoryProduct;
    }

    public BigDecimal getPriceBase() {
        return priceBase;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategoryProduct(CategoryProduct categoryProduct) {
        this.categoryProduct = categoryProduct;
    }

    public void setPriceBase(BigDecimal priceBase) {
        this.priceBase = priceBase;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
    }

    public void setStatus(boolean isStatus) {
        this.status = isStatus;
    }

    // Getter tùy chỉnh: Định dạng ngày
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "N/A"; // Nếu ngày tạo là null, trả về chuỗi mặc định
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(createdAt);
    }

    // Getter tùy chỉnh: Định dạng giá theo tiền tệ VND
    public String getFormattedPriceBase() {
        if (priceBase == null) {
            return "N/A"; // Nếu giá là null, trả về chuỗi mặc định
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Định dạng giá thành 1,000
        return decimalFormat.format(priceBase) + " VND"; // Thêm đơn vị "VND"
    }

    @Override
    public String toString() {
        return productName; // Hiển thị tên danh mục trong JComboBox
    }
    
}
