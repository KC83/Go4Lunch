package com.kcapp.go4lunch.api.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    /**
     * Get the date of the current day
     * @return a string with today's date
     */
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return dateFormat.format(date);
    }

    /**
     * Interface to get the name of a place
     */
    public interface GetNamePlaceCallback {
        void onCompleted(String placeName);
    }
    /**
     * Get a place name
     * @param context context
     * @param placeId placeId of a place
     * @param callback Callback
     */
    public static void getNamePlace(Context context, String placeId, GetNamePlaceCallback callback) {
        InternetManager internetManager = new InternetManagerImpl(context);
        ApiGooglePlaces apiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

        PlacesRepositoryImpl placesRepository = new PlacesRepositoryImpl(internetManager, apiGooglePlaces);
        placesRepository.getPlaceDetail(placeId, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {}

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {
                callback.onCompleted(place.getResult().getName());
            }

            @Override
            public void onError(Exception exception) {}
        });
    }

    /**
     * Interface to get the number of reservation for a place
     */
    public interface GetCountOfReservationCallback {
        void onComplete(int count);
    }
    /**
     * Get the number of reservation for a place
     */
    public static void getCountOfReservation(String placeId, GetCountOfReservationCallback callback) {
        Task<QuerySnapshot> querySnapshotTask = UserHelper.getAllUsersForAPlaceForTask(placeId);
        querySnapshotTask.addOnCompleteListener(task -> {
            callback.onComplete(task.getResult().size());
        });
    }
}
