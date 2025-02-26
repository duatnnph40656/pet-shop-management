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
public class InvoiceDetails {
    private int id;
    private String invoiceDetailCode;
    private Invoices invoice; 
    private ProductDetails productDetail;
    private Pets pet;
    private PetServices petService;
    private int usageOrQuantity;
    private BigDecimal totalPrice;
    private Date createdAt;
    private boolean deleted;
    private boolean status;
    private boolean typeInvoiceDetail;
    private int serviceDuration;

    public InvoiceDetails() {
    }

    public InvoiceDetails(int id, String invoiceDetailCode,Invoices invoice, ProductDetails productDetail, Pets pet, PetServices petService, int usageOrQuantity, BigDecimal totalPrice, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.invoiceDetailCode = invoiceDetailCode;
        this.invoice = invoice;
        this.productDetail = productDetail;
        this.pet = pet;
        this.petService = petService;
        this.usageOrQuantity = usageOrQuantity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public InvoiceDetails(ProductDetails productDetail, int usageOrQuantity) {
        this.productDetail = productDetail;
        this.usageOrQuantity = usageOrQuantity;
    }

    public int getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(int serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    
    public boolean isTypeInvoiceDetail() {
        return typeInvoiceDetail;
    }

    public void setTypeInvoiceDetail(boolean typeInvoiceDetail) {
        this.typeInvoiceDetail = typeInvoiceDetail;
    }

    public int getId() {
        return id;
    }

    public String getInvoiceDetailCode() {
        return invoiceDetailCode;
    }

    public void setInvoiceDetailCode(String invoiceDetailCode) {
        this.invoiceDetailCode = invoiceDetailCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Invoices getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoices invoice) {
        this.invoice = invoice;
    }

    public ProductDetails getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetails productDetail) {
        this.productDetail = productDetail;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Pets getPet() {
        return pet;
    }

    public void setPet(Pets pet) {
        this.pet = pet;
    }

    public PetServices getPetService() {
        return petService;
    }

    public void setPetService(PetServices petService) {
        this.petService = petService;
    }

    public int getUsageOrQuantity() {
        return usageOrQuantity;
    }

    public void setUsageOrQuantity(int usageOrQuantity) {
        this.usageOrQuantity = usageOrQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getFormattedTotalPrice() {
        if (totalPrice == null) {
            return "N/A"; // Nếu giá là null, trả về chuỗi mặc định
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Định dạng giá thành 1,000
        return decimalFormat.format(totalPrice) + " VND"; // Thêm đơn vị "VND"
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
    
    
    // Getter tùy chỉnh: Định dạng ngày
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "N/A"; // Nếu ngày tạo là null, trả về chuỗi mặc định
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(createdAt);
    }
    
}
