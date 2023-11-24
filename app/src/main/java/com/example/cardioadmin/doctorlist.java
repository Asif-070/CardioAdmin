package com.example.cardioadmin;

public class doctorlist {
    String name,spc,exp,imgurl,id;
    int visit;

    public doctorlist() {
    }

    public doctorlist(String name, String spc, String exp, String imgurl, String id, int visit) {
        this.name = name;
        this.spc = spc;
        this.exp = exp;
        this.imgurl = imgurl;
        this.id = id;
        this.visit = visit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

}
