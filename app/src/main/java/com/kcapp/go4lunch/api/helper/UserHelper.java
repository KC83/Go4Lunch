package com.kcapp.go4lunch.api.helper;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    public static Query getAllUsers() {
        return UserHelper.getUsersCollection().orderBy("username", Query.Direction.ASCENDING);
    }

    // DELETE
    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    // ADAPTER
    public static FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query, LifecycleOwner lifecycleOwner){
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(lifecycleOwner)
                .build();
    }
}