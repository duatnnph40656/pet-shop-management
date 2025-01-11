/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.sql.Date;

/**
 *
 * @author dut
 */
public class Invoice {
    public int id;
    public String invoiceCode;
    public int customerId;
    public int employeeId;
    public int paymentMethodId;
    public double totalAmount;
    public boolean isPaid = false;
    public boolean isDeleted = false;
    public Date createdAt;
}
