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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * This class manages user profile
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtVerifyUser;
    private EditText editName;
    private Button btnSave;
    private ImageView imgUser;
    private Uri uriProfileImage;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private static final int CHOOSE_IMAGE = 101;
    private String profileImageUrl=null;
    private String displayName= null;
    private static final String TAG = "ProfileActivity";
    private int TAKE_IMAGE_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        //display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = (EditText) findViewById(R.id.editDisplayName);
        btnSave = findViewById(R.id.btnSave);
        imgUser = findViewById(R.id.imageUserProfile);
        progressBar = findViewById(R.id.progressbar);
        txtVerifyUser=findViewById(R.id.txtVerifyUser);

        btnSave.setOnClickListener(this);
        imgUser.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        loadUserInformation();
    }

    /**
     * This method get profile data from firebase
     */
    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null) {

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(imgUser);
            }
            if (user.getDisplayName()!=null) {
                String displayName = user.getDisplayName();
                editName.setText(displayName);
                editName.setSelection(user.getDisplayName().length());
            }
            if (user.isEmailVerified())
            {
                txtVerifyUser.setText("Email Verified");
            }
            else
            {
                txtVerifyUser.setText("Email Not Verified(Click to verify)");
                txtVerifyUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProfileActivity.this,"Verification Email Sent",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                saveUserInfoUpdateName();
                break;
            case R.id.imageUserProfile:
                showImageChooser();
                //handleImageClick();
                break;
        }
    }

    /**
     * This method update name in user profile in firebase
     */
    private void saveUserInfoUpdateName() {

        displayName=editName.getText().toString();
        if(displayName.isEmpty()){
            editName.setError("Name required");
            editName.requestFocus();
            return;
        }
        btnSave.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        uploadImageToFirebaseStorage();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();
        firebaseUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        btnSave.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Profile name updated successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnSave.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void showImageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Profile Image"), CHOOSE_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imgUser.setImageBitmap(bitmap);
//                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to upload image to firebase
     */
    private void uploadImageToFirebaseStorage() {
        //final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid+".jpeg");
        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressBar.setVisibility(View.GONE);
                            //profileImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                            profileImageRef.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //progressBar.setVisibility(View.GONE);
                                            profileImageUrl = uri.toString();
                                            //Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                            saveUserInfo(uri);
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
     * This method is used to save user data to firebase
     * @param uri
     */
    private void saveUserInfo(Uri uri) {

//        String displayName= editName.getText().toString();
//        if(displayName.isEmpty()){
//            editName.setError("Name required");
//            editName.requestFocus();
//            return;
//        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    // .setDisplayName(displayName)
                    //   .setPhotoUri(Uri.parse(profileImageUrl))
                    .setPhotoUri(uri)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ProfileActivity.this,"Profile updated",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
}