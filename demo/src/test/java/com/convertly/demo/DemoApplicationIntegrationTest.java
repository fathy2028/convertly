package com.convertly.demo;

import com.convertly.demo.enums.Category;
import com.convertly.demo.model.ConversionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class DemoApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCompleteTemperatureConversionFlow() throws Exception {
        // Test temperature conversion
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", 25.0);

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(77.0))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.formula").exists());

        // Test that history was recorded
        mockMvc.perform(get("/history/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalConversions").value(1));
    }

    @Test
    void testCompleteLengthConversionFlow() throws Exception {
        // Test length conversion
        ConversionRequest request = new ConversionRequest(Category.LENGTH, "meter", "foot", 1.0);

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(3.28084))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.formula").exists());
    }

    @Test
    void testGetCategoriesAndUnits() throws Exception {
        // Test get categories
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4));

        // Test get units for temperature
        mockMvc.perform(get("/units")
                .param("category", "temperature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Unit Converter API is up and running"));
    }

    @Test
    void testSamplePayloadEndpoint() throws Exception {
        mockMvc.perform(get("/sample-payload"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("temperature"))
                .andExpect(jsonPath("$.fromUnit").value("celsius"))
                .andExpect(jsonPath("$.toUnit").value("fahrenheit"))
                .andExpect(jsonPath("$.value").value(100.0));
    }

    @Test
    void testInvalidConversionRequest() throws Exception {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "invalid", "fahrenheit", 25.0);

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testInvalidCategory() throws Exception {
        mockMvc.perform(get("/units")
                .param("category", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testHistoryEndpoints() throws Exception {
        // Clear history first
        mockMvc.perform(delete("/history"))
                .andExpect(status().isOk());

        // Perform a conversion to create history
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", 0.0);
        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Test get all history
        mockMvc.perform(get("/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Test download history as JSON
        mockMvc.perform(get("/history/download/json"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=conversion-history.json"));

        // Test download history as CSV
        mockMvc.perform(get("/history/download/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=conversion-history.csv"));
    }
}
