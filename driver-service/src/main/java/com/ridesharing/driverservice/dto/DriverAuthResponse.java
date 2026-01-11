package com.ridesharing.driverservice.dto;

import com.ridesharing.driverservice.model.Driver;

public class DriverAuthResponse {

    private String token;
    private String email;
    private String name;
    private Driver.VehicleType vehicleType;

    public DriverAuthResponse(String token, String email, String name, Driver.VehicleType vehicleType) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.vehicleType = vehicleType;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Driver.VehicleType getVehicleType() { return vehicleType; }
}
