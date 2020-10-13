package com.example.stockitup.models;

public class ManageOrdersModel {
    private String userId;
    private String userEmail;

    public ManageOrdersModel() {
    }

    public ManageOrdersModel(String userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
