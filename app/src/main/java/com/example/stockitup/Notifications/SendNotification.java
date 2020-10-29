package com.example.stockitup.Notifications;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotification extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private APIService apiService;

    public void sendNotification(String userID, final String title, final String message, final Intent intent){
        firebaseFirestore = FirebaseFirestore.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        firebaseFirestore.collection(AppConstants.USER_COLLECTION).document("user"+userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Token token = documentSnapshot.toObject(Token.class);
                    if(token != null){
                        String usertoken=token.getToken();
                        if(usertoken != null)
                            sendNotifications(usertoken, title,message,intent);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void updateToken(String userID){
        firebaseFirestore = FirebaseFirestore.getInstance();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken,userID);
        firebaseFirestore.collection(AppConstants.USER_COLLECTION).document("user"+userID)
                .set(token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void sendNotifications(String usertoken, String title, String message, Intent intent) {
        SendNotificationModel sendNotificationModel = new SendNotificationModel(message, title);
        SendDataModel sendDataModel = new SendDataModel(message, title, intent.toUri(Intent.URI_INTENT_SCHEME));
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        requestNotificaton.setSendDataModel(sendDataModel);
        requestNotificaton.setToken(usertoken);
        apiService.sendNotifcation(requestNotificaton).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
            }
        });
    }

}

