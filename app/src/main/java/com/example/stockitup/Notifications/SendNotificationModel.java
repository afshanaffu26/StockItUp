package com.example.stockitup.Notifications;

/**
 * This class is a model file for notification
 */
public class SendNotificationModel {
    private String body,title;

    /**
     * Constructor to initialize required fields for notification JSON
     * @param body Body message to be shown in notification
     * @param title Title of notification
     */
    public SendNotificationModel(String body, String title) {
        this.body = body;
        this.title = title;
    }

    /**
     * This is a getter method to get body of notification
     * @return body message of notification
     */
    public String getBody() {
        return body;
    }

    /**
     * This is a setter method to set body of notification
     * @param body Message to set to notification
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * This is a getter method to get Title of notification
     * @return Title of Notification
     */
    public String getTitle() {
        return title;
    }

    /**
     * This is a setter method to set title of notification
     * @param title Title to set to notification
     */
    public void setTitle(String title) {
        this.title = title;
    }
}