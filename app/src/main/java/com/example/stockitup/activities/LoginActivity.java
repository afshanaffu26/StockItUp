package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockitup.Notifications.SendNotification;
import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages user authentication
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignUp;
    private TextView txt_recover_password;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private EditText editEmail,editPassword;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        txt_recover_password = findViewById(R.id.txt_recover_password);
        txt_recover_password.setOnClickListener(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        editEmail= findViewById(R.id.editEmail);
        editPassword= findViewById(R.id.editPassword);
        progressBar= findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

    }
    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            finish();
            userLoginSuccess();
        }

    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtSignUp:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
            case R.id.txt_recover_password:
                startActivity(new Intent(this,ForgotActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
        }
    }

    /**
     * This method is used to login to the application with provided email and password.
     * Email, Password are mandatory
     */
    private void userLogin() {

        final String email= editEmail.getText().toString().trim();
        String password= editPassword.getText().toString().trim();

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
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful())
                {
                    finish();
                    Intent i= new Intent(LoginActivity.this,HomeScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    userLoginSuccess();
                    Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userLoginSuccess() {
        String user = AppConstants.ADMIN_EMAIL;
        if (mAuth.getCurrentUser().getEmail().equalsIgnoreCase(user)) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
        }
        else {

            fetchAllConfigurations();
            new SendNotification().updateToken(FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(new Intent(this, HomeScreenActivity.class));
        }
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