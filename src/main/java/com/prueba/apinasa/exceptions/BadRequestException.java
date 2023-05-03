package com.prueba.apinasa.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
