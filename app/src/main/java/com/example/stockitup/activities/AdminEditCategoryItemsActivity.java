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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

/**
 * This class is related to admin.It deals with updation of category Items
 */
public class AdminEditCategoryItemsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName, editDesc, editPrice;
    private String quantity ="0";
    private String name, desc, price, image, documentId, categoryDocumentId;
    private ImageView imageView, imageViewEdit;
    private ProgressBar progressBar;
    private Button btnUpdate;
    private FirebaseFirestore firebaseFirestore;
    private static final int CHOOSE_IMAGE = 101;
    private Uri uriItemImage;
    private String itemImageUrl=null;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_category_items);

        setToolbar();

        editName = findViewById(R.id.editName);
        editDesc = findViewById(R.id.editDesc);
        editPrice = findViewById(R.id.editPrice);
        imageView = findViewById(R.id.imageView);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        progressBar = findViewById(R.id.progressBar);
        btnUpdate = findViewById(R.id.btnUpdate);

        imageViewEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        initializeView();
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
     * This method initializes the view
     * */
    private void initializeView() {
        name = getIntent().getStringExtra("name");
        desc = getIntent().getStringExtra("desc");
        price = getIntent().getStringExtra("price");
        image = getIntent().getStringExtra("image");
        documentId = getIntent().getStringExtra("documentId");
        categoryDocumentId = getIntent().getStringExtra("categoryDocumentId");

        editName.setText(name);
        editDesc.setText(desc);
        editPrice.setText(price);
        if (image != null && !image.isEmpty())
            Picasso.get().load(image).into(imageView);
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
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                updateItemData();
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
     * This method is used validate fields to update Category Item
     */
    private void updateItemData() {
        if (name.isEmpty()) {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        } else {
            uploadImageToFirebaseStorage();
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
                                            //progressBar.setVisibility(View.GONE);
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
        else
        {
            name = editName.getText().toString();
            desc = editDesc.getText().toString();
            price = editPrice.getText().toString();
            DocumentReference documentReference;
            CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, desc, price,quantity);
            if (categoryDocumentId.equalsIgnoreCase(""))
                documentReference = firebaseFirestore.collection(AppConstants.ESSENTIALS_COLLECTION).document(documentId);
            else
                documentReference = firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId);
            documentReference.set(categoryItemsModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminEditCategoryItemsActivity.this, "Item Updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * This method is used to make a database call for adding a category
     * @param itemImageUrl the url of the category image
     */
    private void onUploadImageSuccess(String itemImageUrl) {
        name = editName.getText().toString();
        desc = editDesc.getText().toString();
        price = editPrice.getText().toString();
        image = itemImageUrl;
        DocumentReference documentReference;
        CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, desc, price,quantity);
        if (categoryDocumentId.equalsIgnoreCase(""))
            documentReference = firebaseFirestore.collection(AppConstants.ESSENTIALS_COLLECTION).document(documentId);
        else
            documentReference = firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId);
        documentReference.set(categoryItemsModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminEditCategoryItemsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}