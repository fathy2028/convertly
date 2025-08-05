package com.convertly.demo.controller;

import com.convertly.demo.enums.Category;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import com.convertly.demo.service.ConversionServiceManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for unit conversion operations.
 */
@RestController
@Tag(name = "Unit Converter", description = "API for converting units across different measurement categories")
public class ConverterController {

    private final ConversionServiceManager conversionServiceManager;

    @Autowired
    public ConverterController(ConversionServiceManager conversionServiceManager) {
        this.conversionServiceManager = conversionServiceManager;
    }

    /**
     * Converts a value from one unit to another within a given category.
     */
    @PostMapping("/convert")
    @Operation(summary = "Convert units", 
               description = "Converts a value from one unit to another within a given category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conversion successful",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ConversionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ConversionResponse> convert(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Conversion request payload",
                content = @Content(
                    examples = {
                        @ExampleObject(
                            name = "Temperature conversion",
                            value = """
                                {
                                  "category": "temperature",
                                  "fromUnit": "celsius",
                                  "toUnit": "fahrenheit",
                                  "value": 25
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Length conversion",
                            value = """
                                {
                                  "category": "length",
                                  "fromUnit": "meter",
                                  "toUnit": "foot",
                                  "value": 10
                                }
                                """
                        )
                    }
                )
            )
            ConversionRequest request) {
        
        ConversionResponse response = conversionServiceManager.convert(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns a list of all available conversion categories.
     */
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", 
               description = "Returns a list of all available conversion categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = conversionServiceManager.getCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Returns a list of all supported units for a given category.
     */
    @GetMapping("/units")
    @Operation(summary = "Get units for category", 
               description = "Returns a list of all supported units for a given category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Units retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category")
    })
    public ResponseEntity<List<String>> getUnits(
            @Parameter(description = "The category to get units for", 
                      example = "temperature",
                      schema = @Schema(allowableValues = {"temperature", "length", "weight", "time"}))
            @RequestParam String category) {
        
        List<String> units = conversionServiceManager.getUnitsForCategory(category);
        return ResponseEntity.ok(units);
    }

    /**
     * Returns a sample request body for the /convert endpoint.
     */
    @GetMapping("/sample-payload")
    @Operation(summary = "Get sample payload", 
               description = "Returns a sample request body for the /convert endpoint")
    @ApiResponse(responseCode = "200", description = "Sample payload retrieved successfully")
    public ResponseEntity<ConversionRequest> getSamplePayload() {
        ConversionRequest sample = new ConversionRequest(
                Category.TEMPERATURE, "celsius", "fahrenheit", 100.0);
        return ResponseEntity.ok(sample);
    }

    /**
     * Simple health check endpoint.
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", 
               description = "Simple health check endpoint")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "Unit Converter API is up and running"));
    }
}
