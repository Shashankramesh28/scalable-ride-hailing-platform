package com.ridesharing.driverservice.controller;

import com.ridesharing.driverservice.dto.*;
import com.ridesharing.driverservice.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/register")
    public ResponseEntity<DriverAuthResponse> registerDriver(@Valid @RequestBody DriverRegisterRequest request) {
        DriverAuthResponse response = driverService.registerDriver(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<DriverAuthResponse> loginDriver(@Valid @RequestBody DriverLoginRequest request) {
        DriverAuthResponse response = driverService.loginDriver(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<DriverProfileResponse> getCurrentDriver(Authentication authentication) {
        String email = authentication.getName();
        DriverProfileResponse profile = driverService.getDriverProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/location")
    public ResponseEntity<Map<String, String>> updateLocation(
            Authentication authentication,
            @Valid @RequestBody LocationUpdateRequest request
    ) {
        String email = authentication.getName();
        driverService.updateLocation(email, request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(Map.of("message", "Location updated successfully"));
    }

    @PutMapping("/availability")
    public ResponseEntity<Map<String, String>> updateAvailability(
            Authentication authentication,
            @RequestParam boolean available
    ) {
        String email = authentication.getName();
        driverService.updateAvailability(email, available);
        return ResponseEntity.ok(Map.of("message", "Availability updated to: " + available));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyDriverResponse>> getNearbyDrivers(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radiusKm
    ) {
        List<NearbyDriverResponse> nearbyDrivers = driverService.findNearbyDrivers(lat, lng, radiusKm);
        return ResponseEntity.ok(nearbyDrivers);
    }
}
