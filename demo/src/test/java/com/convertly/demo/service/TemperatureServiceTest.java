package com.convertly.demo.service;

import com.convertly.demo.enums.Category;
import com.convertly.demo.exception.InvalidUnitException;
import com.convertly.demo.model.ConversionRequest;
import com.convertly.demo.model.ConversionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureServiceTest {

    private TemperatureService temperatureService;

    @BeforeEach
    void setUp() {
        temperatureService = new TemperatureService();
    }

    @Test
    void testSupports() {
        assertTrue(temperatureService.supports("temperature"));
        assertTrue(temperatureService.supports("TEMPERATURE"));
        assertFalse(temperatureService.supports("length"));
        assertFalse(temperatureService.supports("weight"));
    }

    @Test
    void testCelsiusToFahrenheit() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", 25.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(77.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
        assertTrue(response.getFormula().contains("25.00°C"));
        assertTrue(response.getFormula().contains("77.00°F"));
    }

    @Test
    void testFahrenheitToCelsius() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "fahrenheit", "celsius", 77.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(25.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
    }

    @Test
    void testCelsiusToKelvin() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "kelvin", 0.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(273.15, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
    }

    @Test
    void testKelvinToCelsius() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "kelvin", "celsius", 273.15);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(0.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
    }

    @Test
    void testFahrenheitToKelvin() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "fahrenheit", "kelvin", 32.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(273.15, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
    }

    @Test
    void testKelvinToFahrenheit() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "kelvin", "fahrenheit", 273.15);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(32.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
    }

    @Test
    void testSameUnitConversion() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "celsius", 25.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(25.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getFormula());
    }

    @Test
    void testInvalidFromUnit() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "invalid", "celsius", 25.0);
        
        assertThrows(InvalidUnitException.class, () -> temperatureService.convert(request));
    }

    @Test
    void testInvalidToUnit() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "invalid", 25.0);
        
        assertThrows(InvalidUnitException.class, () -> temperatureService.convert(request));
    }

    @Test
    void testInvalidCategory() {
        ConversionRequest request = new ConversionRequest(Category.LENGTH, "celsius", "fahrenheit", 25.0);
        
        assertThrows(InvalidUnitException.class, () -> temperatureService.convert(request));
    }

    @Test
    void testCaseInsensitiveUnits() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "CELSIUS", "FAHRENHEIT", 0.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(32.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
    }

    @Test
    void testNegativeTemperature() {
        ConversionRequest request = new ConversionRequest(Category.TEMPERATURE, "celsius", "fahrenheit", -40.0);
        ConversionResponse response = temperatureService.convert(request);
        
        assertEquals(-40.0, response.getResult(), 0.001);
        assertEquals("success", response.getStatus());
    }
}
