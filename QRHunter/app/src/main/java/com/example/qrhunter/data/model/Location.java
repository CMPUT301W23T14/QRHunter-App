package com.example.qrhunter.data.model;

import java.util.ArrayList;

/**
 * A class representing a location
 */
public class Location {
    public Integer latitude;
    public Integer longitude;

    public ArrayList<String> photos;

    public Location(Integer latitude, Integer longitude, ArrayList<String> photos) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.photos = photos;
    }
}
