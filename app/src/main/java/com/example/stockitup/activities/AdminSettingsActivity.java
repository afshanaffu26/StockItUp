package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;

public class AdminSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgNextOffers,imgNextFAQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgNextOffers = findViewById(R.id.imgNextOffers);
        imgNextOffers.setOnClickListener(this);
        imgNextFAQ = findViewById(R.id.imgNextFAQ);
        imgNextFAQ.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imgNextOffers:
                startActivity(new Intent(getApplicationContext(),AdminOffersActivity.class));
                break;
            case R.id.imgNextFAQ:
                startActivity(new Intent(getApplicationContext(), AdminFAQActivity.class));
                break;
        }
    }
}