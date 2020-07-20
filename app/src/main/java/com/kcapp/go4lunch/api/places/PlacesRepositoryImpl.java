package com.kcapp.go4lunch.api.places;

import android.content.Context;

import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepositoryImpl implements PlacesRepository {
    ApiGooglePlaces mApiGooglePlaces;
    InternetManager mInternetManager;
    Context mContext;

    public PlacesRepositoryImpl(InternetManager internetManager, Context context) {
        this.mApiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);
        this.mInternetManager = internetManager;
        this.mContext = context;
    }

    /**
     * Get places
     * @param location user's current location
     * @param callback callback
     */
    @Override
    public void getPlaces(String location, PlacesCallback callback) {
        if (!mInternetManager.isConnected(mContext)) {
            callback.onError(new Exception());
        }

        Call<GooglePlacesResponse> call = mApiGooglePlaces.getPlaces(location, Constants.NEARBY_PROXIMITY_RADIUS, Constants.NEARBY_TYPE, Constants.GOOGLE_BROWSER_KEY);
        call.enqueue(new Callback<GooglePlacesResponse>() {
            @Override
            public void onResponse(Call<GooglePlacesResponse> call, Response<GooglePlacesResponse> response) {
                if (!response.isSuccessful()) {
                    callback.onError(new Exception());
                }

                callback.onPlacesAvailable(response.body());
            }

            @Override
            public void onFailure(Call<GooglePlacesResponse> call, Throwable t) {
                callback.onError(new Exception());
            }
        });
    }
}
