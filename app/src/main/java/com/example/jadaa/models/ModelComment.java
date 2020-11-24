package com.example.jadaa.models;

public class ModelComment {

    public ModelComment(String commentDate, String cId, String comment, String commentTime, String timeStamp, String uid) {
        CommentDate = commentDate;
        this.cId = cId;
        this.comment = comment;
        this.commentTime = commentTime;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }

    String CommentDate;
    String cId;
    String comment;
    String commentTime;
    String timeStamp;
    String uid;


    public ModelComment(){}

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}


