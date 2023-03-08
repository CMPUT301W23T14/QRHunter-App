package com.example.qrhunter.data.model;

import java.util.ArrayList;

/**
 * A class representing a location
 */
public class Location {
    public int latitude;
    public int longitude;

    public ArrayList<String> photos;

    public Location(int latitude, int longitude, ArrayList<String> photos) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.photos = photos;
    }
}
