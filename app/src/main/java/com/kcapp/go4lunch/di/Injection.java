package com.kcapp.go4lunch.di;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.di.manager.FirebaseUserManager;
import com.kcapp.go4lunch.di.manager.UserManager;

public class Injection {
    public static UserManager getUserManager() {
        return new FirebaseUserManager(FirebaseFirestore.getInstance());
    }
}
