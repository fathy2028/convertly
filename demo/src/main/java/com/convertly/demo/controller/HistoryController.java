package com.convertly.demo.controller;

import com.convertly.demo.model.ConversionHistory;
import com.convertly.demo.service.ConversionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST controller for conversion history operations.
 */
@RestController
@RequestMapping("/history")
@Tag(name = "Conversion History", description = "API for managing conversion history")
public class HistoryController {

    private final ConversionHistoryService historyService;

    @Autowired
    public HistoryController(ConversionHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Gets all conversion history entries.
     */
    @GetMapping
    @Operation(summary = "Get all conversion history", 
               description = "Returns all conversion history entries stored in memory")
    @ApiResponse(responseCode = "200", description = "History retrieved successfully")
    public ResponseEntity<List<ConversionHistory>> getAllHistory() {
        List<ConversionHistory> history = historyService.getAllHistory();
        return ResponseEntity.ok(history);
    }

    /**
     * Gets a specific conversion history entry by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get conversion history by ID", 
               description = "Returns a specific conversion history entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "History entry found"),
        @ApiResponse(responseCode = "404", description = "History entry not found")
    })
    public ResponseEntity<ConversionHistory> getHistoryById(
            @Parameter(description = "The history entry ID")
            @PathVariable String id) {
        
        ConversionHistory history = historyService.getHistoryById(id);
        if (history == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

    /**
     * Gets conversion history statistics.
     */
    @GetMapping("/stats")
    @Operation(summary = "Get history statistics", 
               description = "Returns statistics about the conversion history")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getHistoryStats() {
        int count = historyService.getHistoryCount();
        return ResponseEntity.ok(Map.of(
                "totalConversions", count,
                "status", count > 0 ? "History available" : "No conversions yet"
        ));
    }

    /**
     * Downloads conversion history as JSON.
     */
    @GetMapping("/download/json")
    @Operation(summary = "Download history as JSON", 
               description = "Downloads all conversion history as a JSON file")
    @ApiResponse(responseCode = "200", description = "JSON file generated successfully",
                content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<ConversionHistory>> downloadHistoryAsJson() {
        List<ConversionHistory> history = historyService.getAllHistory();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=conversion-history.json");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(history);
    }

    /**
     * Downloads conversion history as CSV.
     */
    @GetMapping("/download/csv")
    @Operation(summary = "Download history as CSV", 
               description = "Downloads all conversion history as a CSV file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "CSV file generated successfully",
                    content = @Content(mediaType = "text/csv")),
        @ApiResponse(responseCode = "500", description = "Error generating CSV file")
    })
    public ResponseEntity<String> downloadHistoryAsCSV() {
        try {
            String csvContent = historyService.exportHistoryAsCSV();
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=conversion-history.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating CSV file: " + e.getMessage());
        }
    }

    /**
     * Clears all conversion history.
     */
    @DeleteMapping
    @Operation(summary = "Clear all history", 
               description = "Clears all conversion history entries")
    @ApiResponse(responseCode = "200", description = "History cleared successfully")
    public ResponseEntity<Map<String, String>> clearHistory() {
        historyService.clearHistory();
        return ResponseEntity.ok(Map.of("message", "Conversion history cleared successfully"));
    }
}
