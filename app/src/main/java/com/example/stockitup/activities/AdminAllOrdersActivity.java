package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AdminAllOrdersAdapter;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.OrdersModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;

public class AdminAllOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private AdminAllOrdersAdapter adapter;
    private ProgressBar progressBar;
    private String userDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_orders);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDocumentId = getIntent().getStringExtra("userDocumentId");

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);

        setRecyclerViewData();
    }

    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document(userDocumentId).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT);
        FirestoreRecyclerOptions<OrdersModel> options = new FirestoreRecyclerOptions.Builder<OrdersModel>()
                .setQuery(query,OrdersModel.class)
                .build();
        adapter= new AdminAllOrdersAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                OrdersModel model = documentSnapshot.toObject(OrdersModel.class);
                String orderDocumentId = documentSnapshot.getId();
                String date = new SimpleDateFormat("dd-MM-yy HH:mm").format(model.getDate());
                Intent intent = new Intent(getApplicationContext(), AdminUpdateOrderActivity.class);
                intent.putExtra("userDocumentId", userDocumentId);
                intent.putExtra("orderDocumentId", orderDocumentId);
                intent.putExtra("date", date);
                intent.putExtra("subtotal", model.getSubtotal());
                intent.putExtra("tax", model.getTax());
                intent.putExtra("deliveryCharge", model.getDeliveryCharge());
                intent.putExtra("total", model.getTotal());
                intent.putExtra("address", model.getAddress());
                intent.putExtra("status", model.getStatus());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}