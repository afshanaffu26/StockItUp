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
import com.example.stockitup.models.OrdersModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrderHistoryAdapter extends FirestoreRecyclerAdapter<OrdersModel,OrderHistoryAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;
    public OrderHistoryAdapter(@NonNull FirestoreRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OrdersModel model) {
        holder.txtOrderDate.setText("Ordered On: "+model.getDate());
        holder.txtSubTotal.setText("Subtotal: "+model.getSubtotal());
        holder.txtTax.setText("Tax: "+model.getTax());
        holder.txtDeliveryCharge.setText("Delivery Charge: "+model.getDeliveryCharge());
        holder.txtTotal.setText("Total: "+model.getTotal());
        holder.txtAddress.setText("Address: "+model.getAddress());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history,parent,false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        dataChangeListener.onDataChanged();
    }

    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener){
        this.dataChangeListener = dataChangeListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtOrderDate,txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtSubTotal = itemView.findViewById(R.id.txtSubTotal);
            txtTax = itemView.findViewById(R.id.txtTax);
            txtDeliveryCharge = itemView.findViewById(R.id.txtDeliveryCharge);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //if an item is deleted from recycler view, this checks when its in delete animation position returning -1
                    if (position != RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
}
