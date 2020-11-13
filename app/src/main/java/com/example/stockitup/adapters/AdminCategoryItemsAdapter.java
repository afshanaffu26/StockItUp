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
import com.example.stockitup.models.CategoryItemsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

/**
 * This is an adapter class to bind data to admin category items recycler view
 * */
public class AdminCategoryItemsAdapter extends FirestoreRecyclerAdapter<CategoryItemsModel,AdminCategoryItemsAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;

    /**
     * Constructor AdminCategoryItemsAdapter is called to bind the data and view for all category items in admin
     * @param options FirestoreRecyclerOptions for CategoryItemsModel
     * */
    public AdminCategoryItemsAdapter(@NonNull FirestoreRecyclerOptions<CategoryItemsModel> options) {
        super(options);
    }

    /**
     * This method binds the data and the view
     * @param holder the view holder
     * @param position the adapter position
     * @param model the model file
     * */
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final CategoryItemsModel model) {
        holder.txtName.setText(model.getName());
        holder.txtPrice.setText("Price: "+model.getPrice()+"$");
        if (model.getImage()!= null && !model.getImage().isEmpty())
            Picasso.get().load(model.getImage()).into(holder.imageView);
    }

    /**
     * This method creates the view
     * @param parent the parent viewGroup
     * @param viewType the viewType
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category_list,parent,false);
        return new ViewHolder(view);
    }

    /**
     * This method deletes the item on given position
     * @param position the position of item
     * */
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    /**
     * This method is called when data is changed
     * */
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        dataChangeListener.onDataChanged();
    }

    /**
     * This method initializes the OnDataChangeListener instance
     * */
    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener){
        this.dataChangeListener = dataChangeListener;
    }

    /**
     * This method initializes the OnItemClickListener instance
     * */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * This class is the ViewHolder for the adapter. It extends RecyclerView.ViewHolder
     * */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName,txtPrice,txtDesc;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            imageView = itemView.findViewById(R.id.imageView);

            dataChangeListener.onDataChanged();

            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         * @param view The view that was clicked.
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //if an item is deleted from recycler view, this checks when its in delete animation position returning -1
            if (position != RecyclerView.NO_POSITION && listener != null)
            {
                listener.onItemClick(view,getSnapshots().getSnapshot(position),position);
            }
        }
    }
}
