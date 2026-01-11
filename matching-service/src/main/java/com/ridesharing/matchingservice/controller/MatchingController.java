package com.ridesharing.matchingservice.controller;

import com.ridesharing.corecontracts.DriverMatchedEvent;
import com.ridesharing.corecontracts.RideRequestedEvent;
import com.ridesharing.matchingservice.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /**
     * Synchronous endpoint for testing matching without Kafka.
     */
    @PostMapping("/trigger")
    public ResponseEntity<?> triggerMatching(@RequestBody RideRequestedEvent event) {
        DriverMatchedEvent result = matchingService.processRideRequest(event);
        if (result != null) {
            return ResponseEntity.ok(Map.of(
                "message", "Driver matched successfully",
                "rideId", result.getRideId(),
                "driverId", result.getDriverId(),
                "driverName", result.getDriverName(),
                "estimatedArrivalMinutes", result.getEstimatedArrivalMinutes()
            ));
        } else {
            return ResponseEntity.ok(Map.of("message", "No drivers available"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
