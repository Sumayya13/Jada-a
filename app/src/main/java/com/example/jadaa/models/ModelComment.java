package com.example.jadaa.models;

public class ModelComment {


    String CommentDate;
    String cId;
    String comment;
    String commentTime;
    String timeStamp;
    String uid;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    String pId;


    public ModelComment(String commentDate, String cId, String comment, String commentTime, String timeStamp, String uid, String pId) {
        CommentDate = commentDate;
        this.cId = cId;
        this.comment = comment;
        this.commentTime = commentTime;
        this.timeStamp = timeStamp;
        this.uid = uid;
        this.pId = pId;
    }

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


