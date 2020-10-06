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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class AdminEditCategoryItemsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName, editDesc, editPrice;
    String quantity ="0";
    private String name, desc, price, image, documentId, categoryDocumentId;
    private ImageView imageView, imageViewEdit;
    private ProgressBar progressBar;
    private Button btnUpdate;
    private FirebaseFirestore firebaseFirestore;
    private static final int CHOOSE_IMAGE = 101;
    private Uri uriItemImage;
    private String itemImageUrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_category_items);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        editName = findViewById(R.id.editName);
        editDesc = findViewById(R.id.editDesc);
        editPrice = findViewById(R.id.editPrice);
        imageView = findViewById(R.id.imageView);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        imageViewEdit.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

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
            CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, desc, price,quantity);
            firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId)
                    .set(categoryItemsModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminEditCategoryItemsActivity.this, "Category Item Updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void onUploadImageSuccess(String itemImageUrl) {
        name = editName.getText().toString();
        desc = editDesc.getText().toString();
        price = editPrice.getText().toString();
        image = itemImageUrl;
        CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name, image, desc, price,quantity);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId)
                .set(categoryItemsModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminEditCategoryItemsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}