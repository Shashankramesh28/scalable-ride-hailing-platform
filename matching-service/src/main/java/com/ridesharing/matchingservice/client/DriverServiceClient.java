package com.ridesharing.matchingservice.client;

import com.ridesharing.matchingservice.dto.NearbyDriverDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class DriverServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(DriverServiceClient.class);

    private final WebClient driverServiceWebClient;

    @Value("${matching.search.radius.km}")
    private double searchRadiusKm;

    public DriverServiceClient(WebClient driverServiceWebClient) {
        this.driverServiceWebClient = driverServiceWebClient;
    }

    public List<NearbyDriverDto> findNearbyDrivers(double latitude, double longitude) {
        try {
            List<NearbyDriverDto> drivers = driverServiceWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/drivers/nearby")
                            .queryParam("lat", latitude)
                            .queryParam("lng", longitude)
                            .queryParam("radiusKm", searchRadiusKm)
                            .build())
                    .retrieve()
                    .bodyToFlux(NearbyDriverDto.class)
                    .collectList()
                    .block();
            
            logger.info("Found {} nearby drivers at ({}, {})", 
                       drivers != null ? drivers.size() : 0, latitude, longitude);
            return drivers != null ? drivers : Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error calling driver-service: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
