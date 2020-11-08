package com.example.stockitup.models;

/**
 * Model file for Manage Orders
 */
public class ManageOrdersModel {
    private String userId;
    private String userEmail;
    private String userName;

    /**
     * Non - parameterized constructor
     */
    public ManageOrdersModel() {
    }

    /**
     * Constructor to initialize userId, userEmail, userName of user in orders
     * @param userId Id of user
     * @param userEmail Email of user
     * @param userName Name of user
     */
    public ManageOrdersModel(String userId, String userEmail, String userName) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    /**
     * Getter method to get userId of orders
     * @return userId of orders
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter method to set the userId for orders
     * @param userId the userId to be set to orders.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter method to get userEmail of orders
     * @return userEmail of orders
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Setter method to set the userEmail for orders
     * @param userEmail the userEmail to be set to orders.
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Getter method to get userName of orders
     * @return userName of orders
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method to set the userName for orders
     * @param userName the userName to be set to orders.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
