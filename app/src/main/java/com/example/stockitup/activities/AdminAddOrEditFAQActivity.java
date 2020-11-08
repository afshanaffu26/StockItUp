package com.example.stockitup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.FAQModel;
import com.example.stockitup.models.OffersModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is related to admin.It deals with add or edit of faq
 */
public class AdminAddOrEditFAQActivity extends AppCompatActivity {

    private String question,answer,flow,documentId;
    private EditText editQuestion,editAnswer;
    private Button btnSubmit;
    private FirebaseFirestore firebaseFirestore;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_or_edit_f_a_q);

        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editQuestion = findViewById(R.id.editQuestion);
        editAnswer = findViewById(R.id.editAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                question = editQuestion.getText().toString();
                answer = editAnswer.getText().toString();
                FAQModel model = new FAQModel(question, answer);
                if (flow.equalsIgnoreCase("add")) {
                    firebaseFirestore.collection(AppConstants.FAQ_COLLECTION)
                            .add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"FAQ Added.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    firebaseFirestore.collection(AppConstants.FAQ_COLLECTION).document(documentId)
                            .set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"FAQ Updated.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        flow = getIntent().getStringExtra("flow");
        if (flow.equalsIgnoreCase("edit"))
        {
            question = getIntent().getStringExtra("question");
            answer = getIntent().getStringExtra("answer");
            documentId = getIntent().getStringExtra("documentId");
            btnSubmit.setText("UPDATE");
            editQuestion.setText(question);
            editAnswer.setText(answer);
        }
        else
        {
            btnSubmit.setText("ADD");
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