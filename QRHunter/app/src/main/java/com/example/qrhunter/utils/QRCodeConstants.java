package com.example.qrhunter.utils;

/**
 * Class containing states and methods for generating visual representation and human readable name.
 * Meant to be implemented by other classes that is responsible for generating visuals and name.
 */
public class QRCodeConstants {
    private final String hashValue;
    private String bitValues;

    /**
     * Constructor for the qrCode hashvalue and bitvalues
     * @param hashValue The hashvalue of the qrCode
     */
    public QRCodeConstants(String hashValue) {
        this.hashValue = hashValue;
        this.bitValues = strToBinary(this.hashValue);
    }

    /**
     * Turns the string into binary values
     * @param s The string to be turned into binary values
     * @return The binary as a string
     */
    private String strToBinary(String s) {
        StringBuilder binary = new StringBuilder();

        for (char c : s.toCharArray()) {
            String binaryChar = "1";
            int intChar = (int) c;
            if (intChar % 2 == 0) {
                binaryChar = "0"; // Pad with leading zeros to ensure 8 bits
            }
            binary.append(binaryChar);
        }

        return binary.toString();
    }

    /**
     * Gets the bit values of the QrCode
     * @return The bit values of the qrCode
     */
    public String getBitValues() {
        return bitValues;
    }
}
