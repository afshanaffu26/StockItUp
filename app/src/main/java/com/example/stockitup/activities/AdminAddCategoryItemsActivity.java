package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoriesModel;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdminAddCategoryItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String name,image,description,price;
    String quantity = "0";
    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private String category,documentId;
    private EditText editName,editPrice,editDescription;
    ImageView imageView,imageViewEdit;
    private Button btnAdd;
    private ProgressBar progressBar;
    private static final int CHOOSE_IMAGE = 101;
    private Uri uriItemImage = null;
    private String itemImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_category_items);

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
        imageView =  findViewById(R.id.imageView);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        imageViewEdit.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        spinner.setOnItemSelectedListener(this);
        progressBar = findViewById(R.id.progressBar);

        ArrayList<String> categoriesList = new ArrayList<>();
        categoriesList.add("Select a category..");
        categoriesList.addAll(AppConstants.categories_map.keySet());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categoriesList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

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
            case R.id.imageViewEdit:
                showImageChooser();
                break;
        }
    }

    private void showImageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"), CHOOSE_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriItemImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriItemImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to upload image to firebase
     */
    private void uploadImageToFirebaseStorage() {
        String imageId = "" + UUID.randomUUID().toString();
        final StorageReference itemImageRef = FirebaseStorage.getInstance().getReference()
                .child("ingredientsImages")
                .child(imageId + ".jpeg");
        if (uriItemImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            itemImageRef.putFile(uriItemImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //itemImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                            itemImageRef.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
//                                            progressBar.setVisibility(View.GONE);
                                            itemImageUrl = uri.toString();
                                            //Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                            onUploadImageSuccess(itemImageUrl);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void onUploadImageSuccess(String itemImageUrl) {
        image = itemImageUrl;
        CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, description, price,quantity);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(documentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                .add(categoryItemsModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminAddCategoryItemsActivity.this, "Category Item Added.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void addItemToCategory() {
        price = editPrice.getText().toString();
        name = editName.getText().toString();
        description = editDescription.getText().toString();
        image="";
        documentId = AppConstants.categories_map.get(category);
        if (category == "")
        {
            Toast.makeText(AdminAddCategoryItemsActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uriItemImage == null){
            Toast.makeText(AdminAddCategoryItemsActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
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
            uploadImageToFirebaseStorage();
        }
    }
}