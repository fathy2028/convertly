package com.convertly.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing temperature units.
 * Supports case-insensitive mapping from JSON.
 */
public enum TemperatureUnit {
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit"),
    KELVIN("kelvin");

    private final String value;

    TemperatureUnit(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a TemperatureUnit enum from a string value (case-insensitive).
     * 
     * @param value the string value
     * @return the corresponding TemperatureUnit enum
     * @throws IllegalArgumentException if the value is not valid
     */
    @JsonCreator
    public static TemperatureUnit fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Temperature unit value cannot be null");
        }
        
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            if (unit.value.equalsIgnoreCase(value.trim())) {
                return unit;
            }
        }
        
        throw new IllegalArgumentException("Invalid temperature unit: " + value + 
            ". Valid units are: celsius, fahrenheit, kelvin");
    }

    @Override
    public String toString() {
        return value;
    }
}
