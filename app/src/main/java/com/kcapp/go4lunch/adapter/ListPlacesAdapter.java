package com.kcapp.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.model.places.result.Result;

import java.util.List;

public class ListPlacesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Result> mResults;
    private Context mContext;
    private double mLat, mLng;

    public ListPlacesAdapter(List<Result> results, Context context, double lat, double lng) {
        this.mResults = results;
        this.mContext = context;
        this.mLat = lat;
        this.mLng = lng;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view, mContext, mLat, mLng);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mResults.get(position));
    }

    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;
        }
        return mResults.size();
    }
}