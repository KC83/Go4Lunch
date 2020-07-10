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

public class MapFragment extends Fragment {
    SupportMapFragment mMapFragment;
    FusedLocationProviderClient mClient;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

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
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
            mMapFragment.getMapAsync(callback);
        }

        // Check context
        if (this.getContext() != null) {
            // Initialize fused location
            mClient = LocationServices.getFusedLocationProviderClient(this.getContext());
            // Get the current location
            getCurrentLocation();
        }
    }

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
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                // When success
                if (location != null) {
                    mMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            // Initialize lat lng
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            // Create marker options
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(String.valueOf(R.string.google_current_location));
                            // Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            // Add marker on map
                            googleMap.addMarker(markerOptions);
                        }
                    });
                }
            }
        });
    }
}