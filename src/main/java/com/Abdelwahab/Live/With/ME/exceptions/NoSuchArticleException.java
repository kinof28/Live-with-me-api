package com.Abdelwahab.Live.With.ME.exceptions;

public class NoSuchArticleException extends RuntimeException{
    public NoSuchArticleException() {
        super("No Such Article Available in our Database");
    }
}
