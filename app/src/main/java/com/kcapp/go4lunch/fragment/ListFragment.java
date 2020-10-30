package com.kcapp.go4lunch.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListFragment extends Fragment implements ListPlacesAdapter.ListPlacesListener {
    List<Result> mResults;
    RecyclerView mListPlaces;
    ListPlacesAdapter mListPlacesAdapter;
    String mLocation;
    double mLat, mLng;
    String mKeyword;

    ApiGooglePlaces mApiGooglePlaces;
    InternetManager mInternetManager;
    PlacesRepository mPlacesRepository;

    Context mContext;

    public static ListFragment newInstance(String keyword) {
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();

        args.putString(Constants.EXTRA_KEYWORD_LIST_FRAGMENT, keyword);
        listFragment.setArguments(args);

        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mContext == null) {
            return;
        }

        // Initialize internet manager
        mInternetManager = new InternetManagerImpl(mContext);
        if (!mInternetManager.isConnected()) {
            TextView information = view.findViewById(R.id.fragment_list_information);
            information.setText(R.string.error_no_internet);
            information.setVisibility(View.VISIBLE);
            return;
        }

        // Initialize fused location
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(mContext);

        // Check permission
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            // Get keyword if it's a search
            if (getArguments() != null) {
                mKeyword = getArguments().getString(Constants.EXTRA_KEYWORD_LIST_FRAGMENT);
            }

            // Get results and set them into the list
            mListPlaces = view.findViewById(R.id.list_places);
            setResults();
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODE_REQUEST_LIST_FRAGMENT) {
            setResults();
        }
    }

    /**
     * Get results and set them into the recyclerview
     */
    private void setResults() {
        mPlacesRepository = new PlacesRepositoryImpl(mInternetManager, mApiGooglePlaces);
        mPlacesRepository.getPlaces(mLocation, mKeyword, new PlacesCallback() {
            @Override
            public void onPlacesAvailable(GooglePlacesResponse places) {
                mResults = places.getResults();
                mListPlacesAdapter = new ListPlacesAdapter(mResults, mContext, mLat, mLng, ListFragment.this);

                mListPlaces.setLayoutManager(new LinearLayoutManager(mContext));
                mListPlaces.setAdapter(mListPlacesAdapter);
            }

            @Override
            public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {}

            @Override
            public void onError(Exception exception) {
                Toast.makeText(mContext, "Error : "+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(String placeId) {
        Intent intent = new Intent(mContext, PlaceActivity.class);
        intent.putExtra(Constants.PLACE_ID, placeId);
        startActivityForResult(intent, Constants.CODE_REQUEST_LIST_FRAGMENT);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}