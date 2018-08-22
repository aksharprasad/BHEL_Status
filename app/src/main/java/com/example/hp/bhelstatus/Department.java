package com.example.hp.bhelstatus;

public class Department {
    String mName;
    String mID;
    int n;
    int p;

    public Department(){
    }

    public Department(String mName, String mID, int n,int p) {
        this.mName = mName;
        this.mID = mID;
        this.n = n;
        this.p = p;
    }

    public int isP() {
        return p;
    }

    public String getID() {
        return mID;
    }

    public String getName() {

        return mName;
    }

    public int getN() {
        return n;
    }
}
