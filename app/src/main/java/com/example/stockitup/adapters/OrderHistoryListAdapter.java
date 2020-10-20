package com.example.stockitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockitup.R;
import com.example.stockitup.listeners.OnItemClickListener;
import com.example.stockitup.models.CategoryItemsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class OrderHistoryListAdapter extends FirestoreRecyclerAdapter<CategoryItemsModel,OrderHistoryListAdapter.ViewHolder>{

    private OnItemClickListener listener;
    public OrderHistoryListAdapter(@NonNull FirestoreRecyclerOptions<CategoryItemsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final CategoryItemsModel model) {

        holder.txtName.setText(model.getName());
        holder.txtPrice.setText("Price: "+model.getPrice()+"$");
        holder.txtQuantity.setText("Qty: "+model.getQuantity());
        if (model.getImage()!= null && !model.getImage().isEmpty())
            Picasso.get().load(model.getImage()).into(holder.imageView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtQuantity,txtPrice;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imageView = itemView.findViewById(R.id.imageView);
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

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
