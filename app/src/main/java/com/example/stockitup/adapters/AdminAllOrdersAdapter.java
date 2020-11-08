package com.example.stockitup.adapters;

import android.graphics.Color;
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

import java.text.SimpleDateFormat;

/**
 * This is an adapter class to bind data to all orders recycler view
 * */
public class AdminAllOrdersAdapter extends FirestoreRecyclerAdapter<OrdersModel,AdminAllOrdersAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;

    /**
     * Constructor AdminAllOrdersAdapter is called to bind the data and view for all orders
     * @param options FirestoreRecyclerOptions for OrdersModel
     * */
    public AdminAllOrdersAdapter(@NonNull FirestoreRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OrdersModel model) {
        String date = new SimpleDateFormat("dd-MM-yy HH:mm").format(model.getDate());
        holder.txtOrderDate.setText("Ordered On: "+date);
        holder.txtOrderId.setText(" "+getSnapshots().getSnapshot(position).getId());
//        holder.txtSubTotal.setText("Subtotal: "+model.getSubtotal()+"$");
//        holder.txtOffer.setText("Offer (-20%): -"+model.getOffer()+"$");
//        holder.txtTax.setText("Tax: "+model.getTax()+"$");
//        holder.txtDeliveryCharge.setText("Delivery Charge: "+model.getDeliveryCharge()+"$");
//        holder.txtTotal.setText("Total: "+model.getTotal()+"$");
//        holder.txtAddress.setText("Address: "+model.getAddress());
        holder.txtStatus.setText(model.getStatus());
        if (model.getStatus().equalsIgnoreCase("pending"))
        {
            holder.txtStatus.setTextColor(Color.parseColor("#ffa700"));
        }
        else if (model.getStatus().equalsIgnoreCase("delivered"))
        {
            holder.txtStatus.setTextColor(Color.parseColor("#00b159"));
        }
        else
        {
            holder.txtStatus.setTextColor(Color.parseColor("#db2544"));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_all_orders,parent,false);
        return new AdminAllOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
       // dataChangeListener.onDataChanged();
    }

    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener){
        this.dataChangeListener = dataChangeListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtOrderDate,txtSubTotal,txtTax,txtDeliveryCharge,txtTotal,txtAddress,txtStatus,txtOffer,txtOrderId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
//            txtSubTotal = itemView.findViewById(R.id.txtSubTotal);
//            txtOffer = itemView.findViewById(R.id.txtOffer);
//            txtTax = itemView.findViewById(R.id.txtTax);
//            txtDeliveryCharge = itemView.findViewById(R.id.txtDeliveryCharge);
//            txtTotal = itemView.findViewById(R.id.txtTotal);
//            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //if an item is deleted from recycler view, this checks when its in delete animation position returning -1
                    if (position != RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onItemClick(view,getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
}
