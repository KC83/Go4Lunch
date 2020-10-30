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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ListPlacesTest {
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

        // Click on the list button
        UiObject listButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/navigation_list_view"));
        listButton.click();

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(Utils.BASIC_PACKAGE).depth(0)), Utils.LAUNCH_TIMEOUT);
    }

    @Test
    public void testShowList() throws UiObjectNotFoundException {
        // Check if the list exists
        UiObject list = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/list_places"));
        assertTrue(list.exists());

        // Check if the list is not empty
        assertTrue(list.getChildCount() > 0);
    }

    @Test
    public void testClickOnElement() throws UiObjectNotFoundException {
        // Get the list
        UiObject list = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/list_places"));

        // Get first element of the list
        UiObject item = list.getChild(new UiSelector().index(0));
        item.click();

        // Check if we are on the place detail page
        boolean isPlaceActivityShowed = mDevice.wait(Until.hasObject(By.descContains(Utils.PLACE_IMAGE)), Utils.LAUNCH_TIMEOUT);
        assertTrue(isPlaceActivityShowed);

        // Test after press back that the list is loaded
        mDevice.pressBack();
        testShowList();
    }
}
