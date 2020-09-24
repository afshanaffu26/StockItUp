package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardLogout,cardViewCategory,cardAddCategoryItems;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private Map<String,String> map=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //teamstockitup@gmail.com
        //stockitup.8

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);

        cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(this);
        cardViewCategory = findViewById(R.id.cardViewCategory);
        cardViewCategory.setOnClickListener(this);
        cardAddCategoryItems = findViewById(R.id.cardAddCategoryItems);
        cardAddCategoryItems.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cardViewCategory:
                startActivity(new Intent(getApplicationContext(), AdminViewCategoriesActivity.class));
                break;
            case R.id.cardAddCategoryItems:
                setCategoriesMapData();
                break;
            case R.id.cardLogout:
                userLogout();
                break;
        }
    }
    private void setCategoriesMapData() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Categories").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot documentSnapshot: task.getResult())
                            {
                                map.put(documentSnapshot.getString("name"),documentSnapshot.getId());
                            }
                            AppConstants.categories_map = map;
                            Intent i = new Intent(getApplicationContext(), AdminAddCategoryItemsActivity.class);
                            startActivity(i);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent i= new Intent(this,LoginActivity.class);
        startActivity(i);
    }

}