package br.com.kualit.devices.infra.controllers;

import br.com.kualit.devices.domain.entities.error.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class DevicesApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Error> handleNoSuchElementException(NoSuchElementException ex) {
        var error = new Error(ex.getMessage(), null, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var fieldError = ex.getBindingResult().getFieldError();

        var message = (fieldError != null) ? fieldError.getDefaultMessage() : "Validation error";
        var field   = (fieldError != null) ? fieldError.getField() : null;

        var error = new Error(message, field, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        var error = new Error(ex.getMessage(), null, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var message = ex.getMessage();

        String[] tokens = message.split(":");
        var lastPosition = tokens.length - 1;

        String errorToDeserializeSomeFieldsMessage = "Error to deserialize some fields";

        var errorMessage = tokens.length == 0 ?
                errorToDeserializeSomeFieldsMessage :
                String.format("%s %s", errorToDeserializeSomeFieldsMessage, tokens[lastPosition]);

        var error = new Error(errorMessage, null, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException ex) {
        var error = new Error(ex.getMessage(), null, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


}
