package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stockitup.R;
import com.example.stockitup.adapters.CategoryItemsAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CategoryItemsActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseFirestore firebaseFirestore;
    private CategoryItemsAdapter adapter;
    private RecyclerView recyclerView;
    private String category ="";
    private TextView txtCategory,txtEmptyItems;
    private String categoryDocumentId;
    private FloatingActionButton floatingActionButton;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        txtCategory = findViewById(R.id.txtCategory);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        linearLayout = findViewById(R.id.linearLayout);
        txtEmptyItems = findViewById(R.id.txtEmptyItems);

        floatingActionButton.setOnClickListener(this);
        categoryDocumentId = getIntent().getStringExtra("categoryDocumentId");
        category = getIntent().getStringExtra("name");
        txtCategory.setText(category);
        setRecyclerViewData();
    }

    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).orderBy("name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query, CategoryItemsModel.class)
                .build();
        adapter= new CategoryItemsAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view,DocumentSnapshot documentSnapshot, int position) {
                CategoryItemsModel model = documentSnapshot.toObject(CategoryItemsModel.class);
                Intent intent = new Intent(getApplicationContext(), ItemDescriptionActivity.class);
                String documentId = documentSnapshot.getId();
                intent.putExtra("category", category);
                intent.putExtra("name", model.getName());
                if (model.getImage() != null && !model.getImage().isEmpty())
                    intent.putExtra("image", model.getImage());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("documentId", documentId);
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
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.floatingActionButton:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                break;
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}