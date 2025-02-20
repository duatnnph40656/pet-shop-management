/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author dut
 */
public class Pets {
    private int id;
    private String petCode;
    private String petName;
    private Customers customer;
    private TypePets typePet;
    private String breed;
    private BigDecimal weight;
    private String color;
    private boolean gender;
    private boolean vaccinated;
    private Date createdAt;
    private boolean deleted;
    private boolean status;
    private String owner;
    private String age;

    public Pets(int id, String petCode, String petName, Customers customer, TypePets typePet, String breed, BigDecimal weight, String color, boolean gender, boolean vaccinated, Date createdAt, boolean deleted, boolean status, String owner, String age) {
        this.id = id;
        this.petCode = petCode;
        this.petName = petName;
        this.customer = customer;
        this.typePet = typePet;
        this.breed = breed;
        this.weight = weight;
        this.color = color;
        this.gender = gender;
        this.vaccinated = vaccinated;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.status = status;
        this.owner = owner;
        this.age = age;
    }

    public Pets() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetCode() {
        return petCode;
    }

    public void setPetCode(String petCode) {
        this.petCode = petCode;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public TypePets getTypePet() {
        return typePet;
    }

    public void setTypePet(TypePets typePet) {
        this.typePet = typePet;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    
}
