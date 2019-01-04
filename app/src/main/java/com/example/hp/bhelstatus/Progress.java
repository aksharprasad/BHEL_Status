package com.example.hp.bhelstatus;

public class Progress {
    private String fieldName;
    private String fromValue;
    private String toValue;
    private int n;
    private String id;
    private String pid;
    private String projectName;
    private String deptName;
    private int number;
    private int type;
    private int admin;

    public Progress(){
    }

    public Progress(String id, String pid, String fieldName, String fromValue, String toValue, int n, String projectName, String deptName, int number,int type,int admin) {
        this.fieldName = fieldName;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.n = n;
        this.pid = pid;
        this.id = id;
        this.projectName = projectName;
        this.deptName = deptName;
        this.number = number;
        this.type = type;
        this.admin = admin;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFromValue() {
        return fromValue;
    }

    public String getToValue() {
        return toValue;
    }

    public int getN() {
        return n;
    }

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFromValue(String fromValue) {
        this.fromValue = fromValue;
    }

    public void setToValue(String toValue) {
        this.toValue = toValue;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {

        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
