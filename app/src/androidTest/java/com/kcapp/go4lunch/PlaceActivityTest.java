package com.kcapp.go4lunch;

import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PlaceActivityTest {
    private UiDevice mDevice;
    private Context mContext;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void startMainActivityFromHomeScreen() throws UiObjectNotFoundException {
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

        openPlace();
    }

    @Test
    public void showInformation() throws UiObjectNotFoundException {
        // Check name
        UiObject placeName = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_place_name"));
        assertEquals(Utils.PLACE_NAME, placeName.getText());

        // Check buttons "Like", "Call", "WebSite"
        UiObject likeButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_like"));
        assertTrue(likeButton.exists());

        UiObject callButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_call"));
        assertTrue(callButton.exists());

        UiObject websiteButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_website"));
        assertTrue(websiteButton.exists());
    }

    @Test
    public void testLunchButton() throws UiObjectNotFoundException, InterruptedException {
        UiObject lunchButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_lunch"));
        assertTrue(lunchButton.exists());

        int nbWorkmates = getSizeWrokmatesList();

        if (lunchButton.getContentDescription().equals(Utils.BUTTON_CHECK)) {
            // The place is already selected

            // When clicked the place is not selected anymore
            lunchButton.click();
            assertEquals(lunchButton.getContentDescription(), Utils.BUTTON_UNCHECK);

            // Wait for the app to appear
            mDevice.wait(Utils.LAUNCH_TIMEOUT);

            nbWorkmates = nbWorkmates-1;
            assertEquals(getSizeWrokmatesList(), nbWorkmates);

            // When clicked the place is selected
            lunchButton.click();
            assertEquals(lunchButton.getContentDescription(), Utils.BUTTON_CHECK);

            // Wait for the app to appear
            mDevice.wait(Utils.LAUNCH_TIMEOUT);

            nbWorkmates = nbWorkmates+1;
            assertEquals(getSizeWrokmatesList(), nbWorkmates);
        } else if (lunchButton.getContentDescription().equals(Utils.BUTTON_UNCHECK)){
            // The place is not selected

            // When clicked the place is selected
            lunchButton.click();
            assertEquals(lunchButton.getContentDescription(), Utils.BUTTON_CHECK);

            // Wait for the app to appear
            mDevice.wait(Utils.LAUNCH_TIMEOUT);

            nbWorkmates = nbWorkmates+1;
            assertEquals(getSizeWrokmatesList(), nbWorkmates);

            // When clicked the place is not selected anymore
            lunchButton.click();
            assertEquals(lunchButton.getContentDescription(), Utils.BUTTON_UNCHECK);

            // Wait for the app to appear
            mDevice.wait(Utils.LAUNCH_TIMEOUT);

            nbWorkmates = nbWorkmates-1;
            assertEquals(getSizeWrokmatesList(), nbWorkmates);
        } else {
            fail();
        }
    }

    @Test
    public void testLikeButton() throws UiObjectNotFoundException {
        UiObject likeButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_like"));
        assertTrue(likeButton.exists());

        if (likeButton.getContentDescription().equals(Utils.BUTTON_CHECK)) {
            // The place is already liked

            // When clicked the place is not liked anymore
            likeButton.click();
            assertEquals(likeButton.getContentDescription(), Utils.BUTTON_UNCHECK);

            // When clicked the place is liked
            likeButton.click();
            assertEquals(likeButton.getContentDescription(), Utils.BUTTON_CHECK);
        } else if (likeButton.getContentDescription().equals(Utils.BUTTON_UNCHECK)){
            // The place is not liked

            // When clicked the place is liked
            likeButton.click();
            assertEquals(likeButton.getContentDescription(), Utils.BUTTON_CHECK);

            // When clicked the place is not liked anymore
            likeButton.click();
            assertEquals(likeButton.getContentDescription(), Utils.BUTTON_UNCHECK);
        } else {
            fail();
        }
    }

    // Open a place
    public void openPlace() throws UiObjectNotFoundException {
        // Get the marker and click on it
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains(Utils.PLACE_NAME));
        marker.click();

        // Check if we are on the place detail page
        boolean isPlaceActivityShowed = mDevice.wait(Until.hasObject(By.descContains(Utils.PLACE_IMAGE)), Utils.LAUNCH_TIMEOUT);
        assertTrue(isPlaceActivityShowed);
    }

    // Get list workmates size
    public int getSizeWrokmatesList() throws UiObjectNotFoundException {
        // Get the list
        UiObject list = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/place_activity_list_workmates"));
        return list.getChildCount();
    }
}
