package com.classapplication.data;

public class MyLetureItem {
    private String courseTitle;
    private String date;
    private String token;
    private String img;
    private String professorToken;

    public String getProfessorToken() {
        return professorToken;
    }

    public void setProfessorToken(String professorToken) {
        this.professorToken = professorToken;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
