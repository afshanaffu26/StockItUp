package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

/**
 * This class deals with history previously ordered items
 */
public class OrderHistoryListActivity extends AppCompatActivity{
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private String cuisine ="";
    private TextView txtCuisine;
    private String documentId,orderHistoryDocumentId;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flavours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        txtCuisine = findViewById(R.id.txtCuisine);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        orderHistoryDocumentId = getIntent().getStringExtra("orderHistoryDocumentId");

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Query query = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders"+uid).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(orderHistoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT);
        //RecyclerOptions
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query,CategoryItemsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<CategoryItemsModel, OrderHistoryListActivity.OrderHistoryListViewHolder>(options) {

            @NonNull
            @Override
            public OrderHistoryListActivity.OrderHistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list,parent,false);
                return new OrderHistoryListActivity.OrderHistoryListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryListActivity.OrderHistoryListViewHolder holder, final int position, @NonNull final CategoryItemsModel model) {
                holder.txtName.setText(model.getName());
                holder.txtPrice.setText("Price: "+model.getPrice());
                holder.txtQuantity.setText("Qty: "+model.getQuantity());
                if (model.getImage()!= null && !model.getImage().isEmpty())
                    Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ItemDescriptionActivity.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("price", model.getPrice());
                        intent.putExtra("desc", model.getDesc());
                        documentId = getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("documentId", documentId);
                        startActivity(intent);
                    } });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
    private class OrderHistoryListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName,txtQuantity,txtPrice;
        private ImageView imageView;
        LinearLayout linearLayout;
        public OrderHistoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imageView = itemView.findViewById(R.id.imageView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
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