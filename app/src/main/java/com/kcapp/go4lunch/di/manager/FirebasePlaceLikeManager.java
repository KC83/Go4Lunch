package com.kcapp.go4lunch.di.manager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.api.helper.PlaceLikeHelper;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.di.Injection;
import com.kcapp.go4lunch.model.PlaceLike;

import java.util.HashMap;
import java.util.Map;

public class FirebasePlaceLikeManager implements PlaceLikeManager {
    private final FirebaseFirestore mFirestore;

    public FirebasePlaceLikeManager(FirebaseFirestore firestore) {
        mFirestore = firestore;
    }

    @Override
    public void createPlaceLike(String userUid, String placeId, OnCreatePlaceLikeCallback callback) {
        Map<String, String> data = new HashMap<>();
        data.put("userUid", userUid);
        data.put("placeId", placeId);

        PlaceLikeHelper.getPlacesLikesCollection(mFirestore)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    PlaceLikeManager firebasePlaceLikeManager = Injection.getPlaceLikeManager(mFirestore);
                    firebasePlaceLikeManager.getPlaceLike(userUid, placeId, new OnGetPlaceLikeCallback() {
                        @Override
                        public void onSuccess(PlaceLike placeLike) {
                            callback.onSuccess(placeLike);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            callback.onError(new Exception(throwable.getMessage()));
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    callback.onError(new Exception("Error : "+e.getMessage()));
                });
    }

    @Override
    public void getPlaceLike(String userUid, String placeId, OnGetPlaceLikeCallback callback) {
        Task<QuerySnapshot> task = PlaceLikeHelper.getPlaceLike(mFirestore, userUid, placeId);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                if (!task1.getResult().isEmpty()) {
                    PlaceLike placeLike = task1.getResult().getDocuments().get(0).toObject(PlaceLike.class);
                    if (placeLike != null) {
                        // Set uid
                        placeLike.setUid(task1.getResult().getDocuments().get(0).getId());
                        callback.onSuccess(placeLike);
                    } else {
                        callback.onError(new Exception("PlaceLike not find"));
                    }
                } else {
                    callback.onSuccess(new PlaceLike());
                }
            } else {
                callback.onError(new Exception("Error get placeLike"));
            }
        });
    }

    @Override
    public void deletePlaceLike(String uid, OnDeletePlaceLikeCallback callback) {
        PlaceLikeHelper.getPlacesLikesCollection(mFirestore)
                .document(uid)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(true))
                .addOnFailureListener(e -> callback.onError(new Exception("Error : placeLike not deleted")));
    }
}
