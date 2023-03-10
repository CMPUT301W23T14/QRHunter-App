package com.example.qrhunter.utils;

import org.apache.commons.codec.digest.DigestUtils;

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
        return DigestUtils.sha256Hex(qrCodeContent);
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
}
