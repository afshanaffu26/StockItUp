package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.Notifications.SendNotification;
import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.example.stockitup.utils.JavaMailAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is related to admin.It deals with updation of order status
 */

public class AdminUpdateOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private String userDocumentId,orderDocumentId;
    private String date,subtotal,tax,deliveryCharge,total,address,status,offer,offerPercent,userEmail,userName,userID;
    private TextView txtOrderDate,txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtAddress,txtStatus,txtOffer,txtOrderId;
    private FirebaseAuth firebaseAuth;
    private ImageView imgEditStatus,imgStatus;
    private Button btnViewOrderDetails;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_order);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtSubTotal = findViewById(R.id.txtSubTotal);
        txtOffer = findViewById(R.id.txtOffer);
        txtTax = findViewById(R.id.txtTax);
        txtDeliveryCharge = findViewById(R.id.txtDeliveryCharge);
        txtTotal = findViewById(R.id.txtTotal);
        txtAddress = findViewById(R.id.txtAddress);
        txtStatus = findViewById(R.id.txtStatus);
        txtOrderId = findViewById(R.id.txtOrderId);
        spinner = (Spinner) findViewById(R.id.spinner);
        imgEditStatus = findViewById(R.id.imgEditStatus);
        imgEditStatus.setOnClickListener(this);
        imgStatus = findViewById(R.id.imgStatus);
        imgStatus.setOnClickListener(this);
        imgStatus.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        btnViewOrderDetails = findViewById(R.id.btnViewOrderDetails);
        btnViewOrderDetails.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        userDocumentId = getIntent().getStringExtra("userDocumentId");
        orderDocumentId = getIntent().getStringExtra("orderDocumentId");
        userID = getIntent().getStringExtra("userID");
        userEmail = getIntent().getStringExtra("userEmail");
        userName = getIntent().getStringExtra("userName");
        status = getIntent().getStringExtra("status");
        date = getIntent().getStringExtra("date");
        subtotal = getIntent().getStringExtra("subtotal");
        offerPercent = getIntent().getStringExtra("offerPercent");
        offer = getIntent().getStringExtra("offer");
        tax = getIntent().getStringExtra("tax");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");
        address = getIntent().getStringExtra("address");

        txtOrderDate.setText("Ordered On: "+date);
        txtSubTotal.setText("Subtotal: "+subtotal+"$");
        double off = Double.parseDouble(offerPercent);
        if (!(off == (double)0.0)){
            txtOffer.setText("Offer (-" + offerPercent + "%): -" + offer + "$");
            txtOffer.setVisibility(View.VISIBLE);
        }
        else {
            txtOffer.setVisibility(View.GONE);
        }
        txtTax.setText("Tax: "+tax+"$");
        txtDeliveryCharge.setText("Delivery Charge: "+deliveryCharge+"$");
        txtTotal.setText("Total: "+total+"$");
        txtAddress.setText("Delivery Address: "+address);
        txtOrderId.setText("Order Id: "+orderDocumentId);
        setSpinner();
        setViewByOrderStatus();
    }

    private void setViewByOrderStatus() {

        if (status.equalsIgnoreCase("delivered"))
            txtStatus.setTextColor(Color.parseColor("#00b159"));
        else if (status.equalsIgnoreCase("cancelled"))
            txtStatus.setTextColor(Color.parseColor("#db2544"));
        else
            txtStatus.setTextColor(Color.parseColor("#ffa700"));

        txtStatus.setText(status);
        txtStatus.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        if (status.equalsIgnoreCase("delivered") || status.equalsIgnoreCase("cancelled"))
        {
            imgEditStatus.setVisibility(View.GONE);
            imgStatus.setVisibility(View.GONE);
        }
        else
        {
            imgEditStatus.setVisibility(View.VISIBLE);
            imgStatus.setVisibility(View.GONE);
        }
    }

    private void setSpinner() {
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        ArrayList<String> status_spinner=new ArrayList<String>();

        status_spinner.add("Pending");
        status_spinner.add("Delivered");
        status_spinner.add("Cancelled");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, status_spinner);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnViewOrderDetails:
                viewOrderDetails();
                break;
            case R.id.imgEditStatus:
                editStatus();
                break;
            case R.id.imgStatus:
                updateStatus();
                break;
        }
    }

    private void editStatus() {
        txtStatus.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        //btnUpdate.setVisibility(View.VISIBLE);
        imgEditStatus.setVisibility(View.GONE);
        imgStatus.setVisibility(View.VISIBLE);
        spinner.setSelection(0);
    }

    private void viewOrderDetails() {
        Intent intent = new Intent(getApplicationContext(),AdminOrderHistoryListActivity.class);
        intent.putExtra("orderHistoryDocumentId",orderDocumentId);
        intent.putExtra("userDocumentId",userDocumentId);
        startActivity(intent);
    }

    private void updateStatus() {
        status = spinner.getSelectedItem().toString();
        if (status.equalsIgnoreCase("pending"))
        {
            imgEditStatus.setVisibility(View.VISIBLE);
            imgStatus.setVisibility(View.GONE);
            txtStatus.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Order is already in pending state.",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        DocumentReference documentReference = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document(userDocumentId).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(orderDocumentId);
        documentReference.update(map).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (status.equalsIgnoreCase("delivered")) {
                            sendInvoice();
                            Intent intent = new Intent(getApplicationContext(), OrderHistoryListActivity.class);
                            intent.putExtra("orderHistoryDocumentId", orderDocumentId);
                            new SendNotification().sendNotification(userID, "Update on order status", "Hi " + userName + ", your order is delivered successfully. Invoice is sent to your email.",intent);
                        }
                        setViewByOrderStatus();
                        Toast.makeText(AdminUpdateOrderActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminUpdateOrderActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method sends email via Email app with provided data.
     */
    private void sendInvoice() {
        String mEmail = userEmail;
        String mSubject = "Invoice for your purchase at StockItUp";
        String mMessage = "Hello "+userName+", \n\nThank you for ordering at StockItUp.\n\nYour order is delivered successfully to "+address+".\n\nPlease find the invoice for your order #"+orderDocumentId +"\n\n\n"+
                "Ordered date: "+date+"\n" +
                "Subtotal: "+subtotal+"$\n"+
                "Offer (-"+offerPercent+"%): -"+offer+"$\n" +
                "Tax: "+tax+"$\n"+
                "Delivery charge: "+deliveryCharge+"$\n" +
                "Total: "+total+"$\n\n\n\n\n\nThank you,\nTeam StockItUp";



        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();
    }
}