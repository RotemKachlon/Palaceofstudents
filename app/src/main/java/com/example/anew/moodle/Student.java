package com.example.anew.moodle;

import java.util.ArrayList;

//קלאס המגדיר סוג משתנה חדש Student, כאשר Student הוא ירושה של User
public class Student extends User{
    private ArrayList<String> classrooms;
    private boolean isBanned;
    private String layer;

    public Student() {
    }

    public Student(String id, String username, String email, String password, String phone, String role, String idschool, boolean isBanned, String layer) {
        super(id, username, email, password, role);
        this.isBanned = isBanned;
        this.layer = layer;
    }
    public void addClass(String classId){
        if(this.classrooms == null)
            this.classrooms = new ArrayList<>();
        this.classrooms.add(classId);
    }

    public ArrayList<String> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(ArrayList<String> classrooms) {
        this.classrooms = classrooms;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}