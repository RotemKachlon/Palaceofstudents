package com.example.anew.moodle;

import java.util.ArrayList;

//קלאס המגדיר סוג משתנה חדש Classroom
public class Classroom {
    private String id;
    private String className;
    private ArrayList<String> studentsId;
    private String teacherID;
    private ArrayList<StudentTask> tasks;

    public Classroom() {
    }

    public Classroom(String id, String className, String teacherID) {
        this.id = id;
        this.className = className;
        this.teacherID = teacherID;
        this.tasks = null;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public ArrayList<StudentTask> getTasks() {
        return tasks;
    }
    public void AddTask(StudentTask task){
        if (this.tasks== null)
            this.tasks = new ArrayList<>();
        this.tasks.add(task);
    }
    public void AddStudend(String s){
        if (this.studentsId== null) {
            this.studentsId = new ArrayList<>();
        }
        this.studentsId.add(s);
    }

    public void setTasks() {
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<String> getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(ArrayList<String> studentsId) {
        this.studentsId = studentsId;
    }

    public void setTasks(ArrayList<StudentTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", teacherID=" + teacherID +
                ", studentsId=" + studentsId +
                '}';
    }
}
