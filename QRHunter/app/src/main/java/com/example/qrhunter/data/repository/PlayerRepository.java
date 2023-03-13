package com.example.qrhunter.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.utils.PlayerUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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
        Map<String, Object> playerHashMap = PlayerUtil.convertPlayerToHashmap(player);

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
                Player player = PlayerUtil.convertDocumentToPlayer(task.getResult());

                repositoryCallback.onSuccess(player);
            }
        });
    }

    public void addScoreToPlayer(String playerId, Double score) {
        db.collection("players").document(playerId).update("totalScore", FieldValue.increment(score));
    }



    /**
     * Gets a list of players from firestore
     *
     *
     * @return LiveData of a list of player objects
     */

    public LiveData<List<Player>> getUsers() {
        AtomicInteger rank = new AtomicInteger(0);
        MutableLiveData<List<Player>> usersLiveData = new MutableLiveData<>();
        CollectionReference usersRef = db.collection("players");
        usersRef.orderBy("totalScore", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Player> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String id = document.getId();
                String username = document.getString("username");
                int totalScore = Objects.requireNonNull(document.getLong("totalScore")).intValue();
                String phoneNumber = document.getString("phoneNumber");
//                int rank = Objects.requireNonNull(document.getLong("rank")).intValue(); // currently i am setting rank based on highest scores, should it be set here or somewhere else?
                rank.addAndGet(1);
                document.getReference().update("rank", rank.get());
                Player player = new Player(id, username, phoneNumber, rank.get(), totalScore);
                users.add(player);
            }
            usersLiveData.setValue(users);
        });
        return usersLiveData;
    }

}
