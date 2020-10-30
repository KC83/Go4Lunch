package com.kcapp.go4lunch.api.services;

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
     * MAIN ACTIVITY
     */
    public static final int CODE_REQUEST_MAIN_ACTIVITY = 5000;
    public static final int AUTOCOMPLETE_MAIN_ACTIVITY = 6000;
    public static final String EXTRA_KEYWORD_MAP_FRAGMENT = "EXTRA_KEYWORD_MAP_FRAGMENT";
    public static final String EXTRA_KEYWORD_LIST_FRAGMENT = "EXTRA_KEYWORD_LIST_FRAGMENT";

    /**
     * PLACE ACTIVITY
     */
    public static final String PLACE_ID = "placeId";
    public static final String PLACE_ACTIVITY = "placeActivity";

    /**
     * FRAGMENTS
     */
    public static final String WORKMATES_FRAMGMENT = "workmatesFragment";
    public static final int CODE_REQUEST_MAP_FRAGMENT = 2000;
    public static final int CODE_REQUEST_LIST_FRAGMENT = 3000;
    public static final int CODE_REQUEST_WORKMATES_FRAGMENT = 4000;

    /**
     * SETTINGS ACTIVITY
     */
    public static final int CODE_REQUEST_SETTINGS_ACTIVITY = 7000;
    public static final String SEND_NOTIFICATION_TRUE = "true";
    public static final String SEND_NOTIFICATION_FALSE = "false";
}
