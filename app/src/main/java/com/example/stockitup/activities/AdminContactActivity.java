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

/**
 * This class is related to admin.It deals with Contact details
 */
public class AdminContactActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtCustomerCareNumber,txtTollFreeNumber;
    private EditText editCustomerCareNumber,editTollFreeNumber;
    private Button btnSubmit;
    private FirebaseFirestore firebaseFirestore;
    private String customerCareNumber,tollFreeNumber,documentId;
    private ProgressBar progressBar;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contact);

        setToolbar();

        txtCustomerCareNumber = findViewById(R.id.txtCustomerCareNumber);
        txtTollFreeNumber = findViewById(R.id.txtTollFreeNumber);
        editCustomerCareNumber = findViewById(R.id.editCustomerCareNumber);
        editTollFreeNumber = findViewById(R.id.editTollFreeNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        initializeView();
        fetchContactDetails();
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
     * This method initializes the view
     * */
    private void initializeView() {
        btnSubmit.setText("EDIT");
        editCustomerCareNumber.setVisibility(View.GONE);
        editTollFreeNumber.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * This method fetches the contact details
     * */
    private void fetchContactDetails() {
        firebaseFirestore.collection(AppConstants.APP_SUPPORT_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            customerCareNumber = task.getResult().getDocuments().get(0).get("customerCareNumber").toString();
                            tollFreeNumber = task.getResult().getDocuments().get(0).get("tollFreeNumber").toString();
                            documentId = task.getResult().getDocuments().get(0).getId();
                            setContactDetails(customerCareNumber,tollFreeNumber);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * This methods sets contact details to view
     * @param customerCareNumber the customer care number of support
     * @param tollFreeNumber the toll free number of support
     * */
    private void setContactDetails(String customerCareNumber,String tollFreeNumber) {
        txtCustomerCareNumber.setText(customerCareNumber);
        txtTollFreeNumber.setText(tollFreeNumber);
    }

    /**
     * This methods updates the contact info
     * */
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

    /**
     * This methods updates view when edit is initiated
     * */
    private void onContactEdit() {
        txtCustomerCareNumber.setVisibility(View.GONE);
        txtTollFreeNumber.setVisibility(View.GONE);
        editCustomerCareNumber.setVisibility(View.VISIBLE);
        editTollFreeNumber.setVisibility(View.VISIBLE);
        editCustomerCareNumber.setText(customerCareNumber);
        editTollFreeNumber.setText(tollFreeNumber);
        btnSubmit.setText("UPDATE");
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
     * when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSubmit:
                onContactSubmit();
                break;
        }
    }

    /**
     * This method is checks if it is edit or update flow
     * */
    private void onContactSubmit() {
        if (btnSubmit.getText().toString().equalsIgnoreCase("edit"))
            onContactEdit();
        else
            onContactUpdate();
    }
}