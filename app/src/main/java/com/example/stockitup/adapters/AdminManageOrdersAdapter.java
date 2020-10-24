package com.example.stockitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockitup.R;
import com.example.stockitup.listeners.OnDataChangeListener;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.ManageOrdersModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdminManageOrdersAdapter extends FirestoreRecyclerAdapter<ManageOrdersModel,AdminManageOrdersAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;

    public AdminManageOrdersAdapter(@NonNull FirestoreRecyclerOptions<ManageOrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final ManageOrdersModel model) {
        holder.txtOrderId.setText("ID: "+model.getUserId());
        holder.txtUserEmail.setText(model.getUserEmail());
        holder.txtName.setText("Name: "+model.getUserName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_manage_orders,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtOrderId,txtUserEmail,txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmail);
            txtName = itemView.findViewById(R.id.txtName);
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

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        dataChangeListener.onDataChanged();
    }

    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener){
        this.dataChangeListener = dataChangeListener;
    }
}
