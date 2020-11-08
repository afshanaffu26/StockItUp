package com.example.stockitup.Notifications;

import com.google.gson.annotations.SerializedName;

/**
 * This is model file for notification
 */
public class RequestNotificaton {

    @SerializedName("to")
    private String token;

    @SerializedName("notification")
    private SendNotificationModel sendNotificationModel;

    @SerializedName("data")
    private SendDataModel sendDataModel;

    /**
     * This method is a getter.
     * It is used to retrieve notification model
     * @return Notification Model
     */
    public SendNotificationModel getSendNotificationModel() {
        return sendNotificationModel;
    }

    /**
     * This method is setter
     * It is used to set the notification model
     * @param sendNotificationModel notification model to be set
     */
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
