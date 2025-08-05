package com.convertly.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Model representing a conversion history entry.
 */
@Schema(description = "Conversion history entry")
public class ConversionHistory {

    @Schema(description = "Unique identifier for the conversion")
    private String id;

    @Schema(description = "Timestamp when the conversion was performed")
    private LocalDateTime timestamp;

    @Schema(description = "The conversion request")
    private ConversionRequest request;

    @Schema(description = "The conversion response")
    private ConversionResponse response;

    // Default constructor
    public ConversionHistory() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with all fields
    public ConversionHistory(String id, ConversionRequest request, ConversionResponse response) {
        this();
        this.id = id;
        this.request = request;
        this.response = response;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ConversionRequest getRequest() {
        return request;
    }

    public void setRequest(ConversionRequest request) {
        this.request = request;
    }

    public ConversionResponse getResponse() {
        return response;
    }

    public void setResponse(ConversionResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ConversionHistory{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", request=" + request +
                ", response=" + response +
                '}';
    }
}
