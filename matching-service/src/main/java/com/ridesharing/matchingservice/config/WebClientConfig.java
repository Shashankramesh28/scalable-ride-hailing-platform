package com.ridesharing.matchingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${driver.service.url}")
    private String driverServiceUrl;

    @Bean
    public WebClient driverServiceWebClient() {
        return WebClient.builder()
                .baseUrl(driverServiceUrl)
                .build();
    }
}
