/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author duat
 */
public class ReturnInvoiceDetail {
    private int id;
    private String returnInvoiceDetailCode;
    private ReturnInvoices returnsInvoices;
    private ProductDetails productDetails;
    private int usageOrQuantity; // Số lượng bị trả
    private BigDecimal totalPrice; // Tổng tiền hoàn/trả
    private boolean typeInvoiceDetail; // 1 = Trả hàng, 2 = Đổi hàng, 3 = Trả dịch vụ
    private LocalDateTime createdAt;
    private boolean deleted;
    private InvoiceDetails invoiceDetails;
    public ReturnInvoiceDetail() {
    }

    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReturnInvoiceDetailCode() {
        return returnInvoiceDetailCode;
    }

    public void setReturnInvoiceDetailCode(String returnInvoiceDetailCode) {
        this.returnInvoiceDetailCode = returnInvoiceDetailCode;
    }

    public ReturnInvoices getReturnsInvoices() {
        return returnsInvoices;
    }

    public void setReturnsInvoices(ReturnInvoices returnsInvoices) {
        this.returnsInvoices = returnsInvoices;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
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

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isTypeInvoiceDetail() {
        return typeInvoiceDetail;
    }

    public void setTypeInvoiceDetail(boolean typeInvoiceDetail) {
        this.typeInvoiceDetail = typeInvoiceDetail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
}
