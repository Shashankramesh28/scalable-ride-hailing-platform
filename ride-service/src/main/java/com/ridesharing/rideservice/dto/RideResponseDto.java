package com.ridesharing.rideservice.dto;

import com.ridesharing.rideservice.model.Ride;
import java.time.LocalDateTime;

public class RideResponseDto {

    private String id;
    private Long riderId;
    private Long driverId;
    private Ride.RideStatus status;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private LocalDateTime requestedAt;
    private LocalDateTime matchedAt;

    public static RideResponseDto fromRide(Ride ride) {
        RideResponseDto dto = new RideResponseDto();
        dto.id = ride.getId();
        dto.riderId = ride.getRiderId();
        dto.driverId = ride.getDriverId();
        dto.status = ride.getStatus();
        dto.pickupLatitude = ride.getPickupLatitude();
        dto.pickupLongitude = ride.getPickupLongitude();
        dto.destinationLatitude = ride.getDestinationLatitude();
        dto.destinationLongitude = ride.getDestinationLongitude();
        dto.requestedAt = ride.getRequestedAt();
        dto.matchedAt = ride.getMatchedAt();
        return dto;
    }

    // Getters
    public String getId() { return id; }
    public Long getRiderId() { return riderId; }
    public Long getDriverId() { return driverId; }
    public Ride.RideStatus getStatus() { return status; }
    public Double getPickupLatitude() { return pickupLatitude; }
    public Double getPickupLongitude() { return pickupLongitude; }
    public Double getDestinationLatitude() { return destinationLatitude; }
    public Double getDestinationLongitude() { return destinationLongitude; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getMatchedAt() { return matchedAt; }
}
