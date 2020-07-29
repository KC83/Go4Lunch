package com.kcapp.go4lunch.model.places.result.openinghours;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {
    @SerializedName("open_now")
    private String openNow;

    @SerializedName("periods")
    private List<Periods> periods;

    /**
     * GETTERS
     **/
    public String getOpenNow() {
        return openNow;
    }
    public List<Periods> getPeriods() {
        return periods;
    }

    /**
     * SETTERS
     **/
    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }
    public void setPeriods(List<Periods> periods) {
        this.periods = periods;
    }
}
