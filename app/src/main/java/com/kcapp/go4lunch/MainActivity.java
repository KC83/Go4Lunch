package com.kcapp.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kcapp.go4lunch.fragment.ListFragment;
import com.kcapp.go4lunch.fragment.MapFragment;
import com.kcapp.go4lunch.fragment.WorkmatesFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get connected user
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mFirebaseUser == null) {
            startAuthActivity();
        } else {
            // Init the navigation drawer
            initNavigationDrawer();
            // Init the bottom navigation
            initBottonNavigationView();

            // Check permission
            checkLocationPermission();

            // Set default fragment when the app is open
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_lunch:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_lunch");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_settings:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_settings");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_logout:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_logout");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                signOutFromFirebase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Recherche");

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Open the authentication page
     */
    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
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
    private void initBottonNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.bringToFront();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_map_view:
                        selectedFragment = new MapFragment();
                        break;
                    case R.id.navigation_list_view:
                        selectedFragment = new ListFragment();
                        break;
                    case R.id.navigation_workmates:
                        selectedFragment = new WorkmatesFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
                return false;
            }
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
            .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startAuthActivity();
                }
            })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ProgressBar progressBar = findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
                }
            });
    }

    /**
     * Check location permission
     */
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If the user didn't accept the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        // Check permission
        checkLocationPermission();
    }
}
