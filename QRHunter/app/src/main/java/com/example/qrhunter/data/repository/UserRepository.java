package com.example.qrhunter.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.qrhunter.data.model.Player;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

//    public Task<QuerySnapshot> getPlayers() {
//        return playersCollection.orderBy("totalScore", Query.Direction.DESCENDING).get();
//    }

    /**
     * Get all users from Firestore, sorted by totalScore in descending order, setting id = documentid and rest of variables from fields
     * @return LiveData of a list of users/players classes
     */
    public LiveData<List<Player>> getUsers() {
        MutableLiveData<List<Player>> usersLiveData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("user_test");
        usersRef.orderBy("totalScore", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Player> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String id = document.getId();
                String username = document.getString("username");
                int totalScore = Objects.requireNonNull(document.getLong("totalScore")).intValue();
                String phoneNumber = document.getString("phoneNumber");
                int rank = Objects.requireNonNull(document.getLong("rank")).intValue();
                Player player = new Player(id, username, phoneNumber, rank, totalScore);
                users.add(player);
            }
            usersLiveData.setValue(users);
        });
        return usersLiveData;
    }

}
