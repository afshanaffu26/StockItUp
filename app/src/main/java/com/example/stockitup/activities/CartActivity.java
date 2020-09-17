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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.fragments.CartFragment;
import com.example.stockitup.models.CategoryItemsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    String uid;
    ProgressBar progressBar;
    Button btnCheckout;
    FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    String documentId;
    TextView txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtEmptyCart;
    double subTotal,deliveryCharge,tax,total;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadAndCalculateCartTotal();
        setCartData();

    }

    private void setCartData() {
        //Query
        Query query = firebaseFirestore.collection("Cart").document("cart"+uid).collection("cart").orderBy("name",Query.Direction.ASCENDING);
        //RecyclerOptions
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query,CategoryItemsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<CategoryItemsModel, CartActivity.CartViewHolder>(options) {
            @NonNull
            @Override
            public CartActivity.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
                return new CartActivity.CartViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull CartActivity.CartViewHolder holder, final int position, @NonNull final CategoryItemsModel model) {
                progressBar.setVisibility(View.VISIBLE);
                holder.txtName.setText(model.getName());
                holder.txtQuantity.setText("Qty: "+model.getQuantity());
                holder.txtPrice.setText("Price: "+model.getPrice()+"$");
                Picasso.get().load(model.getImage()).into(holder.imageView);
                loadAndCalculateCartTotal();

                holder.imgDeleteBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        documentId = getSnapshots().getSnapshot(position).getId();
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseFirestore.collection("Cart").document("cart"+uid).collection("cart").document(documentId)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadAndCalculateCartTotal();
                                progressBar.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"Item removed from Cart",Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void
                    onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ItemDescriptionActivity.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("price", model.getPrice());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("quantity",model.getQuantity());
                        intent.putExtra("screen","cart");
                        documentId = getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("documentId",documentId);
                        startActivity(intent);
                    }
                });
                progressBar.setVisibility(View.GONE);
            }

        };
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    /**
     * This method is used to calculate cart total
     */
    private void loadAndCalculateCartTotal() {
        subTotal =0.0;
        txtEmptyCart.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Cart").document("cart"+uid).collection("cart")
                .get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // if(task.getResult().size() == 0)
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
                Intent intent = new Intent(getApplicationContext(),AddressActivity.class);
                intent.putExtra("subTotal", ""+subTotal);
                intent.putExtra("tax", ""+tax);
                intent.putExtra("deliveryCharge", ""+deliveryCharge);
                intent.putExtra("total", ""+total);
                startActivity(intent);
                break;
        }
    }

    /**
     * This class describes the content of each cart item in recycler view
     * Subclass of {@link RecyclerView.ViewHolder}
     */
    private class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName,txtQuantity,txtPrice;
        private ImageView imageView;
        LinearLayout linearLayout;
        ImageButton imgDeleteBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imageView = itemView.findViewById(R.id.imageView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            imgDeleteBtn = itemView.findViewById(R.id.imgDeleteBtn);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}