package com.example.stockitup.listeners;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * interface OnItemClickListener
 * this is implemented for onClick functionality
 */
public interface OnItemClickListener {

    /**
     * public, abstract method to be implemented when a class implements OnItemClickListener interface
     * @param view the view of the item clicked
     * @param documentSnapshot the snapshot of the document clicked
     * @param position the index of item clicked
     */
    void onItemClick(View view, DocumentSnapshot documentSnapshot, int position);
}
