package com.example.qrhunter.utils;

import java.util.Hashtable;

/**
 * Class that generates the visual representation of the qrCode
 */
public class QRCodeVisual extends QRCodeConstants {

    private final String bitValues;
    private final Hashtable<String, String> qrCodeVisual = new Hashtable<String, String>();
    QRCodeConstants qrCodeConstants;
    // for the qrcode visual representation
    private String visualRepresentation = "";
    /**
     * Generates the qrCodeVisual
     * @param hashValue The hash value of the qrCode to be turned into a visualization
     */
    public QRCodeVisual(String hashValue) {
        super(hashValue);
        qrCodeConstants = new QRCodeConstants(hashValue);
        qrCodeVisual.put("roundFace", ". _____ .\n" + " /  _  \\ \n" + " \\_____/ \n");
        qrCodeVisual.put("squareFace", ". _____ .\n" + " |  _  | \n" + " |_____| \n");
        qrCodeVisual.put("bigBody", "  |   |  \n" + "  |   |  \n" + "  |___|  \n");
        qrCodeVisual.put("smallBody", "    |   \n" + "     |    \n" + "    |    \n");
        qrCodeVisual.put("legs", "   / \\  \n" + "  /    \\  \n");
        qrCodeVisual.put("noLegs", "     \\   \n" + "      &--   \n");
        qrCodeVisual.put("ears", "@");
        qrCodeVisual.put("noEars", "");
        qrCodeVisual.put("happyEyes", "^");
        qrCodeVisual.put("horizontalEyes", "-");
        qrCodeVisual.put("rightArm", "/");
        qrCodeVisual.put("leftArm", "\\");
        qrCodeVisual.put("No_arms", "");

        this.bitValues = qrCodeConstants.getBitValues();
        this.bitValues.toCharArray();
        generateVisuals();
    }

    /**
     * Transforms the string into a visual representation based on the hashValue
     */
    public void generateVisuals() {
        // bit 0 head
        if (bitValues.charAt(0) == '0') {
            visualRepresentation += qrCodeVisual.get("roundFace");
        } else {
            visualRepresentation += qrCodeVisual.get("squareFace");
        }
        // bit 3 ears
        if (bitValues.charAt(1) == '0') {
            visualRepresentation = visualRepresentation.substring(0, 10) + qrCodeVisual.get("ears") +
                    visualRepresentation.substring(11, 18) + qrCodeVisual.get("ears") + visualRepresentation.substring(19);
        }

        // bit 4 eyes
        if (bitValues.charAt(2) == '0') {
            visualRepresentation = visualRepresentation.substring(0, 12) + qrCodeVisual.get("horizontalEyes") +
                    visualRepresentation.substring(12, 17) + qrCodeVisual.get("horizontalEyes") + visualRepresentation.substring(17);
        } else {
            visualRepresentation = visualRepresentation.substring(0, 12) + qrCodeVisual.get("happyEyes") +
                    visualRepresentation.substring(12, 17) + qrCodeVisual.get("happyEyes") + visualRepresentation.substring(17);

        }

        // bit 5 arm
        if (bitValues.charAt(3) == '0') {
            // bit 1 body
            if (bitValues.charAt(4) == '0') {
                visualRepresentation += qrCodeVisual.get("bigBody").substring(0, 1) + qrCodeVisual.get("rightArm") +
                        qrCodeVisual.get("bigBody").substring(2, 7) + qrCodeVisual.get("leftArm") + qrCodeVisual.get("bigBody").substring(8);
            } else {
                visualRepresentation += qrCodeVisual.get("smallBody").substring(0, 3) + qrCodeVisual.get("rightArm") +
                        qrCodeVisual.get("smallBody").substring(4, 5) + qrCodeVisual.get("leftArm") + qrCodeVisual.get("smallBody").substring(6, 11) +
                        qrCodeVisual.get("rightArm") + qrCodeVisual.get("smallBody").substring(12, 17) + qrCodeVisual.get("leftArm") + qrCodeVisual.get("smallBody").substring(18);
            }
        } else {
            // bit 1 body
            if (bitValues.charAt(4) == '0') {
                visualRepresentation += qrCodeVisual.get("bigBody");
            } else {
                visualRepresentation += qrCodeVisual.get("smallBody");
            }
        }

        // bit 2 legs
        if (bitValues.charAt(5) == '0') {
            visualRepresentation += qrCodeVisual.get("legs");
        } else {
            visualRepresentation += qrCodeVisual.get("noLegs");
        }
    }

    /**
     * Get the generated visual representation
     *
     * @return A string of visual representation based on the hashValue
     */
    public String getVisualRepresentation() {
        return this.visualRepresentation;
    }
}