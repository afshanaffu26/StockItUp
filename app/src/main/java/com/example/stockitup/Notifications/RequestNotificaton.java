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
     * This is a getter method to get data JSON
     * @return data JSON
     */
    public SendDataModel getSendDataModel() {
        return sendDataModel;
    }

    /**
     * This is a setter method used to set data JSON
     * @param sendDataModel Data JSON is passed as an object.
     */
    public void setSendDataModel(SendDataModel sendDataModel) {
        this.sendDataModel = sendDataModel;
    }
}
