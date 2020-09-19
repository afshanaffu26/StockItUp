package com.example.stockitup.listeners;

import com.google.firebase.firestore.DocumentSnapshot;

public interface OnItemClickListener {
    void onItemClick(DocumentSnapshot documentSnapshot, int position);
}
