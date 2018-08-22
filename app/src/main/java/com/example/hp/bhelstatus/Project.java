package com.example.hp.bhelstatus;

public class Project  {
    String mName;
    String mID;
    String mpID;
    int n;
    String deptname;

    public Project(){
    }

    public Project(String mName, String mID, String mpID,int n,String deptname) {
        this.mName = mName;
        this.mID = mID;
        this.n = n;
        this.mpID = mpID;
        this.deptname = deptname;
    }

    public String getDeptname() {
        return deptname;
    }

    public String getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getpID(){
        return mpID;
    }

    public int getN() {
        return n;
    }
}
