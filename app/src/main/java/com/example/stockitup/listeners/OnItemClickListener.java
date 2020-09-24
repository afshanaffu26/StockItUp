package com.example.stockitup.listeners;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

public interface OnItemClickListener {
    void onItemClick(View view, DocumentSnapshot documentSnapshot, int position);
}
