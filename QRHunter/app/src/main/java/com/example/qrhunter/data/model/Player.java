package com.example.qrhunter.data.model;

import java.util.ArrayList;

/**
 * Model class for a player
 * TODO: Update the attributes as needed add validation to the setters
 */
public class Player {
    /**
     * The unique id of a player. It is generated using the device id.
     * It is also the document id for the collection "players" in Firestore
     */
    private final String id;
    // The rest of the attributes are public
    private String username;
    private String phoneNumber;
    private Integer rank;
    private Integer totalScore;
    private ArrayList<QRCode> scannedQRCodes;

    /**
     * Constructor for new Players
     */
    public Player(String id, String username) {
        this.id = id;
        this.username = username;
        this.totalScore = 0;
    }

    /**
     * Constructors for Players that already exist in Firestore
     */
    public Player(String id, String username, String phoneNumber, Integer rank, Integer totalScore) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.rank = rank;
        this.totalScore = 0;
    }

    public ArrayList<QRCode> getScannedQRCodes() {
        return scannedQRCodes;
    }

    public void setScannedQRCodes(ArrayList<QRCode> scannedQRCodes) {
        this.scannedQRCodes = scannedQRCodes;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
