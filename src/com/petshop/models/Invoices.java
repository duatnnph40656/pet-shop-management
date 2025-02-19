/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author dut
 */
public class Invoices {
    public int id;
    public String invoiceCode;
    public Customers customer;
    public Employees employee;
    public boolean paymentMethod;
    public BigDecimal totalPrice;
    public boolean paymentStatus;
    public String note;
    public BigDecimal costsIncurred;
    public boolean deleted;
    public LocalDateTime createdAt;
    public boolean status;

    public Invoices() {
    }

    
    public Invoices(String invoiceCode, Customers customer, Employees employee, boolean paymentMethod, BigDecimal totalPrice, boolean paymentStatus, String note, BigDecimal costsIncurred,boolean deleted, boolean status) {
        this.invoiceCode = invoiceCode;
        this.customer = customer;
        this.employee = employee;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.note = note;
        this.costsIncurred = costsIncurred;
        this.deleted = deleted;
        this.status = status;
    }

    public Invoices(String invoiceCode, Customers customer, Employees employee,boolean status) {
        this.invoiceCode = invoiceCode;
        this.customer = customer;
        this.employee = employee;
        this.status = status;
    }

 

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public boolean isPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(boolean paymentMethod) {
        this.paymentMethod = paymentMethod;
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
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


   
    
}
