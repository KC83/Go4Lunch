package com.kcapp.go4lunch.di.manager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.di.Injection;
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
        UserManager firebaseUserManager = Injection.getUserManager(mFirestore);
        firebaseUserManager.getUser(uid, new OnGetUserCallback() {
            @Override
            public void onSuccess(User user) {
                // If the user is already into the database, we replace the values
                // If the user is not into the database, we create a new user

                user.setUid(uid);
                user.setUsername(username);
                user.setEmail(email);
                user.setUrlPicture(urlPicture);


                Task<Void> task = UserHelper.getUsersCollection(mFirestore).document(uid).set(user);
                task.addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError(new Exception("Error creation of a user"));
                    }
                }).addOnFailureListener(callback::onError);
            }

            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
            }
        });
    }

    @Override
    public void getUser(String uid, OnGetUserCallback callback) {
        Task<DocumentSnapshot> task = UserHelper.getUser(uid);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                if (task1.getResult().exists()) {
                    User user = task1.getResult().toObject(User.class);
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError(new Exception("User not find"));
                    }
                } else {
                    callback.onSuccess(new User());
                }
            } else {
                callback.onError(new Exception("Error get user"));
            }
        }).addOnFailureListener(callback::onError);
    }

    @Override
    public void getUsersForAPlace(String placeId, OnGetUsersForAPlaceCallback callback) {
        Task<QuerySnapshot> task = UserHelper.getUsersForAPlaceForTask(mFirestore, placeId);
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
    public void updatePlace(String uid, String placeId, String date, OnUpdatePlaceCallback callback) {
        Task<DocumentSnapshot> task = UserHelper.getUser(mFirestore, uid);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                if (task1.getResult().exists()) {
                    User user = task1.getResult().toObject(User.class);
                    if (user != null) {
                        user.setPlaceId(placeId);
                        user.setPlaceDate(date);

                        UserHelper.getUsersCollection(mFirestore).document(uid).set(user);

                        callback.onSuccess(user);
                    } else {
                        callback.onError(new Exception("Error : no user"));
                    }
                } else {
                    callback.onError(new Exception("Error : no result"));
                }
            } else {
                callback.onError(new Exception("Error : task not successful"));
            }

        });
    }
}
