package com.example.cardioadmin;

public class amb_items {
    String Name;
    String Phone;

    public amb_items() {
    }

    public amb_items(String name, String phone) {
        Name = name;
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
