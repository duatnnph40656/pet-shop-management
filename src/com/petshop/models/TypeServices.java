/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.util.Date;

/**
 *
 * @author duat
 */
public class TypeServices {
    private int id;
    private String typeServiceCode;
    private String typeServiceName;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public TypeServices() {
    }

    public TypeServices(int id, String typeServiceCode, String typeServiceName, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.typeServiceCode = typeServiceCode;
        this.typeServiceName = typeServiceName;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public TypeServices(String typeServiceCode, String typeServiceName, boolean deleted, boolean status) {
        this.typeServiceCode = typeServiceCode;
        this.typeServiceName = typeServiceName;
        this.deleted = deleted;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTypeServiceCode() {
        return typeServiceCode;
    }

    public String getTypeServiceName() {
        return typeServiceName;
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

    public void setTypeServiceCode(String typeServiceCode) {
        this.typeServiceCode = typeServiceCode;
    }

    public void setTypeServiceName(String typeServiceName) {
        this.typeServiceName = typeServiceName;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return typeServiceName;
    }
    
}
