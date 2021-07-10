package com.Abdelwahab.Live.With.ME.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@AllArgsConstructor
public class ExceptionResponse {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime time;



}
