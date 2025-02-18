/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.sql.Date;

/**
 *
 * @author duat
 */
public class PetCareServices {
    private int id;
    private PetServices petS;
    private Pets pet;
    private Date dateStart;
    private Date dateEnd;
    private Date actualEnd;
    private String note;
    private Date createdAt;
    private boolean deleted;
    private boolean status;

    public PetCareServices() {
    }

    public PetCareServices(int id, PetServices petS, Pets pet, Date dateStart, Date dateEnd, Date actualEnd, String note, Date createdAt, boolean deleted, boolean status) {
        this.id = id;
        this.petS = petS;
        this.pet = pet;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.actualEnd = actualEnd;
        this.note = note;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PetServices getPetS() {
        return petS;
    }

    public void setPetS(PetServices petS) {
        this.petS = petS;
    }

    public Pets getPet() {
        return pet;
    }

    public void setPet(Pets pet) {
        this.pet = pet;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(Date actualEnd) {
        this.actualEnd = actualEnd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
    
    
    
}
