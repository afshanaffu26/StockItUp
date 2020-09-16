package com.example.stockitup.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stockitup.activities.CategoryItemsActivity;
import com.example.stockitup.R;
import com.example.stockitup.activities.ItemDescriptionActivity;
import com.example.stockitup.models.CategoriesModel;
import com.example.stockitup.models.CategoryItemsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    String categoryDocumentId,documentId;
    FirebaseFirestore firebaseFirestore;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView,recyclerViewCategory;
    private FirestoreRecyclerAdapter adapter,adapter1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerViewCategory = v.findViewById(R.id.recyclerViewCategory);
        floatingActionButton = v.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartFragment cartFragment = new CartFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), cartFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Query
        Query query = firebaseFirestore.collection("Essentials");
        //RecyclerOptions
        FirestoreRecyclerOptions<CategoryItemsModel> options = new FirestoreRecyclerOptions.Builder<CategoryItemsModel>()
                .setQuery(query, CategoryItemsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<CategoryItemsModel, HomeFragment.CategoryItemsViewHolder>(options) {
            @NonNull
            @Override
            public HomeFragment.CategoryItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_banner,parent,false);
                return new HomeFragment.CategoryItemsViewHolder(view);
            }

            @NonNull
            @Override
            public CategoryItemsModel getItem(int position) {
                return super.getItem(position);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeFragment.CategoryItemsViewHolder holder, final int position, @NonNull final CategoryItemsModel model) {
                holder.txtName.setText(model.getName());

                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void
                    onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ItemDescriptionActivity.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("price", model.getPrice());
                        intent.putExtra("desc", model.getDesc());
                        documentId = getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("documentId", documentId);
                        startActivity(intent);
                    } });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        Query query1 = firebaseFirestore.collection("Categories");
        FirestoreRecyclerOptions<CategoriesModel> options1 = new FirestoreRecyclerOptions.Builder<CategoriesModel>()
                .setQuery(query1,CategoriesModel.class)
                .build();
        adapter1 = new FirestoreRecyclerAdapter<CategoriesModel, HomeFragment.CategoriesViewHolder>(options1) {

            @NonNull
            @Override
            public HomeFragment.CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
                return new HomeFragment.CategoriesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HomeFragment.CategoriesViewHolder holder, final int position, @NonNull final CategoriesModel model) {
                holder.txtName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), CategoryItemsActivity.class);
                        intent.putExtra("name", model.getName());
                        categoryDocumentId = getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("categoryDocumentId", categoryDocumentId);
                        startActivity(intent);
                    } });
            }
        };
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter1.startListening();
        recyclerViewCategory.setAdapter(adapter1);
        return v;
    }

    private class CategoriesViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private ImageView imageView;
        LinearLayout linearLayout;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imageView = itemView.findViewById(R.id.imageView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public class CategoryItemsViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private ImageView imageView;
        LinearLayout linearLayout;

        public CategoryItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imageView = itemView.findViewById(R.id.imageView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}