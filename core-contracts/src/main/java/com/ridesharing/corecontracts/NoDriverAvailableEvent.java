package com.ridesharing.corecontracts;

import java.io.Serializable;

/**
 * Event published when no driver is available for a ride request.
 */
public class NoDriverAvailableEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rideId;
    private String reason;

    public NoDriverAvailableEvent() {
    }

    public NoDriverAvailableEvent(String rideId, String reason) {
        this.rideId = rideId;
        this.reason = reason;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
