package com.example.hp.bhelstatus;

public class User {
    private String username;
    private String uid;
    private String email;
    private int admin;
    private String password;

    public User(){}

    public User(int admin,String email,String uid,String username,String password) {
        this.username = username;
        this.uid = uid;
        this.email = email;
        this.admin = admin;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public int getAdmin() {
        return admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
