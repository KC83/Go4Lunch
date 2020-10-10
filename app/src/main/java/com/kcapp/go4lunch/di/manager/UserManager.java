package com.kcapp.go4lunch.di.manager;

import com.kcapp.go4lunch.model.PlaceLike;
import com.kcapp.go4lunch.model.User;

import java.util.List;

public interface UserManager {
    // Create user
    void createUser(String uid, String username, String email, String urlPicture, OnUserCreationCallback callback);
    interface OnUserCreationCallback {
        void onSuccess(User user);
        void onError(Throwable throwable);
    }

    // Get user
    void getUser(String uid, OnGetUserCallback callback);
    interface OnGetUserCallback {
        void onSuccess(User user);
        void onError(Throwable throwable);
    }

    // Get users for a place
    void getUsersForAPlace(String placeId, OnGetUsersForAPlaceCallback callback);
    interface OnGetUsersForAPlaceCallback {
        void onSuccess(List<User> users);
        void onError(Throwable throwable);
    }

    // Update place for a user
    void updatePlace(String uid, String placeId, String date, OnUpdatePlaceCallback callback);
    interface OnUpdatePlaceCallback {
        void onSuccess(User user);
        void onError(Throwable throwable);
    }
}