package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages with recent order details
 */
public class OrderHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String subtotal,tax,offer,offerPercent,deliveryCharge,total,address,date,status;
    private TextView txtOrderDate,txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtAddress,txtStatus,txtOffer,txtOrderId;
    private Button btnViewOrderDetails,btnCancelOrder;
    private String orderHistoryDocumentId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

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
        btnViewOrderDetails = findViewById(R.id.btnViewOrderDetails);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);

        btnViewOrderDetails.setOnClickListener(this);
        btnCancelOrder.setOnClickListener(this);

        subtotal = getIntent().getStringExtra("subtotal");
        tax = getIntent().getStringExtra("tax");
        offer = getIntent().getStringExtra("offer");
        offerPercent = getIntent().getStringExtra("offerPercent");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");
        address = getIntent().getStringExtra("address");
        orderHistoryDocumentId = getIntent().getStringExtra("orderHistoryDocumentId");
        date = getIntent().getStringExtra("date");
        status = getIntent().getStringExtra("status");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        txtOrderId.setText("OrderId: "+orderHistoryDocumentId);
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
        txtDeliveryCharge.setText("DeliveryCharge: "+deliveryCharge+"$");
        txtTotal.setText("Total: "+total+"$");
        txtAddress.setText("Address: "+address+"$");
        txtStatus.setText(status);
        if (status.equalsIgnoreCase("pending"))
        {
            txtStatus.setTextColor(Color.parseColor("#ffa700"));
            btnCancelOrder.setVisibility(View.VISIBLE);
        }
        else if (status.equalsIgnoreCase("delivered"))
        {
            txtStatus.setTextColor(Color.parseColor("#00b159"));
            btnCancelOrder.setVisibility(View.GONE);
        }
        else
        {
            txtStatus.setTextColor(Color.parseColor("#db2544"));
            btnCancelOrder.setVisibility(View.GONE);
        }
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
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnViewOrderDetails:
                viewOrderDetails();
                break;
            case R.id.btnCancelOrder:
                cancelOrder();
                break;
        }
    }

    /**
     * This method is initiates cancel order
     * */
    private void cancelOrder() {
        if (!status.equalsIgnoreCase("pending"))
        {
            Toast.makeText(getApplicationContext(),"Order Cannot be cancelled.",Toast.LENGTH_SHORT).show();
            return;
        }
        alertMessage();
    }

    /**
     * Navigates to view order details
     * */
    private void viewOrderDetails() {
        Intent intent = new Intent(getApplicationContext(),OrderHistoryListActivity.class);
        intent.putExtra("orderHistoryDocumentId",orderHistoryDocumentId);
        startActivity(intent);
    }

    /**
     * This method sets the view based on status
     * */
    private void setViewByOrderStatus() {
        txtStatus.setTextColor(Color.parseColor("#db2544"));
        txtStatus.setText("Cancelled");
        btnCancelOrder.setVisibility(View.GONE);
    }

    /**
     * This method is used to show an alert with an appropriate message
     */
    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        Map<String,Object> map = new HashMap<>();
                        map.put("status","Cancelled");
                        String uid = firebaseAuth.getUid();
                        DocumentReference documentReference = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders"+uid).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(orderHistoryDocumentId);
                        documentReference.update(map).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        setViewByOrderStatus();
                                        Toast.makeText(getApplicationContext(), "Order is Cancelled.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}