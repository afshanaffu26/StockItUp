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
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DecimalFormat;

/**
 * This class manages cart related functionalityi.
 */
public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private String uid;
    private ProgressBar progressBar;
    private Button btnCheckout,btnPromo;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private CartAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private EditText editPromo;
    private TextView txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtEmpty,txtOfferName,txtOffer,txtPromo;
    private double subTotal,deliveryCharge,tax,total,offer;
    private double offerPercent = 0;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.linearLayout);
        txtSubTotal = findViewById(R.id.txtSubTotal);
        txtTax = findViewById(R.id.txtTax);
        txtDeliveryCharge = findViewById(R.id.txtDeliveryCharge);
        txtTotal = findViewById(R.id.txtTotal);
        txtEmpty = findViewById(R.id.txtEmpty);
        progressBar = findViewById(R.id.progressbar);
        btnCheckout = findViewById(R.id.btnCheckout);
        recyclerView = findViewById(R.id.recyclerView);
        txtOfferName = findViewById(R.id.txtOfferName);
        txtOffer = findViewById(R.id.txtOffer);
        editPromo = findViewById(R.id.editPromo);
        btnPromo = findViewById(R.id.btnPromo);
        txtPromo = findViewById(R.id.txtPromo);
        btnPromo.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        btnPromo.setText("APPLY");
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
                final int position = viewHolder.getAdapterPosition();
                alertMessage(position);
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
    public void alertMessage(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        adapter.deleteItem(position);
                        Toast.makeText(getApplicationContext(), "Item removed from cart successfully.",
                                Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        adapter.notifyItemChanged(position);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this item from cart?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    /**
     * This method is used to calculate cart total
     */
    public void loadAndCalculateCartTotal() {
        subTotal =0.0;
        txtEmpty.setVisibility(View.GONE);
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
                                txtEmpty.setVisibility(View.GONE);
                            }
                            else
                            {
                                txtEmpty.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                            }
                            calculateTotal();
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
     */
    private void calculateTotal(){
        txtSubTotal.setText(""+String.format("%.2f", subTotal)+"$");
        if (offerPercent != 0) {
            txtOfferName.setText("Offer (-"+offerPercent+"%) ");
            offer = (offerPercent * subTotal) / 100;
            txtOfferName.setVisibility(View.VISIBLE);
            txtOffer.setVisibility(View.VISIBLE);
            txtOffer.setText("-"+String.format("%.2f", offer)+"$");
        }
        else
        {
            offer = 0;
            offerPercent = 0;
            txtOfferName.setVisibility(View.GONE);
            txtOffer.setVisibility(View.GONE);
        }
        tax = (15.0*subTotal)/100;
        txtTax.setText(""+String.format("%.2f", tax)+"$");
        deliveryCharge = 3.00;
        txtDeliveryCharge.setText(""+String.format("%.2f", deliveryCharge)+"$");
        total = subTotal - offer + tax + deliveryCharge;
        txtTotal.setText(""+String.format("%.2f", total)+"$");
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckout:
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                intent.putExtra("subTotal", String.format("%.2f", subTotal));
                intent.putExtra("offerPercent",String.format("%.2f", offerPercent));
                intent.putExtra("offer", String.format("%.2f", offer));
                intent.putExtra("tax", String.format("%.2f", tax));
                intent.putExtra("deliveryCharge", String.format("%.2f", deliveryCharge));
                intent.putExtra("total", String.format("%.2f", total));
                startActivity(intent);
                break;
            case R.id.btnPromo:
                if (btnPromo.getText().toString().equalsIgnoreCase("apply"))
                    applyPromo();
                else
                    removePromo();
                break;
        }
    }

    private void removePromo() {
        btnPromo.setText("APPLY");
        txtPromo.setVisibility(View.GONE);
        editPromo.setVisibility(View.VISIBLE);
        editPromo.setText("");
        offerPercent = 0;
        calculateTotal();
    }

    private void applyPromo() {
        String promo = editPromo.getText().toString();
//        if(promo.isEmpty())
//        {
//            editPromo.setError("Promo is empty.");
//            editPromo.requestFocus();
//            return;
//        }
        if (AppConstants.OFFERS_MAP.containsKey(promo))
        {
            double off = Double.parseDouble(AppConstants.OFFERS_MAP.get(promo));
            if (off > 0 && off <= 75) {
                offerPercent = off;
                btnPromo.setText("REMOVE");
                txtPromo.setVisibility(View.VISIBLE);
                txtPromo.setText(promo);
                editPromo.setVisibility(View.GONE);
            }
            else {
                Toast.makeText(getApplicationContext(), "Error applying promo.", Toast.LENGTH_SHORT).show();
            }
            calculateTotal();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Promo not available.",Toast.LENGTH_SHORT).show();
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