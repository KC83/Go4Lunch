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

public class PlaceLunchHelper {
    // COLLECTION REFERENCE
    public static CollectionReference getPlacesLunchCollection() {
        return FirebaseFirestore.getInstance().collection(Constants.PLACE_LUNCH_COLLECTION_NAME);
    }
    // CREATE
    public static void createPlaceLunch(String userUid, String placeId, String date) {
        Map<String, String> data = new HashMap<>();
        data.put("userUid", userUid);
        data.put("placeId", placeId);
        data.put("date", date);

        PlaceLunchHelper.getPlacesLunchCollection()
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, R.string.success_add_place_lunch + " : " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, R.string.error_add_place_lunch + "", e);
                });
    }
    // GET

    /**
     * Get place lunch with a place and a date
     * @param placeId
     * @param date
     * @return
     */
    public static Task<QuerySnapshot> getPlaceLunch(String placeId, String date) {
        return PlaceLunchHelper.getPlacesLunchCollection()
                .whereEqualTo("placeId", placeId)
                .whereEqualTo("date", date)
                .get();
    }
    /**
     * Get place lunch with a user, a place and a date
     * @param userUid
     * @param placeId
     * @param date
     * @return
     */
    public static Task<QuerySnapshot> getPlaceLunch(String userUid, String placeId, String date) {
        return PlaceLunchHelper.getPlacesLunchCollection()
                .whereEqualTo("userUid", userUid)
                .whereEqualTo("placeId", placeId)
                .whereEqualTo("date", date)
                .get();
    }
    /**
     * Get the place of lunch for a user and a date
     * @param userUid
     * @param date
     * @return
     */
    public static Task<QuerySnapshot> getUserPlaceLunch(String userUid, String date) {
        return PlaceLunchHelper.getPlacesLunchCollection()
                .whereEqualTo("userUid", userUid)
                .whereEqualTo("date", date)
                .get();
    }

    // DELETE
    public static void deletePlaceLunch(String uid) {
        PlaceLunchHelper.getPlacesLunchCollection().document(uid).delete();
    }
}
