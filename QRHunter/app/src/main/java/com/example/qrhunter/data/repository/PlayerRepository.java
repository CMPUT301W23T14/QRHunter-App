package com.example.qrhunter.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.utils.PlayerUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A repository class for containing any data access and business logic related to Players
 */
public class PlayerRepository extends DataRepository {
    /**
     * Checks whether a player already exists in Firestore.
     * Returns A boolean in the callback
     *
     * @param id The id of a player to check against. This should be a device id
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
     * Returns A boolean in the callback
     *
     * @param username The username to be checked against
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
     * Returns The player that was created in the callback
     *
     * @param player The player to be added to Firestore
     */
    public void createPlayer(Player player, RepositoryCallback<Player> repositoryCallback) {
        Map<String, Object> playerHashMap = PlayerUtil.convertPlayerToHashmap(player);

        db.collection("players").document(player.getId()).set(playerHashMap).addOnCompleteListener(task -> {
            repositoryCallback.onSuccess(player);
        });
    }

    /**
     * Gets a player with a playerId
     * Returns A player object in the callback
     *
     * @param playerId The player's id
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

    /**
     * Update the player document with a new score
     *
     * @param playerId The id of the player
     * @param score    The score to be added to the player. Can be negative to indicate removal of score
     */
    public void addScoreToPlayer(String playerId, Double score) {
        db.collection("players").document(playerId).update("totalScore", FieldValue.increment(score));
    }


    /**
     * Gets a list of players from firestore
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
                Player player = new Player(id, username, phoneNumber, totalScore);
                users.add(player);
            }
            usersLiveData.setValue(users);
        });
        return usersLiveData;
    }

    public void uploadPhoto(byte[] savedPhoto, String qrCodeId, String playerId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (savedPhoto != null){
            storageReference.child("photos").child(qrCodeId).child(playerId).putBytes(savedPhoto);
        }

    }

    public void addPhoneNumber(String playerID, String phoneNumber) {
        db.collection("players").document(playerID).update("phoneNumber", phoneNumber);
    }


}
