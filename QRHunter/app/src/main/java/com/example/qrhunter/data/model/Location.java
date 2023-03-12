package com.example.qrhunter.data.model;

import java.util.ArrayList;

/**
 * A class representing a location
 */
public class Location {
    public double latitude;
    public double longitude;

    /**
     * This is an array in case we want to add more than one photo in the future
     */
    public ArrayList<String> photos;

    public Location(double latitude, double longitude, ArrayList<String> photos) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.photos = photos;
    }
}
