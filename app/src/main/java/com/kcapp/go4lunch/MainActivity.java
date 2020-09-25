package com.kcapp.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.fragment.ListFragment;
import com.kcapp.go4lunch.fragment.MapFragment;
import com.kcapp.go4lunch.fragment.WorkmatesFragment;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private FirebaseUser mFirebaseUser;
    private Fragment mSelectedFragment;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get connected user
        mFirebaseUser = App.getFirebaseUser();

        if (mFirebaseUser == null) {
            startAuthActivity();
        } else {
            // Init the navigation drawer
            initNavigationDrawer();
            // Init the bottom navigation
            initBottomNavigationView();

            // Check permission
            checkLocationPermission();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        // Check permission
        checkLocationPermission();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            mSearchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_lunch:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_lunch");

                App.getUserPlace(placeId -> {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    if (placeId != null) {
                        Intent intent = new Intent(getApplicationContext(), PlaceActivity.class);
                        intent.putExtra(Constants.PLACE_ID, placeId);
                        startActivityForResult(intent, Constants.CODE_REQUEST_MAIN_ACTIVITY);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.place_not_selected, Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            case R.id.nav_settings:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_settings");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            case R.id.nav_logout:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_logout");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                signOutFromFirebase();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                reloadFragment(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    reloadFragment(null);
                }

                return false;
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODE_REQUEST_MAIN_ACTIVITY) {
            reloadFragment(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Set default fragment when the app is open
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                }  else {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Open the authentication page
     */
    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Init the navigation drawer
     */
    private void initNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView navPicture = headerView.findViewById(R.id.nav_picture);
        TextView navName = headerView.findViewById(R.id.nav_name);
        TextView navEmail = headerView.findViewById(R.id.nav_email);

        //Get picture URL from Firebase
        if (mFirebaseUser.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(mFirebaseUser.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(navPicture);
        }
        navName.setText(TextUtils.isEmpty(mFirebaseUser.getDisplayName()) ? "Pas de nom" : mFirebaseUser.getDisplayName());
        navEmail.setText(TextUtils.isEmpty(mFirebaseUser.getEmail()) ? "Pas d'email" : mFirebaseUser.getEmail());
    }

    /**
     * Init the bottom navigation
     */
    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.bringToFront();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            mSelectedFragment = null;

            if (mSearchView != null) {
                mSearchView.setQuery(null,true);
            }

            switch (item.getItemId()) {
                case R.id.navigation_map_view:
                    mSelectedFragment = new MapFragment();
                    break;
                case R.id.navigation_list_view:
                    mSelectedFragment = new ListFragment();
                    break;
                case R.id.navigation_workmates:
                    mSelectedFragment = new WorkmatesFragment();
                    break;
            }

            if (mSelectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mSelectedFragment).commit();
                return true;
            }
            return false;
        });
    }

    /**
     * Sign out action
     */
    private void signOutFromFirebase() {
        System.out.println("MainActivity :: signOutFromFirebase");

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        AuthUI.getInstance().signOut(this)
            .addOnSuccessListener(this, aVoid -> startAuthActivity())
            .addOnFailureListener(this, e -> {
                ProgressBar progressBar1 = findViewById(R.id.progress_bar);
                progressBar1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            });
    }

    /**
     * Check location permission
     */
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If the user didn't accept the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_CODE);
        } else {
            // Set default fragment when the app is open
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
        }
    }

    /**
     * Reload the fragment selected
     * @param searchKeyword result of the searchview
     */
    private void reloadFragment(@Nullable String searchKeyword) {
        Fragment fragment = null;

        if (searchKeyword != null) {
            if (mSelectedFragment == null || mSelectedFragment.getClass().equals(MapFragment.class)) {
                fragment = MapFragment.newInstance(searchKeyword);
            } else if (mSelectedFragment.getClass().equals(ListFragment.class)) {
                fragment = ListFragment.newInstance(searchKeyword);
            }
        } else {
            if (mSelectedFragment == null || mSelectedFragment.getClass().equals(MapFragment.class)) {
                fragment = new MapFragment();
            } else if (mSelectedFragment.getClass().equals(ListFragment.class)) {
                fragment = new ListFragment();
            }
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
