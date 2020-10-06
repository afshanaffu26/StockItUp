package com.example.stockitup.listeners;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

public interface OnItemDeleteListener {
    void onItemDelete(DocumentSnapshot documentSnapshot, int position);
}
