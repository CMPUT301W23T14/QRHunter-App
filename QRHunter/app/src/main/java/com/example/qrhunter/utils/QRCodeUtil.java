package com.example.qrhunter.utils;

/**
 * A class containing all the helper functions for QRCode.
 * You shouldn't have to initialize a QRCodeUtil object.
 * TODO: Update the methods with actual logic
 */
public final class QRCodeUtil {

    /**
     * Generates a hash given the content of a QR Code/Bar using SHA-256 Hash.
     *
     * @param qrCodeContent The content of the QR Code
     * @return A String Hash
     */
    public static String generateHash(String qrCodeContent) {
        return "123";
    }

    /**
     * Generate a human readable name for a QR code given the hash
     *
     * @param hash The hash of the QR Code
     * @return A human readable name
     */
    public static String generateName(String hash) {
        return "name";
    }

    /**
     * Generates the score of a QR Code given the hash
     *
     * @param hash The hash of the QR Code
     * @return A score
     */
    public static Integer generateScore(String hash) {
        return 123;
    }

    /**
     * Generate a visual representation of a QR Code given the hash
     *
     * @param hash The hash of the QR Code
     * @return A visual representation
     */
    public static String generateVisualRepresentation(String hash) {
        return "Visuals";
    }
}
