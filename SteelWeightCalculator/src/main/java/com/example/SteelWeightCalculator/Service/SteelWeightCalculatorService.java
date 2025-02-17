package com.example.SteelWeightCalculator.Service;


import org.springframework.stereotype.Service;

import com.example.SteelWeightCalculator.Calculator.SteelWeightCalculator;
import com.example.SteelWeightCalculator.model.SteelSheet;

@Service
public class SteelWeightCalculatorService {
	public double getWeight(SteelSheet sheet) {
		double density = sheet.getDensity();
		return SteelWeightCalculator.calculate(sheet.getLength(),sheet.getWidth(),sheet.getThickness(), density);
		
		
	}
}
