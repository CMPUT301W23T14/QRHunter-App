package com.example.qrhunter.data.repository;

import com.example.qrhunter.data.model.Player;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class PlayerRepository extends DataRepository {
    /**
     * Checks whether a player already exists in Firestore.
     *
     * @param id The id of a player to check against. This should be a device id
     * @return A boolean in the callback
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
     * @param username The username to be checked against
     * @return A boolean in the callback
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
     * Create a player document in Firestore.
     *
     * @param player The player to be added to Firestore
     * @return The player that was created in the callback
     */
    public void createPlayer(Player player, RepositoryCallback<Player> repositoryCallback) {
        Map<String, Object> playerHashMap = new HashMap<>();
        playerHashMap.put("username", player.getUsername());
        playerHashMap.put("phoneNumber", player.getPhoneNumber());
        // TODO: Calculate the correct rank
        playerHashMap.put("rank", player.getRank());
        playerHashMap.put("totalScore", player.getTotalScore());

        db.collection("players").document(player.getId()).set(playerHashMap).addOnCompleteListener(task -> {
            repositoryCallback.onSuccess(player);
        });
    }

    /**
     * Gets a player with a playerId
     *
     * @param playerId The player's id
     * @return A player object in the callback
     */
    public void getPlayer(String playerId, RepositoryCallback<Player> repositoryCallback) {
        DocumentReference playerDocRef = db.collection("players").document(playerId);

        playerDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String username = task.getResult().get("username").toString();
                String phoneNumber = task.getResult().get("phoneNumber").toString();
                int rank = ((Long) task.getResult().get("rank")).intValue();
                int totalScore = ((Long) task.getResult().get("totalScore")).intValue();

                Player player = new Player(task.getResult().getId().toString(), username, phoneNumber, rank, totalScore);
                repositoryCallback.onSuccess(player);
            }
        });
    }
}
