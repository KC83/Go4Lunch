package com.kcapp.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get connected user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startAuthActivity();
        } else {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mDrawerLayout = findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            // Init the bottom navigation
            initBottonNavigationView();
            // Set default fragment when the app is open
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
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

    /**
     * Open the authentication page
     */
    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
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

        AuthUI.getInstance().signOut(this).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startAuthActivity();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_lunch:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_lunch");
                return true;
            case R.id.nav_settings:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_settings");
                return true;
            case R.id.nav_logout:
                System.out.println("MainActivity :: onOptionsItemSelected :: nav_logout");
                signOutFromFirebase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
