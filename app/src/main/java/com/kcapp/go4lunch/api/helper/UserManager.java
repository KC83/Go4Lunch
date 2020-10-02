package com.kcapp.go4lunch.api.helper;

import com.kcapp.go4lunch.model.User;

public interface UserManager {
    void createUser(String uid, String username, String email, String urlPicture, OnUserCreationCallback callback);

    interface OnUserCreationCallback {
        void onSuccess(User user);
        void onError(Throwable throwable);
    }
}