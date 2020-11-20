package com.kcapp.go4lunch.di.manager;

import com.kcapp.go4lunch.model.PlaceLike;

public interface PlaceLikeManager {
    // Create a placeLike
    void createPlaceLike(String userUid, String placeId, OnCreatePlaceLikeCallback callback);
    interface OnCreatePlaceLikeCallback {
        void onSuccess(PlaceLike placeLike);
        void onError();
    }

    // Get placeLike
    void getPlaceLike(String userUid, String placeId, OnGetPlaceLikeCallback callback);
    interface OnGetPlaceLikeCallback {
        void onSuccess(PlaceLike placeLike);
        void onError();
    }

    // Delete a placeLike
    void deletePlaceLike(String uid, OnDeletePlaceLikeCallback callback);
    interface OnDeletePlaceLikeCallback {
        void onSuccess(boolean isDeleted);
        void onError();
    }
}
