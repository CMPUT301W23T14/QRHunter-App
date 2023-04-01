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
        int totalScore = documentSnapshot.getLong("totalScore").intValue();

        Player player = new Player(documentSnapshot.getId(), username, phoneNumber, totalScore);

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
        playerHashMap.put("totalScore", player.getTotalScore());

        return playerHashMap;
    }
    /**
     * Get the highest score amongst a list of qr codes
     *
     * @param qrCodes The arraylist that holds the player's qrCodes
     * @return The highest score
     */
    public static double calculateTotalScore(ArrayList<QRCode> qrCodes) {
        double currentTotalScore = 0;
        for (int i = 0; i < qrCodes.size(); i++) {
            QRCode qrcode = qrCodes.get(i);
            currentTotalScore = currentTotalScore + qrcode.getScore();
        }

        return currentTotalScore;
    }

    /**
     * Get the lowest score amongst a list of qr codes
     *
     * @param qrCodes The list of qr codes
     * @return The lowest score
     */
    public static double calculateLowestScore(ArrayList<QRCode> qrCodes) {
        double currentLowestScore = 0;
        for (int i = 0; i < qrCodes.size(); i++) {
            QRCode qrcode = qrCodes.get(i);
            if (i == 0) {
                Log.d("TAG", "i=0 " + String.valueOf(currentLowestScore));
                currentLowestScore = qrcode.getScore();
            } else {
                if (qrcode.getScore() < currentLowestScore) ;
                {
                    Log.d("TAG", "lower val: " + String.valueOf(currentLowestScore));
                    currentLowestScore = qrcode.getScore();
                }
            }
        }

        return currentLowestScore;
    }

    /**
     * Get the highest score amongst a list of qr codes
     *
     * @param qrCodes The list of qr codes
     * @return The highest score
     */
    public static double calculateHighestScore(ArrayList<QRCode> qrCodes) {
        double currentHighestScore = 0;
        for (int i = 0; i < qrCodes.size(); i++) {
            QRCode qrcode = qrCodes.get(i);
            if (i == 0) {
                currentHighestScore = qrcode.getScore();
            } else {
                if (qrcode.getScore() > currentHighestScore) ;
                {
                    currentHighestScore = qrcode.getScore();
                }
            }

        }
        return currentHighestScore;
    }



}
