package com.ecom.ecom.controller;

import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.CustomError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    protected static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleRuntimeException(final RuntimeException e) {
        // Logger l'exception et retourner un code de retour HTTP 500
        LOG.error("Error", e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<CustomError> handleDataNotFoundException(final DataNotFoundException e, final WebRequest request) {
        final String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        final CustomError customError = new CustomError(HttpStatus.NOT_FOUND.value(), e.getLocalizedMessage(), uri);
        LOG.error(String.format("%s, %s, %s", customError.getMessage(), customError.getStatus(), customError.getUri()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customError);
    }

}
