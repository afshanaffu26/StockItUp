package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockitup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages user address to proceed with order
 */
public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editFullName,editAddressLine1,editAddressLine2,editProvince,editCity,editPostalID,editCountry,editPhone;
    private FirebaseFirestore firebaseFirestore;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        
        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        editFullName = findViewById(R.id.editFullName);
        editAddressLine1 = findViewById(R.id.editAddressLine1);
        editAddressLine2 = findViewById(R.id.editAddressLine2);
        editProvince = findViewById(R.id.editProvince);
        editCity = findViewById(R.id.editCity);
        editPostalID = findViewById(R.id.editPostalID);
        editCountry = findViewById(R.id.editCountry);
        editPhone = findViewById(R.id.editPhone);

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                addAddress();
                break;
        }
    }

    /**
     * This method stores the provided address to database.
     */
    private void addAddress() {
        String fullName,addressLine1,addressLine2,apt,province,city,postalID,country,phone;

        fullName = editFullName.getText().toString();
        addressLine1 = editAddressLine1.getText().toString();
        addressLine2 = editAddressLine2.getText().toString();
        province = editProvince.getText().toString();
        city = editCity.getText().toString();
        postalID = editPostalID.getText().toString();
        country = editCountry.getText().toString();
        phone = editPhone.getText().toString();

        if(fullName.isEmpty())
        {
            editFullName.setError("Full name is required");
            editFullName.requestFocus();
            return;
        }
        if(addressLine1.isEmpty())
        {
            editAddressLine1.setError("Address Line 1 is required");
            editAddressLine1.requestFocus();
            return;
        }
        if(city.isEmpty())
        {
            editCity.setError("City is required");
            editCity.requestFocus();
            return;
        }
        if(province.isEmpty())
        {
            editProvince.setError("Province is required");
            editProvince.requestFocus();
            return;
        }
        if(country.isEmpty())
        {
            editCountry.setError("Country is required");
            editCountry.requestFocus();
            return;
        }
        if(postalID.isEmpty())
        {
            editPostalID.setError("Postal code is required");
            editPostalID.requestFocus();
            return;
        }
        if(phone.isEmpty())
        {
            editPhone.setError("Telephone number is required");
            editPhone.requestFocus();
            return;
        }

        Map<String, Object> address = new HashMap<>();
        address.put("fullName", ""+fullName);
        address.put("address", ""+addressLine1+" "+addressLine2+" "+city+" "+province+" "+country+" "+postalID);
        address.put("phone", ""+phone);

        firebaseFirestore.collection("Address").document(uid).set(address)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        goToPayment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddAddressActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Navigates to payment page to show break up of total amount being charged.
     */
    private void goToPayment() {
        String subTotal,deliveryCharge,tax,total;

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        subTotal = getIntent().getStringExtra("subTotal");
        tax = getIntent().getStringExtra("tax");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");

        intent.putExtra("subTotal", subTotal);
        intent.putExtra("tax", tax);
        intent.putExtra("deliveryCharge", deliveryCharge);
        intent.putExtra("total", total);
        startActivity(intent);
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