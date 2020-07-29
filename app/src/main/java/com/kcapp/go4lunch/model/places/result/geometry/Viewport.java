package com.kcapp.go4lunch.model.places.result.geometry;

import com.google.gson.annotations.SerializedName;

public class Viewport {
    @SerializedName("northeast")
    private Northeast northeast;
    @SerializedName("southwest")
    private Southeast southwest;

    /**
     * GETTERS
     */
    public Northeast getNortheast() {
        return northeast;
    }

    public Southeast getSouthwest() {
        return southwest;
    }

    /**
     * SETTERS
     */
    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public void setSouthwest(Southeast southwest) {
        this.southwest = southwest;
    }
}
