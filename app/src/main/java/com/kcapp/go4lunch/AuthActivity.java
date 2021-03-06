package com.kcapp.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kcapp.go4lunch.api.UserHelper;

import java.util.Arrays;
import java.util.Collections;

public class AuthActivity extends AppCompatActivity {
    private ConstraintLayout mConstraintLayout;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void initButtons() {
        mConstraintLayout = findViewById(R.id.auth_activity_constraint_layout);
        Button facebookButton = findViewById(R.id.auth_activity_button_login_facebook);
        Button googleButton = findViewById(R.id.auth_activity_button_login_google);
        Button twitterButton = findViewById(R.id.auth_activity_button_login_twitter);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authWithFacebook();
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authWithGoogle();
            }
        });
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authWithTwitter();
            }
        });
    }
    private void authWithFacebook() {
        Log.d("TAG", "AuthActivity::authWithFacebook");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }
    private void authWithGoogle() {
        Log.d("TAG", "AuthActivity::authWithGoogle");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }
    private void authWithTwitter() {
        Log.d("TAG", "AuthActivity::authWithTwitter");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);

        Log.d("TAG", "AuthActivity::handleResponseAfterSignIn");
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                createUserInFirestore();
                Snackbar.make(this.mConstraintLayout, getString(R.string.connection_succeed), Snackbar.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else { // ERRORS
                if (response == null) {
                    Snackbar.make(this.mConstraintLayout, getString(R.string.error_authentication_canceled), Snackbar.LENGTH_SHORT).show();
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(this.mConstraintLayout, getString(R.string.error_no_internet), Snackbar.LENGTH_SHORT).show();
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(this.mConstraintLayout, getString(R.string.error_unknown_error), Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void createUserInFirestore() {
        Log.d("TAG", "AuthActivity::createUserInFirestore");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            String username = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            String urlPicture = (currentUser.getPhotoUrl() != null) ? currentUser.getPhotoUrl().toString() : null;

            UserHelper.createUser(uid, username, email, urlPicture).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
