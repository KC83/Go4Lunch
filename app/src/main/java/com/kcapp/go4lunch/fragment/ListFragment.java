package com.kcapp.go4lunch.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.kcapp.go4lunch.MainActivity;
import com.kcapp.go4lunch.PlaceActivity;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.adapter.ListPlacesAdapter;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepository;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.result.Result;

import java.util.List;

public class ListFragment extends Fragment implements ListPlacesAdapter.ListPlacesListener {
    List<Result> mResults;
    RecyclerView mListPlaces;
    ListPlacesAdapter mListPlacesAdapter;
    String mLocation;
    double mLat, mLng;

    ApiGooglePlaces mApiGooglePlaces;
    InternetManager mInternetManager;
    PlacesRepository mPlacesRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return;
        }

        // Initialize internet manager
        mInternetManager = new InternetManagerImpl(getContext());
        if (!mInternetManager.isConnected()) {
            TextView information = view.findViewById(R.id.fragment_list_information);
            information.setText(R.string.error_no_internet);
            information.setVisibility(View.VISIBLE);
            return;
        }

        // Initialize fused location
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getContext());

        // Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Initialize task location
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            // When success
            if (location != null) {
                mLocation = location.getLatitude()+","+location.getLongitude();
                mLat = location.getLatitude();
                mLng = location.getLongitude();
            }

            // Create retrofit
            mApiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

            // Get results and set them into the list
            setResults(view);
        });

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Get results and set them into the recyclerview
     * @param view the view of the fragment
     */
    private void setResults(View view) {
        mPlacesRepository = new PlacesRepositoryImpl(mInternetManager, mApiGooglePlaces);
        mPlacesRepository.getPlaces(mLocation, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {
                mResults = places.getResults();
                mListPlacesAdapter = new ListPlacesAdapter(mResults, getContext(), mLat, mLng, ListFragment.this);

                mListPlaces = view.findViewById(R.id.list_places);
                mListPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
                mListPlaces.setAdapter(mListPlacesAdapter);
            }

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {}

            @Override
            public void onError(Exception exception) {
                Toast.makeText(getContext(), "Error : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(String placeId) {
        Intent intent = new Intent(getContext(), PlaceActivity.class);
        intent.putExtra(Constants.PLACE_ID, placeId);
        startActivity(intent);
    }
}