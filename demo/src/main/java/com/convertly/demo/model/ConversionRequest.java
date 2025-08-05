package com.convertly.demo.model;

import com.convertly.demo.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request model for unit conversion.
 */
@Schema(description = "Request payload for unit conversion")
public class ConversionRequest {

    @NotNull(message = "Category is required")
    @Schema(description = "The conversion category", example = "temperature", allowableValues = { "temperature",
            "length", "weight", "time" })
    private Category category;

    @NotNull(message = "From unit is required")
    @Schema(description = "The unit to convert from", example = "celsius")
    private String fromUnit;

    @NotNull(message = "To unit is required")
    @Schema(description = "The unit to convert to", example = "fahrenheit")
    private String toUnit;

    @NotNull(message = "Value is required")
    @Schema(description = "The value to convert", example = "25")
    private Double value;

    // Default constructor
    public ConversionRequest() {
    }

    // Constructor with all fields
    public ConversionRequest(Category category, String fromUnit, String toUnit, Double value) {
        this.category = category;
        this.fromUnit = fromUnit;
        this.toUnit = toUnit;
        this.value = value;
    }

    // Getters and setters
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(String fromUnit) {
        this.fromUnit = fromUnit;
    }

    public String getToUnit() {
        return toUnit;
    }

    public void setToUnit(String toUnit) {
        this.toUnit = toUnit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConversionRequest{" +
                "category=" + category +
                ", fromUnit='" + fromUnit + '\'' +
                ", toUnit='" + toUnit + '\'' +
                ", value=" + value +
                '}';
    }
}
