package com.example.qrhunter.utils;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.google.firebase.firestore.DocumentSnapshot;

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
    public static QRCode calculateLowestScoreQRCode(ArrayList<QRCode> qrCodes) {
        QRCode currentLowestScoreQRCode = null;
        for (int i = 0; i < qrCodes.size(); i++) {
            QRCode qrcode = qrCodes.get(i);
            if (i == 0) {
                currentLowestScoreQRCode = qrcode;
            } else {
                if (qrcode.getScore() < currentLowestScoreQRCode.getScore()) {
                    currentLowestScoreQRCode = qrcode;
                }
            }
        }

        return currentLowestScoreQRCode;
    }

    /**
     * Get the highest score amongst a list of qr codes
     *
     * @param qrCodes The list of qr codes
     * @return The highest score
     */
    public static QRCode calculateHighestScoreQRCode(ArrayList<QRCode> qrCodes) {
        QRCode currentHighestScoreQRCode = null;
        for (int i = 0; i < qrCodes.size(); i++) {
            QRCode qrcode = qrCodes.get(i);
            if (i == 0) {
                currentHighestScoreQRCode = qrcode;
            } else {
                if (qrcode.getScore() > currentHighestScoreQRCode.getScore()) ;
                {
                    currentHighestScoreQRCode = qrcode;
                }
            }

        }
        return currentHighestScoreQRCode;
    }


}
