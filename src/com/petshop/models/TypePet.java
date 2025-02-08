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
public class TypePet {
    
    private int id;
    private String typePetCode;
    private String typePetName;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public TypePet() {
    }

    public TypePet(int id, String typePetCode, String typePetName) {
        this.id = id;
        this.typePetCode = typePetCode;
        this.typePetName = typePetName;
    }

    public TypePet(int id, String typePetCode, String typePetName, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.typePetCode = typePetCode;
        this.typePetName = typePetName;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public TypePet(String typePetCode, String typePetName, boolean deleted, boolean status) {
        this.typePetCode = typePetCode;
        this.typePetName = typePetName;
        this.deleted = deleted;
        this.status = status;
    }

    
    
    public int getId() {
        return id;
    }

    public String getTypePetCode() {
        return typePetCode;
    }

    public String getTypePetName() {
        return typePetName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTypePetCode(String typePetCode) {
        this.typePetCode = typePetCode;
    }

    public void setTypePetName(String typePetName) {
        this.typePetName = typePetName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isStatus() {
        return status;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return typePetName;
    }
    
    
}
