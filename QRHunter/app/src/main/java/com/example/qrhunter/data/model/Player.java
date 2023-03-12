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
    private int rank;
    private int totalScore;
    private ArrayList<String> scannedQRCodeIds;

    /**
     * Constructor for new Players
     */
    public Player(String id, String username) {
        this.id = id;
        this.username = username;
        this.totalScore = 0;
        this.rank = 0;
        this.phoneNumber = "";
        this.scannedQRCodeIds = new ArrayList<>();
    }

    /**
     * Constructors for Players that already exist in Firestore
     */
    public Player(String id, String username, String phoneNumber, int rank, int totalScore, ArrayList<String> setScannedQRCodeIds) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.rank = rank;
        this.totalScore = totalScore;
        this.scannedQRCodeIds = setScannedQRCodeIds;
    }

    public ArrayList<String> getScannedQRCodeIds() {
        return scannedQRCodeIds;
    }

    public void setScannedQRCodeIds(ArrayList<String> scannedQRCodeIds) {
        this.scannedQRCodeIds = scannedQRCodeIds;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
