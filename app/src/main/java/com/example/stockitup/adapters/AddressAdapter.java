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

/**
 * This is an adapter class to bind data to address recycler view
 * */
public class AddressAdapter extends FirestoreRecyclerAdapter<AddressModel,AddressAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private OnDataChangeListener dataChangeListener;

    /**
     * Constructor AddressAdapter is called to bind the data and view for address
     * @param options FirestoreRecyclerOptions for AddressModel
     * */
    public AddressAdapter(@NonNull FirestoreRecyclerOptions<AddressModel> options) {
        super(options);
    }

    /**
     * This method binds the data and the view
     * @param holder the view holder
     * @param position the adapter position
     * @param model the model file
     * */
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

    /**
     * This method creates the view
     * @param parent the parent viewGroup
     * @param viewType the viewType
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
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
