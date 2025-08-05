package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.enums.TemperatureUnit;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.springframework.stereotype.Service;

/**
 * Service for temperature conversions.
 * Supports conversions between Celsius, Fahrenheit, and Kelvin.
 */
@Service
public class TemperatureService implements ConversionService {

    @Override
    public ConversionResponse convert(ConversionRequest request) {
        validateRequest(request);

        TemperatureUnit fromUnit = TemperatureUnit.fromValue(request.getFromUnit());
        TemperatureUnit toUnit = TemperatureUnit.fromValue(request.getToUnit());
        Double value = request.getValue();

        Double result = performConversion(value, fromUnit, toUnit);
        String formula = generateFormula(value, fromUnit, toUnit, result);

        return new ConversionResponse(result, formula, request);
    }

    @Override
    public boolean supports(String category) {
        return Category.TEMPERATURE.getValue().equalsIgnoreCase(category);
    }

    private void validateRequest(ConversionRequest request) {
        if (!supports(request.getCategory().getValue())) {
            throw new InvalidUnitException("Temperature service does not support category: " + request.getCategory());
        }

        try {
            TemperatureUnit.fromValue(request.getFromUnit());
            TemperatureUnit.fromValue(request.getToUnit());
        } catch (IllegalArgumentException e) {
            throw new InvalidUnitException("Invalid temperature unit: " + e.getMessage());
        }
    }

    private Double performConversion(Double value, TemperatureUnit from, TemperatureUnit to) {
        if (from == to) {
            return value;
        }

        // Convert to Celsius first, then to target unit
        Double celsius = toCelsius(value, from);
        return fromCelsius(celsius, to);
    }

    private Double toCelsius(Double value, TemperatureUnit from) {
        switch (from) {
            case CELSIUS:
                return value;
            case FAHRENHEIT:
                return (value - 32) * 5.0 / 9.0;
            case KELVIN:
                return value - 273.15;
            default:
                throw new IllegalArgumentException("Unsupported temperature unit: " + from);
        }
    }

    private Double fromCelsius(Double celsius, TemperatureUnit to) {
        switch (to) {
            case CELSIUS:
                return celsius;
            case FAHRENHEIT:
                return (celsius * 9.0 / 5.0) + 32;
            case KELVIN:
                return celsius + 273.15;
            default:
                throw new IllegalArgumentException("Unsupported temperature unit: " + to);
        }
    }

    private String generateFormula(Double value, TemperatureUnit from, TemperatureUnit to, Double result) {
        if (from == to) {
            return String.format("%.2f°%s = %.2f°%s", value, getUnitSymbol(from), result, getUnitSymbol(to));
        }

        switch (from) {
            case CELSIUS:
                switch (to) {
                    case FAHRENHEIT:
                        return String.format("(%.2f°C × 9/5) + 32 = %.2f°F", value, result);
                    case KELVIN:
                        return String.format("%.2f°C + 273.15 = %.2fK", value, result);
                    default:
                        return "";
                }
            case FAHRENHEIT:
                switch (to) {
                    case CELSIUS:
                        return String.format("(%.2f°F - 32) × 5/9 = %.2f°C", value, result);
                    case KELVIN:
                        return String.format("((%.2f°F - 32) × 5/9) + 273.15 = %.2fK", value, result);
                    default:
                        return "";
                }
            case KELVIN:
                switch (to) {
                    case CELSIUS:
                        return String.format("%.2fK - 273.15 = %.2f°C", value, result);
                    case FAHRENHEIT:
                        return String.format("((%.2fK - 273.15) × 9/5) + 32 = %.2f°F", value, result);
                    default:
                        return "";
                }
            default:
                return "";
        }
    }

    private String getUnitSymbol(TemperatureUnit unit) {
        switch (unit) {
            case CELSIUS:
                return "C";
            case FAHRENHEIT:
                return "F";
            case KELVIN:
                return "K";
            default:
                throw new IllegalArgumentException("Unsupported temperature unit: " + unit);
        }
    }
}
