package com.example.qrhunter.utils;

import com.example.qrhunter.data.model.Player;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

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
}
