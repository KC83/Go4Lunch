package com.kcapp.go4lunch;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseUser;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.di.Injection;
import com.kcapp.go4lunch.di.manager.UserManager;
import com.kcapp.go4lunch.model.User;

public class SettingsActivity extends AppCompatActivity {
    private static UserManager mFirebaseUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // UserManager
        mFirebaseUserManager = Injection.getUserManager();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Send a notification when a place is selected
            Preference notificationPreference = findPreference(getString(R.string.key_notification));
            if (notificationPreference != null) {
                notificationPreference.setOnPreferenceClickListener(preference -> {
                    FirebaseUser firebaseUser = App.getFirebaseUser();

                    boolean isOn = preference.getSharedPreferences().getBoolean(getString(R.string.key_notification), false);
                    String sendNotification = Constants.SEND_NOTIFICATION_TRUE;
                    if (!isOn) {
                        sendNotification = Constants.SEND_NOTIFICATION_FALSE;
                    }

                    mFirebaseUserManager.updateNotificationUser(firebaseUser.getUid(), sendNotification, new UserManager.OnUpdateNotificationUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.makeText(getContext(), R.string.settings_saved,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    return true;
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}