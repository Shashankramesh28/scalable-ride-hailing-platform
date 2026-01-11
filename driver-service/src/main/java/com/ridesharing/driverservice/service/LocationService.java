package com.ridesharing.driverservice.service;

import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private static final String DRIVERS_GEO_KEY = "drivers:locations";
    
    private final RedisTemplate<String, String> redisTemplate;

    public LocationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Update driver's location using Redis GEOADD
     */
    public void updateDriverLocation(Long driverId, double latitude, double longitude) {
        redisTemplate.opsForGeo().add(
            DRIVERS_GEO_KEY,
            new Point(longitude, latitude),  // Note: Redis uses (lng, lat)
            driverId.toString()
        );
    }

    /**
     * Remove driver's location from Redis (when going offline)
     */
    public void removeDriverLocation(Long driverId) {
        redisTemplate.opsForGeo().remove(DRIVERS_GEO_KEY, driverId.toString());
    }

    /**
     * Find nearby drivers using Redis GEORADIUS
     * Returns list of driver IDs with their distances
     */
    public List<DriverLocationResult> findNearbyDrivers(double latitude, double longitude, double radiusKm) {
        Circle searchArea = new Circle(
            new Point(longitude, latitude),  // Note: Redis uses (lng, lat)
            new Distance(radiusKm, Metrics.KILOMETERS)
        );

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
            .newGeoRadiusArgs()
            .includeDistance()
            .includeCoordinates()
            .sortAscending()
            .limit(20);  // Limit to 20 nearest drivers

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = 
            redisTemplate.opsForGeo().radius(DRIVERS_GEO_KEY, searchArea, args);

        if (results == null) {
            return List.of();
        }

        return results.getContent().stream()
            .map(result -> {
                String driverIdStr = result.getContent().getName();
                Point point = result.getContent().getPoint();
                double distance = result.getDistance().getValue();
                
                return new DriverLocationResult(
                    Long.parseLong(driverIdStr),
                    point != null ? point.getY() : 0,  // latitude
                    point != null ? point.getX() : 0,  // longitude
                    distance
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * Get a specific driver's location
     */
    public Point getDriverLocation(Long driverId) {
        List<Point> positions = redisTemplate.opsForGeo().position(DRIVERS_GEO_KEY, driverId.toString());
        if (positions != null && !positions.isEmpty() && positions.get(0) != null) {
            return positions.get(0);
        }
        return null;
    }

    // Inner class to hold location results
    public static class DriverLocationResult {
        private final Long driverId;
        private final double latitude;
        private final double longitude;
        private final double distanceKm;

        public DriverLocationResult(Long driverId, double latitude, double longitude, double distanceKm) {
            this.driverId = driverId;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distanceKm = distanceKm;
        }

        public Long getDriverId() { return driverId; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public double getDistanceKm() { return distanceKm; }
    }
}
