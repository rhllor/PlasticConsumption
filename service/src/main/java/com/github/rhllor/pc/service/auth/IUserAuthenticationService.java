package com.github.rhllor.pc.service.auth;

import com.github.rhllor.pc.library.entity.User;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public interface IUserAuthenticationService {
    String login(String username, String password) throws BadCredentialsException;

    User authenticateByToken(String token) throws AuthenticationException;

    void logout(String username);
}
