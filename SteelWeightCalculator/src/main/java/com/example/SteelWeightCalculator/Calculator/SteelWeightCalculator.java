package com.example.SteelWeightCalculator.Calculator;

public class SteelWeightCalculator {
	public static double calculate(double length, double width, double thickness, double density) {
		return length * width * thickness * density;
	}
}
