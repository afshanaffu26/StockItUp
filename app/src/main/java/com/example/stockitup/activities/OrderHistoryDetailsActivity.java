package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;

public class OrderHistoryDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

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
}