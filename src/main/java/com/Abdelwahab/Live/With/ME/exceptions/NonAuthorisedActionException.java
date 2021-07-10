package com.Abdelwahab.Live.With.ME.exceptions;

public class NonAuthorisedActionException extends RuntimeException{
    public NonAuthorisedActionException() {
        super("You Are Attempting to do a Non Authorised Action");
    }
}
