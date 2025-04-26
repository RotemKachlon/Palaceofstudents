package com.example.anew.moodle;

import java.util.ArrayList;

//קלאס המגדיר סוג משתנה חדש Teacher, כאשר Teacher הוא ירושה של המשתמש האידאלי User
public class Teacher extends User{
    private String professionId;
    private boolean isBanned;
    private ArrayList<String> classesArrayList;

    private String schoolID;

    public Teacher(String id, String username, String email, String password, String phone, String role, String idschool) {
        super(id, username, email, password, role);
        this.schoolID = idschool;
    }
    public void addClass(String classId){
        if(this.classesArrayList == null)
            this.classesArrayList = new ArrayList<>();
        this.classesArrayList.add(classId);
    }

    public Teacher() {
    }

    public ArrayList<String> getClassesArrayList() {
        return classesArrayList;
    }

    public void setClassesArrayList(ArrayList<String> classesArrayList) {
        this.classesArrayList = classesArrayList;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }
}
