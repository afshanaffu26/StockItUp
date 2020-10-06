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
import com.example.stockitup.models.CategoriesModel;
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

public class AdminEditCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName;
    private Button btnUpdate;
    private ImageView imageViewEdit, imageView;
    private String image, documentId, name;
    private FirebaseFirestore firebaseFirestore;
    private static final int CHOOSE_IMAGE = 101;
    private Uri uriItemImage;
    private String itemImageUrl = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_category);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        editName = findViewById(R.id.editName);
        btnUpdate = findViewById(R.id.btnUpdate);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);

        imageViewEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        documentId = getIntent().getStringExtra("categoryDocumentId");

        editName.setText(name);
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
                updateCategory();
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

    private void updateCategory() {
        if(name.isEmpty())
        {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }
        else {
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
                                            itemImageUrl = uri.toString();
                                            //Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                            onUploadImageSuccess(uri);
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
            CategoriesModel categoriesModel = new CategoriesModel(name, image);
            firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(documentId)
                    .set(categoriesModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Category Updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void onUploadImageSuccess(Uri uri) {
        name = editName.getText().toString();
        image = uri.toString();
        CategoriesModel categoriesModel = new CategoriesModel(name, image);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(documentId)
                .set(categoriesModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}