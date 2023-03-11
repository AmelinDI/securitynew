package com.workshop.securitynew.controllers;

import com.workshop.securitynew.config.JwtUtils;
import com.workshop.securitynew.dao.UserDao;
import com.workshop.securitynew.dto.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final UserDetails userDetails = userDao.findUserByEmail(request.getEmail());
        if (userDetails != null) {
            return ResponseEntity.ok(generateTokenInJson(userDetails));
        }

        return ResponseEntity.status(400).body("Some error has occurred");
    }

    private String generateTokenInJson(UserDetails userDetails) {
        return "{\"token\":\"" + jwtUtils.generateToken(userDetails) + "\"}";
    }
}
