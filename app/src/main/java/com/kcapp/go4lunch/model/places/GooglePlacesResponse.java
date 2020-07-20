package com.kcapp.go4lunch.model.places;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GooglePlacesResponse {
    @SerializedName("html_attributions")
    private List<String> htmlAttributions = null;

    @SerializedName("next_page_token")
    private String nextPageToken = null;

    @SerializedName("results")
    private List<Result> results = null;

    @SerializedName("status")
    private String status = null;


    /**
     * GETTERS
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }
    public String getNextPageToken() {
        return nextPageToken;
    }
    public List<Result> getResults() {
        return results;
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
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
    public void setResults(List<Result> results) {
        this.results = results;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
