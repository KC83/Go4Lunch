package com.kcapp.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.twitter.sdk.android.core.models.Place;

public class PlaceActivity extends AppCompatActivity {
    private ImageView mPlaceImage;
    private TextView mPlaceName;
    private TextView mPlaceAddress;
    private String placeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Intent intent = getIntent();
        placeId = intent.getStringExtra(Constants.PLACE_ID);

        mPlaceImage = findViewById(R.id.place_activity_place_image);
        mPlaceName = findViewById(R.id.place_activity_place_name);
        mPlaceAddress = findViewById(R.id.place_activity_place_address);

        InternetManager internetManager = new InternetManagerImpl(this);
        ApiGooglePlaces apiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

        PlacesRepositoryImpl placesRepository = new PlacesRepositoryImpl(internetManager, apiGooglePlaces);
        placesRepository.getPlaceDetail(placeId, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {

            }

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {
                // PHOTO
                if (place.getResult().getPhotos() != null) {
                    RequestManager requestManager = Glide.with(PlaceActivity.this);
                    requestManager
                            .load(Constants.BASE_URL+Constants.PHOTO_SEARCH_URL+"maxwidth=400&photoreference="+place.getResult().getPhotos().get(0).getPhotoReference()+"&key="+Constants.GOOGLE_BROWSER_KEY)
                            .into(mPlaceImage);
                } else {
                    mPlaceImage.setImageResource(R.drawable.ic_no_photo);
                }

                // NAME
                mPlaceName.setText(place.getResult().getName());
                // ADDRESS
                mPlaceAddress.setText(place.getResult().getVicinity());
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(PlaceActivity.this, "Error : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
