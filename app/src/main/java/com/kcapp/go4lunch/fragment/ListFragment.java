package com.kcapp.go4lunch.fragment;

import android.Manifest;
import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.adapter.ListPlacesAdapter;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepository;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;
import com.kcapp.go4lunch.model.places.Result;

import java.util.List;

public class ListFragment extends Fragment {
    List<Result> mResults;
    RecyclerView mListPlaces;
    ListPlacesAdapter mListPlacesAdapter;
    String mLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        InternetManager internetManager = new InternetManagerImpl(getContext());
        Context context = this.getContext();

        // Get the user's current location
        getCurrentLocation();
        mLocation = -34.0+","+151.0;

        ApiGooglePlaces apiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);


        PlacesRepository placesRepository = new PlacesRepositoryImpl(internetManager, apiGooglePlaces);
        placesRepository.getPlaces(mLocation, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {
                mResults = places.getResults();

                mListPlacesAdapter = new ListPlacesAdapter(mResults);

                mListPlaces = view.findViewById(R.id.list_places);
                mListPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
                mListPlaces.setAdapter(mListPlacesAdapter);
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(getContext(), "Error : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Get the current location of the user
     */
    private void getCurrentLocation() {
        if (getContext() == null) {
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
            }
        });
    }
}