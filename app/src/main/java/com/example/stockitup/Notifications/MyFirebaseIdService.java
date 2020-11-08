package com.example.stockitup.Notifications;

import com.example.stockitup.utils.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * This class deals with background services
 */
public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null && firebaseUser.getEmail()!= AppConstants.ADMIN_EMAIL){
            new SendNotification().updateToken(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }
}
