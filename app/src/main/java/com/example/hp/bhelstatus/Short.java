package com.example.hp.bhelstatus;

public class Short {
    String mat;
    String des;
    String sho;
    String no;
    String rem;
    String pdc;
    String sid,pid,id,year,q,name,deptname;
    int n;

    public Short(){}

    public Short(String mat,String des,String sho,String no,String rem,String pdc, String sid, String pid, String id, String year, String q,String name,String deptname, int n) {
        this.mat = mat;
        this.des = des;
        this.sho = sho;
        this.no = no;
        this.rem = rem;
        this.pdc = pdc;
        this.sid = sid;
        this.pid = pid;
        this.id = id;
        this.year = year;
        this.q = q;
        this.name = name;
        this.deptname = deptname;
        this.n = n;
    }

    public String getDeptname() {
        return deptname;
    }

    public int getN() {
        return n;
    }

    public String getRem() {
        return rem;
    }

    public String getNo() {

        return no;
    }

    public String getSho() {

        return sho;
    }

    public String getDes() {

        return des;
    }

    public String getMat() {

        return mat;
    }

    public String getPdc() {

        return pdc;
    }

    public String getSid() {
        return sid;
    }

    public String getPid() {
        return pid;
    }

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getQ() {
        return q;
    }

    public String getName() {
        return name;
    }
}
