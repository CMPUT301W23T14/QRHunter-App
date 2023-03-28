package com.example.qrhunter.utils;

/**
<<<<<<< HEAD:QRHunter/app/src/main/java/com/example/qrhunter/utils/QRCodeVisualize.java
 * A class that assists in creating the visualization of the QR code by calculating the bit values of the QR
 * code that was recently scanned.
 */
public class QRCodeVisualize {
=======
 * Class containing states and methods for generating visual representation and human readable name.
 * Meant to be implemented by other classes that is responsible for generating visuals and name.
 */
public class QRCodeConstants {
>>>>>>> main:QRHunter/app/src/main/java/com/example/qrhunter/utils/QRCodeConstants.java
    private final String hashValue;
    private String bitValues;

    public QRCodeConstants(String hashValue) {
        this.hashValue = hashValue;
        this.bitValues = strToBinary(this.hashValue);
    }

    /**
     * turns the string into binary values
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
     * @return The bit values of the qr code
     */
    public String getBitValues() {
        return bitValues;
    }
}
