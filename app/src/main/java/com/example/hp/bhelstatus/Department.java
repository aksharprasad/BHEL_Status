package com.example.hp.bhelstatus;

public class Department {
    String mName;
    String mID;

    public Department(){
    }

    public Department(String mName, String mID) {
        this.mName = mName;
        this.mID = mID;
    }

    public String getID() {
        return mID;
    }

    public String getName() {

        return mName;
    }
}
