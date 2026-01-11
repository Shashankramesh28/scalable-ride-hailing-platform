package com.ridesharing.rideservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String RIDE_REQUESTED_TOPIC = "ride-requested";
    public static final String DRIVER_MATCHED_TOPIC = "driver-matched";
    public static final String NO_DRIVER_TOPIC = "no-driver-available";

    @Bean
    public NewTopic rideRequestedTopic() {
        return TopicBuilder.name(RIDE_REQUESTED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic driverMatchedTopic() {
        return TopicBuilder.name(DRIVER_MATCHED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic noDriverTopic() {
        return TopicBuilder.name(NO_DRIVER_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
