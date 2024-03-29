package com.example.qrhunter.utils;

import android.util.Log;

import com.example.qrhunter.data.model.QRCode;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.DocumentSnapshot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
     * TODO: Move these convert functions into the model classes themselves?
     *
     * @param qrCodeDoc The document reference to the QR Code object
     * @return A QR Code object
     */
    public static QRCode convertDocumentToQRCode(DocumentSnapshot qrCodeDoc) {
        QRCode qrCode = qrCodeDoc.toObject(QRCode.class);
        qrCode.setId(qrCodeDoc.getId());

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

        qrCodeHashMap.put("locations", qrCode.getLocations());
        qrCodeHashMap.put("photos", qrCode.getPhotos());
        
        qrCodeHashMap.put("commentIds", qrCode.getCommentIds());
        qrCodeHashMap.put("playerIds", qrCode.getPlayerIds());

        return qrCodeHashMap;
    }

    // From GeekForGeeks
    // URL: https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
    // Author: Saurav Jain
    /**
     * Sorts Hashmap by points of the QRCode
     * @param hm The hashmap to be sorted
     * @return A sorted hashmap
     */
    public static Map<String, Double> sortByValue(Map<String, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        Map<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * Checks if the QRCode is unique
     * @param deviceId The device ID of the player
     * @param sortedMap The sorted map of QRCode according to score
     * @param playerMap The map of QRCode and player ID
     * @return A list of the QRCode ID and the rank of the QRCode
     */
    public static List<String> checkUniqueQRCode(String deviceId, HashMap<String, Double> sortedMap, HashMap<String, String> playerMap) {
        Integer count = 0;
        List<String> id = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            if (playerMap.containsKey(key)) {
                if (playerMap.get(key).equals(deviceId)) {
                    id.add(key);
                    id.add(String.valueOf(count+1));
                    break;
                }
            }
            count++;
        }
        return id;
    }
}
