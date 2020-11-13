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

/**
 * This class is related to admin.It deals with addition of new items
 */
public class AdminAddItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String name,image,description,price;
    private String quantity = "0";
    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private String category,documentId;
    private EditText editName,editPrice,editDescription;
    private ImageView imageView,imageViewEdit;
    private Button btnAdd;
    private ProgressBar progressBar;
    private static final int CHOOSE_IMAGE = 101;
    private Uri uriItemImage = null;
    private String itemImageUrl = null;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_items);

        setToolbar();

        spinner = findViewById(R.id.spinner);
        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        editDescription = findViewById(R.id.editDescription);
        btnAdd = findViewById(R.id.btnAdd);
        imageView =  findViewById(R.id.imageView);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        progressBar = findViewById(R.id.progressBar);

        imageViewEdit.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        setSpinner();
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
     * This method sets up the spinner data
     * */
    private void setSpinner() {
        ArrayList<String> categoriesList = new ArrayList<>();
        categoriesList.add("Select a category..");
        categoriesList.addAll(AppConstants.CATEGORIES_MAP.keySet());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categoriesList);
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

    /**
     *Callback method to be invoked when an item in this view has been selected.
     * @param adapterView The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param i The position of the view in the adapter
     * @param l The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i!=0)
            category = adapterView.getSelectedItem().toString();
        else
            category = "";
    }

    /**
     *  Callback method to be invoked when the selection disappears from this view
     * @param adapterView The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
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

    /**
     * This method is used to open device storage for choosing image
     */
    private void showImageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"), CHOOSE_IMAGE);
    }

    /**
     * This method is used to assign the chosen image from device to imageview
     */
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

    /**
     * This method is used to make a database call for adding an item
     * @param itemImageUrl the url of the item image
     */
    private void onUploadImageSuccess(String itemImageUrl) {
        image = itemImageUrl;
        CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, description, price,quantity);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(documentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                .add(categoryItemsModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminAddItemsActivity.this, "Category Item Added.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This method is used validate fields to add Category Item
     */
    private void addItemToCategory() {
        price = editPrice.getText().toString();
        name = editName.getText().toString();
        description = editDescription.getText().toString();
        image="";
        documentId = AppConstants.CATEGORIES_MAP.get(category);
        if (category == "")
        {
            Toast.makeText(AdminAddItemsActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uriItemImage == null){
            Toast.makeText(AdminAddItemsActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
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