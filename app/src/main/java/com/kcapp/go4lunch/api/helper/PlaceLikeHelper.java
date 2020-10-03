package com.kcapp.go4lunch.api.helper;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.services.Constants;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PlaceLikeHelper {
    // COLLECTION REFERENCE WITH A FIREBASE FIRESTORE
    public static CollectionReference getPlacesLikesCollection(FirebaseFirestore firestore) {
        return firestore.collection(Constants.PLACE_LIKE_COLLECTION_NAME);
    }

    // COLLECTION REFERENCE
    private static CollectionReference getPlacesLikesCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.PLACE_LIKE_COLLECTION_NAME);
    }
    // CREATE
    public static void createPlaceLike(String userUid, String placeId) {
        Map<String, String> data = new HashMap<>();
        data.put("userUid", userUid);
        data.put("placeId", placeId);

        PlaceLikeHelper.getPlacesLikesCollection()
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, R.string.success_add_place_like + " : " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, R.string.error_add_place_like + "", e);
                });
    }
    // GET
    public static Task<QuerySnapshot> getPlaceLike(String userUid, String placeId) {
        return PlaceLikeHelper.getPlacesLikesCollection()
                .whereEqualTo("userUid", userUid)
                .whereEqualTo("placeId", placeId)
                .get();
    }

    // DELETE
    public static void deletePlaceLike(String uid) {
        PlaceLikeHelper.getPlacesLikesCollection().document(uid).delete();
    }
}
