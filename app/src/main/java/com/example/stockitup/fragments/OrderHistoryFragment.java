package com.example.stockitup.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stockitup.R;
import com.example.stockitup.activities.OrderHistoryDetailsActivity;
import com.example.stockitup.adapters.OrderHistoryAdapter;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.OrdersModel;
import com.example.stockitup.utils.AppConstants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;

/**
 * This class deals with order history of application
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private String orderHistoryDocumentId;
    private LinearLayout linearLayout;
    private TextView txtEmptyOrders;
    private String uid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Non-parameterized constructor
     * */
    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment
     * Note that this can be called while the fragment's activity is still in the process of being created. As such, you can not rely on things like the activity's content view hierarchy being initialized at this point. If you want to do work once the activity itself is created, add a {@link androidx.lifecycle.LifecycleObserver} on the activity's Lifecycle, removing it when it receives the Lifecycle.State.CREATED callback.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null. This will be called between onCreate(Bundle) and onViewCreated(View, Bundle).
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_history, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        linearLayout = v.findViewById(R.id.linearLayout);
        txtEmptyOrders = v.findViewById(R.id.txtEmptyOrders);

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        setRecyclerViewData();

        return v;
    }

    /**
     * This method sets the recycler view data
     * */
    private void setRecyclerViewData() {
        Query query = firebaseFirestore.collection(AppConstants.ORDERS_COLLECTION).document("orders"+uid).collection(AppConstants.ORDERS_COLLECTION_DOCUMENT).orderBy("date",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<OrdersModel> options = new FirestoreRecyclerOptions.Builder<OrdersModel>()
                .setQuery(query,OrdersModel.class)
                .build();
        final OrderHistoryAdapter adapter = new OrderHistoryAdapter(options);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view,DocumentSnapshot documentSnapshot, int position) {
                OrdersModel ordersModel = documentSnapshot.toObject(OrdersModel.class);
                Intent intent = new Intent(getContext(), OrderHistoryDetailsActivity.class);
                orderHistoryDocumentId = documentSnapshot.getId();
                String date = new SimpleDateFormat("dd-MM-yy HH:mm").format(ordersModel.getDate());
                intent.putExtra("date",date);
                intent.putExtra("subtotal", ordersModel.getSubtotal());
                intent.putExtra("offerPercent", ordersModel.getOfferPercent());
                intent.putExtra("offer", ordersModel.getOffer());
                intent.putExtra("tax", ordersModel.getTax());
                intent.putExtra("deliveryCharge", ordersModel.getDeliveryCharge());
                intent.putExtra("total", ordersModel.getTotal());
                intent.putExtra("address", ordersModel.getAddress());
                intent.putExtra("status",ordersModel.getStatus());
                intent.putExtra("orderHistoryDocumentId", orderHistoryDocumentId);
                startActivity(intent);
            }
        });
        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() != 0)
                {
                    txtEmptyOrders.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtEmptyOrders.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}