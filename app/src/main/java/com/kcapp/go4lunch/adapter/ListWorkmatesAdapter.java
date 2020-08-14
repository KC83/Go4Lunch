package com.kcapp.go4lunch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.model.User;

public class ListWorkmatesAdapter extends FirestoreRecyclerAdapter<User, ViewHolder> {

    public ListWorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull User user) {
        viewHolder.bind(user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new ViewHolder(view, parent.getContext());
    }
}