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

import com.example.stockitup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class manages user authentication
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtSignUp;
    TextView txt_recover_password;
    Button btnLogin;
    FirebaseAuth mAuth;
    EditText editEmail,editPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StockItUp");

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        txt_recover_password = findViewById(R.id.txt_recover_password);
        txt_recover_password.setOnClickListener(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        editEmail=(EditText) findViewById(R.id.editEmail);
        editPassword=(EditText) findViewById(R.id.editPassword);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

    }

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
        String user = getApplicationContext().getResources().getString(R.string.stockitup_gmail_com);
        if (mAuth.getCurrentUser().getEmail().equalsIgnoreCase(user))
            startActivity(new Intent(this,AdminDashboardActivity.class));
        else
            startActivity(new Intent(this,HomeScreenActivity.class));
    }

}