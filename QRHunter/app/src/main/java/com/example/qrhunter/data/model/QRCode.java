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
    private Integer score;
    private Location location;
    private ArrayList<Comment> comments;

    public QRCode(String hash, Location location, ArrayList<Comment> comments) {
        this.hash = hash;

        this.name = QRCodeUtil.generateName(hash);
        this.visualRepresentation = QRCodeUtil.generateVisualRepresentation(hash);
        this.score = QRCodeUtil.generateScore(hash);

        this.location = location;
        this.comments = comments;
    }
}

