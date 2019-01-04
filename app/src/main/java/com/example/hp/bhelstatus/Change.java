package com.example.hp.bhelstatus;

public class Change {
    String c;
    long time;

    public Change(){};

    public Change(String c, long time) {
        this.c = c;
        this.time = time;
    }

    public String getC() {
        return c;
    }

    public long getTime() {
        return time;
    }
}
