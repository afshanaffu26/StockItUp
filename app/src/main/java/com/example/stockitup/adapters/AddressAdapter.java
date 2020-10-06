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
import com.example.stockitup.models.AddressModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AddressAdapter extends FirestoreRecyclerAdapter<AddressModel,AddressAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;
    public AddressAdapter(@NonNull FirestoreRecyclerOptions<AddressModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AddressModel model) {
        holder.txtName.setText(model.getName());
        holder.txtAddressLine.setText(model.getAddressLine());
        holder.txtCity.setText(model.getCity());
        holder.txtProvince.setText(model.getProvince());
        holder.txtCountry.setText(model.getCountry());
        holder.txtPincode.setText(model.getPincode());
        holder.txtPhone.setText(model.getPhone());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new ViewHolder(view);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName,txtAddressLine,txtCity,txtProvince,txtCountry,txtPincode,txtPhone;
        private ImageView imgBtnEditAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddressLine = itemView.findViewById(R.id.txtAddressLine);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtProvince = itemView.findViewById(R.id.txtProvince);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtPincode = itemView.findViewById(R.id.txtPincode);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgBtnEditAddress = itemView.findViewById(R.id.imgBtnEditAddress);
            imgBtnEditAddress.setOnClickListener(this);
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
