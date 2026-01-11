package com.ridesharing.corecontracts;

import java.io.Serializable;

/**
 * Event published when a driver has been matched to a ride request.
 */
public class DriverMatchedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rideId;
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String vehicleNumber;
    private int estimatedArrivalMinutes;

    public DriverMatchedEvent() {
    }

    public DriverMatchedEvent(String rideId, Long driverId, String driverName,
            String driverPhone, String vehicleNumber, int estimatedArrivalMinutes) {
        this.rideId = rideId;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.vehicleNumber = vehicleNumber;
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }

    // Getters and Setters
    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getEstimatedArrivalMinutes() {
        return estimatedArrivalMinutes;
    }

    public void setEstimatedArrivalMinutes(int estimatedArrivalMinutes) {
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }

    @Override
    public String toString() {
        return "DriverMatchedEvent{" +
                "rideId='" + rideId + '\'' +
                ", driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", estimatedArrivalMinutes=" + estimatedArrivalMinutes +
                '}';
    }
}
