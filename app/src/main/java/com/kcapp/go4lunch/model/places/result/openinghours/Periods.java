package com.kcapp.go4lunch.model.places.result.openinghours;

import com.google.gson.annotations.SerializedName;

public class Periods {
    @SerializedName("close")
    private Close close;

    @SerializedName("open")
    private Open open;

    /**
     * GETTERS
     */
    public Close getClose() {
        return close;
    }
    public Open getOpen() {
        return open;
    }

    /**
     * SETTERS
     */
    public void setClose(Close close) {
        this.close = close;
    }
    public void setOpen(Open open) {
        this.open = open;
    }
}
