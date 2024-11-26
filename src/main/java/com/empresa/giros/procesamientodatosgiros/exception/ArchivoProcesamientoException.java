package com.empresa.giros.procesamientodatosgiros.exception;

public final class ArchivoProcesamientoException extends RuntimeException {

    public ArchivoProcesamientoException(String message) {
        super(message);
    }

    public ArchivoProcesamientoException(String message, Throwable cause) {
        super(message, cause);
    }
}