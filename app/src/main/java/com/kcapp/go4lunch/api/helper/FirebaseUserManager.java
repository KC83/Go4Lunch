package com.kcapp.go4lunch.api.helper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.model.User;

public class FirebaseUserManager implements UserManager {
    private final FirebaseFirestore mFirestore;

    public FirebaseUserManager(FirebaseFirestore firestore) {
        mFirestore = firestore;
    }

    @Override
    public void createUser(String uid, String username, String email, String urlPicture, OnUserCreationCallback callback) {
        User userToCreate = new User(uid, username, email, urlPicture);
        Task<Void> task = UserHelper.getUsersCollection(mFirestore).document(uid).set(userToCreate);
        task.addOnCompleteListener(task1 -> {
           if (task1.isSuccessful()) {
               callback.onSuccess(userToCreate);
           } else {
               callback.onError(new Exception("Erreur création de l'utilisateur"));
           }
        }).addOnFailureListener(callback::onError);
    }
}
