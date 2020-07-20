package com.kcapp.go4lunch.api.places;

import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiGooglePlaces {
    /**
     * Retrofit builder
     */
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(Constants.NEARBY_SEARCH_URL)
    Call<GooglePlacesResponse> getPlaces(@Query("location") String location, @Query("radius") String radius, @Query("type") String placeType, @Query("key") String apiKey);
}
