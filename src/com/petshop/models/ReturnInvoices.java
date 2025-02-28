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
public class ReturnInvoices {
    private int id;
    private String returnInvoiceCode;
    private Invoices invoices;
    private Customers customers;
    private Employees employees;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceReturn;
    private boolean deleted;
    private boolean status;  // true = Hoàn tất, false = Chờ duyệt
    private boolean paymentStatus; // true = Đã hoàn tiền, false = Chưa hoàn tiền
    private String note;
    private BigDecimal costsIncurred;
    private boolean paymentMethod;
    private LocalDateTime createdAt;

    public ReturnInvoices() {
    }

    public BigDecimal getTotalPriceReturn() {
        return totalPriceReturn;
    }

    public void setTotalPriceReturn(BigDecimal totalPriceReturn) {
        this.totalPriceReturn = totalPriceReturn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReturnInvoiceCode() {
        return returnInvoiceCode;
    }

    public void setReturnInvoiceCode(String returnInvoiceCode) {
        this.returnInvoiceCode = returnInvoiceCode;
    }

    public Invoices getInvoices() {
        return invoices;
    }

    public void setInvoices(Invoices invoices) {
        this.invoices = invoices;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getCostsIncurred() {
        return costsIncurred;
    }

    public void setCostsIncurred(BigDecimal costsIncurred) {
        this.costsIncurred = costsIncurred;
    }

    public boolean isPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(boolean paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
    
}
