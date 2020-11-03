package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.OffersModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminAddOrEditOffersActivity extends AppCompatActivity implements View.OnClickListener {

    private String name,value,flow,documentId;
    private EditText editName,editValue;
    private Button btnSubmit;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_or_edit_offers);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = findViewById(R.id.editName);
        editValue = findViewById(R.id.editValue);
        btnSubmit = findViewById(R.id.btnSubmit);
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(this);
        flow = getIntent().getStringExtra("flow");
        if (flow.equalsIgnoreCase("edit"))
        {
            name = getIntent().getStringExtra("name");
            value = getIntent().getStringExtra("value");
            documentId = getIntent().getStringExtra("documentId");
            btnSubmit.setText("UPDATE");
            editName.setText(name);
            editValue.setText(value);
        }
        else
        {
            btnSubmit.setText("ADD");
        }
    }

    private void submitOffer() {
        name = editName.getText().toString();
        value = editValue.getText().toString();
        int off = Integer.parseInt(value);
        if (off < (int)0 || off > (int)100 ) {
            Toast.makeText(this, "Offer should be between 0 to 100%.", Toast.LENGTH_SHORT).show();
            return;
        }
        OffersModel model = new OffersModel(name, value);
        if (flow.equalsIgnoreCase("add")) {
            firebaseFirestore.collection(AppConstants.OFFERS_COLLECTION)
                    .add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(),"Offer Added.",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            firebaseFirestore.collection(AppConstants.OFFERS_COLLECTION).document(documentId)
                    .set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Offer Updated.",Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public void onClick(View v) {
        submitOffer();
    }
}