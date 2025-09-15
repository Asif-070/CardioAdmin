package com.example.cardioadmin;

public class Dataclass {
    private String Id, Name, Time, Room, Day, Email, Pass, Spc, Imgurl;
    private int Visit,Age;
    private String Exp, Gender, Phone, Edu, Note;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public Dataclass(String id, String name, String time, String room, String day, String email, String pass, String spc, String imgurl, int visit, int age, String exp, String gender, String phone, String edu, String note) {
        Id = id;
        Name = name;
        Time = time;
        Room = room;
        Day = day;
        Email = email;
        Pass = pass;
        Spc = spc;
        Imgurl = imgurl;
        Visit = visit;
        Age = age;
        Exp = exp;
        Gender = gender;
        Phone = phone;
        Edu = edu;
        Note = note;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSpc() {
        return Spc;
    }

    public void setSpc(String spc) {
        Spc = spc;
    }

    public String getEdu() {
        return Edu;
    }

    public void setEdu(String edu) {
        Edu = edu;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getExp() {
        return Exp;
    }

    public void setExp(String exp) {
        Exp = exp;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public Dataclass() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String imgurl) {
        Imgurl = imgurl;
    }

    public int getVisit() {
        return Visit;
    }

    public void setVisit(int visit) {
        Visit = visit;
    }
}
