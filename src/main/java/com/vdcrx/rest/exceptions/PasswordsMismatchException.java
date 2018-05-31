package com.vdcrx.rest.exceptions;

public class PasswordsMismatchException extends RuntimeException {
    public PasswordsMismatchException() {}
    public PasswordsMismatchException(String message) { super(message); }
}
