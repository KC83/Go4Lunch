package com.kcapp.go4lunch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.result.Result;

import java.util.List;

public class ListPlacesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Result> mResults;
    private Context mContext;

    public ListPlacesAdapter(List<Result> results, Context context) {
        this.mResults = results;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view, mContext);
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
    private View mItemView;

    private InternetManager mInternetManager;
    private ApiGooglePlaces mApiGooglePlaces;
    private Context mContext;

    private TextView mItemPlaceName;
    private TextView mItemPlaceAddress;
    private TextView mItemPlaceOpeningHours;
    private TextView mItemPlaceDistance;
    private ImageView mItemPlaceWorkmates;
    private TextView mItemPlaceNumberWorkmates;
    private ImageView mItemPlaceRating;
    private ImageView mItemPlaceImage;

    ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mItemView = itemView;

        mContext = context;
        mInternetManager = new InternetManagerImpl(context);
        mApiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

        mItemPlaceName = itemView.findViewById(R.id.item_place_name);
        mItemPlaceAddress = itemView.findViewById(R.id.item_place_address);
        mItemPlaceOpeningHours = itemView.findViewById(R.id.item_place_opening_hours);
        mItemPlaceDistance = itemView.findViewById(R.id.item_place_distance);
        mItemPlaceWorkmates = itemView.findViewById(R.id.item_place_workmates);
        mItemPlaceNumberWorkmates = itemView.findViewById(R.id.item_place_number_workmates);
        mItemPlaceRating = itemView.findViewById(R.id.item_place_rating);
        mItemPlaceImage = itemView.findViewById(R.id.item_place_image);
    }

    /**
     * Set information for a place
     * @param result result of a place
     */
    void bind(Result result) {
        PlacesRepositoryImpl placesRepository = new PlacesRepositoryImpl(mInternetManager, mApiGooglePlaces);

        // DETAIL
        placesRepository.getPlaceDetail(result.getPlaceId(), new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {}

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {
                mItemPlaceName.setText(result.getName());
                mItemPlaceAddress.setText(result.getVicinity());
                mItemPlaceWorkmates.setVisibility(View.INVISIBLE);
                mItemPlaceNumberWorkmates.setText(null);

                // OPENING HOURS
                if (place.getResult().getOpeningHours() != null) {
                    mItemPlaceOpeningHours.setText(place.getResult().getOpeningHours().getOpeningHoursText(mContext));
                } else {
                    mItemPlaceOpeningHours.setText(R.string.opening_hours_not_indicated);
                }

                if (mItemPlaceOpeningHours.getText() == mContext.getString(R.string.opening_hours_close_soon)) {
                    mItemPlaceOpeningHours.setTextColor(mContext.getResources().getColor(R.color.colorYellow));
                } else if (mItemPlaceOpeningHours.getText() == mContext.getString(R.string.opening_hours_close) || mItemPlaceOpeningHours.getText() == mContext.getString(R.string.opening_hours_close_today)) {
                    mItemPlaceOpeningHours.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                } else {
                    mItemPlaceOpeningHours.setTextColor(mContext.getResources().getColor(R.color.colorAccentGray));
                }

                // PHOTO
                if (result.getPhotos() != null) {
                    RequestManager requestManager = Glide.with(mItemView);
                    requestManager.load(Constants.BASE_URL+Constants.PHOTO_SEARCH_URL+"maxwidth=400&photoreference="+result.getPhotos().get(0).getPhotoReference()+"&key="+Constants.GOOGLE_BROWSER_KEY).into(mItemPlaceImage);
                } else {
                    mItemPlaceImage.setImageResource(R.drawable.ic_no_camera);
                }

                // RATING
                mItemPlaceRating.setImageResource(getRatingImageResource(result.getRating()));
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(mContext, "Error : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Get image resource for a place
     * @param rating the rating of the place
     * @return a image resource
     */
    int getRatingImageResource(double rating) {
        int resource;
        if (rating <= 2) {
            resource = R.drawable.rating_1;
        } else if (rating < 4) {
            resource = R.drawable.rating_2;
        } else {
            resource = R.drawable.rating_3;
        }

        return resource;
    }
}
