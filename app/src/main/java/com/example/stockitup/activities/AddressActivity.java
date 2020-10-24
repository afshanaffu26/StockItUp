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
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.adapters.AddressAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.AddressModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AddressActivity extends AppCompatActivity{

    private FirebaseFirestore firebaseFirestore;
    private AddressAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private String uid;
    private TextView txtEmpty;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        txtEmpty = findViewById(R.id.txtEmpty);
        linearLayout = findViewById(R.id.linearLayout);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        txtEmpty.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                startActivity(intent);
            }
        });

        setRecyclerViewData();

    }

    private void setRecyclerViewData() {
        final Query query = firebaseFirestore.collection(AppConstants.ADDRESS_COLLECTION).document("address"+uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT);
        FirestoreRecyclerOptions<AddressModel> options = new FirestoreRecyclerOptions.Builder<AddressModel>()
                .setQuery(query,AddressModel.class)
                .build();
        adapter = new AddressAdapter(options);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                AddressModel model = documentSnapshot.toObject(AddressModel.class);
                String documentId = documentSnapshot.getId();
                if (view.getId() == R.id.imgBtnEditAddress)
                {
                    Intent intent = new Intent(getApplicationContext(), EditAddressActivity.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("addressLine", model.getAddressLine());
                    intent.putExtra("city", model.getCity());
                    intent.putExtra("province", model.getProvince());
                    intent.putExtra("country", model.getCountry());
                    intent.putExtra("pincode", model.getPincode());
                    intent.putExtra("phone", model.getPhone());
                    intent.putExtra("documentId", documentId);
                    startActivity(intent);
                }
                else {
                    if (getIntent().getStringExtra("screen") == null )
                    {
                        String address = ""+model.getAddressLine() + " " + model.getCity() + " " + model.getProvince()+" "+model.getCountry()+" " +model.getPincode();
                        goToPayment(address);
                    }
                    else if (getIntent().getStringExtra("screen").equalsIgnoreCase("settings"))
                    {
                        return;
                    }
                }
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

                if (adapter.getItemCount() != 0)
                {
                    if (adapter.getItemCount() < 3)
                    {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        floatingActionButton.setVisibility(View.GONE);
                    }
                    txtEmpty.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
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
    /**
     * Navigates to payment page to show break up of total amount being charged.
     */
    private void goToPayment(String address) {
        String subTotal,deliveryCharge,tax,total,offer,offerPercent;
        subTotal = getIntent().getStringExtra("subTotal");
        offerPercent = getIntent().getStringExtra("offerPercent");
        offer = getIntent().getStringExtra("offer");
        tax = getIntent().getStringExtra("tax");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra("subTotal", subTotal);
        intent.putExtra("offerPercent",offerPercent);
        intent.putExtra("offer", offer);
        intent.putExtra("tax", tax);
        intent.putExtra("deliveryCharge", deliveryCharge);
        intent.putExtra("total", total);
        intent.putExtra("address", address);
        startActivity(intent);
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