package com.example.stockitup.Notifications;

/**
 * This class is a model file for token
 */
public class Token {
    private String token;
    private String userID;

    public Token(String token, String userID) {
        this.token = token;
        this.userID = userID;
    }

    public Token() {
    }

    /**
     * Getter method used to get the device token
     * @return returns device token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter method used to set the device token
     * @param token device token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Getter method used to get the user ID
     * @return returns user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Setter method used to set the user ID
     * @param userID Unique user auth ID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
