package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversionServiceManagerTest {

    @Mock
    private TemperatureService temperatureService;

    @Mock
    private LengthService lengthService;

    @Mock
    private WeightService weightService;

    @Mock
    private TimeService timeService;

    @Mock
    private ConversionHistoryService historyService;

    private ConversionServiceManager serviceManager;

    @BeforeEach
    void setUp() {
        serviceManager = new ConversionServiceManager(
                temperatureService, lengthService, weightService, timeService, historyService);
    }

    @Test
    void testConvertTemperature() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", 25.0);
        ConversionResponse expectedResponse = new ConversionResponse(77.0, "formula", request);

        when(temperatureService.supports("temperature")).thenReturn(true);
        when(temperatureService.convert(request)).thenReturn(expectedResponse);
        when(historyService.addConversion(any(), any())).thenReturn("history-id");

        ConversionResponse actualResponse = serviceManager.convert(request);

        assertEquals(expectedResponse, actualResponse);
        verify(temperatureService).convert(request);
        verify(historyService).addConversion(request, expectedResponse);
    }

    @Test
    void testConvertLength() {
        ConversionRequest request = new ConversionRequest(Category.LENGTH, "meter", "foot", 10.0);
        ConversionResponse expectedResponse = new ConversionResponse(32.8084, "formula", request);

        when(lengthService.supports("length")).thenReturn(true);
        when(lengthService.convert(request)).thenReturn(expectedResponse);
        when(historyService.addConversion(any(), any())).thenReturn("history-id");

        ConversionResponse actualResponse = serviceManager.convert(request);

        assertEquals(expectedResponse, actualResponse);
        verify(lengthService).convert(request);
        verify(historyService).addConversion(request, expectedResponse);
    }

    @Test
    void testGetCategories() {
        List<String> categories = serviceManager.getCategories();

        assertEquals(4, categories.size());
        assertTrue(categories.contains("temperature"));
        assertTrue(categories.contains("length"));
        assertTrue(categories.contains("weight"));
        assertTrue(categories.contains("time"));
    }

    @Test
    void testGetUnitsForTemperatureCategory() {
        List<String> units = serviceManager.getUnitsForCategory("temperature");

        assertEquals(3, units.size());
        assertTrue(units.contains("celsius"));
        assertTrue(units.contains("fahrenheit"));
        assertTrue(units.contains("kelvin"));
    }

    @Test
    void testGetUnitsForLengthCategory() {
        List<String> units = serviceManager.getUnitsForCategory("length");

        assertEquals(5, units.size());
        assertTrue(units.contains("meter"));
        assertTrue(units.contains("kilometer"));
        assertTrue(units.contains("mile"));
        assertTrue(units.contains("inch"));
        assertTrue(units.contains("foot"));
    }

    @Test
    void testGetUnitsForWeightCategory() {
        List<String> units = serviceManager.getUnitsForCategory("weight");

        assertEquals(4, units.size());
        assertTrue(units.contains("gram"));
        assertTrue(units.contains("kilogram"));
        assertTrue(units.contains("pound"));
        assertTrue(units.contains("ounce"));
    }

    @Test
    void testGetUnitsForTimeCategory() {
        List<String> units = serviceManager.getUnitsForCategory("time");

        assertEquals(4, units.size());
        assertTrue(units.contains("seconds"));
        assertTrue(units.contains("minutes"));
        assertTrue(units.contains("hours"));
        assertTrue(units.contains("days"));
    }

    @Test
    void testGetUnitsForInvalidCategory() {
        assertThrows(InvalidUnitException.class, () -> {
            serviceManager.getUnitsForCategory("invalid");
        });
    }

    @Test
    void testConvertWithUnsupportedCategory() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", 25.0);

        when(temperatureService.supports("temperature")).thenReturn(false);
        when(lengthService.supports("temperature")).thenReturn(false);
        when(weightService.supports("temperature")).thenReturn(false);
        when(timeService.supports("temperature")).thenReturn(false);

        assertThrows(InvalidUnitException.class, () -> {
            serviceManager.convert(request);
        });
    }
}
