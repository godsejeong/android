package com.classapplication.data;

public class ChatData {
    private String send;
    private String resive;
    private String time;
    private String name;
    private boolean viewtype;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isViewtype() {
        return viewtype;
    }

    public ChatData(String send, String resive, String name, String time, boolean viewtype){
        this.send = send;
        this.resive = resive;
        this.viewtype = viewtype;
        this.time = time;
        this.name = name;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getResive() {
        return resive;
    }

    public void setResive(String resive) {
        this.resive = resive;
    }

    public boolean getViewtype() {
        return viewtype;
    }

    public void setViewtype(boolean viewtype) {
        this.viewtype = viewtype;
    }
}
