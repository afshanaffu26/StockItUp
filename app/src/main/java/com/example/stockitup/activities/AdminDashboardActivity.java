package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.stockitup.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cardLogout,cardViewCategory,cardAddCategoryItems;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        //teamstockitup@gmil.com
        //stockitup.8
        cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(this);
        cardViewCategory = findViewById(R.id.cardViewCategory);
        cardViewCategory.setOnClickListener(this);
        cardAddCategoryItems = findViewById(R.id.cardAddCategoryItems);
        cardAddCategoryItems.setOnClickListener(this);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cardViewCategory:
                startActivity(new Intent(getApplicationContext(),ViewCategoriesActivity.class));
                break;
            case R.id.cardAddCategoryItems:
                startActivity(new Intent(getApplicationContext(),AddCategoryItemsActivity.class));
                break;
            case R.id.cardLogout:
                userLogout();
                break;
        }
    }
    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent i= new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}