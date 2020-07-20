package com.kcapp.go4lunch.api.places;

import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

public interface PlacesCallback {
    void onPlacesAvailable(GooglePlacesResponse places);
    void onError(Exception exception);
}
