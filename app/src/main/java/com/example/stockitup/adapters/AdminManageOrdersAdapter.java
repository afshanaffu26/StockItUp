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
import com.example.stockitup.models.ManageOrdersModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * This is an adapter class to bind data to manage orders recycler view
 * */
public class AdminManageOrdersAdapter extends FirestoreRecyclerAdapter<ManageOrdersModel,AdminManageOrdersAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;

    /**
     * Constructor AdminManageOrdersAdapter is called to bind the data and view for manage orders in admin
     * @param options FirestoreRecyclerOptions for ManageOrdersModel
     * */
    public AdminManageOrdersAdapter(@NonNull FirestoreRecyclerOptions<ManageOrdersModel> options) {
        super(options);
    }

    /**
     * This method binds the data and the view
     * @param holder the view holder
     * @param position the adapter position
     * @param model the model file
     * */
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final ManageOrdersModel model) {
        holder.txtOrderId.setText("ID: "+model.getUserId());
        holder.txtUserEmail.setText(model.getUserEmail());
        holder.txtName.setText("Name: "+model.getUserName());
    }

    /**
     * This method creates the view
     * @param parent the parent viewGroup
     * @param viewType the viewType
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_manage_orders,parent,false);
        return new ViewHolder(view);
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

        private TextView txtOrderId,txtUserEmail,txtName;
        private ImageView imgNext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmail);
            txtName = itemView.findViewById(R.id.txtName);
            imgNext = itemView.findViewById(R.id.imgNext);

            imgNext.setOnClickListener(this);
            itemView.setOnClickListener(this);

            dataChangeListener.onDataChanged();
        }

        /**
         * Called when a view has been clicked.
         * @param view The view that was clicked.
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //if an item is deleted from recycler view, this checks when its in delete animation position returning -1
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(view,getSnapshots().getSnapshot(position), position);
            }
        }
    }
}
