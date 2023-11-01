package com.example.models;

import java.io.Serializable;

public class Contact implements Serializable {

    private long id;
    private String name;
    private String phone;
    private String icon;

    public Contact() {
    }

    public Contact(String name, String phone, String icon) {
        this.name = name;
        this.phone = phone;
        this.icon = icon;
    }

    public Contact(long id, String name, String phone, String icon) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.icon = icon;
    }

    // Getters and setters for the ID field
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String toString(){
        return "ID: " + this.id + "\n" + this.name + "\n" + this.phone + "\n" + this.icon;
    }
}
