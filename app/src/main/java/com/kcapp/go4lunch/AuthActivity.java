package com.kcapp.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.di.Injection;
import com.kcapp.go4lunch.di.manager.UserManager;
import com.kcapp.go4lunch.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity {
    private UserManager mFirebaseUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initButtons();

        // UserManager
        mFirebaseUserManager = Injection.getUserManager();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void initButtons() {
        Button facebookButton = findViewById(R.id.auth_activity_button_login_facebook);
        Button googleButton = findViewById(R.id.auth_activity_button_login_google);
        Button twitterButton = findViewById(R.id.auth_activity_button_login_twitter);

        facebookButton.setOnClickListener(v -> authWithFacebook());
        googleButton.setOnClickListener(v -> authWithGoogle());
        twitterButton.setOnClickListener(v -> authWithTwitter());
    }
    private void authWithFacebook() {
        Log.d("TAG", "AuthActivity::authWithFacebook");

        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                Constants.RC_SIGN_IN);
    }
    private void authWithGoogle() {
        Log.d("TAG", "AuthActivity::authWithGoogle");

        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                Constants.RC_SIGN_IN);
    }
    private void authWithTwitter() {
        Log.d("TAG", "AuthActivity::authWithTwitter");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            Log.d("TAG", "AuthActivity::authWithTwitter :: is not null");
            pendingResultTask
                    .addOnSuccessListener(authResult -> createUserInFirebase())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show());
        } else {
            Log.d("TAG", "AuthActivity::authWithTwitter :: is null");
            firebaseAuth
                    .startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(authResult -> createUserInFirebase()
                    )
                    .addOnFailureListener(e -> {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);

        Log.d("TAG", "AuthActivity::handleResponseAfterSignIn");
        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                createUserInFirebase();
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void createUserInFirebase() {
        Log.d("TAG", "AuthActivity::createUserInFirebase");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            String username = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            String urlPicture = (currentUser.getPhotoUrl() != null) ? currentUser.getPhotoUrl().toString() : null;

            mFirebaseUserManager.createUser(uid, username, email, urlPicture, new UserManager.OnUserCreationCallback() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(getApplicationContext(), getString(R.string.connection_succeed), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
