package com.convertly.demo.service;

import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;

/**
 * Interface for unit conversion services.
 */
public interface ConversionService {
    
    /**
     * Converts a value from one unit to another.
     * 
     * @param request the conversion request
     * @return the conversion response with result and formula
     */
    ConversionResponse convert(ConversionRequest request);
    
    /**
     * Checks if the service supports the given category.
     * 
     * @param category the category to check
     * @return true if supported, false otherwise
     */
    boolean supports(String category);
}
