package com.ridesharing.driverservice.dto;

public class NearbyDriverResponse {

    private Long driverId;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;

    public NearbyDriverResponse(Long driverId, String name, Double latitude, Double longitude, Double distanceKm) {
        this.driverId = driverId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
    }

    public Long getDriverId() { return driverId; }
    public String getName() { return name; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getDistanceKm() { return distanceKm; }
}
