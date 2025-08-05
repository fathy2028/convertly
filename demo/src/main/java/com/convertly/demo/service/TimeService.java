package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.enums.TimeUnit;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.springframework.stereotype.Service;

/**
 * Service for time conversions.
 * Supports conversions between seconds, minutes, hours, and days.
 */
@Service
public class TimeService implements ConversionService {

    // Conversion factors to seconds
    private static final double SECONDS_TO_SECONDS = 1.0;
    private static final double MINUTES_TO_SECONDS = 60.0;
    private static final double HOURS_TO_SECONDS = 3600.0;
    private static final double DAYS_TO_SECONDS = 86400.0;

    @Override
    public ConversionResponse convert(ConversionRequest request) {
        validateRequest(request);

        TimeUnit fromUnit = TimeUnit.fromValue(request.getFromUnit());
        TimeUnit toUnit = TimeUnit.fromValue(request.getToUnit());
        Double value = request.getValue();

        // Validate positive value for time
        if (value < 0) {
            throw new InvalidUnitException("Time value must be non-negative");
        }

        Double result = performConversion(value, fromUnit, toUnit);
        String formula = generateFormula(value, fromUnit, toUnit, result);

        return new ConversionResponse(result, formula, request);
    }

    @Override
    public boolean supports(String category) {
        return Category.TIME.getValue().equalsIgnoreCase(category);
    }

    private void validateRequest(ConversionRequest request) {
        if (!supports(request.getCategory().getValue())) {
            throw new InvalidUnitException("Time service does not support category: " + request.getCategory());
        }

        try {
            TimeUnit.fromValue(request.getFromUnit());
            TimeUnit.fromValue(request.getToUnit());
        } catch (IllegalArgumentException e) {
            throw new InvalidUnitException("Invalid time unit: " + e.getMessage());
        }
    }

    private Double performConversion(Double value, TimeUnit from, TimeUnit to) {
        if (from == to) {
            return value;
        }

        // Convert to seconds first, then to target unit
        Double seconds = toSeconds(value, from);
        return fromSeconds(seconds, to);
    }

    private Double toSeconds(Double value, TimeUnit from) {
        switch (from) {
            case SECONDS:
                return value * SECONDS_TO_SECONDS;
            case MINUTES:
                return value * MINUTES_TO_SECONDS;
            case HOURS:
                return value * HOURS_TO_SECONDS;
            case DAYS:
                return value * DAYS_TO_SECONDS;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + from);
        }
    }

    private Double fromSeconds(Double seconds, TimeUnit to) {
        switch (to) {
            case SECONDS:
                return seconds / SECONDS_TO_SECONDS;
            case MINUTES:
                return seconds / MINUTES_TO_SECONDS;
            case HOURS:
                return seconds / HOURS_TO_SECONDS;
            case DAYS:
                return seconds / DAYS_TO_SECONDS;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + to);
        }
    }

    private String generateFormula(Double value, TimeUnit from, TimeUnit to, Double result) {
        if (from == to) {
            return String.format("%.6f %s = %.6f %s", value, from.getValue(), result, to.getValue());
        }

        double factor = getConversionFactor(from, to);
        return String.format("%.6f %s × %.6f = %.6f %s",
                value, from.getValue(), factor, result, to.getValue());
    }

    private double getConversionFactor(TimeUnit from, TimeUnit to) {
        double fromToSeconds = getToSecondsFactor(from);
        double secondsToTo = 1.0 / getToSecondsFactor(to);
        return fromToSeconds * secondsToTo;
    }

    private double getToSecondsFactor(TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return SECONDS_TO_SECONDS;
            case MINUTES:
                return MINUTES_TO_SECONDS;
            case HOURS:
                return HOURS_TO_SECONDS;
            case DAYS:
                return DAYS_TO_SECONDS;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + unit);
        }
    }
}
