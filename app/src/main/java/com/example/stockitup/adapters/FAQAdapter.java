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
import com.example.stockitup.models.FAQModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * This is an adapter class to bind data to faq recycler view
 * This class extends FirestoreRecyclerAdapter
 * The model file used is FAQModel
 * It uses the model file and the view holder to create and bind data to recycler view
 * */
public class FAQAdapter extends FirestoreRecyclerAdapter<FAQModel, FAQAdapter.ViewHolder> {

    private OnDataChangeListener dataChangeListener;

    /**
     * Constructor FAQAdapter is called to bind the data and view for FAQ
     * @param options FirestoreRecyclerOptions for FAQModel
     * */
    public FAQAdapter(@NonNull FirestoreRecyclerOptions<FAQModel> options) {
        super(options);
    }

    /**
     * This method binds the data and the view
     * @param holder the view holder
     * @param position the adapter position
     * @param model the model file
     * */
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FAQModel model) {
        holder.txtQuestion.setText(model.getQuestion());
        holder.txtAnswer.setText(model.getAnswer());
    }

    /**
     * This method creates the view
     * @param parent the parent viewGroup
     * @param viewType the viewType
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq,parent,false);
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
     * This class is the ViewHolder for the adapter. It extends RecyclerView.ViewHolder
     * */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtQuestion,txtAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            txtAnswer = itemView.findViewById(R.id.txtAnswer);

            dataChangeListener.onDataChanged();
        }
    }
}