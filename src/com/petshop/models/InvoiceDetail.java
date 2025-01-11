/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

/**
 *
 * @author dut
 */
public class InvoiceDetail {

    private Invoice invoice; // Đối tượng Invoice chứa invoiceId
    public int productDetailId;
    public int petId;
    public int serviceId;
    public int serviceQuantity;
    public int productQuantity;
    public int itemType;
    public double unitPrice;
    public boolean isDeleted = false;
    public boolean isActive = true;
}
