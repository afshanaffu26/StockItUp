package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the details about a category item
 */
public class ItemDescriptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final int MAX_QUANTITY = 6;
    private Button btnAddToCart;
    private ImageView imageView;
    private TextView txtName,txtPrice,txtDesc;
    private FirebaseFirestore firebaseFirestore;
    private String name,image,price,quantity,id,desc,documentId;
    private Spinner spinner;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        imageView = findViewById(R.id.imageView);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.price);
        txtDesc = findViewById(R.id.txtDesc);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(this);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<Integer> categories = new ArrayList<>();
        for (int i=1;i<=MAX_QUANTITY;i++){
            categories.add(i);
        }
        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (getIntent().getStringExtra("screen") == null ) {
            btnAddToCart.setText("Add to Cart");
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        else if (getIntent().getStringExtra("screen").equalsIgnoreCase("cart")) {
            btnAddToCart.setText("Update Cart");
            floatingActionButton.setVisibility(View.GONE);
        }
        else if (getIntent().getStringExtra("screen").equalsIgnoreCase("orders")) {
            btnAddToCart.setText("Add to Cart");
            floatingActionButton.setVisibility(View.GONE);
        }

        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        price = getIntent().getStringExtra("price");
        desc = getIntent().getStringExtra("desc");
        quantity = getIntent().getStringExtra("quantity");
        documentId = getIntent().getStringExtra("documentId");
        if (image != null && !image.isEmpty())
            Picasso.get().load(image).into(imageView);
        txtName.setText(name);
        txtPrice.setText("Price : " + price+"$");
        txtDesc.setText(desc);
        spinner.setSelection(getIndex(spinner, quantity));

    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    /**
     * This method adds a particular product to cart
     * @param name Product name
     * @param image Product image
     * @param price Product price
     * @param desc Product description
     */
    private void addItemToCart(String name, String image,String desc, String price, String quantity) {
        CategoryItemsModel cuisineItemsModel = new CategoryItemsModel(name, image, desc, price,quantity);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection(AppConstants.CART_COLLECTION).document("cart"+uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId)
                .set(cuisineItemsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Items added to cart succesfully.",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
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
     *Callback method to be invoked when an item in this view has been selected.
     * @param adapterView The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param i The position of the view in the adapter
     * @param l The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     *  Callback method to be invoked when the selection disappears from this view
     * @param adapterView The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAddToCart:
                quantity = spinner.getSelectedItem().toString();
                addItemToCart(name,image,desc,price,quantity);
                break;
            case R.id.floatingActionButton:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                break;
        }
    }
}