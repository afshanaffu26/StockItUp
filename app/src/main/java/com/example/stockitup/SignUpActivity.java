package com.example.stockitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtLogin;
    TextView txtConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StockItUp");

        txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(this);
        txtConditions = findViewById(R.id.txtConditions);
        txtConditions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtLogin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.txtConditions:
                startActivity(new Intent(getApplicationContext(), TermsActivity.class));
                break;
        }
    }
}