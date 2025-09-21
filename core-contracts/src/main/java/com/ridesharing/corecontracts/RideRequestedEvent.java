package com.ridesharing.corecontracts;

import java.io.Serializable;

/**
 * Represents an event published when a rider requests a new ride.
 * This class would be in the 'core-contracts' shared module.
 */
public class RideRequestedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rideId;
    private String riderId;
    private LocationInfo pickupLocation;
    private LocationInfo destination;

    // Constructors, Getters, Setters, etc.

    public RideRequestedEvent() {}

    public RideRequestedEvent(String rideId, String riderId, LocationInfo pickupLocation, LocationInfo destination) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public LocationInfo getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationInfo pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationInfo getDestination() {
        return destination;
    }

    public void setDestination(LocationInfo destination) {
        this.destination = destination;
    }

    /**
     * A simple inner class to represent a geographical location.
     */
    public static class LocationInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private double latitude;
        private double longitude;

        public LocationInfo() {}

        public LocationInfo(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}

