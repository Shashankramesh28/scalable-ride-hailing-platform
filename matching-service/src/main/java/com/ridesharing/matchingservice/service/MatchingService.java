package com.ridesharing.matchingservice.service;

import com.ridesharing.corecontracts.DriverMatchedEvent;
import com.ridesharing.corecontracts.NoDriverAvailableEvent;
import com.ridesharing.corecontracts.RideRequestedEvent;
import com.ridesharing.matchingservice.client.DriverServiceClient;
import com.ridesharing.matchingservice.dto.NearbyDriverDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingService.class);
    
    private static final String DRIVER_MATCHED_TOPIC = "driver-matched";
    private static final String NO_DRIVER_TOPIC = "no-driver-available";

    private final DriverServiceClient driverServiceClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Store the last matched result for testing
    private DriverMatchedEvent lastMatchedEvent;

    public MatchingService(DriverServiceClient driverServiceClient, KafkaTemplate<String, Object> kafkaTemplate) {
        this.driverServiceClient = driverServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public DriverMatchedEvent processRideRequest(RideRequestedEvent event) {
        logger.info("Processing ride request: rideId={}", event.getRideId());

        // Find nearby drivers at pickup location
        List<NearbyDriverDto> nearbyDrivers = driverServiceClient.findNearbyDrivers(
                event.getPickupLocation().getLatitude(),
                event.getPickupLocation().getLongitude()
        );

        if (nearbyDrivers.isEmpty()) {
            logger.warn("No drivers available for ride {}", event.getRideId());
            publishNoDriverAvailable(event.getRideId(), "No drivers available in your area");
            return null;
        }

        // Select the closest driver (first in the sorted list)
        NearbyDriverDto selectedDriver = nearbyDrivers.get(0);
        logger.info("Selected driver {} for ride {}", selectedDriver.getDriverId(), event.getRideId());

        // Calculate estimated arrival (rough estimate: 2 min per km)
        int estimatedArrival = Math.max(1, (int) Math.ceil(selectedDriver.getDistanceKm() * 2));

        // Publish driver matched event
        DriverMatchedEvent matchedEvent = new DriverMatchedEvent(
                event.getRideId(),
                selectedDriver.getDriverId(),
                selectedDriver.getName(),
                null,  // Phone would come from a full driver lookup
                null,  // Vehicle number would come from a full driver lookup
                estimatedArrival
        );

        this.lastMatchedEvent = matchedEvent;

        // Try to publish to Kafka, but don't fail if it doesn't work
        try {
            kafkaTemplate.send(DRIVER_MATCHED_TOPIC, event.getRideId(), matchedEvent)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.error("Failed to send DriverMatchedEvent: {}", ex.getMessage());
                        } else {
                            logger.info("Published DriverMatchedEvent to Kafka for ride {}", event.getRideId());
                        }
                    });
        } catch (Exception e) {
            logger.error("Kafka unavailable, event not published: {}", e.getMessage());
        }

        logger.info("Matched driver {} to ride {}, ETA={}min",
                   selectedDriver.getDriverId(), event.getRideId(), estimatedArrival);
        
        return matchedEvent;
    }

    private void publishNoDriverAvailable(String rideId, String reason) {
        try {
            NoDriverAvailableEvent event = new NoDriverAvailableEvent(rideId, reason);
            kafkaTemplate.send(NO_DRIVER_TOPIC, rideId, event);
        } catch (Exception e) {
            logger.error("Failed to publish NoDriverAvailableEvent: {}", e.getMessage());
        }
    }

    public DriverMatchedEvent getLastMatchedEvent() {
        return lastMatchedEvent;
    }
}
