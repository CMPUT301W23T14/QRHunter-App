package com.example.qrhunter.data.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrhunter.data.model.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserRepository extends DataRepository {
    private boolean playerExists;
    private ArrayList<String> usernames = new ArrayList<>();

    /**
     * Checks whether a player already exists in Firestore
     *
     * @param id The id of a player to check against. This should be a device id
     * @return True if player exist, and false otherwise
     */
    public boolean doesPlayerExist(String id) {
        db.collection("users").document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                playerExists = true;
            }
            else {
                playerExists= false;
            }
        });

        return playerExists;
    }

    /**
     * Checks whether a username already exists in Firestore
     *
     * @param username The username to be checked against
     * @return True if username already exists, false otherwise
     */
    public boolean doesUsernameExist(String username) {
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                if (list.contains(username)) {
                    playerExists = true;
                }
                else {
                    playerExists = false;
                }
            }
        });
        return playerExists;
    }

    private void readData(FirebaseCallback firebaseCallback){
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    usernames.add(document.get("username").toString());
                }
                firebaseCallback.onCallback(usernames);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private interface FirebaseCallback {
        void onCallback(ArrayList<String> list);
    }

    /**
     * Create a player document in Firestore
     *
     * @param player The player to be added to Firestore
     */
    public void initializePlayer(Player player) {
        // Check whether the user already exist
        if (!doesPlayerExist(player.getId())) {
            return;
        }
    }
}
