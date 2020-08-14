package com.kcapp.go4lunch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.CircleTransform;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.User;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.result.Result;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import retrofit2.http.Url;

class ViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private Context mContext;
    private double mLat, mLng;

    private InternetManager mInternetManager;
    private ApiGooglePlaces mApiGooglePlaces;

    // LIST FRAGMENT
    private TextView mItemPlaceName;
    private TextView mItemPlaceAddress;
    private TextView mItemPlaceOpeningHours;
    private TextView mItemPlaceDistance;
    private ImageView mItemPlaceWorkmates;
    private TextView mItemPlaceNumberWorkmates;
    private ImageView mItemPlaceRating;
    private ImageView mItemPlaceImage;

    // WORKMATES FRAGMENT
    private ImageView mItemWorkmateImage;
    private TextView mItemWorkmateTitle;

    ViewHolder(@NonNull View itemView, Context context, double lat, double lng) {
        super(itemView);

        mItemView = itemView;
        mContext = context;
        mLat = lat;
        mLng = lng;

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

    ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mContext = context;

        mItemWorkmateImage = itemView.findViewById(R.id.item_workmate_image);
        mItemWorkmateTitle = itemView.findViewById(R.id.item_workmate_title);
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

                // DISTANCE LOCATION
                float[] distances = new float[2];
                Location.distanceBetween(mLat, mLng, place.getResult().getGeometry().getLocation().getLat(), place.getResult().getGeometry().getLocation().getLng(), distances);
                String distance = null;
                if (distances[0] >= 1000) {
                    distance = String.format(Locale.getDefault(),"%.1f", distances[0]/1000)+"km";
                } else {
                    distance = Math.round(distances[0])+"m";
                }
                mItemPlaceDistance.setText(distance);

                // PHOTO
                if (result.getPhotos() != null) {
                    RequestManager requestManager = Glide.with(mItemView);
                    requestManager
                            .load(Constants.BASE_URL+Constants.PHOTO_SEARCH_URL+"maxwidth=400&photoreference="+result.getPhotos().get(0).getPhotoReference()+"&key="+Constants.GOOGLE_BROWSER_KEY)
                            .into(mItemPlaceImage);
                } else {
                    mItemPlaceImage.setImageResource(R.drawable.ic_no_photo);
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
     * Set information for a workmate
     * @param user user
     */
    void bind(User user) {
        if (user.getUrlPicture() != null) {
            if (user.getUrlPicture().length() > 0) {
                Picasso.with(mContext)
                        .load(user.getUrlPicture())
                        .transform(new CircleTransform())
                        .into(mItemWorkmateImage);
            } else {
                mItemWorkmateImage.setImageResource(R.drawable.ic_no_photo);
            }
        } else {
            mItemWorkmateImage.setImageResource(R.drawable.ic_no_photo);
        }
        mItemWorkmateTitle.setText(user.getUsername());
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