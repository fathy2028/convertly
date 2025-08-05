package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.enums.WeightUnit;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.springframework.stereotype.Service;

/**
 * Service for weight conversions.
 * Supports conversions between gram, kilogram, pound, and ounce.
 */
@Service
public class WeightService implements ConversionService {

    // Conversion factors to grams
    private static final double GRAM_TO_GRAM = 1.0;
    private static final double KILOGRAM_TO_GRAM = 1000.0;
    private static final double POUND_TO_GRAM = 453.592;
    private static final double OUNCE_TO_GRAM = 28.3495;

    @Override
    public ConversionResponse convert(ConversionRequest request) {
        validateRequest(request);

        WeightUnit fromUnit = WeightUnit.fromValue(request.getFromUnit());
        WeightUnit toUnit = WeightUnit.fromValue(request.getToUnit());
        Double value = request.getValue();

        // Validate positive value for weight
        if (value < 0) {
            throw new InvalidUnitException("Weight value must be non-negative");
        }

        Double result = performConversion(value, fromUnit, toUnit);
        String formula = generateFormula(value, fromUnit, toUnit, result);

        return new ConversionResponse(result, formula, request);
    }

    @Override
    public boolean supports(String category) {
        return Category.WEIGHT.getValue().equalsIgnoreCase(category);
    }

    private void validateRequest(ConversionRequest request) {
        if (!supports(request.getCategory().getValue())) {
            throw new InvalidUnitException("Weight service does not support category: " + request.getCategory());
        }

        try {
            WeightUnit.fromValue(request.getFromUnit());
            WeightUnit.fromValue(request.getToUnit());
        } catch (IllegalArgumentException e) {
            throw new InvalidUnitException("Invalid weight unit: " + e.getMessage());
        }
    }

    private Double performConversion(Double value, WeightUnit from, WeightUnit to) {
        if (from == to) {
            return value;
        }

        // Convert to grams first, then to target unit
        Double grams = toGrams(value, from);
        return fromGrams(grams, to);
    }

    private Double toGrams(Double value, WeightUnit from) {
        switch (from) {
            case GRAM:
                return value * GRAM_TO_GRAM;
            case KILOGRAM:
                return value * KILOGRAM_TO_GRAM;
            case POUND:
                return value * POUND_TO_GRAM;
            case OUNCE:
                return value * OUNCE_TO_GRAM;
            default:
                throw new IllegalArgumentException("Unsupported weight unit: " + from);
        }
    }

    private Double fromGrams(Double grams, WeightUnit to) {
        switch (to) {
            case GRAM:
                return grams / GRAM_TO_GRAM;
            case KILOGRAM:
                return grams / KILOGRAM_TO_GRAM;
            case POUND:
                return grams / POUND_TO_GRAM;
            case OUNCE:
                return grams / OUNCE_TO_GRAM;
            default:
                throw new IllegalArgumentException("Unsupported weight unit: " + to);
        }
    }

    private String generateFormula(Double value, WeightUnit from, WeightUnit to, Double result) {
        if (from == to) {
            return String.format("%.6f %s = %.6f %s", value, from.getValue(), result, to.getValue());
        }

        double factor = getConversionFactor(from, to);
        return String.format("%.6f %s × %.6f = %.6f %s",
                value, from.getValue(), factor, result, to.getValue());
    }

    private double getConversionFactor(WeightUnit from, WeightUnit to) {
        double fromToGram = getToGramFactor(from);
        double gramToTo = 1.0 / getToGramFactor(to);
        return fromToGram * gramToTo;
    }

    private double getToGramFactor(WeightUnit unit) {
        switch (unit) {
            case GRAM:
                return GRAM_TO_GRAM;
            case KILOGRAM:
                return KILOGRAM_TO_GRAM;
            case POUND:
                return POUND_TO_GRAM;
            case OUNCE:
                return OUNCE_TO_GRAM;
            default:
                throw new IllegalArgumentException("Unsupported weight unit: " + unit);
        }
    }
}
