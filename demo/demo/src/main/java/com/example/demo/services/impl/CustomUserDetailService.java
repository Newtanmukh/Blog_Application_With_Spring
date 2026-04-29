package com.example.demo.services.impl;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.UserRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws ResourceNotFoundException {

        return userRepo.findFirstByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }
}
