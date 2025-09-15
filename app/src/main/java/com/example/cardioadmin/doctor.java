package com.example.cardioadmin;

public class doctor {
    String name,email,pass,exp,gender,type,phone,room,time,edu,imgurl,spc,note,day;
    int age,visit;

    public doctor(String name, String email, String pass, String exp, String gender, String type, String phone, String room, String time, String edu, String imgurl, String spc, String note, int age, int visit) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.exp = exp;
        this.gender = gender;
        this.type = type;
        this.phone = phone;
        this.room = room;
        this.time = time;
        this.edu = edu;
        this.imgurl = imgurl;
        this.spc = spc;
        this.note = note;
        this.age = age;
        this.visit = visit;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
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

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public doctor() {
    }
}
