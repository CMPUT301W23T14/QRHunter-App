package com.example.qrhunter.data.repository;

import com.example.qrhunter.data.model.Player;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository extends DataRepository {
    /**
     * Checks whether a player already exists in Firestore
     *
     * @param id The id of a player to check against. This should be a device id
     * @return True if player exist, and false otherwise
     */
    public boolean doesPlayerExist(String id) {
        return false;
    }

    /**
     * Checks whether a username already exists in Firestore
     *
     * @param username The username to be checked against
     * @return True if username already exists, false otherwise
     */
    public boolean doesUsernameExist(String username) {
        return false;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference playersCollection = db.collection("user_test");

    public Task<QuerySnapshot> getPlayers() {
        return playersCollection.orderBy("totalScore", Query.Direction.DESCENDING).get();
    }

}
