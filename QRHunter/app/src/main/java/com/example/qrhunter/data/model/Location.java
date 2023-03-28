package com.example.qrhunter.data.model;

/**
 * A class representing a location.
 * The Location class represents a geographic location in terms of its latitude and longitude.
 *  This class provides methods to retrieve the latitude and longitude of the location.
 */
public class Location {
    // The latitude of this location.
    public double latitude;
    // The longitude of this location.
    public double longitude;

    /**
     * Constructs a new Location object with the given latitude and longitude, and a list of photos.
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @param photos photos that are associated with this location
     */
    public Location(double latitude, double longitude, ArrayList<String> photos) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
