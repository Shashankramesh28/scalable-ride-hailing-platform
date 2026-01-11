package com.ridesharing.rideservice.controller;

import com.ridesharing.rideservice.dto.RideRequestDto;
import com.ridesharing.rideservice.dto.RideResponseDto;
import com.ridesharing.rideservice.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/request")
    public ResponseEntity<RideResponseDto> requestRide(
            Authentication authentication,
            @Valid @RequestBody RideRequestDto request
    ) {
        // Extract rider ID from token (email is stored in principal, we'd need to look up the user)
        // For simplicity, we'll extract user ID from the token subject
        String email = authentication.getName();
        // In production, you'd call user-service to get the ID. For now, hash the email.
        Long riderId = (long) email.hashCode();
        
        RideResponseDto ride = rideService.requestRide(riderId, request);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideResponseDto> getRide(@PathVariable String rideId) {
        RideResponseDto ride = rideService.getRide(rideId);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/history")
    public ResponseEntity<List<RideResponseDto>> getRideHistory(Authentication authentication) {
        String email = authentication.getName();
        Long riderId = (long) email.hashCode();
        List<RideResponseDto> history = rideService.getRiderHistory(riderId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<RideResponseDto> cancelRide(
            Authentication authentication,
            @PathVariable String rideId
    ) {
        String email = authentication.getName();
        Long riderId = (long) email.hashCode();
        RideResponseDto ride = rideService.cancelRide(rideId, riderId);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
