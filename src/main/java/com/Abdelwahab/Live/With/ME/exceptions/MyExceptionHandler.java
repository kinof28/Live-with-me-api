//package com.Abdelwahab.Live.With.ME.exceptions;
//
//import io.jsonwebtoken.SignatureException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//
//@ControllerAdvice
//public class MyExceptionHandler {
//
//    @ExceptionHandler(value = {BadCredentialsException.class , SignatureException.class,Exception.class})
//    public ResponseEntity<Object> handleException(Exception e){
//
//        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),e, HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now(ZoneId.of("Z")));
//
//        return new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//}
