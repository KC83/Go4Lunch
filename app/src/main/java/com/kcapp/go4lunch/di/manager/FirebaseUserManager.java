package com.kcapp.go4lunch.di.manager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
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

                if (user.getSendNotification() == null) {
                    user.setSendNotification(Constants.SEND_NOTIFICATION_TRUE);
                }

                firebaseUserManager.updateUser(user, new OnUpdateUserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void getUser(String uid, OnGetUserCallback callback) {
        Task<DocumentSnapshot> task = UserHelper.getUser(mFirestore, uid);
        task.addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        if (task1.getResult().exists()) {
                            User user = task1.getResult().toObject(User.class);
                            if (user != null) {
                                callback.onSuccess(user);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onSuccess(new User());
                        }
                    } else {
                        callback.onError();
                    }
                })
            .addOnFailureListener(task1 -> {
                callback.onError();
            });
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
                callback.onError();
            }
        }).addOnFailureListener(task1 -> {
            callback.onError();
        });
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
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            } else {
                callback.onError();
            }

        });
    }

    @Override
    public void updateUser(User user, OnUpdateUserCallback callback) {
        Task<Void> task = UserHelper.updateUser(mFirestore, user);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                callback.onSuccess(user);
            } else {
                callback.onError();
            }
        }).addOnFailureListener(task1 -> {
            callback.onError();
        });
    }

    @Override
    public void updateNotificationUser(String uid, String sendNotification, OnUpdateNotificationUserCallback callback) {
        UserManager firebaseUserManager = Injection.getUserManager(mFirestore);
        firebaseUserManager.getUser(uid, new OnGetUserCallback() {
            @Override
            public void onSuccess(User user) {
                user.setSendNotification(sendNotification);

                firebaseUserManager.updateUser(user, new OnUpdateUserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}
