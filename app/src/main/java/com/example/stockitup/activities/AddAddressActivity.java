package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages addition of new user addresses
 */
public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName,editAddressLine,editProvince,editCity,editPostalID,editCountry,editPhone;
    private FirebaseFirestore firebaseFirestore;
    private String uid;
    private Button btnNext;
    private String name,addressLine,province,city,postalID,country,phone;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        setToolbar();
        initializeReferencesAndListeners();
    }

    /**
     * sets toolbar title, back navigation
     * */
    private void setToolbar() {
        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * initialize references and listeners
     * */
    private void initializeReferencesAndListeners() {
        btnNext = findViewById(R.id.btnNext);
        editName = findViewById(R.id.editName);
        editAddressLine = findViewById(R.id.editAddressLine);
        editProvince = findViewById(R.id.editProvince);
        editCity = findViewById(R.id.editCity);
        editPostalID = findViewById(R.id.editPostalID);
        editCountry = findViewById(R.id.editCountry);
        editPhone = findViewById(R.id.editPhone);

        btnNext.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                validateAndAddAddress();
                break;
        }
    }

    /**
     * This method validates and stores the provided address to database.
     */
    private void validateAndAddAddress() {
        name = editName.getText().toString();
        addressLine = editAddressLine.getText().toString();
        province = editProvince.getText().toString();
        city = editCity.getText().toString();
        postalID = editPostalID.getText().toString();
        country = editCountry.getText().toString();
        phone = editPhone.getText().toString();

        if(name.isEmpty())
        {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }
        if(addressLine.isEmpty())
        {
            editAddressLine.setError("Street and Apt/Suit# is required");
            editAddressLine.requestFocus();
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
        addAddress();
    }

    /**
     * This method add address to database
     * */
    private void addAddress() {
        Map<String, Object> address = new HashMap<>();
        address.put("name", ""+name);
        address.put("addressLine", ""+addressLine);
        address.put("city", ""+city);
        address.put("province", ""+province);
        address.put("country", ""+country);
        address.put("pincode", ""+postalID);
        address.put("phone", ""+phone);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection(AppConstants.ADDRESS_COLLECTION).document("address"+uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                .add(address)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        finish();
                        Toast.makeText(AddAddressActivity.this, "Address Added.", Toast.LENGTH_SHORT).show();
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