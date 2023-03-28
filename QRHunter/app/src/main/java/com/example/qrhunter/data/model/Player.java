package com.example.qrhunter.data.model;

/**
 * Model class for a player
 * The Player class represents a player and stores information such as the player's unique ID,
 * username, phone number, rank, and total score.
 */

public class Player {
    /**
     * The unique id of a player. It is generated using the device id.
     * It is also the document id for the collection "players" in Firestore
     */

    private final String id;
    private String username;
    private String phoneNumber;
    private int rank;
    private double totalScore;

    /**
     * Constructor for new Players
     * @param id The unique ID of the player.
     * @param username The username of the player.
     */
    public Player(String id, String username) {
        this.id = id;
        this.username = username;
        this.totalScore = 0;
        this.rank = 0;
        this.phoneNumber = "";
    }

    /**
     * Constructors for Players that already exist in Firestore
     * @param id The unique ID of the player.
     * @param username The username of the player.
     * @param phoneNumber The phone number of the player.
     * @param rank The rank of the player.
     * @param totalScore The total score of the player.
     */
    public Player(String id, String username, String phoneNumber, int rank, int totalScore) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.rank = rank;
        this.totalScore = totalScore;
    }

    /**
     * Returns the ID of the player.
     *
     * @return The ID of the player.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the username of the player.
     *
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the player.
     *
     * @param username The new username of the player.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the phone number of the player.
     *
     * @return The phone number of the player.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the player.
     *
     * @param phoneNumber The new phone number of the player.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the rank of the player.
     *
     * @return The rank of the player.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the rank of the player.
     *
     * @param rank The new rank of the player.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Returns the total score of the player.
     *
     * @return The total score of the player.
     */
    public double getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the total score of the player.
     *
     * @param totalScore The new total score of the player.
     */
    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }
}
