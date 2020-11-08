package com.example.stockitup.Notifications;

/**
 * This class deals with Notification structure
 */
public class SendDataModel {

    private String body,title, intent;

    public SendDataModel(String body, String title, String intent) {
        this.body = body;
        this.title = title;
        this.intent = intent;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}