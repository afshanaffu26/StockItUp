package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminAddCategoryItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private String category,documentId;
    private EditText editName,editPrice,editDescription;
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_items);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.spinner);
        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        editDescription = findViewById(R.id.editDescription);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayList<String> categoriesList = new ArrayList<>();
        categoriesList.add("Select a category..");
        categoriesList.addAll(AppConstants.categories_map.keySet());
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i!=0)
            category = adapterView.getSelectedItem().toString();
        else
            category = "";
        Toast.makeText(AdminAddCategoryItemsActivity.this, "Selected: "+category, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btnAdd:
                addItemToCategory();
                break;
        }
    }

    private void addItemToCategory() {
        String price = editPrice.getText().toString();
        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        String image="";
        documentId = AppConstants.categories_map.get(category);
        if (category == "")
        {
            Toast.makeText(AdminAddCategoryItemsActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.isEmpty())
        {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }
        if(description.isEmpty())
        {
            editDescription.setError("Description is required");
            editDescription.requestFocus();
            return;
        }
        if(price.isEmpty())
        {
            editPrice.setError("Price is required");
            editPrice.requestFocus();
            return;
        }
        else
        {
            CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, description, price);
            firebaseFirestore.collection("Categories").document(documentId).collection(category)
                    .add(categoryItemsModel)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(AdminAddCategoryItemsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}