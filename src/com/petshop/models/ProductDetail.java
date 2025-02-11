/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import com.petshop.swing.table.ModelProfile;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author dut
 */
public class ProductDetail {

    private int id;
    private Icon icon;
    private String productDetailCode;
    private String productDetailName;
    private String barCode;
    private Product product;
    private TypePet typePet;
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

    public ProductDetail() {
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public ProductDetail(String productDetailName, Product product, TypePet typePet, int expirydate, Date productionDate, BigDecimal weight, int quantityInStock, String flavor, String description, BigDecimal price, String imagePath,boolean status) {
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

    public ProductDetail(int id, Icon icon, String productDetailCode, String productDetailName, String barCode, Product product, TypePet typePet, int expirydate, Date productionDate, BigDecimal weight, int quantityInStock, String flavor, String description, BigDecimal price, String imagePath, Date createdAt, boolean deleted, boolean status) {
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

    public ProductDetail(String productDetailCode, String productDetailName, String barCode, Product product, TypePet typePet, int expirydate, Date productionDate, BigDecimal weight, int quantityInStock, String flavor, String description, BigDecimal price, String imagePath,boolean status) {
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

    public Product getProduct() {
        return product;
    }

    public TypePet getTypePet() {
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

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setTypePet(TypePet typePet) {
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

    public ModelProfile getModelProfile() {
        return new ModelProfile(getIcon(), productDetailName);
    }

    public ImageIcon getIcon() {
        if (imagePath != null && !imagePath.isEmpty()) {
            String basePath = "D:/FPT/DA1/Project1/pet-shop/src/com/petshop/images/";
            File file = new File(basePath + imagePath); // Chỉ lấy tên ảnh, không cần đường dẫn đầy đủ

            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath()); // Load ảnh từ file
            }
        }

        // Trả về ảnh mặc định từ resources nếu ảnh không tồn tại
        return new ImageIcon(getClass().getResource("/com/petshop/images/01.jpg"));
    }

}
