package com.vdcrx.rest.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message) { super(message);}

    public ResourceNotFoundException(Throwable cause) { super(cause);}

    public ResourceNotFoundException(String message, Throwable cause) { super(message, cause); }

    public ResourceNotFoundException(String message, Throwable cause, boolean enableSupression, boolean writeableStackTrace) {
        super(message, cause, enableSupression, writeableStackTrace);
    }
}
