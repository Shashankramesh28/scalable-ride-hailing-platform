package com.ridesharing.driverservice.service;

import com.ridesharing.driverservice.dto.*;
import com.ridesharing.driverservice.model.Driver;
import com.ridesharing.driverservice.repository.DriverRepository;
import com.ridesharing.driverservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LocationService locationService;

    public DriverService(
            DriverRepository driverRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            LocationService locationService
    ) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.locationService = locationService;
    }

    public DriverAuthResponse registerDriver(DriverRegisterRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setEmail(request.getEmail());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setVehicleNumber(request.getVehicleNumber());
        driver.setVehicleType(request.getVehicleType());
        driver.setAvailable(false);

        driverRepository.save(driver);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                driver.getEmail(),
                driver.getPassword(),
                new ArrayList<>()
        );
        String token = jwtService.generateToken(userDetails);

        return new DriverAuthResponse(token, driver.getEmail(), driver.getName(), driver.getVehicleType());
    }

    public DriverAuthResponse loginDriver(DriverLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Driver driver = driverRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                driver.getEmail(),
                driver.getPassword(),
                new ArrayList<>()
        );
        String token = jwtService.generateToken(userDetails);

        return new DriverAuthResponse(token, driver.getEmail(), driver.getName(), driver.getVehicleType());
    }

    public DriverProfileResponse getDriverProfile(String email) {
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return new DriverProfileResponse(
                driver.getId(),
                driver.getName(),
                driver.getEmail(),
                driver.getPhoneNumber(),
                driver.getVehicleNumber(),
                driver.getVehicleType(),
                driver.isAvailable()
        );
    }

    public void updateLocation(String email, double latitude, double longitude) {
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        locationService.updateDriverLocation(driver.getId(), latitude, longitude);
    }

    public void updateAvailability(String email, boolean available) {
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setAvailable(available);
        driverRepository.save(driver);

        // Remove from location tracking when going offline
        if (!available) {
            locationService.removeDriverLocation(driver.getId());
        }
    }

    public List<NearbyDriverResponse> findNearbyDrivers(double latitude, double longitude, double radiusKm) {
        List<LocationService.DriverLocationResult> nearbyLocations = 
            locationService.findNearbyDrivers(latitude, longitude, radiusKm);

        return nearbyLocations.stream()
            .map(loc -> {
                Driver driver = driverRepository.findById(loc.getDriverId()).orElse(null);
                if (driver != null && driver.isAvailable()) {
                    return new NearbyDriverResponse(
                        driver.getId(),
                        driver.getName(),
                        loc.getLatitude(),
                        loc.getLongitude(),
                        Math.round(loc.getDistanceKm() * 100.0) / 100.0  // Round to 2 decimal places
                    );
                }
                return null;
            })
            .filter(response -> response != null)
            .collect(Collectors.toList());
    }
}
