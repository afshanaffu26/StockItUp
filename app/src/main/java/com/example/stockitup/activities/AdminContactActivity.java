package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminContactActivity extends AppCompatActivity {

    private TextView txtCustomerCareNumber,txtTollFreeNumber;
    private EditText editCustomerCareNumber,editTollFreeNumber;
    private Button btnSubmit;
    private FirebaseFirestore firebaseFirestore;
    private String customerCareNumber,tollFreeNumber,documentId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contact);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtCustomerCareNumber = findViewById(R.id.txtCustomerCareNumber);
        txtTollFreeNumber = findViewById(R.id.txtTollFreeNumber);
        editCustomerCareNumber = findViewById(R.id.editCustomerCareNumber);
        editTollFreeNumber = findViewById(R.id.editTollFreeNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setText("EDIT");
        editCustomerCareNumber.setVisibility(View.GONE);
        editTollFreeNumber.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnSubmit.getText().toString().equalsIgnoreCase("edit"))
                    onContactEdit();
                else
                    onContactUpdate();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection(AppConstants.APP_SUPPORT_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            customerCareNumber = task.getResult().getDocuments().get(0).get("customerCareNumber").toString();
                            tollFreeNumber = task.getResult().getDocuments().get(0).get("tollFreeNumber").toString();
                            txtCustomerCareNumber.setText(customerCareNumber);
                            txtTollFreeNumber.setText(tollFreeNumber);
                            documentId = task.getResult().getDocuments().get(0).getId();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void onContactUpdate() {
        customerCareNumber = editCustomerCareNumber.getText().toString();
        tollFreeNumber = editTollFreeNumber.getText().toString();
        Map<String,String> map = new HashMap<String, String>();
        map.put("customerCareNumber",customerCareNumber);
        map.put("tollFreeNumber",tollFreeNumber);

        firebaseFirestore.collection(AppConstants.APP_SUPPORT_COLLECTION).document(documentId)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        txtCustomerCareNumber.setVisibility(View.VISIBLE);
                        txtTollFreeNumber.setVisibility(View.VISIBLE);
                        editCustomerCareNumber.setVisibility(View.GONE);
                        editTollFreeNumber.setVisibility(View.GONE);
                        txtCustomerCareNumber.setText(customerCareNumber);
                        txtTollFreeNumber.setText(tollFreeNumber);
                        btnSubmit.setText("EDIT");
                        Toast.makeText(AdminContactActivity.this, "Contact Updated.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onContactEdit() {
        txtCustomerCareNumber.setVisibility(View.GONE);
        txtTollFreeNumber.setVisibility(View.GONE);
        editCustomerCareNumber.setVisibility(View.VISIBLE);
        editTollFreeNumber.setVisibility(View.VISIBLE);
        editCustomerCareNumber.setText(customerCareNumber);
        editTollFreeNumber.setText(tollFreeNumber);
        btnSubmit.setText("UPDATE");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}