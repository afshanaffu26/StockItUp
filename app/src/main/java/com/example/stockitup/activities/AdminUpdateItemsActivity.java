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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AdminUpdateItemsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName,editDesc,editPrice;
    private String name,desc,price,image,documentId,categoryDocumentId;
    private ImageView imageView,imageViewEdit;
    private ProgressBar progressBar;
    private Button btnUpdate;
    private FirebaseFirestore firebaseFirestore;
    private static final int CHOOSE_IMAGE = 101;
    private Uri uriItemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_items);

        String appName = getApplicationContext().getResources().getString(R.string.app_name);
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

        if (image!=null && !image.isEmpty())
            Picasso.get().load(image).into(imageView);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
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
        String name = editName.getText().toString();
        String desc = editDesc.getText().toString();
        String price = editPrice.getText().toString();
        String image = "";
        CategoryItemsModel categoryItemsModel = new CategoryItemsModel(name,image,desc,price);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(categoryDocumentId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(documentId)
                .set(categoryItemsModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminUpdateItemsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}