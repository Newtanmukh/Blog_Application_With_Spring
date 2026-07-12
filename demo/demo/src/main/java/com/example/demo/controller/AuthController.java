package com.example.demo.controller;

import com.example.demo.security.JwtAuthRequest;
import com.example.demo.security.JwtAuthResponse;
import com.example.demo.security.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JwtTokenHelper jwtTokenHelper,
                          UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager){
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest) throws Exception {
        authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
        String generatedToken = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(generatedToken);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException e){
            throw new Exception("Username is disabled.");
        }
    }

}
