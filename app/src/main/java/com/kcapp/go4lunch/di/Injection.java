package com.kcapp.go4lunch.di;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.api.helper.FirebaseUserManager;
import com.kcapp.go4lunch.api.helper.UserManager;

public class Injection {
    public static UserManager getUserManager() {
        return new FirebaseUserManager(FirebaseFirestore.getInstance());
    }
}
