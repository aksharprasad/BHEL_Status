package com.example.hp.bhelstatus;

public class UserRequest {

    private String name;
    private String eid;
    private String email;
    private String cred;
    private String dob;
    private String id;

    public UserRequest(){}

    public UserRequest(String name,String eid,String email,String cred,String dob,String id) {
        this.eid = eid;
        this.name = name;
        this.email = email;
        this.cred = cred;
        this.dob = dob;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEid() {
        return eid;
    }

    public String getEmail() {
        return email;
    }

    public String getCred() {
        return cred;
    }

    public String getDob() {
        return dob;
    }

    public String getId() {
        return id;
    }
}
