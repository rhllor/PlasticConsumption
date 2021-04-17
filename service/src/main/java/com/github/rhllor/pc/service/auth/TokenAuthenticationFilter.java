package com.github.rhllor.pc.service.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class TokenAuthenticationFilter extends  AbstractAuthenticationProcessingFilter  {


    protected TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    private static final String _authorization = "Authorization";
    private static final String _bearer = "Bearer";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        String token = Optional.ofNullable(request.getHeader(_authorization))
            .map(value -> value.replace(_bearer, "").trim())
            .orElseThrow(() -> new BadCredentialsException("Missing authentication token."));

        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain,
        final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
