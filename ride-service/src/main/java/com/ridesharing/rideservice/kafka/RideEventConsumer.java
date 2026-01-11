package com.ridesharing.rideservice.kafka;

import com.ridesharing.corecontracts.DriverMatchedEvent;
import com.ridesharing.rideservice.config.KafkaConfig;
import com.ridesharing.rideservice.model.Ride;
import com.ridesharing.rideservice.repository.RideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RideEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RideEventConsumer.class);
    
    private final RideRepository rideRepository;

    public RideEventConsumer(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @KafkaListener(topics = KafkaConfig.DRIVER_MATCHED_TOPIC, groupId = "ride-service-group",
                   properties = {"spring.json.value.default.type=com.ridesharing.corecontracts.DriverMatchedEvent"})
    public void handleDriverMatched(DriverMatchedEvent event) {
        logger.info("Received DriverMatchedEvent: rideId={}, driverId={}", event.getRideId(), event.getDriverId());
        
        rideRepository.findById(event.getRideId()).ifPresent(ride -> {
            ride.setDriverId(event.getDriverId());
            ride.setStatus(Ride.RideStatus.MATCHED);
            ride.setMatchedAt(LocalDateTime.now());
            rideRepository.save(ride);
            logger.info("Ride {} updated with driver {}", event.getRideId(), event.getDriverId());
        });
    }
}
