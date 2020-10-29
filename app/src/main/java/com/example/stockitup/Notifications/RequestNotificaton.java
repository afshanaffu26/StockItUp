package com.example.stockitup.Notifications;

import com.google.gson.annotations.SerializedName;

public class RequestNotificaton {

    @SerializedName("to")
    private String token;

    @SerializedName("notification")
    private SendNotificationModel sendNotificationModel;

    @SerializedName("data")
    private SendDataModel sendDataModel;

    public SendNotificationModel getSendNotificationModel() {
        return sendNotificationModel;
    }

    public void setSendNotificationModel(SendNotificationModel sendNotificationModel) {
        this.sendNotificationModel = sendNotificationModel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SendDataModel getSendDataModel() {
        return sendDataModel;
    }

    public void setSendDataModel(SendDataModel sendDataModel) {
        this.sendDataModel = sendDataModel;
    }
}
