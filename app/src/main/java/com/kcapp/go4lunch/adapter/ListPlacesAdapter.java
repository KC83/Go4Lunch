package com.kcapp.go4lunch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.model.places.Result;

import java.util.List;

public class ListPlacesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Result> mResults;

    public ListPlacesAdapter(List<Result> results) {
        this.mResults = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
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

class ViewHolder extends RecyclerView.ViewHolder {
    private TextView mItemPlaceName;
    private TextView mItemPlaceAddress;
    private TextView mItemPlaceOpeningHours;
    private TextView mItemPlaceDistance;
    private ImageView mItemPlaceWorkmates;
    private TextView mItemPlaceNumberWorkmates;
    private RatingBar mItemPlaceRating;
    private ImageView mItemPlaceImage;

    ViewHolder(@NonNull View itemView) {
        super(itemView);

        mItemPlaceName = itemView.findViewById(R.id.item_place_name);
        mItemPlaceAddress = itemView.findViewById(R.id.item_place_address);
        mItemPlaceOpeningHours = itemView.findViewById(R.id.item_place_opening_hours);
        mItemPlaceDistance = itemView.findViewById(R.id.item_place_distance);
        mItemPlaceWorkmates = itemView.findViewById(R.id.item_place_workmates);
        mItemPlaceNumberWorkmates = itemView.findViewById(R.id.item_place_number_workmates);
        mItemPlaceRating = itemView.findViewById(R.id.item_place_rating);
        mItemPlaceImage = itemView.findViewById(R.id.item_place_image);
    }

    void bind(Result result) {
        mItemPlaceName.setText(result.getName());
        mItemPlaceAddress.setText(result.getVicinity());
        //mItemPlaceOpeningHours.setText(result.getOpeningHours().getOpenNow());

        mItemPlaceWorkmates.setVisibility(View.INVISIBLE);
        mItemPlaceNumberWorkmates.setText(null);

        //mItemPlaceRating.setNumStars((int) result.getRating());
    }
}
