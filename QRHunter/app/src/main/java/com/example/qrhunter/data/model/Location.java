package com.example.qrhunter.data.model;

/**
 * A class representing a location
 * The Location class represents a geographic location in terms of its latitude and longitude.
 * This class provides methods to retrieve the latitude and longitude of the location.
 */
public class Location {
    public double latitude;
    public double longitude;

    /**
     * Creates a new Location object with the specified latitude and longitude.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }

    /**
     * Returns the latitude of the location.
     *
     * @return The latitude value of the location.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of the location.
     *
     * @return The longitude value of the location.
     */
    public double getLongitude() {
        return longitude;
    }

}
