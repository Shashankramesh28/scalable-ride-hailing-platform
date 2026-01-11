package com.ridesharing.rideservice.service;

import com.ridesharing.corecontracts.RideRequestedEvent;
import com.ridesharing.rideservice.dto.RideRequestDto;
import com.ridesharing.rideservice.dto.RideResponseDto;
import com.ridesharing.rideservice.kafka.RideEventProducer;
import com.ridesharing.rideservice.model.Ride;
import com.ridesharing.rideservice.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final RideEventProducer eventProducer;

    public RideService(RideRepository rideRepository, RideEventProducer eventProducer) {
        this.rideRepository = rideRepository;
        this.eventProducer = eventProducer;
    }

    public RideResponseDto requestRide(Long riderId, RideRequestDto request) {
        // Create ride
        Ride ride = new Ride();
        ride.setRiderId(riderId);
        ride.setPickupLatitude(request.getPickupLat());
        ride.setPickupLongitude(request.getPickupLng());
        ride.setDestinationLatitude(request.getDestLat());
        ride.setDestinationLongitude(request.getDestLng());
        ride.setStatus(Ride.RideStatus.REQUESTED);
        
        ride = rideRepository.save(ride);

        // Publish event to Kafka for matching service
        RideRequestedEvent event = new RideRequestedEvent(
            ride.getId(),
            riderId.toString(),
            new RideRequestedEvent.LocationInfo(request.getPickupLat(), request.getPickupLng()),
            new RideRequestedEvent.LocationInfo(request.getDestLat(), request.getDestLng())
        );
        eventProducer.publishRideRequested(event);

        return RideResponseDto.fromRide(ride);
    }

    public RideResponseDto getRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        return RideResponseDto.fromRide(ride);
    }

    public List<RideResponseDto> getRiderHistory(Long riderId) {
        return rideRepository.findByRiderIdOrderByRequestedAtDesc(riderId)
                .stream()
                .map(RideResponseDto::fromRide)
                .collect(Collectors.toList());
    }

    public RideResponseDto cancelRide(String rideId, Long riderId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        
        if (!ride.getRiderId().equals(riderId)) {
            throw new RuntimeException("Not authorized to cancel this ride");
        }
        
        if (ride.getStatus() == Ride.RideStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot cancel ride in progress");
        }
        
        ride.setStatus(Ride.RideStatus.CANCELLED);
        rideRepository.save(ride);
        
        return RideResponseDto.fromRide(ride);
    }
}
