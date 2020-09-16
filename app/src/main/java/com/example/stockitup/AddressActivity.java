package com.example.stockitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stockitup.activities.PaymentActivity;
import com.example.stockitup.activities.SignUpActivity;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
        }
    }
}