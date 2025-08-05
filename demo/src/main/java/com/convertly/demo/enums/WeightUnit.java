package com.convertly.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing weight units.
 * Supports case-insensitive mapping from JSON.
 */
public enum WeightUnit {
    GRAM("gram"),
    KILOGRAM("kilogram"),
    POUND("pound"),
    OUNCE("ounce");

    private final String value;

    WeightUnit(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a WeightUnit enum from a string value (case-insensitive).
     * 
     * @param value the string value
     * @return the corresponding WeightUnit enum
     * @throws IllegalArgumentException if the value is not valid
     */
    @JsonCreator
    public static WeightUnit fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Weight unit value cannot be null");
        }
        
        for (WeightUnit unit : WeightUnit.values()) {
            if (unit.value.equalsIgnoreCase(value.trim())) {
                return unit;
            }
        }
        
        throw new IllegalArgumentException("Invalid weight unit: " + value + 
            ". Valid units are: gram, kilogram, pound, ounce");
    }

    @Override
    public String toString() {
        return value;
    }
}
