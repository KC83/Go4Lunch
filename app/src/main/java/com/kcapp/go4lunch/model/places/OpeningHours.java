package com.kcapp.go4lunch.model.places;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    private String openNow;

    /**
     * GETTERS
     **/
    public String getOpenNow() {
        return openNow;
    }

    /**
     * SETTERS
     **/
    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }
}
