package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stockitup.R;
import com.example.stockitup.adapters.OrderHistoryListAdapter;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrderHistoryListActivity extends AppCompatActivity{
    private FirebaseFirestore firebaseFirestore;
    private OrderHistoryListAdapter adapter;
    private RecyclerView recyclerView;
    private String orderHistoryDocumentId;
    private String uid;
    private TextView txtEmptyOrders;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_list);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        txtEmptyOrders = findViewById(R.id.txtEmptyOrders);
        linearLayout = findViewById(R.id.linearLayout);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderHistoryDocumentId = getIntent().getStringExtra("orderHistoryDocumentId");

        setRecyclerViewData();
    }

    private void setRecyclerViewData() {

        Query query = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders"+uid).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(orderHistoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT);
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query,CategoryItemsModel.class)
                .build();
        adapter = new OrderHistoryListAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                CategoryItemsModel model = documentSnapshot.toObject(CategoryItemsModel.class);
                Intent intent = new Intent(view.getContext(), ItemDescriptionActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("image", model.getImage());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("desc", model.getDesc());
                String documentId = documentSnapshot.getId();
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
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