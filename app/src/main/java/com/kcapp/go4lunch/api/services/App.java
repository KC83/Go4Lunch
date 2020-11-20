package com.kcapp.go4lunch.api.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.di.Injection;
import com.kcapp.go4lunch.di.manager.UserManager;
import com.kcapp.go4lunch.model.User;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.result.Result;

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
    public interface GetPlaceCallback {
        void onCompleted(Result place);
    }
    /**
     * Get a place name
     * @param context context
     * @param placeId placeId of a place
     * @param callback Callback
     */
    public static void getPlace(Context context, String placeId, GetPlaceCallback callback) {
        InternetManager internetManager = new InternetManagerImpl(context);
        ApiGooglePlaces apiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

        PlacesRepositoryImpl placesRepository = new PlacesRepositoryImpl(internetManager, apiGooglePlaces);
        placesRepository.getPlaceDetail(placeId, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {}

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {
                callback.onCompleted(place.getResult());
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
        UserManager firebaseUserManager = Injection.getUserManager();
        firebaseUserManager.getUsersForAPlace(placeId, new UserManager.OnGetUsersForAPlaceCallback() {
            @Override
            public void onSuccess(List<User> users) {
                callback.onComplete(users.size());
            }

            @Override
            public void onError() {}
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
            UserManager firebaseUserManager = Injection.getUserManager();
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
                public void onError() {}
            });
        } else {
            callback.onCompleted(null);
        }
    }

    public interface GetUsersForAPlaceCallback {
        void onCompleted(List<User> users);
    }
    public static void getUsersForAPlace(String placeId, GetUsersForAPlaceCallback callback) {
        FirebaseUser firebaseUser = App.getFirebaseUser();
        if (firebaseUser != null) {
            UserManager firebaseUserManager = Injection.getUserManager();
            firebaseUserManager.getUsersForAPlace(placeId, new UserManager.OnGetUsersForAPlaceCallback() {
                @Override
                public void onSuccess(List<User> users) {
                    // Send notification
                    callback.onCompleted(users);
                }

                @Override
                public void onError() {}
            });
        }
    }
}
