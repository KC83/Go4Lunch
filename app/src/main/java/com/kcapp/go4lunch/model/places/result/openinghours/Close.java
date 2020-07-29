package com.kcapp.go4lunch.model.places.result.openinghours;

import com.google.gson.annotations.SerializedName;

public class Close {
    @SerializedName("day")
    private int day;

    @SerializedName("time")
    private String time;

    /**
     * GETTERS
     */
    public int getDay() {
        return day;
    }
    public String getTime() {
        return time;
    }

    /**
     * SETTERS
     */
    public void setDay(int day) {
        this.day = day;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
