package com.ridesharing.rideservice.kafka;

import com.ridesharing.corecontracts.RideRequestedEvent;
import com.ridesharing.rideservice.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(RideEventProducer.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RideEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRideRequested(RideRequestedEvent event) {
        logger.info("Publishing RideRequestedEvent: rideId={}", event.getRideId());
        
        try {
            kafkaTemplate.send(KafkaConfig.RIDE_REQUESTED_TOPIC, event.getRideId(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.error("Failed to send RideRequestedEvent for rideId={}: {}", 
                                        event.getRideId(), ex.getMessage());
                        } else {
                            logger.info("Successfully sent RideRequestedEvent for rideId={} to partition {}", 
                                        event.getRideId(), result.getRecordMetadata().partition());
                        }
                    });
        } catch (Exception e) {
            logger.error("Error publishing RideRequestedEvent: {}", e.getMessage());
            // Don't throw - let the ride creation succeed even if Kafka fails
        }
    }
}
