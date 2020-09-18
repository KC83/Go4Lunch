package com.kcapp.go4lunch.api.places;

import androidx.annotation.Nullable;

public interface PlacesRepository {
    void getPlaces(String location, @Nullable String keyword, PlacesCallback callback);
    void getPlaceDetail(String placeId, PlacesCallback callback);
}
