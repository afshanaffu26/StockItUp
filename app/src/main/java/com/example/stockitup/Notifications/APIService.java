package com.example.stockitup.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * This interface is used make a request to FCM
 */
public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAARPvODBM:APA91bFULpaY_ahA3hq1W3xEeIKSuklXVoqSdSNGgFXG5IpZjZMLulMNiTDGgVa2Md94FpgwNKQbrbG3uGYW27sgNqGsEwXWjTg2Y1IHM-bznBXjdZPddX6qQIKZ9ekOcfRk5ohg0r2t"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body RequestNotificaton requestNotification);
}

