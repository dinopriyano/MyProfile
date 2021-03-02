package com.dupat.layouttest.model;

public class ContactModel {

    String name;
    int id;

    public ContactModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
