package com.example.anew.moodle;

import java.io.Serializable;

//קלאס המגדיר משתנה חדש User, מתוך User נוצרות הירושות של סוגי המשתמשים האחרים Student וTeacher, וכמו כן - User הוא סוג המשתמש שבו האפליקציה מגדירה את סוג המשתמש של מנהל בית ספר
public class User implements Serializable {
    private String id;
    private String username;
    private String email;
    private String password;
    private String role;


    public User(String id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
