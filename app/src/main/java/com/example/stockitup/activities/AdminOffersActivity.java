package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AddressAdapter;
import com.example.stockitup.adapters.OffersAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.AddressModel;
import com.example.stockitup.models.OffersModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminOffersActivity extends AppCompatActivity{

    private FirebaseFirestore firebaseFirestore;
    private OffersAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_offers);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminAddOrEditOffersActivity.class);
                intent.putExtra("flow", "add");
                startActivity(intent);
            }
        });

        setRecyclerViewData();

    }

    private void setRecyclerViewData() {
        final Query query = firebaseFirestore.collection(AppConstants.OFFERS_COLLECTION);
        FirestoreRecyclerOptions<OffersModel> options = new FirestoreRecyclerOptions.Builder<OffersModel>()
                .setQuery(query,OffersModel.class)
                .build();
        adapter = new OffersAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                OffersModel model = documentSnapshot.toObject(OffersModel.class);
                String documentId = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), AdminAddOrEditOffersActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("value", model.getValue());
                intent.putExtra("flow", "edit");
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                alertMessage(position);
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() < 3)
                {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
    public void alertMessage(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        adapter.deleteItem(position);
                        Toast.makeText(getApplicationContext(), "Deleted Successfully.",
                                Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        adapter.notifyItemChanged(position);
                        Toast.makeText(getApplicationContext(), "Delete Cancelled.",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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