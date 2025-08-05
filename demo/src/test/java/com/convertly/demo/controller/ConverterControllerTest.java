package com.convertly.demo.controller;

import com.convertly.demo.enums.Category;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import com.convertly.demo.service.ConversionServiceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversionServiceManager conversionServiceManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testConvertEndpoint() throws Exception {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", 25.0);
        ConversionResponse response = new ConversionResponse(77.0, "(25.00°C × 9/5) + 32 = 77.00°F", request);

        when(conversionServiceManager.convert(any(ConversionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(77.0))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.formula").exists())
                .andExpect(jsonPath("$.originalInput.category").value("temperature"))
                .andExpect(jsonPath("$.originalInput.fromUnit").value("celsius"))
                .andExpect(jsonPath("$.originalInput.toUnit").value("fahrenheit"))
                .andExpect(jsonPath("$.originalInput.value").value(25.0));
    }

    @Test
    void testConvertEndpointWithInvalidInput() throws Exception {
        ConversionRequest request = new ConversionRequest();
        // Missing required fields

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCategoriesEndpoint() throws Exception {
        List<String> categories = Arrays.asList("temperature", "length", "weight", "time");
        when(conversionServiceManager.getCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0]").value("temperature"))
                .andExpect(jsonPath("$[1]").value("length"))
                .andExpect(jsonPath("$[2]").value("weight"))
                .andExpect(jsonPath("$[3]").value("time"));
    }

    @Test
    void testGetUnitsEndpoint() throws Exception {
        List<String> temperatureUnits = Arrays.asList("celsius", "fahrenheit", "kelvin");
        when(conversionServiceManager.getUnitsForCategory("temperature")).thenReturn(temperatureUnits);

        mockMvc.perform(get("/units")
                .param("category", "temperature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("celsius"))
                .andExpect(jsonPath("$[1]").value("fahrenheit"))
                .andExpect(jsonPath("$[2]").value("kelvin"));
    }

    @Test
    void testGetSamplePayloadEndpoint() throws Exception {
        mockMvc.perform(get("/sample-payload"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("temperature"))
                .andExpect(jsonPath("$.fromUnit").value("celsius"))
                .andExpect(jsonPath("$.toUnit").value("fahrenheit"))
                .andExpect(jsonPath("$.value").value(100.0));
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Unit Converter API is up and running"));
    }
}
