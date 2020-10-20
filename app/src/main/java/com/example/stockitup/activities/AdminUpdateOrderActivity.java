package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.OrdersModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUpdateOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinner;
    private Button btnUpdate;
    private FirebaseFirestore firebaseFirestore;
    private String userDocumentId,orderDocumentId;
    private String date,subtotal,tax,deliveryCharge,total,address,status,offer;
    private TextView txtOrderDate,txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtAddress,txtStatus,txtOffer;

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
        spinner = (Spinner) findViewById(R.id.spinner);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        userDocumentId = getIntent().getStringExtra("userDocumentId");
        orderDocumentId = getIntent().getStringExtra("orderDocumentId");
        status = getIntent().getStringExtra("status");
        date = getIntent().getStringExtra("date");
        subtotal = getIntent().getStringExtra("subtotal");
        offer = getIntent().getStringExtra("offer");
        tax = getIntent().getStringExtra("tax");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");
        address = getIntent().getStringExtra("address");

        txtOrderDate.setText("Ordered On: "+date);
        txtSubTotal.setText("Subtotal: "+subtotal+"$");
        txtOffer.setText("Offer (-20%): -"+offer+"$");
        txtTax.setText("Tax: "+tax+"$");
        txtDeliveryCharge.setText("Delivery Charge: "+deliveryCharge+"$");
        txtTotal.setText("Total: "+total+"$");
        txtAddress.setText("Delivery Address: "+address);
        setSpinner();
        setViewByOrderStatus();
    }

    private void setViewByOrderStatus() {
        if (status.equalsIgnoreCase("delivered") || status.equalsIgnoreCase("cancelled"))
        {
            if (status.equalsIgnoreCase("delivered"))
                txtStatus.setTextColor(Color.parseColor("#00b159"));
            else if (status.equalsIgnoreCase("cancelled"))
                txtStatus.setTextColor(Color.parseColor("#db2544"));

            txtStatus.setText(status);
            txtStatus.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        }
        else
        {
            txtStatus.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
            spinner.setSelection(0);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnUpdate:
                updateStatus();
                break;
        }
    }

    private void updateStatus() {
        status = spinner.getSelectedItem().toString();
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        DocumentReference documentReference = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document(userDocumentId).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(orderDocumentId);
        documentReference.update(map).addOnSuccessListener(
                new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
}