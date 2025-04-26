package com.example.anew.moodle;

import java.util.ArrayList;

//קלאס המגדיר משתנה חדש Task, המשומש להחזקת פרטי המשימה שהזין המורה
public class StudentTask {
    private String id;
    private String fileUrl;
    private String subTheme;
    private String subject;
    private String profession;
    private ArrayList<String> understand;
    private ArrayList<String> dontUnderstand;
    private int grade;
    private int numOfStudents;



    public StudentTask(String id, String fileUrl, String subTheme, String subject, String profession) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.subTheme = subTheme;
        this.subject = subject;
        this.profession = profession;
        this.grade = 0;
        this.numOfStudents = 0;
    }
    public void addUnderstand(String id){
        if(this.dontUnderstand==null)
            this.dontUnderstand = new ArrayList<>();
        this.dontUnderstand.add(id);
    }
    public void addDontUnderstand(String id){
        if(this.understand==null)
            this.understand = new ArrayList<>();
        this.understand.add(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getSubTheme() {
        return subTheme;
    }

    public void setSubTheme(String subTheme) {
        this.subTheme = subTheme;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public ArrayList<String> getUnderstand() {
        return understand;
    }

    public void setUnderstand(ArrayList<String> understand) {
        this.understand = understand;
    }

    public ArrayList<String> getDontUnderstand() {
        return dontUnderstand;
    }

    public void setDontUnderstand(ArrayList<String> dontUnderstand) {
        this.dontUnderstand = dontUnderstand;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getNumOfStudents() {
        return numOfStudents;
    }

    public void setNumOfStudents(int numOfStudents) {
        this.numOfStudents = numOfStudents;
    }

    public double Avg  ()
    {
        return (double)(grade)/(int)(numOfStudents);
    }


    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", subTheme='" + subTheme + '\'' +
                ", subject='" + subject + '\'' +
                ", profession='" + profession + '\'' +
                ", understand=" + understand +
                ", dontUnderstand=" + dontUnderstand +
                ", grade=" + grade +
                ", numOfStudents=" + numOfStudents +
                '}';
    }
}
