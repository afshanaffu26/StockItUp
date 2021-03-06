package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stockitup.utils.MyPreferences;
import com.example.stockitup.R;

/**
 *  This class deals with first welcome screen
 */
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonNext;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initializeReferencesAndListeners();
        navigateToLoginIfLaunchedPreviously();
    }

    /**
     * initialize references and listeners
     * */
    private void initializeReferencesAndListeners() {
        buttonNext  = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
    }

    /**
     * If the app was launched previously, navigate to login directly
     * */
    private void navigateToLoginIfLaunchedPreviously() {
        boolean isFirstTime = MyPreferences.isFirst(WelcomeActivity.this);
        if (!isFirstTime) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNext:
                navigatesToWelcomeActivity2();
                break;
        }
    }

    /**
     * Navigates to WelcomeActivity2
     * */
    private void navigatesToWelcomeActivity2() {
        startActivity(new Intent(getApplicationContext(), WelcomeActivity2.class));
        finish();
    }
}

