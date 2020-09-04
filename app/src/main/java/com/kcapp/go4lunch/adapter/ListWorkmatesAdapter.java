package com.kcapp.go4lunch.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.helper.PlaceLunchHelper;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.model.PlaceLunch;
import com.kcapp.go4lunch.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ListWorkmatesAdapter extends FirestoreRecyclerAdapter<User, ViewHolder> {

    public ListWorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull User user) {
        Task<QuerySnapshot> query = PlaceLunchHelper.getUserPlaceLunch(user.getUid(), App.getTodayDate());
        query.addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()) {
                viewHolder.bind(user, false, null);
            } else {
                PlaceLunch placeLunch = task.getResult().getDocuments().get(0).toObject(PlaceLunch.class);
                viewHolder.bind(user, false, placeLunch.getPlaceId());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new ViewHolder(view, parent.getContext());
    }
}