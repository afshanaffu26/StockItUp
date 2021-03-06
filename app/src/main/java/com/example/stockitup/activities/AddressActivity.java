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

/**
 * This class deals with user addresses
 */
public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private AddressAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private String uid;
    private TextView txtEmpty;
    private LinearLayout linearLayout;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        setToolbar();
        initializeReferencesAndListeners();
        initializeViewAndControls();
        setRecyclerViewData();
    }

    /**
     * sets toolbar title, back navigation
     * */
    private void setToolbar() {
        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * initialize references and listeners
     * */
    private void initializeReferencesAndListeners() {
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        txtEmpty = findViewById(R.id.txtEmpty);
        linearLayout = findViewById(R.id.linearLayout);

        floatingActionButton.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    /**
     * This method initializes the view and controls
     * */
    private void initializeViewAndControls() {
        txtEmpty.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * This method is used to set the data to a recyclerview
     */
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

    /**
     * This method is used to show an alert with an appropriate message
     * @param position position of item in a recycler view
     */
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
    /**
     * When the activity enters the Started state, the system invokes this callback. The onStart() call makes the activity visible to the user, as the app prepares for the activity to enter the foreground and become interactive.
     * */
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /**
     * onStop is called whenever an application goes out of view, and is no longer visible. This is usually caused by a new activity being created over the top of the old one.
     * */
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
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButton:
                navigateToAddAddressActivity();
                break;
        }
    }

    /**
     * Navigates to AddAddressActivity
     * */
    private void navigateToAddAddressActivity() {
        Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
        startActivity(intent);
    }
}