package com.kcapp.go4lunch.model.places.result.geometry;

import com.google.gson.annotations.SerializedName;

public class Northeast {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    /**
     * GETTERS
     */
    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    /**
     * SETTERS
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
