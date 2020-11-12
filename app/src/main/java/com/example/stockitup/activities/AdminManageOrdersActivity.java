package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AdminManageOrdersAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.ManageOrdersModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * This class is related to admin.It deals with Managing of orders
 */

public class AdminManageOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private AdminManageOrdersAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private LinearLayout linearLayout;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_orders);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);
        txtEmpty = findViewById(R.id.txtEmpty);
        linearLayout = findViewById(R.id.linearLayout);

        firebaseFirestore = FirebaseFirestore.getInstance();

        txtEmpty.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        setRecyclerViewData();
    }

    /**
     * set data and functionality to recycler view
     * */
    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION);
        FirestoreRecyclerOptions<ManageOrdersModel> options = new FirestoreRecyclerOptions.Builder<ManageOrdersModel>()
                .setQuery(query,ManageOrdersModel.class)
                .build();
        adapter= new AdminManageOrdersAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                ManageOrdersModel model = documentSnapshot.toObject(ManageOrdersModel.class);
                String documentId = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), AdminAllOrdersActivity.class);
                intent.putExtra("userDocumentId", documentId);
                intent.putExtra("userID", model.getUserId());
                intent.putExtra("userEmail", model.getUserEmail());
                intent.putExtra("userName", model.getUserName());
                startActivity(intent);
            }
        });
        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() != 0)
                {
                    txtEmpty.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtEmpty.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
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