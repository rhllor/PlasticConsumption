package com.github.rhllor.pc.service.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
    
    @Autowired
    private IUserAuthenticationService userAuthenticationService;

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        Object token = authentication.getCredentials();
        return Optional
        .ofNullable(token)
        .flatMap(t ->
                Optional.of(userAuthenticationService.authenticateByToken(String.valueOf(t)))
                        .map(u -> User.builder()
                                .username(u.getUsername())
                                .password(u.getPassword())
                                .roles("user")
                                .build()))
        .orElseThrow(() -> new BadCredentialsException("Token non valido"));
                            
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1)
            throws AuthenticationException {        
    }
}
