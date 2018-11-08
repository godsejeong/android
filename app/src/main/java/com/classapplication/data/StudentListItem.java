package com.classapplication.data;

public class StudentListItem {
    private String name;
    private String token;
    private String attendance;
    private String mytoken;

    public StudentListItem(String name,String token,String mytoken,String attendance){
        this.name = name;
        this.token = token;
        this.mytoken = mytoken;
        this.attendance = attendance;
    }

    public String getMytoken() {
        return mytoken;
    }

    public void setMytoken(String mytoken) {
        this.mytoken = mytoken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
