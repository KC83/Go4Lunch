package com.kcapp.go4lunch.di.manager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.model.User;

import java.util.Arrays;
import java.util.List;

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
               callback.onError(new Exception("Error creation of a user"));
           }
        }).addOnFailureListener(callback::onError);
    }

    @Override
    public void getUser(String uid, OnGetUserCallback callback) {
        Task<DocumentSnapshot> task = UserHelper.getUsersCollection(mFirestore).document(uid).get();
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                if (task1.getResult() != null) {
                    User user = task.getResult().toObject(User.class);
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError(new Exception("User not find"));
                    }
                }
                callback.onSuccess(new User());
            } else {
                callback.onError(new Exception("Error get user"));
            }
        }).addOnFailureListener(callback::onError);
    }

    @Override
    public void getUsersForAPlace(String placeId, OnGetUsersForAPlaceCallback callback) {
        Task<QuerySnapshot> task = UserHelper.getUsersCollection(mFirestore).whereEqualTo("placeId", placeId).whereEqualTo("placeDate", App.getTodayDate()).get();
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                List<User> users = Arrays.asList();
                if (task1.getResult() != null) {
                    users = task1.getResult().toObjects(User.class);
                }
                callback.onSuccess(users);
            } else {
                callback.onError(new Exception("Error when getting all users for a place"));
            }
        }).addOnFailureListener(callback::onError);
    }

    @Override
    public void deleteUser(String uid, OnDeleteUserCallback callback) {
        Task<Void> task = UserHelper.getUsersCollection(mFirestore).document(uid).delete();
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                callback.onSuccess(true);
            } else {
                callback.onError(new Exception("Error delete user"));
            }
        }).addOnFailureListener(callback::onError);
    }
}
