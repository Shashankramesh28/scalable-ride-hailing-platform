package com.ridesharing.rideservice.repository;

import com.ridesharing.rideservice.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, String> {
    
    List<Ride> findByRiderIdOrderByRequestedAtDesc(Long riderId);
    
    List<Ride> findByDriverIdOrderByRequestedAtDesc(Long driverId);
    
    List<Ride> findByStatus(Ride.RideStatus status);
}
