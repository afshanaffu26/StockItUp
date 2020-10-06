package com.example.stockitup.activities;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;

/**
 * This class deals with contact us details for user to contact
 */
public class ContactActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgCall1,imgCall2;
    private String number;

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

        imgCall1 = findViewById(R.id.imgCall1);
        imgCall2 = findViewById(R.id.imgCall2);

        imgCall1.setOnClickListener(this);
        imgCall2.setOnClickListener(this);
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
                number = "4388961212";
                callToHelplineNumber(number);
                break;
            case R.id.imgCall2:
                number = "1800776665";
                callToHelplineNumber(number);
                break;
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