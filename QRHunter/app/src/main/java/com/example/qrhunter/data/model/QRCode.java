package com.example.qrhunter.data.model;

import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;

/**
 * Model class for a QRCode.
 * The QRCode class represents a QR code. It contains information about the code's
 * unique ID, the hash calculated from its contents, a name and visual representation generated from the hash, the
 * score calculated from the hash, the location of the code, and a list of player IDs who have scanned the code.
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
     * Constructor for a QRCode object.
     *
     * @param id The unique ID of the QR code in Firestore.
     * @param hash The hash calculated from the QR code or barcode contents.
     * @param location The location of the QR code.
     * @param commentIds A list of comment IDs associated with the QR code.
     * @param playerIds A list of player IDs who have scanned the QR code.
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
    /**
     * Returns the id of the QR code document.
     * @return the id of the QR code document
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the QR code document.
     * @param id the new id of the QR code document
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the list of player ids who scanned this QR code.
     * @return the list of player ids who scanned this QR code
     */
    public ArrayList<String> getPlayerIds() {
        return playerIds;
    }

    /**
     * Sets the list of player ids who scanned this QR code.
     * @param playerIds the new list of player ids who scanned this QR code
     */
    public void setPlayerIds(ArrayList<String> playerIds) {
        this.playerIds = playerIds;
    }

    /**
     * Returns the hash calculated from the QR code.
     * @return the hash calculated from the QR code.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash calculated from the QR code.
     * @param hash the new hash calculated from the QR code.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Returns the name generated from the hash.
     * @return the name generated from the hash
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name generated from the hash.
     * @param name the new name generated from the hash
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the visual representation generated from the hash.
     * @return the visual representation generated from the hash
     */
    public String getVisualRepresentation() {
        return visualRepresentation;
    }

    /**
     * Sets the visual representation generated from the hash.
     * @param visualRepresentation the new visual representation generated from the hash
     */
    public void setVisualRepresentation(String visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
    }

    /**
     * Returns the score of the QR code calculated from the hash.
     * @return the score of the QR code calculated from the hash
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the score of the QR code calculated from the hash.
     * @param score the new score of the QR code calculated from the hash
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Returns the location of the QR code.
     * @return the location of the QR code
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the QR code.
     * @param location the new location of the QR code
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the list of comment ids associated with this QR code.
     * @return the list of comment ids associated with this QR code
     */
    public ArrayList<String> getCommentIds() {
        return commentIds;
    }

    /**
     * Sets the list of comment ids associated with this QR code.
     * @param commentIds the new list of comment ids associated with this QR code
     */
    public void setCommentIds(ArrayList<String> commentIds) {
        this.commentIds = commentIds;
    }
}

