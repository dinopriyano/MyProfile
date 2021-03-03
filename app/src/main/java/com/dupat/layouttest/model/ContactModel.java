package com.dupat.layouttest.model;

public class ContactModel implements Comparable<ContactModel> {

    String name;
    int id;
    byte[] image;

    public ContactModel(String name, int id,byte[] image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public int compareTo(ContactModel o) {
        return getName().compareTo(o.getName());
    }
}
