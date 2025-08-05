package com.convertly.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Model for structured error responses.
 */
@Schema(description = "Error response payload")
public class ErrorResponse {

    @Schema(description = "Error type", example = "InvalidUnitException")
    private String error;

    @Schema(description = "Error message", example = "Unit 'fahrenheit' does not belong to category 'length'.")
    private String message;

    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Request path where the error occurred", example = "/convert")
    private String path;

    // Default constructor
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with all fields
    public ErrorResponse(String error, String message, int status, String path) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
    }

    // Getters and setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", path='" + path + '\'' +
                '}';
    }
}
