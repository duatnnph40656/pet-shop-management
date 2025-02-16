/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import com.petshop.swing.table.ModelProfile;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author dut
 */
public class ProductDetails {

    private int id;
    private Icon icon;
    private String productDetailCode;
    private String productDetailName;
    private String barCode;
    private Products product;
    private TypePets typePet;
    private int expirydate;
    private Date productionDate;
    private BigDecimal weight;
    private int quantityInStock;
    private String flavor;
    private String description;
    private BigDecimal price;
    private String imagePath;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public ProductDetails() {
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public ProductDetails(String productDetailName, Products product, TypePets typePet, int expirydate, Date productionDate, BigDecimal weight, int quantityInStock, String flavor, String description, BigDecimal price, String imagePath, boolean status) {
        this.productDetailName = productDetailName;
        this.product = product;
        this.typePet = typePet;
        this.expirydate = expirydate;
        this.productionDate = productionDate;
        this.weight = weight;
        this.quantityInStock = quantityInStock;
        this.flavor = flavor;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.status = status;
    }

    public ProductDetails(int id, Icon icon, String productDetailCode, String productDetailName, String barCode, Products product, TypePets typePet, int expirydate, Date productionDate, BigDecimal weight, int quantityInStock, String flavor, String description, BigDecimal price, String imagePath, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.icon = icon;
        this.productDetailCode = productDetailCode;
        this.productDetailName = productDetailName;
        this.barCode = barCode;
        this.product = product;
        this.typePet = typePet;
        this.expirydate = expirydate;
        this.productionDate = productionDate;
        this.weight = weight;
        this.quantityInStock = quantityInStock;
        this.flavor = flavor;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public ProductDetails(String productDetailCode, String productDetailName, String barCode, Products product, TypePets typePet, int expirydate, Date productionDate, BigDecimal weight, int quantityInStock, String flavor, String description, BigDecimal price, String imagePath, boolean status) {
        this.productDetailCode = productDetailCode;
        this.productDetailName = productDetailName;
        this.barCode = barCode;
        this.product = product;
        this.typePet = typePet;
        this.expirydate = expirydate;
        this.productionDate = productionDate;
        this.weight = weight;
        this.quantityInStock = quantityInStock;
        this.flavor = flavor;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getProductDetailCode() {
        return productDetailCode;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public String getBarCode() {
        return barCode;
    }

    public Products getProduct() {
        return product;
    }

    public TypePets getTypePet() {
        return typePet;
    }

    public int getExpirydate() {
        return expirydate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
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

    public void setProductDetailCode(String productDetailCode) {
        this.productDetailCode = productDetailCode;
    }

    public void setProductDetailName(String productDetailName) {
        this.productDetailName = productDetailName;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public void setTypePet(TypePets typePet) {
        this.typePet = typePet;
    }

    public void setExpirydate(int expirydate) {
        this.expirydate = expirydate;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "N/A"; // Nếu ngày tạo là null, trả về chuỗi mặc định
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(createdAt);
    }

    public String getFormattedProductionDate() {
        if (productionDate == null) {
            return "N/A"; // Nếu ngày tạo là null, trả về chuỗi mặc định
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(productionDate);
    }

    // Getter tùy chỉnh: Định dạng giá theo tiền tệ VND
    public String getFormattedPriceBase() {
        if (price == null) {
            return "N/A"; // Nếu giá là null, trả về chuỗi mặc định
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Định dạng giá thành 1,000
        return decimalFormat.format(price) + " VND"; // Thêm đơn vị "VND"
    }

    public ModelProfile getModelProfile() throws IOException {
        return new ModelProfile(getIcon(), productDetailName);
    }

    public ImageIcon getIcon() {
        if (imagePath != null && !imagePath.isEmpty()) {
            // Lấy ảnh từ thư mục resources
            URL imageUrl = getClass().getResource("/com/resources/images/" + imagePath);
            if (imageUrl != null) {
                return new ImageIcon(imageUrl);
            }
        }

        // Nếu ảnh không tồn tại, trả về ảnh mặc định từ resources
        URL defaultUrl = getClass().getResource("/com/resources/images/default.jpg");
        if (defaultUrl != null) {
            return new ImageIcon(defaultUrl);
        }

        return null; // Nếu không có ảnh nào, trả về null
    }

    public String getImageName() {
        return imagePath; // imagePath chỉ chứa tên ảnh, ví dụ: "dog.jpg"
    }

}
