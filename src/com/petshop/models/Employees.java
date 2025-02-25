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
public class Employees {
    private int id;
    private String employeeCode;
    private String employeeName;
    private String phoneNumber;
    private String email;
    private Roles role;
    private String address;
    private Date createdAt;
    private boolean gender;
    private boolean deleted;
    private boolean status;

    public Employees() {
    }

    public Employees(int id, String employeeCode, String employeeName, String phoneNumber, String email, Roles role, String address, Date createdAt, boolean gender, boolean deleted, boolean status) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.address = address;
        this.createdAt = createdAt;
        this.gender = gender;
        this.deleted = deleted;
        this.status = status;
    }

    public Employees(int id, String employeeName) {
        this.id = id;
        this.employeeName = employeeName;
        
    }

    public Employees(int id, String employeeCode, String employeeName, String phoneNumber, String email) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
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
    
   
    
}
