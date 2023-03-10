package com.example.qrhunter.data.model;

import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;

/**
 * Model class for a QRCode
 * TODO: Update the attributes as needed
 */
public class QRCode {
    /**
     * The hash calculated from the QR Code/Bar contents
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
    private ArrayList<Comment> comments;

    /**
     * Constructor for a new QR Code
     */
    public QRCode(String hash, Location location, ArrayList<Comment> comments) {
        this.hash = hash;

        this.name = QRCodeUtil.generateName(hash);
        this.visualRepresentation = QRCodeUtil.generateVisualRepresentation(hash);
        this.score = QRCodeUtil.generateScore(hash);

        this.location = location;
        this.comments = comments;
    }

    /**
     * Constructor for an existing QR Code in Firestore
     */
    public QRCode(String hash, Location location, String name, double score, String visualRepresentation, ArrayList<Comment> comments) {
        this.hash = hash;

        this.name = name;
        this.visualRepresentation = visualRepresentation;
        this.score = score;

        this.location = location;
        this.comments = comments;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}

