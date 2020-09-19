package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stockitup.utils.MyPreferences;
import com.example.stockitup.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener  {

    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonNext  = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        boolean isFirstTime = MyPreferences.isFirst(WelcomeActivity.this);
        if (!isFirstTime) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNext:
                startActivity(new Intent(getApplicationContext(), WelcomeActivity2.class));
                finish();
                break;
        }
    }
}

