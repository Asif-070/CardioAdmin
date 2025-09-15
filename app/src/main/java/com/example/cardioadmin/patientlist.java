package com.example.cardioadmin;

public class patientlist {
    String name,type,bt,id,imgurl;

    public patientlist(String name, String type, String bt, String id, String imgurl) {
        this.name = name;
        this.type = type;
        this.bt = bt;
        this.id = id;
        this.imgurl = imgurl;
    }

    public patientlist() {
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
