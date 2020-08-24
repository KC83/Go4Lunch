package com.kcapp.go4lunch.model.places.result;

import com.google.gson.annotations.SerializedName;
import com.kcapp.go4lunch.model.places.result.openinghours.OpeningHours;
import com.kcapp.go4lunch.model.places.result.geometry.Geometry;

import java.util.List;

public class Result {
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("id")
    private String id;

    @SerializedName("icon")
    private String icon;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @SerializedName("photos")
    private List<Photos> photos;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("plus_code")
    private PlusCode plusCode;

    @SerializedName("rating")
    private double rating;

    @SerializedName("reference")
    private String reference;

    @SerializedName("scope")
    private String scope;

    @SerializedName("types")
    private List<String> types = null;

    @SerializedName("user_ratings_total")
    private Integer UserRatingsTotal;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("website")
    private String website;

    /**
     * GETTERS
     **/
    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public double getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public String getScope() {
        return scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public Integer getUserRatingsTotal() {
        return UserRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getWebsite() {
        return website;
    }

    /**
     * SETTERS
     **/
    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        UserRatingsTotal = userRatingsTotal;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
