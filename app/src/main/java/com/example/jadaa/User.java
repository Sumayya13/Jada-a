package com.example.jadaa;

public class User {

    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String uid;
    private String image;
    private String cover;


    private String numAllBooks; // كل البوست الكتب اللي انباعت واللي ما انباعت
    private String numFreeBooks; //
    private String numPayedBooks; //


    private String numAllSoldBooks; // مجموع الكتب اللي انباعت كلها
    private String numFreeSoldBooks; // اللي مجانا وانباعت
    private String numPayedSoldBooks; // اللي بفلوس وانباعت

    public User(String email, String password, String fullName, String phone, String uid, String image, String cover, String numAllBooks, String numFreeBooks, String numPayedBooks, String numAllSoldBooks, String numFreeSoldBooks, String numPayedSoldBooks) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.uid = uid;
        this.image = image;
        this.cover = cover;
        this.numAllBooks = numAllBooks;
        this.numFreeBooks = numFreeBooks;
        this.numPayedBooks = numPayedBooks;
        this.numAllSoldBooks = numAllSoldBooks;
        this.numFreeSoldBooks = numFreeSoldBooks;
        this.numPayedSoldBooks = numPayedSoldBooks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNumAllBooks() {
        return numAllBooks;
    }

    public void setNumAllBooks(String numAllBooks) {
        this.numAllBooks = numAllBooks;
    }

    public String getNumFreeBooks() {
        return numFreeBooks;
    }

    public void setNumFreeBooks(String numFreeBooks) {
        this.numFreeBooks = numFreeBooks;
    }

    public String getNumPayedBooks() {
        return numPayedBooks;
    }

    public void setNumPayedBooks(String numPayedBooks) {
        this.numPayedBooks = numPayedBooks;
    }

    public String getNumAllSoldBooks() {
        return numAllSoldBooks;
    }

    public void setNumAllSoldBooks(String numAllSoldBooks) {
        this.numAllSoldBooks = numAllSoldBooks;
    }

    public String getNumFreeSoldBooks() {
        return numFreeSoldBooks;
    }

    public void setNumFreeSoldBooks(String numFreeSoldBooks) {
        this.numFreeSoldBooks = numFreeSoldBooks;
    }

    public String getNumPayedSoldBooks() {
        return numPayedSoldBooks;
    }

    public void setNumPayedSoldBooks(String numPayedSoldBooks) {
        this.numPayedSoldBooks = numPayedSoldBooks;
    }

    public User() {
    }
}
