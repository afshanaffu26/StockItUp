package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;

/**
 * This class is related to admin.It deals with Additional menu options
 */
public class AdminMoreActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgNextOffers,imgNextFAQ,imgNextContact;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_more);

        setToolbar();

        imgNextOffers = findViewById(R.id.imgNextOffers);
        imgNextFAQ = findViewById(R.id.imgNextFAQ);
        imgNextContact = findViewById(R.id.imgNextContact);

        imgNextOffers.setOnClickListener(this);
        imgNextFAQ.setOnClickListener(this);
        imgNextContact.setOnClickListener(this);
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
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imgNextOffers:
                startActivity(new Intent(getApplicationContext(),AdminOffersActivity.class));
                break;
            case R.id.imgNextFAQ:
                startActivity(new Intent(getApplicationContext(), AdminFAQActivity.class));
                break;
            case R.id.imgNextContact:
                startActivity(new Intent(getApplicationContext(), AdminContactActivity.class));
                break;
        }
    }
}