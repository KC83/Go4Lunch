package com.kcapp.go4lunch.api.places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    /**
     * Return information of a place
     * @param googlePlaceJson JSON of a place
     * @return information of a place
     */
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity = "--NA--";

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            String latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            String reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }

    /**
     * Return information from all places
     * @param jsonArray JSON of places
     * @return information from all places
     */
    private List<HashMap<String, String>>getPlaces(JSONArray jsonArray) {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();

        for(int i = 0; i<count;i++) {
            try {
                HashMap<String, String> placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placelist;
    }

    /**
     * Parse the data sent by google
     * @param jsonData data sent by google
     * @return places
     */
    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            return getPlaces(jsonArray);
        }

        return null;
    }
}
