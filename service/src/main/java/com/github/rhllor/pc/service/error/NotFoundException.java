package com.github.rhllor.pc.service.error;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6997342766860166923L;

    public NotFoundException() {
        super("Nessun dato trovato.");
    }
}