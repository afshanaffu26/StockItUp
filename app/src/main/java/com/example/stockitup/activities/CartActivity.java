package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.adapters.CartAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private String uid;
    private ProgressBar progressBar;
    private Button btnCheckout;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private CartAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private TextView txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtEmptyCart;
    private double subTotal,deliveryCharge,tax,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.linearLayout);
        txtSubTotal = findViewById(R.id.txtSubTotal);
        txtTax = findViewById(R.id.txtTax);
        txtDeliveryCharge = findViewById(R.id.txtDeliveryCharge);
        txtTotal = findViewById(R.id.txtTotal);
        txtEmptyCart = findViewById(R.id.txtEmptyCart);
        progressBar = findViewById(R.id.progressbar);
        btnCheckout = findViewById(R.id.btnCheckout);
        recyclerView = findViewById(R.id.recyclerView);

        btnCheckout.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        collectionReference = firebaseFirestore.collection(AppConstants.CART_COLLECTION).document("cart"+uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT);

        setRecyclerViewData();
    }
    private void setRecyclerViewData() {
        Query query = collectionReference.orderBy("name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query,CategoryItemsModel.class)
                .build();
        adapter = new CartAdapter(options);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        loadAndCalculateCartTotal();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        })
        .attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view,DocumentSnapshot documentSnapshot, int position) {
                CategoryItemsModel model = documentSnapshot.toObject(CategoryItemsModel.class);
                Intent intent = new Intent(getApplicationContext(), ItemDescriptionActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("image", model.getImage());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("quantity",model.getQuantity());
                intent.putExtra("screen","cart");
                String documentId = documentSnapshot.getId();
                intent.putExtra("documentId",documentId);
                startActivity(intent);
            }
        });
        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                loadAndCalculateCartTotal();
            }
        });
    }

    /**
     * This method is used to calculate cart total
     */
    public void loadAndCalculateCartTotal() {
        subTotal =0.0;
        txtEmptyCart.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection(AppConstants.CART_COLLECTION).document("cart"+uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                .get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        subTotal = 0.0;
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                CategoryItemsModel cuisineItemsModel = documentSnapshot.toObject(CategoryItemsModel.class);
                                double price = Double.parseDouble(cuisineItemsModel.getPrice());
                                double quantity = Double.parseDouble(cuisineItemsModel.getQuantity());
                                subTotal += price * quantity;
                            }

                            if (!(subTotal == (double)0.0)) {
                                linearLayout.setVisibility(View.VISIBLE);
                                txtEmptyCart.setVisibility(View.GONE);
                            }
                            else
                            {
                                txtEmptyCart.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                            }
                            calculateTotal(subTotal);
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to calculate the total amount including taxes and delivery fees
     * @param subTotal This param is total before taxes
     */
    private void calculateTotal(double subTotal){
        txtSubTotal.setText(""+subTotal+"$");
        tax = (15.0*subTotal)/100;
        txtTax.setText(""+tax+"$");
        deliveryCharge = 3;
        txtDeliveryCharge.setText(""+deliveryCharge+"$");
        total = subTotal + tax + deliveryCharge;
        txtTotal.setText(""+total+"$");
        progressBar.setVisibility(View.GONE);

    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckout:
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                intent.putExtra("subTotal", ""+subTotal);
                intent.putExtra("tax", ""+tax);
                intent.putExtra("deliveryCharge", ""+deliveryCharge);
                intent.putExtra("total", ""+total);
                startActivity(intent);
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
}