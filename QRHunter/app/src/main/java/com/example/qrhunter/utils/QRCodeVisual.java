package com.example.qrhunter.utils;

public class QRCodeVisual {
    /**
     * generates the visual representation of the qrCode
     */
    private String hashValue;
    private String bitValues;

    // for the qrcode visual representation
    private String visualRepresentation = "";

    private String roundFace = ". _____ .\n" + " /  _  \\ \n" + " \\_____/ \n";
    private String squareFace = ". _____ .\n" + " |  _  | \n" + " |_____| \n" ;

    private String bigBody = "  |   |  \n" + "  |   |  \n" + "  |___|  \n";
    private String smallBody = "    |   \n" + "     |     \n" + "    |    \n";

    private String legs = "   / \\  \n" + "  /    \\  \n";
    private String noLegs = "     \\  \n" + "     &-- \n";

    private String ears = "@";
    private String noEars = "";

    private String happyEyes = "^";
    private String horizontalEyes = "-";

    private String rightArm = "/";
    private String leftArm= "\\";
    private String No_arms = "";

    /**
     * Constructor for new QRCodeVisual
     */
    // constructor
    public QRCodeVisual(String hashValue){

        this.hashValue = hashValue;
        strToBinary(this.hashValue);
        this.bitValues.toCharArray();
        qrVisual();
    }

    /**
     * turns the string into binary values
     */
    private void strToBinary(String s)
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

        bitValues = binary.toString();
    }

    /**
     * transforms the string into a visual representation based on the hashValue
     */
    public void qrVisual(){
        // bit 0 head
        if(bitValues.charAt(0) == '0'){
            visualRepresentation += roundFace;
        }
        else{
            visualRepresentation += squareFace;
        }
        // bit 3 ears
        if(bitValues.charAt(1) == '0'){
            visualRepresentation = visualRepresentation.substring(0, 10) + ears +
                    visualRepresentation.substring(11, 18) + ears + visualRepresentation.substring(19);
        }

        // bit 4 eyes
        if(bitValues.charAt(2) == '0'){
            visualRepresentation = visualRepresentation.substring(0, 12) + horizontalEyes +
                    visualRepresentation.substring(13, 16) + horizontalEyes + visualRepresentation.substring(17);
        }
        else{
            visualRepresentation = visualRepresentation.substring(0, 12) + happyEyes +
                    visualRepresentation.substring(13, 16) + happyEyes + visualRepresentation.substring(17);

        }

        // bit 5 arm
        if(bitValues.charAt(5) == '0'){
            // bit 1 body
            if (bitValues.charAt(1) == '0') {
                visualRepresentation += bigBody.substring(0, 1) + rightArm +
                        bigBody.substring(2, 7) + leftArm + bigBody.substring(8);
            }
            else {
                visualRepresentation += smallBody.substring(0, 3) + rightArm +
                        smallBody.substring(4, 5) + leftArm + smallBody.substring(6, 11) +
                        rightArm+ smallBody.substring(12, 17)+ leftArm + smallBody.substring(18);
            }
        }
        else{
            // bit 1 body
            if(bitValues.charAt(1) == '0'){
                visualRepresentation += bigBody;
            }
            else{
                visualRepresentation += smallBody;
            }
        }

        // bit 2 legs
        if(bitValues.charAt(2) == '0'){
            visualRepresentation += legs;
        }
        else{
            visualRepresentation += noLegs;
        }
    }

    /**
     * returns a string of visual representation based on the hashValue
     */
    public String getVisualRepresentation(){
        return this.visualRepresentation;
    }
}
