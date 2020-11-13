package com.example.stockitup.Notifications;

/**
 * This class deals with Notification structure
 */
public class SendDataModel {

    private String body,title, intent;

    /**
     * Constructor to initialize required fields for data JSON
     * @param body Body message to be shown in notification
     * @param title Title of notification
     * @param intent Intent object for notification.
     */
    public SendDataModel(String body, String title, String intent) {
        this.body = body;
        this.title = title;
        this.intent = intent;
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

    /**
     * This is a getter method to get intent for notification
     * @return Intent object for notification
     */
    public String getIntent() {
        return intent;
    }

    /**
     * This is a setter method to set intent for notification
     * @param intent Intent object for notification
     */
    public void setIntent(String intent) {
        this.intent = intent;
    }
}