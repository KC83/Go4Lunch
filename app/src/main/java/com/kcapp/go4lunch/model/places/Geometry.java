package com.kcapp.go4lunch.model.places;

import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location location;
    @SerializedName("viewport")
    private Viewport viewport;

    /**
     * GETTERS
     **/
    public Location getLocation() {
        return location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    /**
     * SETTERS
     **/
    public void setLocation(Location location) {
        this.location = location;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
