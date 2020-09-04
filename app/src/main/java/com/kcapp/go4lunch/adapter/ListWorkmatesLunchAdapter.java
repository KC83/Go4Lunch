package com.kcapp.go4lunch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.model.User;

import java.util.List;

public class ListWorkmatesLunchAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<User> mUsers;
    private String mPlaceId;

    public ListWorkmatesLunchAdapter(List<User> users, String placeId) {
        this.mUsers = users;
        this.mPlaceId = placeId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mUsers.get(position), true, mPlaceId);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
