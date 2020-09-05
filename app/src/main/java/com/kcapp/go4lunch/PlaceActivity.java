package com.kcapp.go4lunch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.adapter.ListWorkmatesAdapter;
import com.kcapp.go4lunch.api.helper.PlaceLikeHelper;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.model.PlaceLike;
import com.kcapp.go4lunch.model.User;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

public class PlaceActivity extends AppCompatActivity implements PlacesCallback, ListWorkmatesAdapter.ListWorkmatesListener {
    private ImageView mPlaceImage;
    private TextView mPlaceName;
    private TextView mPlaceAddress;

    private TextView mInformation;
    private ProgressBar mProgressBar;
    private FloatingActionButton mLunchButton;

    private Button mCallButton;
    private Button mLikeButton;
    private Button mWebsiteButton;

    private RecyclerView mListWorkmates;

    private FirebaseUser mFirebaseUser;
    private String mPlaceId;

    private boolean mIsPlaceLiked;
    private boolean mIsPlaceLunch;

    private InternetManager mInternetManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        // Init view
        initView();
        showElements(false, true,getString(R.string.loading));

        mInternetManager = new InternetManagerImpl(this);
        if (!mInternetManager.isConnected()) {
            showElements(false, false, getString(R.string.error_no_internet));
            return;
        }
        ApiGooglePlaces apiGooglePlaces = ApiGooglePlaces.retrofit.create(ApiGooglePlaces.class);

