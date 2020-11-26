package com.example.jadaa.models;

public class ModelPost {

    String BookAuthor;
    String BookDescription;
    String BookImage;
    String BookPrice;
    String BookStatus;
    String BookTitle;
    String College;
    String CommentCounter;
    String PostDate;
    String PostTime;
    String Publisher;
    String uid;
    String BookEdition;
    String pId;
    String uEmail;
    String purchaseDate;
    String purchaseTime;
    String BookID;
    ModelComment Comment;


    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String uEmail, ModelComment comment, String pTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        PostDate = postDate;
        PostTime = postTime;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.uEmail = uEmail;
        Comment = comment;
        this.pTime = pTime;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String commentCounter, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String uEmail, String purchaseDate, String purchaseTime, String bookID, ModelComment comment, String pTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        CommentCounter = commentCounter;
        PostDate = postDate;
        PostTime = postTime;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.uEmail = uEmail;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        BookID = bookID;
        Comment = comment;
        this.pTime = pTime;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String postDate, String postTime, String uid, String bookEdition, String pId, String bookID, String pTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        PostDate = postDate;
        PostTime = postTime;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        BookID = bookID;
        this.pTime = pTime;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String commentCounter, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String uEmail, String purchaseDate, String purchaseTime, String bookID, String pTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        CommentCounter = commentCounter;
        PostDate = postDate;
        PostTime = postTime;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.uEmail = uEmail;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        BookID = bookID;
        this.pTime = pTime;
    }

    String pTime;

    public String getuEmail() {
        return uEmail;
    }


    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String commentCounter, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String uEmail, String purchaseDate, String purchaseTime, String bookID) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        CommentCounter = commentCounter;
        PostDate = postDate;
        PostTime = postTime;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.uEmail = uEmail;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        BookID = bookID;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String commentCounter, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String uEmail, String purchaseDate, String purchaseTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        CommentCounter = commentCounter;
        PostDate = postDate;
        PostTime = postTime;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.uEmail = uEmail;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
    }

    public String getCommentCounter() {
        return CommentCounter;
    }

    public void setCommentCounter(String commentCounter) {
        CommentCounter = commentCounter;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String uEmail, String purchaseDate, String purchaseTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        PostDate = postDate;
        PostTime = postTime;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.uEmail = uEmail;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }



    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public ModelPost(String bookAuthor, String bookDescription, String bookImage, String bookPrice, String bookStatus, String bookTitle, String college, String postDate, String postTime, String publisher, String uid, String bookEdition, String pId, String purchaseDate, String purchaseTime) {
        BookAuthor = bookAuthor;
        BookDescription = bookDescription;
        BookImage = bookImage;
        BookPrice = bookPrice;
        BookStatus = bookStatus;
        BookTitle = bookTitle;
        College = college;
        PostDate = postDate;
        PostTime = postTime;
        String pTime;
        String purchaserID;
        String sellerID;
        Publisher = publisher;
        this.uid = uid;
        BookEdition = bookEdition;
        this.pId = pId;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;

    }





   public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }



    public ModelPost() {

    }



    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public void setBookDescription(String bookDescription) {
        BookDescription = bookDescription;
    }

    public void setBookImage(String bookImage) {
        BookImage = bookImage;
    }

    public void setBookStatus(String bookStatus) {
        BookStatus = bookStatus;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCollege(String college) {
        College = college;
    }

    public void setBookAuthor(String bookAuthor) {
        BookAuthor = bookAuthor;
    }

    public void setBookEdition(String bookEdition) {
        BookAuthor = bookEdition;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public String getBookDescription() {
        return BookDescription;
    }

    public String getBookImage() {
        return BookImage;
    }

    public String getBookStatus() {
        return BookStatus;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public String getUid() {
        return uid;
    }

    public String getCollege() {
        return College;
    }

    public String getBookAuthor() {
        return BookAuthor;
    }

    public String getBookEdition() {
        return BookEdition;
    }

    public String getPostDate() {
        return PostDate;
    }

    public String getPostTime() {
        return PostTime;
    }

    public String getPublisher() {
        return Publisher;
    }

}
