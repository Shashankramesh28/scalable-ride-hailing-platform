package com.ridesharing.driverservice.security;

import com.ridesharing.driverservice.repository.DriverRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DriverRepository driverRepository;

    public CustomUserDetailsService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return driverRepository.findByEmail(email)
                .map(driver -> new org.springframework.security.core.userdetails.User(
                        driver.getEmail(),
                        driver.getPassword(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found with email: " + email));
    }
}
