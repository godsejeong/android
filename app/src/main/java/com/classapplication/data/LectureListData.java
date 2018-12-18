package com.classapplication.data;

public class LectureListData {
    private String courseTitle;
    private String date;
    private String category;
    private String token;
    private String professor;
    private boolean viewtype;
    private String studentToken;
    private String professorToken;
    private int maxMember;
    private String time;
    private String term;
    private int currentMember;
    private int index;

    public LectureListData(String courseTitle, String date, String category, String professor, String token, String time, int maxMember, String term, String studentToken, String professorToken, int currentMember,int index, boolean viewtype) {
        this.courseTitle = courseTitle;
        this.date = date;
        this.category = category;
        this.professor = professor;
        this.viewtype = viewtype;
        this.token =token;
        this.currentMember = currentMember;
        this.studentToken = studentToken;
        this.time =time;
        this.index = index;
        this.maxMember = maxMember;
        this.term = term;
        this.professorToken = professorToken;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(int currentMember) {
        this.currentMember = currentMember;
    }

    public String getProfessorToken() {
        return professorToken;
    }

    public void setProfessorToken(String professorToken) {
        this.professorToken = professorToken;
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

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public boolean isViewtype() {
        return viewtype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
