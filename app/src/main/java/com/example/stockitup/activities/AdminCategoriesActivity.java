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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AdminCategoriesAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.listeners.OnItemDeleteListener;
import com.example.stockitup.models.CategoriesModel;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
/**
 * This class is related to admin.It deals with Categories
 */

public class AdminCategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private AdminCategoriesAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private LinearLayout linearLayout;
    private TextView txtEmpty;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbar);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        linearLayout = findViewById(R.id.linearLayout);
        txtEmpty = findViewById(R.id.txtEmpty);

        txtEmpty.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        setRecyclerViewData();
    }

    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION);
        FirestoreRecyclerOptions<CategoriesModel> options = new FirestoreRecyclerOptions.Builder<CategoriesModel>()
                .setQuery(query,CategoriesModel.class)
                .build();
        adapter= new AdminCategoriesAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                CategoryItemsModel model = documentSnapshot.toObject(CategoryItemsModel.class);
                String documentId = documentSnapshot.getId();
                if (view.getId() == R.id.imageViewEdit)
                {
                    Intent intent = new Intent(getApplicationContext(), AdminEditCategoryActivity.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("image", model.getImage());
                    intent.putExtra("categoryDocumentId", documentId);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), AdminCategoryItemsActivity.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("categoryDocumentId", documentId);
                    startActivity(intent);
                }
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
        adapter.setOnItemDeleteListener(new OnItemDeleteListener() {
            @Override
            public void onItemDelete(DocumentSnapshot documentSnapshot, int position) {
                final String id = documentSnapshot.getId();
                progressBar.setVisibility(View.VISIBLE);
                firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(id).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() == 0)
                                    {
                                        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(id).delete();
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Deleted Successfully.",
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(id).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(queryDocumentSnapshot.getId()).delete();
                                        }
                                        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(id).delete();
                                        Toast.makeText(getApplicationContext(), "Deleted Successfully.",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
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

    /**
     * when a view has been clicked.
     * @param view The view that was clicked.
     */

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.floatingActionButton:
                startActivity(new Intent(getApplicationContext(), AdminAddCategoryActivity.class));
                break;
        }
    }

}