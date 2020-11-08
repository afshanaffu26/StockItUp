package com.example.stockitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockitup.R;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.listeners.OnItemDeleteListener;
import com.example.stockitup.models.CategoriesModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

/**
 * This is an adapter class to bind data to admin categories recycler view
 * */
public class AdminCategoriesAdapter extends FirestoreRecyclerAdapter<CategoriesModel,AdminCategoriesAdapter.ViewHolder>{

    private OnItemClickListener listener;
    private OnItemDeleteListener itemDeleteListener;
    private OnDataChangeListener dataChangeListener;

    /**
     * Constructor AdminCategoriesAdapter is called to bind the data and view for all categories in admin
     * @param options FirestoreRecyclerOptions for CategoriesModel
     * */
    public AdminCategoriesAdapter(@NonNull FirestoreRecyclerOptions<CategoriesModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final CategoriesModel model) {
        holder.txtName.setText(model.getName());
        if (model.getImage()!= null && !model.getImage().isEmpty())
            Picasso.get().load(model.getImage()).into(holder.imageView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category,parent,false);
        return new ViewHolder(view);
    }

    public void deleteItem(int position){
        itemDeleteListener.onItemDelete(getSnapshots().getSnapshot(position), position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName;
        private ImageView imageView,imageViewEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imageView = itemView.findViewById(R.id.imageView);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewEdit.setOnClickListener(this);
            itemView.setOnClickListener(this);
            dataChangeListener.onDataChanged();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //if an item is deleted from recycler view, this checks when its in delete animation position returning -1
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(view,getSnapshots().getSnapshot(position), position);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemDeleteListener(OnItemDeleteListener listener){
        this.itemDeleteListener = listener;
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        dataChangeListener.onDataChanged();
    }

    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener){
        this.dataChangeListener = dataChangeListener;
    }
}
