package com.kcapp.go4lunch.api.places;

public interface PlacesRepository {
    void getPlaces(String location, PlacesCallback callback);
}
