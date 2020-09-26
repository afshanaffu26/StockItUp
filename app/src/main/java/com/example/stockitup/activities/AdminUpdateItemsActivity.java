package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
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

public class AdminUpdateItemsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName,editDesc,editPrice;
    private String name,desc,price,image,documentId,categoryDocumentId;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button btnUpdate;
    private FirebaseFirestore firebaseFirestore;

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