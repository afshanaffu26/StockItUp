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

/**
 * This class is related to admin.It deals with all orders
 */
public class AdminAllOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private AdminAllOrdersAdapter adapter;
    private ProgressBar progressBar;
    private String userDocumentId,userEmail,userName,userID;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_orders);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        userDocumentId = getIntent().getStringExtra("userDocumentId");
        userID = getIntent().getStringExtra("userID");
        userEmail = getIntent().getStringExtra("userEmail");
        userName = getIntent().getStringExtra("userName");

        setRecyclerViewData();
    }

    /**
     * set data and functionality to recycler view
     * */
    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document(userDocumentId).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).orderBy("date",Query.Direction.DESCENDING);
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
                intent.putExtra("userID", userID);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userName", userName);
                intent.putExtra("date", date);
                intent.putExtra("subtotal", model.getSubtotal());
                intent.putExtra("offerPercent", model.getOfferPercent());
                intent.putExtra("offer", model.getOffer());
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

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
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