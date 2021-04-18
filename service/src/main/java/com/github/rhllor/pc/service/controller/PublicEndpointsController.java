package com.github.rhllor.pc.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.github.rhllor.pc.service.auth.IUserAuthenticationService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API per la gestione dell'autenticazione.")
public class PublicEndpointsController {

    @Autowired
    private IUserAuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Effettua login utente", description = "Ritorna Token di autenticazione.", tags = { "Authentication" })
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
