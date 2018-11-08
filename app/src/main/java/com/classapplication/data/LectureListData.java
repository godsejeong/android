package com.classapplication.data;

public class LectureListData {
    private String name;
    private String day;
    private String room;
    private String token;
    private String professor;
    private boolean viewtype;
    private String studentToken;
    private String img;
    private String professorToken;

    public LectureListData(String name, String day, String room, String professor, String token, String studentToken, String img, String professorToken, boolean viewtype) {
        this.name = name;
        this.day = day;
        this.room = room;
        this.professor = professor;
        this.viewtype = viewtype;
        this.token =token;
        this.studentToken = studentToken;
        this.img =img;
        this.professorToken = professorToken;
    }


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

    public String getStudentToken() {
        return studentToken;
    }

    public void setStudentToken(String studentToken) {
        this.studentToken = studentToken;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getViewtype() {
        return viewtype;
    }

    public void setViewtype(boolean viewtype) {
        this.viewtype = viewtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
