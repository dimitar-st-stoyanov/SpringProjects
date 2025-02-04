package com.example.simplespringap.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
class CalculatorController {

    @GetMapping("/calculate")
    public CalculationResult calculate(@RequestParam String operation, @RequestParam double num1, @RequestParam double num2) {
        double result = 0;
        
        switch (operation) {
            case "add":
                result = num1 + num2;
                break;
            case "subtract":
                result = num1 - num2;
                break;
            case "multiply":
                result = num1 * num2;
                break;
            case "divide":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown operation");
        }
        
        return new CalculationResult(result);
    }
}

class CalculationResult {
    private double result;

    public CalculationResult(double result) {
        this.result = result;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
