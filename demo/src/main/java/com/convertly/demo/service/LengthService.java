package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.enums.LengthUnit;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.springframework.stereotype.Service;

/**
 * Service for length conversions.
 * Supports conversions between meter, kilometer, mile, inch, and foot.
 */
@Service
public class LengthService implements ConversionService {

    // Conversion factors to meters
    private static final double METER_TO_METER = 1.0;
    private static final double KILOMETER_TO_METER = 1000.0;
    private static final double MILE_TO_METER = 1609.344;
    private static final double INCH_TO_METER = 0.0254;
    private static final double FOOT_TO_METER = 0.3048;

    @Override
    public ConversionResponse convert(ConversionRequest request) {
        validateRequest(request);

        LengthUnit fromUnit = LengthUnit.fromValue(request.getFromUnit());
        LengthUnit toUnit = LengthUnit.fromValue(request.getToUnit());
        Double value = request.getValue();

        Double result = performConversion(value, fromUnit, toUnit);
        String formula = generateFormula(value, fromUnit, toUnit, result);

        return new ConversionResponse(result, formula, request);
    }

    @Override
    public boolean supports(String category) {
        return Category.LENGTH.getValue().equalsIgnoreCase(category);
    }

    private void validateRequest(ConversionRequest request) {
        if (!supports(request.getCategory().getValue())) {
            throw new InvalidUnitException("Length service does not support category: " + request.getCategory());
        }

        try {
            LengthUnit.fromValue(request.getFromUnit());
            LengthUnit.fromValue(request.getToUnit());
        } catch (IllegalArgumentException e) {
            throw new InvalidUnitException("Invalid length unit: " + e.getMessage());
        }
    }

    private Double performConversion(Double value, LengthUnit from, LengthUnit to) {
        if (from == to) {
            return value;
        }

        // Convert to meters first, then to target unit
        Double meters = toMeters(value, from);
        return fromMeters(meters, to);
    }

    private Double toMeters(Double value, LengthUnit from) {
        switch (from) {
            case METER:
                return value * METER_TO_METER;
            case KILOMETER:
                return value * KILOMETER_TO_METER;
            case MILE:
                return value * MILE_TO_METER;
            case INCH:
                return value * INCH_TO_METER;
            case FOOT:
                return value * FOOT_TO_METER;
            default:
                throw new IllegalArgumentException("Unsupported length unit: " + from);
        }
    }

    private Double fromMeters(Double meters, LengthUnit to) {
        switch (to) {
            case METER:
                return meters / METER_TO_METER;
            case KILOMETER:
                return meters / KILOMETER_TO_METER;
            case MILE:
                return meters / MILE_TO_METER;
            case INCH:
                return meters / INCH_TO_METER;
            case FOOT:
                return meters / FOOT_TO_METER;
            default:
                throw new IllegalArgumentException("Unsupported length unit: " + to);
        }
    }

    private String generateFormula(Double value, LengthUnit from, LengthUnit to, Double result) {
        if (from == to) {
            return String.format("%.6f %s = %.6f %s", value, from.getValue(), result, to.getValue());
        }

        double factor = getConversionFactor(from, to);
        return String.format("%.6f %s × %.6f = %.6f %s",
                value, from.getValue(), factor, result, to.getValue());
    }

    private double getConversionFactor(LengthUnit from, LengthUnit to) {
        double fromToMeter = getToMeterFactor(from);
        double meterToTo = 1.0 / getToMeterFactor(to);
        return fromToMeter * meterToTo;
    }

    private double getToMeterFactor(LengthUnit unit) {
        switch (unit) {
            case METER:
                return METER_TO_METER;
            case KILOMETER:
                return KILOMETER_TO_METER;
            case MILE:
                return MILE_TO_METER;
            case INCH:
                return INCH_TO_METER;
            case FOOT:
                return FOOT_TO_METER;
            default:
                throw new IllegalArgumentException("Unsupported length unit: " + unit);
        }
    }
}
