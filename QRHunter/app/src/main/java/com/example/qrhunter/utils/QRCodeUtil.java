package com.example.qrhunter.utils;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.model.QRCode;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.DocumentSnapshot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class containing all the helper functions for QRCode.
 * You shouldn't have to initialize a QRCodeUtil object.
 */
public final class QRCodeUtil {

    /**
     * Generates a hash given the content of a QR Code/Bar using SHA-256 Hash.
     *
     * @param qrCodeContent The content of the QR Code
     * @return A String Hash
     */
    public static String generateHash(String qrCodeContent) {
        return Hashing.sha256().hashString(qrCodeContent, StandardCharsets.UTF_8).toString();
    }

    /**
     * Generate a human readable name for a QR code given the hash
     *
     * @param hash The hash of the QR Code
     * @return A human readable name
     */
    public static String generateName(String hash) {
        QRCodeName qrCodeName = new QRCodeName(hash);
        return qrCodeName.getQRName();
    }

    /**
     * Generates the score of a QR Code given the hash
     *
     * @param hash The hash of the QR Code
     * @return A score
     */
    public static double generateScore(String hash) {
        int score = 0;
        char[] array = hash.toCharArray();
        for (int i = 0; i < array.length - 1; i++) {
            int value = 0;
            if (array[i] == '0' && array[i + 1] != '0') {
                score += 1;
            } else if (array[i] == array[i + 1]) {
                int j = i + 1;
                while (array[i] == array[j]) {
                    value += 1;
                    j += 1;
                    if (j == array.length) {
                        break;
                    }
                }
            }
            if (value > 0) {
                score += generatePoints(array[i], value);
                i += value;
            }
        }
        return score;
    }

    /**
     * Generates the score of a string of characters within a hash
     *
     * @param key   The character to be converted
     * @param value The exponent to multiply the points by
     * @return A score
     */
    public static double generatePoints(Character key, Integer value) {
        double points;
        int ascii = (int) key;
        if (ascii < 58) {
            ascii -= 48;
        } else {
            ascii -= 87;
        }
        points = Math.pow(ascii, value);
        return points;
    }

    /**
     * Generate a visual representation of a QR Code given the hash
     *
     * @param hash The hash of the QR Code
     * @return A visual representation
     */
    public static String generateVisualRepresentation(String hash) {
        QRCodeVisual qrCodeVisual = new QRCodeVisual(hash);
        return qrCodeVisual.getVisualRepresentation();
    }

    /**
     * Converts A Document Snapshot object to a QR Code object. Used when getting data from Firestore
     *
     * @param qrCodeDoc The document reference to the QR Code object
     * @return A QR Code object
     */
    public static QRCode convertDocumentToQRCode(DocumentSnapshot qrCodeDoc) {
        String hash = qrCodeDoc.get("hash").toString();
        Location location = new Location(qrCodeDoc.getDouble("latitude"), qrCodeDoc.getDouble("latitude"), new ArrayList<>());
        ArrayList<String> commentIds = (ArrayList<String>) qrCodeDoc.get("commentIds");
        ArrayList<String> playerIds = (ArrayList<String>) qrCodeDoc.get("playerIds");

        QRCode qrCode = new QRCode(qrCodeDoc.getId(), hash, location, commentIds, playerIds);

        return qrCode;
    }

    /**
     * Converts A QR Code object to a Hashmap. Used when adding data to Firestore
     *
     * @param qrCode The QR Code object to be converted
     * @return A hashmap
     */
    public static Map<String, Object> convertQRCodeToHashmap(QRCode qrCode) {
        Map<String, Object> qrCodeHashMap = new HashMap<>();
        qrCodeHashMap.put("hash", qrCode.getHash());
        qrCodeHashMap.put("name", qrCode.getName());
        qrCodeHashMap.put("visualRepresentation", qrCode.getVisualRepresentation());
        qrCodeHashMap.put("score", qrCode.getScore());
        qrCodeHashMap.put("latitude", qrCode.getLocation().latitude);
        qrCodeHashMap.put("longitude", qrCode.getLocation().longitude);
        // TODO: Upload location photo to firebase storage
        qrCodeHashMap.put("comments", qrCode.getCommentIds());
        qrCodeHashMap.put("playerIds", qrCode.getPlayerIds());

        return qrCodeHashMap;
    }
}
