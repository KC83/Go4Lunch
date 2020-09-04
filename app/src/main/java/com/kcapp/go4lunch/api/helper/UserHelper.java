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

import java.util.ArrayList;
import java.util.List;

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
    public static Query getUsersByUid(List<String> list) {
        Query query = UserHelper.getUsersCollection();
        for (int i=0; i<list.size(); i++) {
            //query = query.whereEqualTo("uid",list.get(i));
        }

        //query = query.whereEqualTo("uid","MJSqkIqoMwS45Mrx56Xq").whereEqualTo("uid","p9x3jSboKiff1YcxpkKvwSRf0GB3");
        //query = query.whereGreaterThanOrEqualTo("uid","MJSqkIqoMwS45Mrx56Xq").whereLessThanOrEqualTo("uid","p9x3jSboKiff1YcxpkKvwSRf0GB3");

        return query;
    }

    /*
    public static List<Query> getUsersByUid(List<String> list) {
        List<Query> queries = new ArrayList<Query>();

        for (int i=0; i<list.size(); i++) {
            Query query = UserHelper.getUsersCollection().whereEqualTo("uid", list.get(i));
            queries.add(query);
        }

        return queries;
    }*/

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
    /*public static FirestoreRecyclerOptions<User> generateOptionsForAdapter(List<Query> queries, LifecycleOwner lifecycleOwner){
        FirestoreRecyclerOptions.Builder<User> builder = new FirestoreRecyclerOptions.Builder<User>();
        for (int i=0; i<queries.size(); i++) {
            Query query = queries.get(i);
            builder.setQuery(query, User.class);
        }
        builder.setLifecycleOwner(lifecycleOwner);

        return builder.build();
    }*/
}
