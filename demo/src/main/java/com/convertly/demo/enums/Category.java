package com.convertly.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing different conversion categories.
 * Supports case-insensitive mapping from JSON.
 */
public enum Category {
    TEMPERATURE("temperature"),
    LENGTH("length"),
    WEIGHT("weight"),
    TIME("time");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a Category enum from a string value (case-insensitive).
     * 
     * @param value the string value
     * @return the corresponding Category enum
     * @throws IllegalArgumentException if the value is not valid
     */
    @JsonCreator
    public static Category fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Category value cannot be null");
        }
        
        for (Category category : Category.values()) {
            if (category.value.equalsIgnoreCase(value.trim())) {
                return category;
            }
        }
        
        throw new IllegalArgumentException("Invalid category: " + value + 
            ". Valid categories are: temperature, length, weight, time");
    }

    @Override
    public String toString() {
        return value;
    }
}
