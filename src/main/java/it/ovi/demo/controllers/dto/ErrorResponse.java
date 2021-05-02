package it.ovi.demo.controllers.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public class ErrorResponse {
    private OffsetDateTime timestamp;
    @JsonSerialize(using = HttpStatusCodeSerializer.class)
    private HttpStatus status;
    private String message;
    private String path;

    public ErrorResponse(OffsetDateTime timestamp, HttpStatus status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return status != null ? status.getReasonPhrase() : null;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
