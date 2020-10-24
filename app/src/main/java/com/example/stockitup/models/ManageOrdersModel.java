package com.example.stockitup.models;

public class ManageOrdersModel {
    private String userId;
    private String userEmail;
    private String userName;

    public ManageOrdersModel() {
    }

    public ManageOrdersModel(String userId, String userEmail, String userName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
