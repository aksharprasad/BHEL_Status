package com.example.hp.bhelstatus;

public class Project  {
    String mName;
    String mID;

    public Project(){
    }

    public Project(String mName, String mID) {
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
