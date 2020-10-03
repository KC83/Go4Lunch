package com.kcapp.go4lunch.di.manager;

import com.kcapp.go4lunch.model.PlaceLike;

public interface PlaceLikeManager {
    // createPlaceLike
    // getPlaceLike
    // deletePlaceLike

    void createPlaceLike(String userUid, String placeId, OnCreatePlaceLikeCallback callback);
    interface OnCreatePlaceLikeCallback {
        void onSuccess(PlaceLike placeLike);
        void onError(Throwable throwable);
    }
}
