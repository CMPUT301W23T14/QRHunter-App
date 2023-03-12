package com.example.qrhunter.data.repository;

import com.example.qrhunter.data.model.Player;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class UserRepository extends DataRepository {
    /**
     * Checks whether a player already exists in Firestore.
     *
     * @param id                 The id of a player to check against. This should be a device id
     * @param repositoryCallback The callback class that contains "onSuccess" method to be called with the result
     */
    public void doesPlayerExist(String id, RepositoryCallback<Boolean> repositoryCallback) {
        DocumentReference playerDocRef = db.collection("players").document(id);

        playerDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot playerDocument = task.getResult();

                if (playerDocument.exists()) {
                    repositoryCallback.onSuccess(true);
                } else {
                    repositoryCallback.onSuccess(false);
                }
            }
        });
    }

    /**
     * Checks whether a username already exists in Firestore
     *
     * @param username           The username to be checked against
     * @param repositoryCallback The callback class that contains "onSuccess" method to be called with the result
     */
    public void doesUsernameExist(String username, RepositoryCallback<Boolean> repositoryCallback) {
        Query usernameQuery = db.collection("players").whereEqualTo("username", username);

        usernameQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    repositoryCallback.onSuccess(false);
                } else {
                    repositoryCallback.onSuccess(true);
                }
            }
        });
    }

    /**
     * Create a player document in Firestore
     *
     * @param player The player to be added to Firestore
     */
    public void createPlayer(Player player, RepositoryCallback<Player> repositoryCallback) {
        Map<String, Object> playerHashMap = new HashMap<>();
        playerHashMap.put("username", player.getUsername());

        db.collection("players").document(player.getId()).set(playerHashMap).addOnCompleteListener(task -> {
            repositoryCallback.onSuccess(player);
        });
    }


}
