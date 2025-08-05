package com.convertly.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI/Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Convertly - Unit Converter API")
                        .description("""
                                A clean and organized REST API for converting units across different measurement categories.
                                
                                ## Supported Categories:
                                - **Temperature**: Celsius, Fahrenheit, Kelvin
                                - **Length**: Meter, Kilometer, Mile, Inch, Foot
                                - **Weight**: Gram, Kilogram, Pound, Ounce
                                - **Time**: Seconds, Minutes, Hours, Days
                                
                                ## Features:
                                - Precise calculations with detailed formulas
                                - Input validation and structured error responses
                                - Case-insensitive unit mapping
                                - Comprehensive API documentation
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Convertly API Team")
                                .email("support@convertly.com")
                                .url("https://convertly.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.convertly.com")
                                .description("Production server")
                ));
    }
}