        // Get place
        PlacesRepositoryImpl placesRepository = new PlacesRepositoryImpl(mInternetManager, apiGooglePlaces);
        placesRepository.getPlaceDetail(mPlaceId, this);
    }

    @Override
    public void onPlacesAvailable(GooglePlacesResponse places) {}

    @Override
    public void onPlaceDetailAvailable(GooglePlaceDetailResponse place) {
        setValues(place);

        // After 1s the elements are visible
        final Handler handler = new Handler();
        handler.postDelayed(() -> showElements(true, false,null), 1000);
    }

    @Override
    public void onError(Exception exception) {
        if (mInternetManager.isConnected()) {
            Toast.makeText(PlaceActivity.this, "Error : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Init view
     */
    private void initView() {
        Intent intent = getIntent();
        mPlaceId = intent.getStringExtra(Constants.PLACE_ID);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mPlaceImage = findViewById(R.id.place_activity_place_image);
        mPlaceName = findViewById(R.id.place_activity_place_name);
        mPlaceAddress = findViewById(R.id.place_activity_place_address);

        mInformation = findViewById(R.id.place_activity_information);
        mProgressBar = findViewById(R.id.place_activity_progress_bar);
        mLunchButton = findViewById(R.id.place_activity_lunch);

        mCallButton = findViewById(R.id.place_activity_call);
        mLikeButton = findViewById(R.id.place_activity_like);
        mWebsiteButton = findViewById(R.id.place_activity_website);

        mListWorkmates = findViewById(R.id.place_activity_list_workmates);
    }

    /**
     * Set values to the view
     * @param place the google place selected
     */
    private void setValues(GooglePlaceDetailResponse place) {
        // PHOTO
        if (place.getResult().getPhotos() != null) {
            RequestManager requestManager = Glide.with(PlaceActivity.this);
            requestManager
                    .load(Constants.BASE_URL + Constants.PHOTO_SEARCH_URL + "maxwidth=400&photoreference=" + place.getResult().getPhotos().get(0).getPhotoReference() + "&key=" + Constants.GOOGLE_BROWSER_KEY)
                    .into(mPlaceImage);
        } else {
            mPlaceImage.setImageResource(R.drawable.ic_no_photo);
        }

        // NAME
        mPlaceName.setText(place.getResult().getName());
        // ADDRESS
        mPlaceAddress.setText(place.getResult().getVicinity());

        // LIKE IMAGE
        onLikeButtonClicked(false);

        // BUTTON LUNCH
        onLunchButtonClicked(false);
        mLunchButton.setOnClickListener(view -> {
            onLunchButtonClicked(true);
            // After 1s the elements are visible
            final Handler handler = new Handler();
            handler.postDelayed(this::showListOfWorkmates, 1000);
        });

        // BUTTONS
        // Call button
        mCallButton.setOnClickListener(view -> {
            if (place.getResult().getFormattedPhoneNumber() != null) {
                dialPhoneNumber(place.getResult().getFormattedPhoneNumber());
            } else {
                Toast.makeText(PlaceActivity.this, R.string.no_phone, Toast.LENGTH_SHORT).show();
            }
        });

        // Like button
        mLikeButton.setOnClickListener(view -> {
            onLikeButtonClicked(true);
        });

        // Website button
        mWebsiteButton.setOnClickListener(view -> {
            if (place.getResult().getWebsite() != null) {
                openWebPage(place.getResult().getWebsite());
            } else {
                Toast.makeText(PlaceActivity.this, R.string.no_website, Toast.LENGTH_SHORT).show();
            }
        });

        // List of workmates
        showListOfWorkmates();
    }

    /**
     * Show elements on the view
     * @param isShowed if true, show elements
     * @param informationText set the text for the information text
     */
    private void showElements(boolean isShowed, boolean isProgressBarShowed, @Nullable String informationText) {
        int visibility = View.VISIBLE;
        if (!isShowed) {
            visibility = View.INVISIBLE;
        }

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.place_activity_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setVisibility(visibility);
        }

        mInformation.setText(informationText);
        mInformation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if (!isShowed) {
            mInformation.setVisibility(View.VISIBLE);
        } else {
            mInformation.setVisibility(View.INVISIBLE);
        }

        if (informationText != null) {
            if (informationText.equals(getString(R.string.error_no_internet))) {
                //mInformation.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_no_wifi, 0, 0);
                mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.ic_no_wifi));
            }
        }

        if (isProgressBarShowed) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When the button "Like/Dislike" is clicked
     * @param isClicked if true, the "place like" is created or deleted
     */
    private void onLikeButtonClicked(boolean isClicked) {
        Task<QuerySnapshot> query = PlaceLikeHelper.getPlaceLike(mFirebaseUser.getUid(), mPlaceId);
        query.addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()) {
                if (isClicked) {
                    // Create action
                    PlaceLikeHelper.createPlaceLike(mFirebaseUser.getUid(), mPlaceId);

                    // Set to true
                    mIsPlaceLiked = true;
                } else {
                    mIsPlaceLiked = false;
                }
            } else {
                if (isClicked) {
                    PlaceLike placeLike = task.getResult().getDocuments().get(0).toObject(PlaceLike.class);
                    placeLike.setUid(task.getResult().getDocuments().get(0).getId());

                    // Delete action
                    PlaceLikeHelper.deletePlaceLike(placeLike.getUid());

                    // Set to false
                    mIsPlaceLiked = false;
                } else {
                    // Set to true
                    mIsPlaceLiked = true;
                }
            }

            // Update view
            if (mIsPlaceLiked) {
                // Show favorite icon
                mPlaceName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_yellow, 0);
                // Set text of the button
                mLikeButton.setText(R.string.dislike);
            } else {
                // Remove favorite icon
                mPlaceName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                // Set text of the button
                mLikeButton.setText(R.string.like);
            }
        });
    }

    /**
     * When the button "Set as lunch place" is clicked
     * @param isClicked if true, the "place lunch" is created or deleted
     */
    private void onLunchButtonClicked(boolean isClicked) {
        Task<DocumentSnapshot> documentSnapshotTask = UserHelper.getUser(mFirebaseUser.getUid());
        documentSnapshotTask.addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);

            // Current user is going to this place today
            if (user.getPlaceId() != null && user.getPlaceDate() != null && user.getPlaceId().equals(mPlaceId) && user.getPlaceDate().equals(App.getTodayDate())) {
                mIsPlaceLunch = true;

                // If clicked, the current user don't go there anymore
                if (isClicked) {
                    mIsPlaceLunch = false;
                    // Update place, remove placeId and placeDate
                    UserHelper.updatePlace(mFirebaseUser.getUid(), null, null);
                }
            } else {
                mIsPlaceLunch = false;

                // If clicked, the current user go there
                if (isClicked) {
                    mIsPlaceLunch = true;
                    // Update place
                    UserHelper.updatePlace(mFirebaseUser.getUid(), mPlaceId, App.getTodayDate());
                }
            }

            // Update view
            if (mIsPlaceLunch) {
                // Change the button with a uncheck icon
                mLunchButton.setImageResource(R.drawable.ic_check);
            } else {
                // Change the button with a uncheck icon
                mLunchButton.setImageResource(R.drawable.ic_uncheck);
            }
        });
    }

    /**
     * Open the phone app and pre-dial the phone number
     * @param phoneNumber phone number of a place
     */
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Open the web page of a place
     * @param url the website url of a place
     */
    public void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Show the list of workmates
     */
    public void showListOfWorkmates() {
        ListWorkmatesAdapter listWorkmatesAdapter = new ListWorkmatesAdapter(UserHelper.generateOptionsForAdapter(UserHelper.getAllUsersForAPlace(mPlaceId), this), Constants.PLACE_ACTIVITY, PlaceActivity.this);

        mListWorkmates.setLayoutManager(new LinearLayoutManager(this));
        mListWorkmates.setAdapter(listWorkmatesAdapter);
    }

    @Override
    public void onClick(String placeId) {}
}