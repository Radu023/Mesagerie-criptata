package com.example.mesagerie_criptata;

public class Contacts {

    public String uid,name,image,status;

    public Contacts() {
    }

    public Contacts(String uid, String name, String image, String status) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
