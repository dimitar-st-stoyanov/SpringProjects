package com.example.SteelWeightCalculator.Calculator;

public class SteelWeightCalculator {
	public static double calculate(double length, double width, double thickness, double density) {
		double lengthConverted = length/1000;
		double widthConverted = width/1000;
		double thicknessConverted = thickness/1000;
		
		return lengthConverted * widthConverted * thicknessConverted * density;
	}
}
