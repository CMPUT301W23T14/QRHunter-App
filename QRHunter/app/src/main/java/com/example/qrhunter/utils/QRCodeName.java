package com.example.qrhunter.utils;

/**
 * Class for generating a human readable QR Code name
 */
public class QRCodeName extends QRCodeConstants {
    /**
     * Generates the name of the qrCode
     */
    private final String bitValues;
    QRCodeConstants qrCodeConstants;
    private String qrCodeName = "";

    /**
     * Constructor for new QRCodeName
     *
     * @param hashValue The hash value of the qr code scanned
     */
    public QRCodeName(String hashValue) {
        super(hashValue);
        qrCodeConstants = new QRCodeConstants(hashValue);
        this.bitValues = qrCodeConstants.getBitValues();
        createQRName();
    }

    /**
     * Generate a name based on the hashValue
     */
    public void createQRName() {

        // bit 0 head
        if (bitValues.charAt(0) == '0') {
            qrCodeName += "Ice";
        } else {
            qrCodeName += "hot";
        }
        // bit 1 head
        if (bitValues.charAt(1) == '0') {
            qrCodeName += "Fro";
        } else {
            qrCodeName += "Gol";
        }
        // bit 2 head
        if (bitValues.charAt(2) == '0') {
            qrCodeName += "Mo";
        } else {
            qrCodeName += "Lo";
        }
        // bit 3 head
        if (bitValues.charAt(3) == '0') {
            qrCodeName += "Mega";
        } else {
            qrCodeName += "Huge";
        }
        // bit 4 head
        if (bitValues.charAt(4) == '0') {
            qrCodeName += "Fusc";
        } else {
            qrCodeName += "Amok";
        }
        // bit 5 head
        if (bitValues.charAt(5) == '0') {
            qrCodeName += "Human";
        } else {
            qrCodeName += "Snake";
        }
    }

    /**
     * Retrieve the generated name
     *
     * @return The name of the qrcode
     */
    public String getQRName() {
        return this.qrCodeName;
    }
}