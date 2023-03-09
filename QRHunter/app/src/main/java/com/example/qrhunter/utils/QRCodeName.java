package com.example.qrhunter.utils;

public class QRCodeName {
    /**
     * generates the name for qrcode
     */
    private String hashValue;
    private String bitValues;

    private String qrCodeName;

    /**
     * Constructor for new QRCodeName
     */
    public QRCodeName(String hashValue){
        this.hashValue = hashValue;
        strToBinary(this.hashValue);
        createQRName();
    }

    /**
     * turns the string into binary values for generating the name
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
     * generate a name based on the hashValue
     */
    public void createQRName(){
        qrCodeName = "";

        // bit 0 head
        if(bitValues.charAt(0) == '0'){
            qrCodeName+= "cool";
        }
        else{
            qrCodeName+= "hot";
        }
        // bit 1 head
        if(bitValues.charAt(1) == '0'){
            qrCodeName+= "Fro";
        }
        else{
            qrCodeName+= "Gol";
        }
        // bit 2 head
        if(bitValues.charAt(2) == '0'){
            qrCodeName+= "Mo";
        }
        else{
            qrCodeName+= "Lo";
        }
        // bit 3 head
        if(bitValues.charAt(3) == '0'){
            qrCodeName+= "Mega";
        }
        else{
            qrCodeName+= "Ultra";
        }
        // bit 4 head
        if(bitValues.charAt(4) == '0'){
            qrCodeName+= "Giant";
        }
        else{
            qrCodeName+= "Special";
        }
        // bit 5 head
        if(bitValues.charAt(5) == '0'){
            qrCodeName+= "Person";
        }
        else{
            qrCodeName+= "Snake";
        }
    }

    /**
     * returns the name of the qrcode
     */
    public String getQRName(){
        return this.qrCodeName;
    }
}
