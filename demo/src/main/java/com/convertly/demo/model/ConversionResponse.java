package com.convertly.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response model for unit conversion.
 */
@Schema(description = "Response payload for unit conversion")
public class ConversionResponse {

    @Schema(description = "The converted result", example = "77.0")
    private Double result;

    @Schema(description = "The formula used for conversion", 
            example = "(25°C × 9/5) + 32 = 77°F")
    private String formula;

    @Schema(description = "The original input request")
    private ConversionRequest originalInput;

    @Schema(description = "Status of the conversion", example = "success")
    private String status;

    // Default constructor
    public ConversionResponse() {}

    // Constructor with all fields
    public ConversionResponse(Double result, String formula, ConversionRequest originalInput, String status) {
        this.result = result;
        this.formula = formula;
        this.originalInput = originalInput;
        this.status = status;
    }

    // Convenience constructor for successful conversions
    public ConversionResponse(Double result, String formula, ConversionRequest originalInput) {
        this(result, formula, originalInput, "success");
    }

    // Getters and setters
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public ConversionRequest getOriginalInput() {
        return originalInput;
    }

    public void setOriginalInput(ConversionRequest originalInput) {
        this.originalInput = originalInput;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConversionResponse{" +
                "result=" + result +
                ", formula='" + formula + '\'' +
                ", originalInput=" + originalInput +
                ", status='" + status + '\'' +
                '}';
    }
}
