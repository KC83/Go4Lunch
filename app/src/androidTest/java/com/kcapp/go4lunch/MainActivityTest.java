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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private UiDevice mDevice;
    private Context mContext;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

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

    @Test
    public void testShowBottomMenu() {
        UiObject bottomMenu = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/bottom_navigation"));
        assertTrue(bottomMenu.exists());
    }

    @Test
    public void testDrawer() throws UiObjectNotFoundException {
        // Open the navigation drawer
        UiObject drawerOpen = mDevice.findObject(new UiSelector().description(Utils.NAVIGATION_DRAWER_OPEN));
        drawerOpen.click();

        // Check name of the app
        UiObject navTitle = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/nav_title"));
        assertTrue(navTitle.exists());
        assertEquals(navTitle.getText(),Utils.APP_NAME);

        // Check information of the user
        UiObject navNameUser = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/nav_name"));
        assertTrue(navNameUser.exists());
        assertEquals(navNameUser.getText(),Utils.USER_NAME);

        UiObject navEmailUser = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/nav_email"));
        assertTrue(navEmailUser.exists());
        assertEquals(navEmailUser.getText(),Utils.USER_EMAIL);

        // Check buttons
        UiObject navLunchButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/nav_lunch"));
        assertTrue(navLunchButton.exists());

        UiObject navSettingsButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/nav_settings"));
        assertTrue(navSettingsButton.exists());

        UiObject navLogoutButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/nav_logout"));
        assertTrue(navLogoutButton.exists());

        // Close the navigation drawer
        //UiObject drawerClose = mDevice.findObject(new UiSelector().description(Utils.NAVIGATION_DRAWER_CLOSE));
        //drawerClose.click();

        // Check if the elements don't exists
        //assertFalse(navLunchButton.exists());
        //assertFalse(navSettingsButton.exists());
        //assertFalse(navLogoutButton.exists());
    }

    @Test
    public void testSearchButton() throws UiObjectNotFoundException {
        UiObject searchButton = mDevice.findObject(new UiSelector().resourceId(Utils.BASIC_PACKAGE+":id/toolbar_search"));
        assertTrue(searchButton.exists());

        searchButton.click();
    }
}
