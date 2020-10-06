package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AddressAdapter;
import com.example.stockitup.adapters.CartAdapter;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.AddressModel;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AddressActivity extends AppCompatActivity{

    private FirebaseFirestore firebaseFirestore;
    private AddressAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                startActivity(intent);
            }
        });

        setRecyclerViewData();

    }

    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.ADDRESS_COLLECTION);
        FirestoreRecyclerOptions<AddressModel> options = new FirestoreRecyclerOptions.Builder<AddressModel>()
                .setQuery(query,AddressModel.class)
                .build();
        adapter = new AddressAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                AddressModel model = documentSnapshot.toObject(AddressModel.class);
                String address = ""+model.getAddressLine() + " " + model.getCity() + " " + model.getProvince()+" "+model.getCountry()+" " +model.getPincode();
                goToPayment(address);
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
    /**
     * Navigates to payment page to show break up of total amount being charged.
     */
    private void goToPayment(String address) {
        String subTotal,deliveryCharge,tax,total;

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        subTotal = getIntent().getStringExtra("subTotal");
        tax = getIntent().getStringExtra("tax");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");

        intent.putExtra("subTotal", subTotal);
        intent.putExtra("tax", tax);
        intent.putExtra("deliveryCharge", deliveryCharge);
        intent.putExtra("total", total);
        intent.putExtra("address", address);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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