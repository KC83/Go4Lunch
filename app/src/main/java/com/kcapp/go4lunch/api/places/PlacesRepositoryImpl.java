package com.kcapp.go4lunch.api.places;

import android.content.Context;

import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepositoryImpl implements PlacesRepository {
    ApiGooglePlaces mApiGooglePlaces;
    InternetManager mInternetManager;

    public PlacesRepositoryImpl(InternetManager internetManager, ApiGooglePlaces apiGooglePlaces) {
        this.mApiGooglePlaces = apiGooglePlaces;
        this.mInternetManager = internetManager;
    }

    /**
     * Get places
     * @param location user's current location
     * @param callback callback
     */
    @Override
    public void getPlaces(String location, PlacesCallback callback) {
        if (!mInternetManager.isConnected()) {
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

    /**
     * Get place detail
     * @param placeId the place id
     * @param callback callback
     */
    @Override
    public void getPlaceDetail(String placeId, PlacesCallback callback) {
        if (!mInternetManager.isConnected()) {
            callback.onError(new Exception());
        }

        Call<GooglePlaceDetailResponse> call = mApiGooglePlaces.getPlaceDetail(placeId, Constants.GOOGLE_BROWSER_KEY);
        call.enqueue(new Callback<GooglePlaceDetailResponse>() {
            @Override
            public void onResponse(Call<GooglePlaceDetailResponse> call, Response<GooglePlaceDetailResponse> response) {
                if (!response.isSuccessful()) {
                    callback.onError(new Exception());
                }

                callback.onPlaceDetailAvailable(response.body());
            }

            @Override
            public void onFailure(Call<GooglePlaceDetailResponse> call, Throwable t) {
                callback.onError(new Exception());
            }
        });
    }
}
