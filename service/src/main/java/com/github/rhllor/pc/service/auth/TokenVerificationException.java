package com.github.rhllor.pc.service.auth;

public class TokenVerificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenVerificationException(Throwable t) {
        super(t);
    }
}
