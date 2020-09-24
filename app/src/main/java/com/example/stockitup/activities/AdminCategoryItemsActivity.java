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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AdminCategoryItemsAdapter;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.CategoryItemsModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminCategoryItemsActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private AdminCategoryItemsAdapter adapter;
    private RecyclerView recyclerView;
    private String category ="";
    private TextView txtCategory;
    private String categoryDocumentId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_items);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        txtCategory = findViewById(R.id.txtCategory);
        categoryDocumentId = getIntent().getStringExtra("categoryDocumentId");
        category = getIntent().getStringExtra("name");
        txtCategory.setText(category);

        setRecyclerViewData();
    }
    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection("Categories").document(categoryDocumentId).collection(category).orderBy("name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query, CategoryItemsModel.class)
                .build();


        adapter= new AdminCategoryItemsAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                CategoryItemsModel model = documentSnapshot.toObject(CategoryItemsModel.class);
                Intent intent = new Intent(getApplicationContext(), AdminUpdateItemsActivity.class);
                String documentId = documentSnapshot.getId();
                intent.putExtra("category", category);
                intent.putExtra("name", model.getName());
                if (model.getImage() != "")
                    intent.putExtra("image", model.getImage());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("documentId", documentId);
                startActivity(intent);
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
                        Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        adapter.notifyItemChanged(position);
                        Toast.makeText(getApplicationContext(), "Delete cancelled",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void editCategory() {
        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
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