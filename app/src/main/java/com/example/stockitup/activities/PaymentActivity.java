package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.models.CategoryItemsModel;
import com.example.stockitup.models.ManageOrdersModel;
import com.example.stockitup.models.OrdersModel;
import com.example.stockitup.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.UUID;

/**
 * This class deals with the payment to place the order
 */
public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPay;
    private FirebaseFirestore firebaseFirestore;
    private String uid;
    private String subTotal, deliveryCharge, tax, total, offer, offerPercent;
    private String address;
    private EditText editName,editCardNo,editCVV,editExpDate;
    private String name,cardNo,cvv,expDate;
    private String docId;

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setToolbar();
        initializeReferencesAndListeners();
        initializeViewAndControls();
    }

    /**
     * sets toolbar title, back navigation
     * */
    private void setToolbar() {
        String appName = AppConstants.APP_NAME;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * initialize references and listeners
     * */
    private void initializeReferencesAndListeners() {
        editName = findViewById(R.id.editName);
        editCardNo = findViewById(R.id.editCardNo);
        editCVV = findViewById(R.id.editCVV);
        editExpDate = findViewById(R.id.editExpDate);
        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    /**
     * This method initializes the view and controls
     * */
    private void initializeViewAndControls() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        cartCheckout();
    }

    /**
     * This method is used to checkout the items added to cart
     */
    private void cartCheckout() {
        name = editName.getText().toString();
        cardNo = editCardNo.getText().toString();
        cvv = editCVV.getText().toString();
        expDate = editExpDate.getText().toString();

        if(name.isEmpty())
        {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }
        if(cardNo.isEmpty())
        {
            editCardNo.setError("Card Number is required");
            editCardNo.requestFocus();
            return;
        }
        if(cvv.isEmpty())
        {
            editCVV.setError("CVV is required");
            editCVV.requestFocus();
            return;
        }
        if (cvv.length() < 3 || cvv.length() > 4){
            editCVV.setError("Please enter valid CVV");
            editCVV.requestFocus();
            return;
        }
        if(expDate.isEmpty())
        {
            editExpDate.setError("Expiry Date is required");
            editExpDate.requestFocus();
            return;
        }
        if (expDate.length()!=AppConstants.MAX_EXPIRY_LENGTH || Integer.parseInt(expDate.substring(0,2)) < 1 || Integer.parseInt(expDate.substring(0,2)) > 12){
            editExpDate.setError("Please enter valid expiry date");
            editExpDate.requestFocus();
            return;
        }
        Toast.makeText(getApplicationContext(), "Payment is being Processed, Please don't refresh", Toast.LENGTH_SHORT).show();
        placeOrder();
    }

    /**
     * place order of items in cart
     * */
    private void placeOrder() {
        docId = "" + UUID.randomUUID().toString();
        //fetch cart items
        firebaseFirestore.collection(AppConstants.CART_COLLECTION).document("cart" + uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                .get()
                .addOnCompleteListener(new OnCompleteListener < QuerySnapshot > () {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //iterating through each cart item
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                //adding each cart item to orders collection inside a particular docId
                                addOrderedItems(documentSnapshot,docId);
                            }
                        }

                    }
                });
    }

    /**
     * This methods adds ordered items to database from cart
     * */
    private void addOrderedItems(DocumentSnapshot documentSnapshot, final String docId) {
        String cartDocumentId = documentSnapshot.getId();
        CategoryItemsModel cuisineItemsModel = documentSnapshot.toObject(CategoryItemsModel.class);
        firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders" + uid).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(docId).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(cartDocumentId)
                .set(cuisineItemsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    addOrderDetails(docId);
                }
            }
        });
    }

    /**
     * This method adds order details to database when order is placed
     * @param docId the document id of the order in AllOrders collection
     * */
    private void addOrderDetails(String docId) {
        Date date = new Date();
        subTotal = getIntent().getStringExtra("subTotal");
        offerPercent = getIntent().getStringExtra("offerPercent");
        offer = getIntent().getStringExtra("offer");
        tax = getIntent().getStringExtra("tax");
        deliveryCharge = getIntent().getStringExtra("deliveryCharge");
        total = getIntent().getStringExtra("total");
        address = getIntent().getStringExtra("address");
        String status = "Pending";
        OrdersModel ordersModel = new OrdersModel(date, subTotal, offerPercent, offer, tax, deliveryCharge, total, address,status);
        //adding orders data to that particular docId
        firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders" + uid).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).document(docId)
                .set(ordersModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //userId,userEmail,userName
                        addUserDetails();
                        fetchAndDeleteCart();
                    }
                });
    }

    /**
     * fetch cart items and delete
     * */
    private void fetchAndDeleteCart() {
        //get cart details to delete each item
        firebaseFirestore.collection(AppConstants.CART_COLLECTION).document("cart" + uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Order placed successfully.", Toast.LENGTH_SHORT).show();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                //delete each cart item
                                firebaseFirestore.collection(AppConstants.CART_COLLECTION).document("cart" + uid).collection(AppConstants.ITEMS_COLLECTION_DOCUMENT).document(queryDocumentSnapshot.getId())
                                        .delete();
                            }
                        }
                        startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                    }
                });
    }

    /**
     * This methods adds user details to database when order is placed
     * */
    private void addUserDetails() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        ManageOrdersModel manageOrdersModel = new ManageOrdersModel(uid,userEmail,userName);
        firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders"+uid).set(manageOrdersModel);
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