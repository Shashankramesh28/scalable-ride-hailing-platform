package com.ridesharing.driverservice.dto;

import com.ridesharing.driverservice.model.Driver;

public class DriverProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String vehicleNumber;
    private Driver.VehicleType vehicleType;
    private boolean isAvailable;

    public DriverProfileResponse(Long id, String name, String email, String phoneNumber, 
                                  String vehicleNumber, Driver.VehicleType vehicleType, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.isAvailable = isAvailable;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public Driver.VehicleType getVehicleType() { return vehicleType; }
    public boolean isAvailable() { return isAvailable; }
}
