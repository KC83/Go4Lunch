package com.kcapp.go4lunch;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UnitTest {
    @Before
    public void setUp() throws FileNotFoundException {
        /*FirebaseFirestore firebase = new FirebaseFirestore();
        firebase.initializeTestApp({
                projectId: "my-test-project",
                auth: { uid: "alice", email: "alice@example.com" }
        });*/

        /*FirebaseApp firebaseApp = FirebaseApp.initializeTestApp({
                projectId: "fir-op-f829d",
                auth: { uid: "alice", email: "alice@example.com" }
        });*/


        /*FileInputStream serviceAccount = new FileInputStream("app/go4lunch-f9b80-firebase-adminsdk-3zth0-0ee4bb668b.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://go4lunch-f9b80.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);*/

        /*
        FirebaseApp firebaseApp = FirebaseApp.initializeTestApp({
                projectId: "fir-op-f829d",
                auth: { uid: "alice", email: "alice@example.com" }
        });
        */
    }

    @Test
    public void test() {

    }
}
