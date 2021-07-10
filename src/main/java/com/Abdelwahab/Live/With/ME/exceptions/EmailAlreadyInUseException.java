package com.Abdelwahab.Live.With.ME.exceptions;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException() {
        super("This E-Mail is Already Used by Another User");
    }
}
