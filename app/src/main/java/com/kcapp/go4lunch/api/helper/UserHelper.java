package com.kcapp.go4lunch.api.helper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.model.User;

public class UserHelper {
    // COLLECTION REFERENCE WITH A FIREBASE FIRESTORE
    public static CollectionReference getUsersCollection(FirebaseFirestore firestore) {
        return firestore.collection(Constants.USER_COLLECTION_NAME);
    }
    // COLLECTION REFERENCE
    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION_NAME);
    }

    // UPDATE USER
    public static Task<Void> updateUser(User user) {
        return UserHelper.getUsersCollection().document(user.getUid()).set(user);
    }
    public static Task<Void> updateUser(FirebaseFirestore firestore,User user) {
        return UserHelper.getUsersCollection(firestore).document(user.getUid()).set(user);
    }

    // GET USER
    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }
    public static Task<DocumentSnapshot> getUser(FirebaseFirestore firestore, String uid) {
        return UserHelper.getUsersCollection(firestore).document(uid).get();
    }
    // GET ALL USERS
    public static Task<QuerySnapshot> getUsersForAPlaceForTask(String placeId) {
        return UserHelper.getUsersCollection().whereEqualTo("placeId", placeId).whereEqualTo("placeDate", App.getTodayDate()).get();
    }
    public static Task<QuerySnapshot> getUsersForAPlaceForTask(FirebaseFirestore firestore, String placeId) {
        return UserHelper.getUsersCollection(firestore).whereEqualTo("placeId", placeId).whereEqualTo("placeDate", App.getTodayDate()).get();
    }

    // Query for the list of workmates
    public static Query getAllUsers() {
        return UserHelper.getUsersCollection().orderBy("username", Query.Direction.ASCENDING);
    }
    // Query for the list of workmates in a detail place
    public static Query getAllUsersForAPlace(String placeId) {
        return UserHelper.getUsersCollection().whereEqualTo("placeId", placeId).whereEqualTo("placeDate", App.getTodayDate()).orderBy("username", Query.Direction.ASCENDING);
    }

    // ADAPTER
    public static FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query, LifecycleOwner lifecycleOwner){
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(lifecycleOwner)
                .build();
    }
}
