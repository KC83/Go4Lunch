package com.kcapp.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.model.User;

public class UserHelper {

    // COLLECTION REFERENCE
    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION_NAME);
    }
    // CREATE
    public static Task<Void> createUser(String uid, String username, String email, String urlPicture) {
        User userToCreate = new User(uid, username, email, urlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }
    // GET
    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }
    // DELETE
    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
