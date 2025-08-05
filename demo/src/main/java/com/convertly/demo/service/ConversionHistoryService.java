package com.convertly.demo.service;

import com.convertly.demo.model.ConversionHistory;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing conversion history.
 * Stores conversions in memory (session-level).
 */
@Service
public class ConversionHistoryService {

    private final ConcurrentHashMap<String, ConversionHistory> historyMap = new ConcurrentHashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Adds a conversion to the history.
     * 
     * @param request the conversion request
     * @param response the conversion response
     * @return the history entry ID
     */
    public String addConversion(ConversionRequest request, ConversionResponse response) {
        String id = UUID.randomUUID().toString();
        ConversionHistory history = new ConversionHistory(id, request, response);
        historyMap.put(id, history);
        return id;
    }

    /**
     * Gets all conversion history entries.
     * 
     * @return list of all history entries
     */
    public List<ConversionHistory> getAllHistory() {
        return new ArrayList<>(historyMap.values());
    }

    /**
     * Gets a specific conversion history entry by ID.
     * 
     * @param id the history entry ID
     * @return the history entry, or null if not found
     */
    public ConversionHistory getHistoryById(String id) {
        return historyMap.get(id);
    }

    /**
     * Clears all conversion history.
     */
    public void clearHistory() {
        historyMap.clear();
    }

    /**
     * Gets the count of conversion history entries.
     * 
     * @return the number of history entries
     */
    public int getHistoryCount() {
        return historyMap.size();
    }

    /**
     * Exports conversion history as CSV format.
     * 
     * @return CSV string representation of the history
     * @throws IOException if there's an error generating the CSV
     */
    public String exportHistoryAsCSV() throws IOException {
        StringWriter stringWriter = new StringWriter();
        
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("ID", "Timestamp", "Category", "From Unit", "To Unit", "Input Value", "Result", "Formula")
                .build();
        
        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat)) {
            for (ConversionHistory history : getAllHistory()) {
                csvPrinter.printRecord(
                        history.getId(),
                        history.getTimestamp().format(DATE_FORMATTER),
                        history.getRequest().getCategory().getValue(),
                        history.getRequest().getFromUnit(),
                        history.getRequest().getToUnit(),
                        history.getRequest().getValue(),
                        history.getResponse().getResult(),
                        history.getResponse().getFormula()
                );
            }
        }
        
        return stringWriter.toString();
    }
}
