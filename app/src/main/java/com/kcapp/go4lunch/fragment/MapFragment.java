package com.kcapp.go4lunch.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import com.kcapp.go4lunch.PlaceActivity;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepository;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.result.Result;

public class MapFragment extends Fragment {
    GoogleMap mMap;
    Double mLat, mLng;
    SupportMapFragment mMapFragment;
    View mMapView;
    FusedLocationProviderClient mClient;
    String mKeyword;

    public static MapFragment newInstance(String keyword) {
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();

        args.putString(Constants.EXTRA_KEYWORD_MAP_FRAGMENT, keyword);
        mapFragment.setArguments(args);

        return mapFragment;
    }

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

            if (getContext() != null) {
                boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
            }

            // Get places
            getPlaces();

            mMapView.setContentDescription("MAP READY");
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
            mMapView = mMapFragment.getView();
            if (mMapView != null) {
                mMapView.setContentDescription("MAP NOT READY");
            }

            mMapFragment.getMapAsync(mCallback);
        }

        // Check context
        if (this.getContext() != null) {
            // Initialize fused location
            mClient = LocationServices.getFusedLocationProviderClient(this.getContext());
            // Get keyword if it's a search
            if (getArguments() != null) {
                mKeyword = getArguments().getString(Constants.EXTRA_KEYWORD_MAP_FRAGMENT);
            }
            // Get the current location
            getCurrentLocation();
            // Get places
            getPlaces();

            mMapView.setContentDescription("MAP READY");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CODE_REQUEST_MAP_FRAGMENT) {
            getPlaces();
        }
    }

    /**
     * Get the current location of the user
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
                    // Move the camera instantly to location with a zoom of 18.
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
    @SuppressLint("MissingPermission")
    private void getPlaces() {
        if (mMap == null) {
            return;
        }

        String location = mLat + "," + mLng;

        Context context = this.getContext();
        InternetManager internetManager = new InternetManagerImpl(context);
        ApiGooglePlaces apiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

        PlacesRepository placesRepository = new PlacesRepositoryImpl(internetManager, apiGooglePlaces);
        placesRepository.getPlaces(location, mKeyword, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {
                for (Result result : places.getResults()) {
                     App.getCountOfReservation(result.getPlaceId(), count -> {
                         showPlace(result, count);
                    });
                }
            }

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {}

            @Override
            public void onError(Exception exception) {
                if (!internetManager.isConnected()) {
                    Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set "My location" button
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(marker -> {
            String placeId = (String) marker.getTag();
            marker.hideInfoWindow();


            Intent intent = new Intent(getContext(), PlaceActivity.class);
            intent.putExtra(Constants.PLACE_ID, placeId);
            startActivityForResult(intent, Constants.CODE_REQUEST_MAP_FRAGMENT);

            return false;
        });
    }

    /**
     * Show places on map
     * @param result a google place
     */
    private void showPlace(Result result, int count) {
        MarkerOptions markerOptions = new MarkerOptions();

        // Set position, title and icon of the place
        LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
        markerOptions.position(latLng);

        //markerOptions.title(result.getName() + " : " + result.getVicinity());
        if (count > 0) {
            markerOptions.icon(getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_marker_green)));
        } else {
            markerOptions.icon(getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_marker_red)));
        }

        // Put the place on the map
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(result.getPlaceId());
        marker.setTitle(result.getName());
    }

    /**
     * Return a drawable to a bitmap icon
     * @param drawable drawable
     * @return bitmap
     */
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}