package com.example.stockitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class  WelcomeActivity2 extends AppCompatActivity  implements View.OnClickListener {

    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
        }
    }