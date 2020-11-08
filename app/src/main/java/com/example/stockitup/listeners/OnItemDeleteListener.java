package com.example.stockitup.listeners;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * interface OnItemDeleteListener
 * this is implemented for delete functionality
 */
public interface OnItemDeleteListener {

    /**
     * public, abstract method to be implemented when a class implements OnItemDeleteListener interface
     * @param documentSnapshot the snapshot of the document to be deleted
     * @param position the index of item to be deleted
     */
    void onItemDelete(DocumentSnapshot documentSnapshot, int position);
}
