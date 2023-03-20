package com.example.qrhunter.data.model;

import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;

/**
 * Model class for a QRCode
 */
public class QRCode {
    /**
     * The id of the QRCode document. This is generated automatically by Firestore
     */
    private String id;
    /**
     * The hash calculated from the QR Code/Bar contents.
     * Note: The hash is not the id of the document in Firestore. The id will be randomly generated by Firestore instead.
     */
    private String hash;
    /**
     * The name generated from the hash
     */
    private String name;
    /**
     * The visual representation generated from the hash
     */
    private String visualRepresentation;
    /**
     * The score of the QR Code calculated from the hash
     */
    private double score;
    private Location location;
    private ArrayList<String> commentIds = new ArrayList<>();
    /**
     * List of player id who scanned this qr code
     */
    private ArrayList<String> playerIds = new ArrayList<>();

    /**
     * Constructor for a QR Code
     */
    public QRCode(String id, String hash, Location location, ArrayList<String> commentIds, ArrayList<String> playerIds) {
        this.id = id;
        this.hash = hash;

        this.name = QRCodeUtil.generateName(hash);
        this.visualRepresentation = QRCodeUtil.generateVisualRepresentation(hash);
        this.score = QRCodeUtil.generateScore(hash);

        this.location = location;
        this.commentIds = commentIds;
        this.playerIds = playerIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(ArrayList<String> playerIds) {
        this.playerIds = playerIds;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisualRepresentation() {
        return visualRepresentation;
    }

    public void setVisualRepresentation(String visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(ArrayList<String> commentIds) {
        this.commentIds = commentIds;
    }

}

