package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This class deals with contact us details for user to contact
 */
public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtEmail,txtCustomerCarePhone,txtTollFreePhone;
    private ImageView imgCall1,imgCall2,imgEmail;
    private FirebaseFirestore firebaseFirestore;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        //display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();

        imgCall1 = findViewById(R.id.imgCall1);
        imgCall2 = findViewById(R.id.imgCall2);
        imgEmail = findViewById(R.id.imgEmail);
        txtEmail = findViewById(R.id.txtEmail);
        txtCustomerCarePhone = findViewById(R.id.txtCustomerCarePhone);
        txtTollFreePhone = findViewById(R.id.txtTollFreePhone);

        imgCall1.setOnClickListener(this);
        imgCall2.setOnClickListener(this);
        imgEmail.setOnClickListener(this);


        txtEmail.setText("Email - "+AppConstants.ADMIN_EMAIL);
        txtCustomerCarePhone.setText("Customer Care - "+AppConstants.CUSTOMER_CARE_NUMBER);
        txtTollFreePhone.setText("Toll Free - "+AppConstants.TOLL_FREE_NUMBER);
    }

    /**
     * Called when a view has been clicked te contact
     * @param view The view that was clicked.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCall1:
                callToHelplineNumber(AppConstants.CUSTOMER_CARE_NUMBER);
                break;
            case R.id.imgCall2:
                callToHelplineNumber(AppConstants.TOLL_FREE_NUMBER);
                break;
            case R.id.imgEmail:
                sendEmailWithoutChooser();
                break;
        }
    }

    /**
     * This method sends email via Email app with provided data.
     */
    private void sendEmailWithoutChooser() {
        String email = AppConstants.ADMIN_EMAIL;
        String feedback_msg = "";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        String aEmailList[] = {email};
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<i><font color='your color'>" + feedback_msg + "</font></i>"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request help from Customer Support");

        PackageManager packageManager = getApplicationContext().getPackageManager();
        boolean isIntentSafe = emailIntent.resolveActivity(packageManager) != null;
        if (isIntentSafe) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Email app is not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * this method places a call to provided number by using native dialing
     * @param number Phone number to place a call.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callToHelplineNumber(String number) {
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:"+number));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(),"Please grant permission",Toast.LENGTH_SHORT).show();
            requestPermissions();
        }
        else
        {
            startActivity(intentCall);
        }
    }

    /**
     * This method is used to request permission to access phone to make a call
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(ContactActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }
}