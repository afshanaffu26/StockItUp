package com.example.stockitup.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.stockitup.activities.ItemDescriptionActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        linearLayout = v.findViewById(R.id.linearLayout);
        txtSubTotal = v.findViewById(R.id.txtSubTotal);
        txtTax = v.findViewById(R.id.txtTax);
        txtDeliveryCharge = v.findViewById(R.id.txtDeliveryCharge);
        txtTotal = v.findViewById(R.id.txtTotal);
        txtEmptyCart = v.findViewById(R.id.txtEmptyCart);
        progressBar = v.findViewById(R.id.progressbar);
        btnCheckout = v.findViewById(R.id.btnCheckout);
        recyclerView = v.findViewById(R.id.recyclerView);

        btnCheckout.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadAndCalculateCartTotal();

        //Query
        Query query = firebaseFirestore.collection("Cart").document("cart"+uid).collection("cart");
        //RecyclerOptions
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query,CategoryItemsModel.class)
                .build();
        //progressBar.setVisibility(View.VISIBLE);
        adapter = new FirestoreRecyclerAdapter<CategoryItemsModel, CartFragment.CartViewHolder>(options) {
            @NonNull
            @Override
            public CartFragment.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
                return new CartFragment.CartViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull CartFragment.CartViewHolder holder, final int position, @NonNull final CategoryItemsModel model) {
                holder.txtName.setText(model.getName());
                holder.txtQuantity.setText("Qty: "+model.getQuantity());
                holder.txtPrice.setText("Price: "+model.getPrice()+"$");
                Picasso.get().load(model.getImage()).into(holder.imageView);
                loadAndCalculateCartTotal();

                holder.imgDeleteBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        documentId = getSnapshots().getSnapshot(position).getId();
                        firebaseFirestore.collection("Cart").document("cart"+uid).collection("cart").document(documentId)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadAndCalculateCartTotal();
                                Toast.makeText(getContext(),"Item removed from Cart",Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
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
                        documentId = getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("documentId",documentId);
                        startActivity(intent);
                    } });
                progressBar.setVisibility(View.GONE);
            }

        };
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return v;
    }

    /**
     * This method is used to calculate cart total depending on selected items
     */
    private void loadAndCalculateCartTotal() {
        subTotal =0.0;
        txtEmptyCart.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        firebaseFirestore.collection("Cart").document("cart"+uid).collection("cart")
                .get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() == 0)
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
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
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckout:
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
}