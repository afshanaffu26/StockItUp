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
import com.example.stockitup.adapters.AdminCategoryItemsAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminCategoryItemsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private AdminCategoryItemsAdapter adapter;
    private RecyclerView recyclerView;
    private String category ="";
    private TextView txtCategory,txtEmptyItems;
    private String categoryDocumentId;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private Map<String,String> map=new HashMap<String,String>();
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_items);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        txtCategory = findViewById(R.id.txtCategory);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        categoryDocumentId = getIntent().getStringExtra("categoryDocumentId");
        category = getIntent().getStringExtra("name");
        txtCategory.setText(category);
        linearLayout = findViewById(R.id.linearLayout);
        txtEmptyItems = findViewById(R.id.txtEmptyItems);

        setRecyclerViewData();
    }
    private void setRecyclerViewData() {
        Query query;
        if (categoryDocumentId.equalsIgnoreCase(""))
            query = firebaseFirestore.collection(AppConstants.ESSENTIALS_COLLECTION);
        else
            query = firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).orderBy("name",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query, CategoryItemsModel.class)
                .build();

        adapter= new AdminCategoryItemsAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                CategoryItemsModel model = documentSnapshot.toObject(CategoryItemsModel.class);
                Intent intent = new Intent(getApplicationContext(), AdminEditCategoryItemsActivity.class);
                String documentId = documentSnapshot.getId();
                intent.putExtra("category", category);
                intent.putExtra("name", model.getName());
                if (model.getImage() != "")
                    intent.putExtra("image", model.getImage());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("documentId", documentId);
                intent.putExtra("categoryDocumentId",categoryDocumentId);
                startActivity(intent);
            }
        });
        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() != 0)
                {
                    txtEmptyItems.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtEmptyItems.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
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
        builder.setMessage("Are you sure you want to delete this item?")
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
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.floatingActionButton:
                Intent i = new Intent(getApplicationContext(), AdminAddCategoryItemsActivity.class);
                i.putExtra("categoryDocumentId",categoryDocumentId);
                i.putExtra("categoryName",category);
                if (categoryDocumentId.equalsIgnoreCase(""))
                    i.putExtra("isEssential","true");
                else
                    i.putExtra("isEssential","false");
                startActivity(i);
                break;
        }
    }
}