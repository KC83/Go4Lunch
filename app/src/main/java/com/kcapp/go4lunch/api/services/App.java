package com.kcapp.go4lunch.api.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.model.User;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.result.Result;

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

    public static FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
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
            if (task.getResult() != null) {
                callback.onComplete(task.getResult().size());
            } else {
                callback.onComplete(0);
            }
        });
    }

    /**
     * Interface to get the place of the current user
     */
    public interface GetUserPlaceCallback {
        void onCompleted(String placeId);
    }
    public static void getUserPlace(GetUserPlaceCallback callback) {
        FirebaseUser firebaseUser = App.getFirebaseUser();
        if (firebaseUser != null) {
            Task<DocumentSnapshot> documentSnapshotTask = UserHelper.getUser(firebaseUser.getUid());
            documentSnapshotTask.addOnCompleteListener(task -> {
                if (task.getResult() != null) {
                    User user = task.getResult().toObject(User.class);
                    if (user != null && user.getPlaceDate() != null) {
                        if (user.getPlaceDate().equals(App.getTodayDate())) {
                            callback.onCompleted(user.getPlaceId());
                        }
                    } else {
                        callback.onCompleted(null);
                    }
                } else {
                    callback.onCompleted(null);
                }
            });
        } else {
            callback.onCompleted(null);
        }
    }
}
