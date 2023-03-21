package com.example.qrhunter.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class containing helper methods related to Players
 */
public final class PlayerUtil {

    /**
     * Converts A Document Snapshot object to a Player Object. Used when retrieving data from Firestore
     *
     * @param documentSnapshot The document snapshot to be converted
     * @return A Player object
     */
    public static Player convertDocumentToPlayer(DocumentSnapshot documentSnapshot) {
        String username = documentSnapshot.get("username").toString();
        String phoneNumber = documentSnapshot.get("phoneNumber").toString();
        int rank = documentSnapshot.getLong("rank").intValue();
        int totalScore = documentSnapshot.getLong("totalScore").intValue();

        Player player = new Player(documentSnapshot.getId(), username, phoneNumber, rank, totalScore);

        return player;
    }

    /**
     * Converts A Player object to a hash map. Used when adding data to Firestore
     *
     * @param player The Player object to be converted
     * @return A hash map
     */
    public static Map<String, Object> convertPlayerToHashmap(Player player) {
        Map<String, Object> playerHashMap = new HashMap<>();
        playerHashMap.put("username", player.getUsername());
        playerHashMap.put("phoneNumber", player.getPhoneNumber());
        // TODO: Calculate the correct rank
        playerHashMap.put("rank", player.getRank());
        playerHashMap.put("totalScore", player.getTotalScore());

        return playerHashMap;
    }

    /**
     * Calculate the total score given the list of qr codes
     *
     * @param user The reference to the user to obtain scores from
     * @return The sum of the scores
     */
    public static double calculateTotalScore(String user) {
        final double[] currentTotalScore = {0};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("players")
                .whereEqualTo("username", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Double i = (Double) document.getDouble("totalScore");
                                currentTotalScore[0] += i;
                                Log.d("TAG", String.valueOf(currentTotalScore[0]));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return currentTotalScore[0];
    }

    /**
     * Get the lowest score amongst a list of qr codes
     *
     * @param user The user to get the lowest score for the user
     * @return The lowest score
     */
    public static double calculateLowestScore(String user) {
        final double[] currentLowestScore = {0};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("players")
                .whereEqualTo("username", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Double i = (Double) document.getDouble("totalScore");
                                if (currentLowestScore[0] == 0){
                                    currentLowestScore[0] = i;
                                }
                                else {
                                    if (i < currentLowestScore[0]){
                                        currentLowestScore[0] = i;
                                    }
                                }

                                Log.d("TAG", String.valueOf(currentLowestScore[0]));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return currentLowestScore[0];
    }

    /**
     * Get the highest score amongst a list of qr codes
     *
     * @param user The user to get the highest score from
     * @return The highest score
     */
    public static double calculateHighestScore(String user) {
        final double[] currentHighestScore = {0};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("players")
                .whereEqualTo("username", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Double i = (Double) document.getDouble("totalScore");
                                if (currentHighestScore[0] == 0){
                                    currentHighestScore[0] = i;
                                }
                                else {
                                    if (i > currentHighestScore[0]){
                                        currentHighestScore[0] = i;
                                    }
                                }

                                Log.d("TAG", String.valueOf(currentHighestScore[0]));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return currentHighestScore[0];
    }
}
