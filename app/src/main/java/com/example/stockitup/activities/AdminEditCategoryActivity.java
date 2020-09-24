package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoriesModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AdminEditCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName;
    private Button btnUpdate;
    private ImageView imageViewEdit,imageView;
    private String image,documentId,name;
    private FirebaseFirestore firebaseFirestore;

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

        imageViewEdit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        documentId = getIntent().getStringExtra("categoryDocumentId");

        editName.setText(name);
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
        switch (view.getId())
        {
            case R.id.btnUpdate:
                updateCategory();
                break;
            case R.id.imageViewEdit:
                break;
        }
    }

    private void updateCategory() {
        String name,image;
        name = editName.getText().toString();
        image = "";
        CategoriesModel categoriesModel = new CategoriesModel(name,image);
        firebaseFirestore.collection(AppConstants.CATEGORY_COLLECTION).document(documentId)
                .set(categoriesModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}