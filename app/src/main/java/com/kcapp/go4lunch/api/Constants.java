package com.kcapp.go4lunch.api;

import com.kcapp.go4lunch.R;

public final class Constants {
    /**
     * AUTH
     */
    public static final String USER_COLLECTION_NAME = "users";
    public static final int RC_SIGN_IN = 100;

    /**
     * GOOGLE MAPS
     */
    public static final int LOCATION_CODE = 1000;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    public static final String NEARBY_SEARCH_URL = "nearbysearch/json?";
    public static final String NEARBY_PROXIMITY_RADIUS = "10000";
    public static final String NEARBY_TYPE = "restaurant";
    public static final String GOOGLE_BROWER_KEY = String.valueOf(R.string.google_browser_key);
}
