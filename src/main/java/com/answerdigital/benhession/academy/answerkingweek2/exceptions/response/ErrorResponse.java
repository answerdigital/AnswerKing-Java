package com.answerdigital.benhession.academy.answerkingweek2.exceptions.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Date timestamp;
    private final int status;
    private final String error;
    private final String message;
    private String path;
    @JsonIgnore
    private final HttpStatus httpStatus;

    public ErrorResponse(HttpStatus httpStatus, String message, String path) {
        this.timestamp = new Date();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
