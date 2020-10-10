package com.kcapp.go4lunch.api.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.di.manager.FirebaseUserManager;
import com.kcapp.go4lunch.di.manager.UserManager;
import com.kcapp.go4lunch.model.User;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
     * Get logged firebaseUser
     * @return FirebaseUser
     */
    public static FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Get firebaseFirestore instance
     * @return FirebaseFirestore
     */
    public static FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
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
        UserManager firebaseUserManager = new FirebaseUserManager(App.getFirestore());
        firebaseUserManager.getUsersForAPlace(placeId, new UserManager.OnGetUsersForAPlaceCallback() {
            @Override
            public void onSuccess(List<User> users) {
                callback.onComplete(users.size());
            }

            @Override
            public void onError(Throwable throwable) {}
        });
    }

    /**
     * Interface to get the place of the current user
     */
    public interface GetUserPlaceCallback {
        void onCompleted(String placeId);
    }
    /**
     * Get the place where the user go
     */
    public static void getUserPlace(GetUserPlaceCallback callback) {
        FirebaseUser firebaseUser = App.getFirebaseUser();
        if (firebaseUser != null) {
            UserManager firebaseUserManager = new FirebaseUserManager(App.getFirestore());
            firebaseUserManager.getUser(firebaseUser.getUid(), new UserManager.OnGetUserCallback() {
                @Override
                public void onSuccess(User user) {
                    if (user != null && user.getPlaceDate() != null) {
                        if (user.getPlaceDate().equals(App.getTodayDate())) {
                            callback.onCompleted(user.getPlaceId());
                        }
                    } else {
                        callback.onCompleted(null);
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        } else {
            callback.onCompleted(null);
        }
    }
}
