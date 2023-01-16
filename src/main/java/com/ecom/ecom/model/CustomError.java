package com.ecom.ecom.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class CustomError {

    private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private int status;

    private String message;

    private String date;

    private String uri;

    public CustomError(final int status, final String message, final String uri) {
        this.status = status;
        this.message = message;

        this.uri = uri;
    }

    public String getDate() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DATETIMEFORMATTER);
    }
}
