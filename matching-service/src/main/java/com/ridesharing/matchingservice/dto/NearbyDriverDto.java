package com.ridesharing.matchingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyDriverDto {

    private Long driverId;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    @Override
    public String toString() {
        return "NearbyDriverDto{driverId=" + driverId + ", name='" + name + "', distanceKm=" + distanceKm + "}";
    }
}
