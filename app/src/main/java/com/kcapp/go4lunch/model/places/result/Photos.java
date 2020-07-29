package com.kcapp.go4lunch.model.places.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {
    @SerializedName("height")
    private double height;

    @SerializedName("html_attributions")
    private List<String> htmlAttributions = null;

    @SerializedName("photo_reference")
    private String photoReference;

    @SerializedName("width")
    private double width;

    /**
     * GETTERS
     **/
    public double getHeight() {
        return height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public double getWidth() {
        return width;
    }

    /**
     * SETTERS
     **/
    public void setHeight(double height) {
        this.height = height;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
