package com.example.stockitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtSignUp;
    TextView txt_recover_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StockItUp");

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        txt_recover_password = findViewById(R.id.txt_recover_password);
        txt_recover_password.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtSignUp:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
            case R.id.txt_recover_password:
                startActivity(new Intent(this,ForgotActivity.class));
                break;

        }
    }
}