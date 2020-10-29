package com.example.stockitup.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.stockitup.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_ONE_ID ="channel_one_id";
    public static final String CHANNEL_TWO_ID ="channel_one_two";
    PendingIntent pendingIntent = null;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channelOne=new NotificationChannel(CHANNEL_ONE_ID,
                    "Channel One",
                    NotificationManager.IMPORTANCE_HIGH);

            channelOne.setDescription("This is channel one for notifications");

            NotificationChannel channelTwo=new NotificationChannel(CHANNEL_TWO_ID,
                    "Channel Two",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channelTwo.setDescription("This is channel two for notifications");

            NotificationManager manager= (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            List<NotificationChannel> channels=new ArrayList<>();
            channels.add(channelOne);
            channels.add(channelTwo);

            manager.createNotificationChannels(channels);


        }

        if(remoteMessage.getData().size() >0){
            Log.d("Notification",remoteMessage.getData().toString());
            if(remoteMessage.getData().get("intent") != null) {
                Intent intent = null;
                try {
                    intent = new Intent().getIntent(remoteMessage.getData().get("intent"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                pendingIntent = PendingIntent.getActivity(this, 1,intent,PendingIntent.FLAG_CANCEL_CURRENT );
            }

        }

        if(remoteMessage.getNotification() != null) {
            Log.d("Notification",remoteMessage.getNotification().toString());
            Notification notification;
            if(pendingIntent != null) {
                 notification = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
            }else{
                 notification = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .build();
            }
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(123, notification);
        }


    }
}
