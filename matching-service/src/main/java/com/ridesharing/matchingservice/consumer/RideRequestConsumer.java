package com.ridesharing.matchingservice.consumer;

import com.ridesharing.corecontracts.RideRequestedEvent;
import com.ridesharing.matchingservice.service.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RideRequestConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RideRequestConsumer.class);

    private final MatchingService matchingService;

    public RideRequestConsumer(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @KafkaListener(
            topics = "ride-requested",
            groupId = "matching-service-group",
            properties = {"spring.json.value.default.type=com.ridesharing.corecontracts.RideRequestedEvent"}
    )
    public void handleRideRequested(RideRequestedEvent event) {
        logger.info("Received RideRequestedEvent: rideId={}, riderId={}", event.getRideId(), event.getRiderId());
        
        try {
            matchingService.processRideRequest(event);
        } catch (Exception e) {
            logger.error("Error processing ride request {}: {}", event.getRideId(), e.getMessage(), e);
        }
    }
}
