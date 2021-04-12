package com.github.rhllor.pc.service;

class NotFoundException extends RuntimeException {

    NotFoundException() {
        super("Nessun dato trovato.");
    }
}