package com.kcapp.go4lunch.model.places;

import com.google.gson.annotations.SerializedName;
import com.kcapp.go4lunch.model.places.result.Result;

import java.util.List;

public class GooglePlaceDetailResponse {
    @SerializedName("html_attributions")
    private List<String> htmlAttributions = null;

    @SerializedName("result")
    private Result result = null;

    @SerializedName("status")
    private String status = null;


    /**
     * GETTERS
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }
    public Result getResult() {
        return result;
    }
    public String getStatus() {
        return status;
    }

    /**
     * SETTERS
     */
    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }
    public void setResult(Result result) {
        this.result = result;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
