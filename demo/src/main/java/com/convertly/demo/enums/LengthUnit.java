package com.convertly.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing length units.
 * Supports case-insensitive mapping from JSON.
 */
public enum LengthUnit {
    METER("meter"),
    KILOMETER("kilometer"),
    MILE("mile"),
    INCH("inch"),
    FOOT("foot");

    private final String value;

    LengthUnit(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a LengthUnit enum from a string value (case-insensitive).
     * 
     * @param value the string value
     * @return the corresponding LengthUnit enum
     * @throws IllegalArgumentException if the value is not valid
     */
    @JsonCreator
    public static LengthUnit fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Length unit value cannot be null");
        }
        
        for (LengthUnit unit : LengthUnit.values()) {
            if (unit.value.equalsIgnoreCase(value.trim())) {
                return unit;
            }
        }
        
        throw new IllegalArgumentException("Invalid length unit: " + value + 
            ". Valid units are: meter, kilometer, mile, inch, foot");
    }

    @Override
    public String toString() {
        return value;
    }
}
