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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.kcapp.go4lunch.adapter.ListWorkmatesAdapter;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.di.Injection;
import com.kcapp.go4lunch.di.manager.PlaceLikeManager;
import com.kcapp.go4lunch.di.manager.UserManager;
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
    private UserManager mFirebaseUserManager;
    private PlaceLikeManager mFirebasePlaceLikeManager;

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

        // UserManager
        mFirebaseUserManager = Injection.getUserManager();
        // PlaceLikeManager
        mFirebasePlaceLikeManager = Injection.getPlaceLikeManager();

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
        mFirebaseUser = App.getFirebaseUser();

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
        mFirebasePlaceLikeManager.getPlaceLike(mFirebaseUser.getUid(), mPlaceId, new PlaceLikeManager.OnGetPlaceLikeCallback() {
            @Override
            public void onSuccess(PlaceLike placeLike) {
                if (placeLike.getUid() != null) {
                    if (isClicked) {
                        // Delete action
                        mFirebasePlaceLikeManager.deletePlaceLike(placeLike.getUid(), new PlaceLikeManager.OnDeletePlaceLikeCallback() {
                            @Override
                            public void onSuccess(boolean isDeleted) {
                                // Set to false
                                mIsPlaceLiked = false;

                                // Update view
                                changePlaceLikeButton();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Set to true
                        mIsPlaceLiked = true;
                    }
                } else {
                    if (isClicked) {
                        // Create action
                        mFirebasePlaceLikeManager.createPlaceLike(mFirebaseUser.getUid(), mPlaceId, new PlaceLikeManager.OnCreatePlaceLikeCallback() {
                            @Override
                            public void onSuccess(PlaceLike placeLike) {
                                // Set to true
                                mIsPlaceLiked = true;

                                // Update view
                                changePlaceLikeButton();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mIsPlaceLiked = false;
                    }
                }

                // Update view
                changePlaceLikeButton();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Change the like button and the favorite star
     */
    private void changePlaceLikeButton() {
        // Update view
        if (mIsPlaceLiked) {
            // Show favorite icon
            mPlaceName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_yellow, 0);
            // Set text of the button
            mLikeButton.setText(R.string.dislike);
            // Set description
            mLikeButton.setContentDescription(getString(R.string.button_check));
        } else {
            // Remove favorite icon
            mPlaceName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            // Set text of the button
            mLikeButton.setText(R.string.like);
            // Set description
            mLikeButton.setContentDescription(getString(R.string.button_uncheck));
        }
    }

    /**
     * When the button "Set as lunch place" is clicked
     * @param isClicked if true, the "place lunch" is created or deleted
     */
    private void onLunchButtonClicked(boolean isClicked) {
        mFirebaseUserManager.getUser(mFirebaseUser.getUid(), new UserManager.OnGetUserCallback() {
            @Override
            public void onSuccess(User user) {
                // Current user is going to this place today
                if (user.getPlaceId() != null && user.getPlaceDate() != null && user.getPlaceId().equals(mPlaceId) && user.getPlaceDate().equals(App.getTodayDate())) {
                    // If clicked, the current user don't go there anymore
                    if (isClicked) {
                        // Update place, remove placeId and placeDate
                        mFirebaseUserManager.updatePlace(mFirebaseUser.getUid(), null, null, new UserManager.OnUpdatePlaceCallback() {
                            @Override
                            public void onSuccess(User user) {
                                mIsPlaceLunch = false;
                                // Update view
                                changeLunchButton();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mIsPlaceLunch = true;
                    }
                } else {
                    // If clicked, the current user go there
                    if (isClicked) {
                        // Update place
                        mFirebaseUserManager.updatePlace(mFirebaseUser.getUid(), mPlaceId, App.getTodayDate(), new UserManager.OnUpdatePlaceCallback() {
                            @Override
                            public void onSuccess(User user) {
                                mIsPlaceLunch = true;
                                // Update view
                                changeLunchButton();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mIsPlaceLunch = false;
                    }
                }

                // Update view
                changeLunchButton();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Change the lunch button
     */
    public void changeLunchButton() {
        if (mIsPlaceLunch) {
            // Change the button with a uncheck icon
            mLunchButton.setImageResource(R.drawable.ic_check);
            mLunchButton.setContentDescription(getString(R.string.button_check));
        } else {
            // Change the button with a uncheck icon
            mLunchButton.setImageResource(R.drawable.ic_uncheck);
            mLunchButton.setContentDescription(getString(R.string.button_uncheck));
        }
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
    public void onClick(String placeId) {
        // No action when we click on a workmates in the placeActivity
    }
}
