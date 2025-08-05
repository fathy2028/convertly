package com.convertly.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing time units.
 * Supports case-insensitive mapping from JSON.
 */
public enum TimeUnit {
    SECONDS("seconds"),
    MINUTES("minutes"),
    HOURS("hours"),
    DAYS("days");

    private final String value;

    TimeUnit(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a TimeUnit enum from a string value (case-insensitive).
     * 
     * @param value the string value
     * @return the corresponding TimeUnit enum
     * @throws IllegalArgumentException if the value is not valid
     */
    @JsonCreator
    public static TimeUnit fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Time unit value cannot be null");
        }
        
        for (TimeUnit unit : TimeUnit.values()) {
            if (unit.value.equalsIgnoreCase(value.trim())) {
                return unit;
            }
        }
        
        throw new IllegalArgumentException("Invalid time unit: " + value + 
            ". Valid units are: seconds, minutes, hours, days");
    }

    @Override
    public String toString() {
        return value;
    }
}
