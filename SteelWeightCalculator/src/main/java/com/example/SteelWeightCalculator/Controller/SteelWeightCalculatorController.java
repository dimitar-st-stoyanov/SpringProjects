package com.example.SteelWeightCalculator.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SteelWeightCalculator.Service.SteelWeightCalculatorService;
import com.example.SteelWeightCalculator.model.SteelSheet;

@RestController
@RequestMapping("/steel-weight-calculator")
public class SteelWeightCalculatorController {
	private final SteelWeightCalculatorService service;
	
	public SteelWeightCalculatorController(SteelWeightCalculatorService service) {
		this.service=service;
		
	}
	
	@GetMapping("/calculate-weight")
	public double calculateWeight(@RequestParam String type,
								 	@RequestParam double length,
								 	@RequestParam double width,
								 	@RequestParam double thickness) {
		SteelSheet sheet = new SteelSheet(type, length, width, thickness);
		return service.getWeight(sheet)*1000;
	}
}
