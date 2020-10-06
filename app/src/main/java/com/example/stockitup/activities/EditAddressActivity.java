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
import com.example.stockitup.models.AddressModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditAddressActivity extends AppCompatActivity {

    private String name,addressLine,city,province,country,pincode,phone;
    private EditText editName,editAddressLine,editProvince,editCity,editPostalID,editCountry,editPhone;
    private Button btnNext;
    private FirebaseFirestore firebaseFirestore;
    private String documentId;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        addressLine = getIntent().getStringExtra("addressLine");
        city = getIntent().getStringExtra("city");
        province = getIntent().getStringExtra("province");
        country = getIntent().getStringExtra("country");
        pincode = getIntent().getStringExtra("pincode");
        phone = getIntent().getStringExtra("phone");
        documentId = getIntent().getStringExtra("documentId");

        editName = findViewById(R.id.editName);
        editAddressLine = findViewById(R.id.editAddressLine);
        editProvince = findViewById(R.id.editProvince);
        editCity = findViewById(R.id.editCity);
        editPostalID = findViewById(R.id.editPostalID);
        editCountry = findViewById(R.id.editCountry);
        editPhone = findViewById(R.id.editPhone);
        btnNext = findViewById(R.id.btnNext);
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        editName.setText(name);
        editAddressLine.setText(addressLine);
        editCity.setText(city);
        editProvince.setText(province);
        editCountry.setText(country);
        editPostalID.setText(pincode);
        editPhone.setText(phone);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editName.getText().toString();
                addressLine = editAddressLine.getText().toString();
                city = editCity.getText().toString();
                province = editProvince.getText().toString();
                country = editCountry.getText().toString();
                pincode = editPostalID.getText().toString();
                phone = editPhone.getText().toString();

                AddressModel addressModel = new AddressModel(name,addressLine,city,province,country,pincode,phone);
                firebaseFirestore.collection(AppConstants.ADDRESS_COLLECTION).document("address"+uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId)
                        .set(addressModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    finish();
                                    Toast.makeText(getApplicationContext(),"Address Updated.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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