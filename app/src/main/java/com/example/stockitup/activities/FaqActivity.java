package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.stockitup.R;
import com.example.stockitup.adapters.FAQAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.models.FAQModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * This class deals with the faq.
 */
public class FaqActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FAQAdapter adapter;
    private ScrollView scrollView;
    private TextView txtEmpty;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setToolbar();
        initializeReferencesAndListeners();
        initializeViewAndControls();
        setRecyclerViewData();
    }

    /**
     * initialize references and listeners
     * */
    private void initializeReferencesAndListeners() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        scrollView = findViewById(R.id.scrollView);
        txtEmpty = findViewById(R.id.txtEmpty);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    /**
     * sets toolbar title, back navigation
     * */
    private void setToolbar() {
        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method initializes the view and controls
     * */
    private void initializeViewAndControls() {
        txtEmpty.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
    }

    /**
     * set data and functionality to recycler view
     * */
    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.FAQ_COLLECTION);
        FirestoreRecyclerOptions<FAQModel> options = new FirestoreRecyclerOptions.Builder<FAQModel>()
                .setQuery(query, FAQModel.class)
                .build();
        adapter= new FAQAdapter(options);
        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() != 0)
                {
                    txtEmpty.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtEmpty.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
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