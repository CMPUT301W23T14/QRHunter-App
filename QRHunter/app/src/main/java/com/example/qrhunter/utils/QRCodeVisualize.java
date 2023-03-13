package com.example.qrhunter.utils;

public class QRCodeVisualize {
    private final String hashValue;
    private String bitValues;

    public QRCodeVisualize(String hashValue) {
        this.hashValue = hashValue;
        this.bitValues = strToBinary(this.hashValue);
    }
    /**
     * turns the string into binary values
     */
    private String strToBinary(String s)
    {
        StringBuilder binary = new StringBuilder();

        for (char c : s.toCharArray()) {
            String binaryChar = "1";
            int intChar = (int) c;
            if (intChar % 2 == 0) {
                binaryChar = "0" ; // Pad with leading zeros to ensure 8 bits
            }
            binary.append(binaryChar);
        }

        return binary.toString();
    }
    /**
     * returns the bitValue
     */
    public String getBitValues() {
        return bitValues;
    }
}
