package com.kcapp.go4lunch;

import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.internal.Util;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MapTest {
    private UiDevice mDevice;
    private Context mContext;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = Utils.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), Utils.LAUNCH_TIMEOUT);

        // Launch the blueprint app
        mContext = getApplicationContext();
        final Intent intent = mContext.getPackageManager()
                .getLaunchIntentForPackage(Utils.BASIC_PACKAGE);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        }
        mContext.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(Utils.BASIC_PACKAGE).depth(0)), Utils.LAUNCH_TIMEOUT);
    }

    /**
     * Check if the map is loaded
     */
    @Test
    public void testShowMap() {
        boolean isMapLoaded = mDevice.wait(Until.hasObject(By.desc("MAP READY")), Utils.LAUNCH_TIMEOUT);
        assertTrue(isMapLoaded);
    }

    /**
     * Check if after a click on a marker, the place activity opens
     * When press back, the user is redirect to the map page
     */
    @Test
    public void testClickOnAMarker() throws UiObjectNotFoundException {
        // Get the marker and click on it
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains(Utils.PLACE_NAME));
        marker.click();

        // Check if we are on the place detail page
        boolean isPlaceActivityShowed = mDevice.wait(Until.hasObject(By.descContains(Utils.PLACE_IMAGE)), Utils.LAUNCH_TIMEOUT);
        assertTrue(isPlaceActivityShowed);

        // Test after press back that the map is loaded
        mDevice.pressBack();
        testShowMap();
    }
}