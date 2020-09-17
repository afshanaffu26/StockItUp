package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.models.OrdersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * This class deals with the payment to place the order
 */
public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPay;
    FirebaseFirestore firebaseFirestore;
    String uid;
    String subTotal, deliveryCharge, tax, total;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Payment is being Processed, Please don't refresh", Toast.LENGTH_SHORT).show();
        cartCheckout();
    }

    /**
     * This method is used to checkout the items added to cart
     */
    private void cartCheckout() {
        final String docId = "" + UUID.randomUUID().toString();
        //fetch the recent address added
        firebaseFirestore.collection("Address").document(uid).get().addOnSuccessListener(new OnSuccessListener < DocumentSnapshot > () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            address = documentSnapshot.get("address").toString();
        }
        });
        //fetch cart items
        firebaseFirestore.collection("Cart").document("cart" + uid).collection("cart").get().addOnCompleteListener(
                new OnCompleteListener < QuerySnapshot > () {
                    @Override
                    public void onComplete(@NonNull Task < QuerySnapshot > task) {
                    if (task.isSuccessful()) {
                        //iterating through each cart item
                        for (DocumentSnapshot documentSnapshot: task.getResult()) {
                            CategoryItemsModel cuisineItemsModel = documentSnapshot.toObject(CategoryItemsModel.class);
                            //adding each cart item to orders collection inside a particular docId
                            firebaseFirestore.collection("Orders").document("orders" + uid).collection("orders").document(docId).collection("Order").add(cuisineItemsModel).addOnSuccessListener(new OnSuccessListener < DocumentReference > () {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                String date = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());
                                subTotal = getIntent().getStringExtra("subTotal");
                                tax = getIntent().getStringExtra("tax");
                                deliveryCharge = getIntent().getStringExtra("deliveryCharge");
                                total = getIntent().getStringExtra("total");

                                OrdersModel ordersModel = new OrdersModel(date, subTotal, tax, deliveryCharge, total, address);
                                //adding orders data to that particular docId
                                firebaseFirestore.collection("Orders").document("orders" + uid).collection("orders").document(docId).set(ordersModel).addOnSuccessListener(new OnSuccessListener < Void > () {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //get cart details to delete each item
                                        firebaseFirestore.collection("Cart").document("cart" + uid).collection("cart").get().addOnCompleteListener(new OnCompleteListener < QuerySnapshot > () {
                                        @Override
                                        public void onComplete(@NonNull Task < QuerySnapshot > task) {
                                            if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Order placed successfully.", Toast.LENGTH_SHORT).show();
                                            for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                                                //delete each cart item
                                                firebaseFirestore.collection("Cart").document("cart" + uid).collection("cart").document(queryDocumentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener < Void > () {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                                                }
                                                });
                                            }
                                        }
                                    }
                                    });
                                }
                                });
                            }
                            });

                        }
                    }
                }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            });
    }

    /**
     * This method is called whenever the user chooses to navigate up within your application's activity hierarchy from the action bar.
     * @return boolean:true if Up navigation completed successfully and this Activity was finished, false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}