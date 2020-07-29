package com.kcapp.go4lunch.api.places;

public interface PlacesRepository {
    void getPlaces(String location, PlacesCallback callback);
    void getPlaceDetail(String placeId, PlacesCallback callback);
}
