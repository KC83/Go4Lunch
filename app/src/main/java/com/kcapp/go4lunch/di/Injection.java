package com.kcapp.go4lunch.di;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.di.manager.FirebasePlaceLikeManager;
import com.kcapp.go4lunch.di.manager.FirebaseUserManager;
import com.kcapp.go4lunch.di.manager.PlaceLikeManager;
import com.kcapp.go4lunch.di.manager.UserManager;

public class Injection {
    public static UserManager getUserManager() {
        return new FirebaseUserManager(FirebaseFirestore.getInstance());
    }
    public static UserManager getUserManager(FirebaseFirestore firestore) {
        return new FirebaseUserManager(firestore);
    }

    public static PlaceLikeManager getPlaceLikeManager() {
        return new FirebasePlaceLikeManager(FirebaseFirestore.getInstance());
    }
    public static PlaceLikeManager getPlaceLikeManager(FirebaseFirestore firestore) {
        return new FirebasePlaceLikeManager(firestore);
    }
}
