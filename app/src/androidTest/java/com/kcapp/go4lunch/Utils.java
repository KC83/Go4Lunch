package com.kcapp.go4lunch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class Utils {
    public static final String BASIC_PACKAGE = "com.kcapp.go4lunch";
    public static final int LAUNCH_TIMEOUT = 15000;

    public static final String PLACE_NAME = "Le Four";
    public static final String PLACE_IMAGE = "Image du restaurant";
    public static final String PLACE_DONT_DECIDED = "n'a pas encore décidé";

    public static final String NAVIGATION_DRAWER_OPEN = "Ouverture du menu";
    public static final String NAVIGATION_DRAWER_CLOSE = "Fermeture du menu";

    public static final String APP_NAME = "Go4Lunch";
    public static final String USER_NAME = "Kelly Chiarotti";
    public static final String USER_EMAIL = "kellyc83780@gmail.com";

    public static final String BUTTON_CHECK = "Button check";
    public static final String BUTTON_UNCHECK = "Button uncheck";

    public static final String LIKE = "AIMER";
    public static final String DISLIKE = "NE PAS AIMER";

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    public static String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
