package com.github.rhllor.pc.service.auth;

import java.util.UUID;

import com.github.rhllor.pc.library.entity.User;
import com.github.rhllor.pc.library.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class UUIDAuthenticationService implements IUserAuthenticationService {

    @Autowired
    private UserService userService;

    @Override
    public String login(String username, String password) {
        return userService.getByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .map(u -> {
                    u.setToken(UUID.randomUUID().toString());
                    userService.save(u);
                    return u.getToken();
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
    }

    @Override
    public User authenticateByToken(String token) {
        return userService.getByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Token not found."));
    }

    @Override
    public void logout(String username) {
        userService.getByUsername(username)
                .ifPresent(u -> {
                    u.setToken(null);
                    userService.save(u);
                });
    }
}
