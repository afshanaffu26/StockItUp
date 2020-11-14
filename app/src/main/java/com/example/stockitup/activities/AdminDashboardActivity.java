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

/**
 * This class is related to admin.It deals with Dashboard
 */
public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardLogout,cardViewCategory,cardAddItems,cardManageOrders,cardEssentialItems,cardMore;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setToolbar();
        initializeReferencesAndListeners();
    }

    /**
     * sets toolbar title, back navigation
     * */
    private void setToolbar() {
        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
    }

    /**
     * initialize references and listeners
     * */
    private void initializeReferencesAndListeners() {
        cardLogout = findViewById(R.id.cardLogout);
        cardViewCategory = findViewById(R.id.cardViewCategory);
        cardAddItems = findViewById(R.id.cardAddItems);
        cardManageOrders = findViewById(R.id.cardManageOrders);
        cardEssentialItems = findViewById(R.id.cardEssentialItems);
        cardMore = findViewById(R.id.cardMore);
        progressBar = findViewById(R.id.progressBar);

        cardMore.setOnClickListener(this);
        cardLogout.setOnClickListener(this);
        cardViewCategory.setOnClickListener(this);
        cardAddItems.setOnClickListener(this);
        cardManageOrders.setOnClickListener(this);
        cardEssentialItems.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cardViewCategory:
                startActivity(new Intent(getApplicationContext(), AdminCategoriesActivity.class));
                break;
            case R.id.cardAddItems:
                setCategoriesMapData();
                break;
            case R.id.cardManageOrders:
                startActivity(new Intent(getApplicationContext(),AdminManageOrdersActivity.class));
                break;
            case R.id.cardEssentialItems:
                Intent i = new Intent(getApplicationContext(),AdminCategoryItemsActivity.class);
                i.putExtra("categoryDocumentId","");
                i.putExtra("name","Essential Items");
                startActivity(i);
                break;
            case R.id.cardMore:
                startActivity(new Intent(getApplicationContext(), AdminMoreActivity.class));
                break;
            case R.id.cardLogout:
                userLogout();
                break;
        }
    }

    /**
     * set data to CATEGORIES_MAP
     * */
    private void setCategoriesMapData() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Map<String,String> map=new HashMap<String,String>();
                            for (DocumentSnapshot documentSnapshot: task.getResult())
                            {
                                map.put(documentSnapshot.getString("name"),documentSnapshot.getId());
                            }
                            AppConstants.CATEGORIES_MAP = map;
                            Intent i = new Intent(getApplicationContext(), AdminAddItemsActivity.class);
                            startActivity(i);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * This method is used to logout of the app
     * */
    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent i= new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}