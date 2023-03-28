package com.example.qrhunter.utils;

/**
 * A class which creates a name for QR codes using their hash value.
 */
public class QRCodeName extends QRCodeVisualize{
    /**
     * generates the name for qrcode
     */
    private final String bitValues;

    private String qrCodeName = "";
    QRCodeVisualize qrCodeVisualize;

    /**
     * Constructor for new QRCodeName
     */
    public QRCodeName(String hashValue){
        super(hashValue);
        qrCodeVisualize = new QRCodeVisualize(hashValue);
        this.bitValues = qrCodeVisualize.getBitValues();
        createQRName();
    }

    /**
     * generate a name based on the hashValue
     */
    public void createQRName(){

        // bit 0 head
        if(bitValues.charAt(0) == '0'){
            qrCodeName+= "Ice";
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
            qrCodeName+= "Huge";
        }
        // bit 4 head
        if(bitValues.charAt(4) == '0'){
            qrCodeName+= "Fusc";
        }
        else{
            qrCodeName+= "Amok";
        }
        // bit 5 head
        if(bitValues.charAt(5) == '0'){
            qrCodeName+= "Human";
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