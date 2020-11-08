package com.example.jadaa.models;

public class soldBooks {

    String BookPrice;
    String BookTitle;
    String delivered;
    String inTransit;
    String pId;
    String processing;
    String purchaseDate;
    String purchaseTime;
    String purchaserID;
    String sellerID;
    String shipped;
    String orderConfirmation;
    String uri;


    public String getOrderConfirmation() {
        return orderConfirmation;
    }

    public void setOrderConfirmation(String orderConfirmation) {
        this.orderConfirmation = orderConfirmation;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public soldBooks(){}

    public soldBooks(String bookPrice, String bookTitle, String delivered, String inTransit, String pId, String processing, String purchaseDate, String purchaseTime, String purchaserID, String sellerID, String shipped,String uri , String orderConfirmation) {
        BookPrice = bookPrice;
        BookTitle = bookTitle;
        this.delivered = delivered;
        this.inTransit = inTransit;
        this.pId = pId;
        this.processing = processing;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        this.purchaserID = purchaserID;
        this.sellerID = sellerID;
        this.shipped = shipped;
        this.uri=uri;
        this.orderConfirmation=orderConfirmation;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getInTransit() {
        return inTransit;
    }

    public void setInTransit(String inTransit) {
        this.inTransit = inTransit;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
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

    public String getPurchaserID() {
        return purchaserID;
    }

    public void setPurchaserID(String purchaserID) {
        this.purchaserID = purchaserID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }








}
