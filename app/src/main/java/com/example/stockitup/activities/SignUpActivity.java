package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to sign up a user to firebase
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtLogin;
    private TextView txtConditions;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    private EditText editName,editEmail,editPassword,editConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button btnSignUp;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        //display back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(this);
        txtConditions = findViewById(R.id.txtConditions);
        txtConditions.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        editName=(EditText) findViewById(R.id.editName);
        editEmail=(EditText) findViewById(R.id.editEmail);
        editPassword=(EditText) findViewById(R.id.editPassword);
        editConfirmPassword=(EditText) findViewById(R.id.editConfirmPassword);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        checkBox = findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtLogin:
                finish();
                Intent i= new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.txtConditions:
                startActivity(new Intent(getApplicationContext(), TermsActivity.class));
                break;
            case R.id.btnSignUp:
                userSignUp();
                break;
        }
    }
    /**
     * This method is used to sign up a user and redirect him to login page.
     */
    public void userSignUp() {
        final String name = editName.getText().toString().trim();
        String email=editEmail.getText().toString().trim();
        String password=editPassword.getText().toString().trim();
        String confirmPassword=editConfirmPassword.getText().toString().trim();
        if(name.isEmpty())
        {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please enter valid email");
            editEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editPassword.setError("Minimum length of password should be 6");
            editPassword.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty())
        {
            editConfirmPassword.setError("Password is required");
            editConfirmPassword.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword))
        {
            editPassword.setError("Passwords do not match");
            editPassword.requestFocus();
            return;
        }
        if(!checkBox.isChecked())
        {
            Toast.makeText(getApplicationContext(),"Please read and accept the Terms and Conditions",Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    finish();
                    Intent i= new Intent(SignUpActivity.this,HomeScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    user.updateProfile(profileUpdates);
                    fetchAllConfigurations();
                    //if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    //FirebaseAuth.getInstance().signOut();
                    //finish();
                    //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                    //}
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(),"Email is already registered",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void fetchAllConfigurations() {
        final Map<String,String> map = new HashMap<String, String>();
        firebaseFirestore.collection(AppConstants.OFFERS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot documentSnapshot: task.getResult())
                            {
                                map.put(documentSnapshot.getString("name"),documentSnapshot.getString("value"));
                            }
                            AppConstants.OFFERS_MAP = map;
                        }
                    }
                });
        firebaseFirestore.collection(AppConstants.APP_SUPPORT_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        task.getResult().getDocuments().get(0).get("customerCareNumber");
                        AppConstants.TOLL_FREE_NUMBER = task.getResult().getDocuments().get(0).get("tollFreeNumber").toString();
                        AppConstants.CUSTOMER_CARE_NUMBER = task.getResult().getDocuments().get(0).get("customerCareNumber").toString();
                    }
                });
    }
}