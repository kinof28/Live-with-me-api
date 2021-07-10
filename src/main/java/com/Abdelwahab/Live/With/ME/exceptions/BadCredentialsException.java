package com.Abdelwahab.Live.With.ME.exceptions;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Your JWT token Was Corrupted please try and login again");
    }
}
