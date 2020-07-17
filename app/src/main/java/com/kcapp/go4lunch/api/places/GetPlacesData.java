package com.kcapp.go4lunch.api.places;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class GetPlacesData extends AsyncTask<Object, String, String> {
    private String mGooglePlacesData;
    private GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            // Get places data
            mGooglePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mGooglePlacesData;
    }

    @Override
    protected void onPostExecute(String s){
        List<HashMap<String, String>> placeList;
        DataParser parser = new DataParser();
        // Parse the data
        placeList = parser.parse(s);
        showPlaces(placeList);
    }

    /**
     * Show places on map
     * @param placeList list of places near the user's current location
     */
    private void showPlaces(List<HashMap<String, String>> placeList) {
        if (placeList.size() == 0) {
            return;
        }
        for(int i = 0; i < placeList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            // Get a place
            HashMap<String, String> googlePlace = placeList.get(i);

            // Name of the place
            String placeName = googlePlace.get("place_name");
            // Vicinity of the place
            String vicinity = googlePlace.get("vicinity");
            // Latitude of the place
            double lat = Double.parseDouble(Objects.requireNonNull(googlePlace.get("lat")));
            // Longitude of the place
            double lng = Double.parseDouble(Objects.requireNonNull(googlePlace.get("lng")));

            // Set position, title and icon of the place
            LatLng latLng = new LatLng( lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            // Put the place on the map
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
