package com.kcapp.go4lunch.api.services;

import com.kcapp.go4lunch.R;

public final class Constants {
    /**
     * AUTH
     */
    public static final int RC_SIGN_IN = 100;

    /**
     * HELPER
     */
    public static final String USER_COLLECTION_NAME = "users";
    public static final String PLACE_LIKE_COLLECTION_NAME = "placeLike";

    /**
     * GOOGLE MAPS
     */
    public static final int LOCATION_CODE = 1000;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    public static final String NEARBY_SEARCH_URL = "nearbysearch/json?";
    public static final String DETAIL_SEARCH_URL = "details/json?";
    public static final String PHOTO_SEARCH_URL = "photo?";
    public static final String NEARBY_PROXIMITY_RADIUS = "10000";
    public static final String NEARBY_TYPE = "restaurant";
    public static final String GOOGLE_BROWSER_KEY = "AIzaSyBSlvem5jVlKoFGpiIS_2-tj8Q9xhPlcEw";

    /**
     * PLACE ACTIVITY
     */
    public static final String PLACE_ID = "placeId";
    public static final String PLACE_ACTIVITY = "placeActivity";

    /**
     * WORKMATES FRAGMENT
     */
    public static final String WORKMATES_FRAMGMENT = "workmatesFragment";
}
