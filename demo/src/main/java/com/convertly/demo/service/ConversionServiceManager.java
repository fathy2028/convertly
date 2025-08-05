package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Main service that manages all conversion services and routes requests to the
 * appropriate service.
 */
@Service
public class ConversionServiceManager {

    private final List<ConversionService> conversionServices;
    private final ConversionHistoryService historyService;

    @Autowired
    public ConversionServiceManager(TemperatureService temperatureService,
            LengthService lengthService,
            WeightService weightService,
            TimeService timeService,
            ConversionHistoryService historyService) {
        this.conversionServices = Arrays.asList(
                temperatureService, lengthService, weightService, timeService);
        this.historyService = historyService;
    }

    /**
     * Converts a value from one unit to another.
     *
     * @param request the conversion request
     * @return the conversion response
     * @throws InvalidUnitException if the category or units are not supported
     */
    public ConversionResponse convert(ConversionRequest request) {
        ConversionService service = findServiceForCategory(request.getCategory().getValue());
        ConversionResponse response = service.convert(request);

        // Add to history
        historyService.addConversion(request, response);

        return response;
    }

    /**
     * Gets all available categories.
     * 
     * @return list of category names
     */
    public List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Category::getValue)
                .toList();
    }

    /**
     * Gets all units for a given category.
     * 
     * @param category the category name
     * @return list of unit names for the category
     * @throws InvalidUnitException if the category is not supported
     */
    public List<String> getUnitsForCategory(String category) {
        Category cat;
        try {
            cat = Category.fromValue(category);
        } catch (IllegalArgumentException e) {
            throw new InvalidUnitException("Invalid category: " + category);
        }

        switch (cat) {
            case TEMPERATURE:
                return Arrays.stream(com.convertly.demo.enums.TemperatureUnit.values())
                        .map(com.convertly.demo.enums.TemperatureUnit::getValue)
                        .toList();
            case LENGTH:
                return Arrays.stream(com.convertly.demo.enums.LengthUnit.values())
                        .map(com.convertly.demo.enums.LengthUnit::getValue)
                        .toList();
            case WEIGHT:
                return Arrays.stream(com.convertly.demo.enums.WeightUnit.values())
                        .map(com.convertly.demo.enums.WeightUnit::getValue)
                        .toList();
            case TIME:
                return Arrays.stream(com.convertly.demo.enums.TimeUnit.values())
                        .map(com.convertly.demo.enums.TimeUnit::getValue)
                        .toList();
            default:
                throw new InvalidUnitException("Unsupported category: " + cat);
        }
    }

    private ConversionService findServiceForCategory(String category) {
        return conversionServices.stream()
                .filter(service -> service.supports(category))
                .findFirst()
                .orElseThrow(() -> new InvalidUnitException("No service found for category: " + category));
    }
}
