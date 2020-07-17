package com.kcapp.go4lunch.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.places.GetPlacesData;

public class MapFragment extends Fragment {
    GoogleMap mMap;
    Double mLat, mLng;
    SupportMapFragment mMapFragment;
    FusedLocationProviderClient mClient;

    int PROXIMITY_RADIUS = 10000;

    private OnMapReadyCallback mCallback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mLat = Double.parseDouble("-34");
            mLng = Double.parseDouble("151");

            LatLng sydney = new LatLng(mLat, mLng);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            // Get places
            getPlaces();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(mCallback);
        }

        // Check context
        if (this.getContext() != null) {
            // Initialize fused location
            mClient = LocationServices.getFusedLocationProviderClient(this.getContext());
            // Get the current location
            getCurrentLocation();
            // Get places
            getPlaces();
        }
    }

    /**
     * Get the current locacation of the user
     */
    private void getCurrentLocation() {
        // Check context
        if (this.getContext() == null) {
            return;
        }

        // Check permission
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Initialize task location
        Task<Location> task = mClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            // When success
            if (location != null) {
                mMapFragment.getMapAsync(googleMap -> {
                    mMap = googleMap;
                    mLat = location.getLatitude();
                    mLng = location.getLongitude();

                    // Initialize lat lng
                    LatLng latLng = new LatLng(mLat, mLng);

                    // Create marker options
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);

                    // Zoom map
                    googleMap.addMarker(new MarkerOptions().position(latLng)).setVisible(true);
                    // Move the camera instantly to location with a zoom of 15.
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    // Zoom in, animating the camera.
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

                    // Add marker on map
                    googleMap.addMarker(markerOptions);
                    // Move map
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Get places
                    getPlaces();
                });
            }
        });
    }

    //*** PLACES ***//

    /**
     * Get places
     */
    private void getPlaces() {
        Object[] dataTransfer = new Object[2];
        GetPlacesData getPlacesData = new GetPlacesData();

        if (mMap == null) {
            return;
        }

        mMap.clear();
        String url = getUrl(mLat, mLng);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getPlacesData.execute(dataTransfer);
    }

    /**
     * Return the URL to recover the places
     * @param latitude latitude of the user's current location
     * @param longitude longitude of the user's current location
     * @return URL
     */
    private String getUrl(double latitude, double longitude) {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + latitude + "," + longitude +
                "&radius=" + PROXIMITY_RADIUS +
                "&type=restaurant" +
                "&sensor=true" +
                "&key=" + getString(R.string.google_browser_key);
    }
}