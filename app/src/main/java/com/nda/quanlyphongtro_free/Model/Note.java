package com.nda.quanlyphongtro_free.Model;

public class Note {
    private String id, userID;
    private String title, content, createdDate, backgroundColor;

    public Note(){}

    public Note(String id, String userID, String title, String content, String createdDate, String backgroundColor) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.backgroundColor = backgroundColor;
    }

    public String getId() {
        return id;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
