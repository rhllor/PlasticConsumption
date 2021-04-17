package com.github.rhllor.pc.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.github.rhllor.pc.service.auth.IUserAuthenticationService;

@RestController
public class PublicEndpointsController {

    @Autowired
    private IUserAuthenticationService authenticationService;

    @PostMapping("/login")
    public Object login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        try {
            return authenticationService
                    .login(username, password);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }
}
