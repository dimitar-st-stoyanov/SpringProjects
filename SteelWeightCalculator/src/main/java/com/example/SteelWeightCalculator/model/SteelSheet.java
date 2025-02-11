package com.example.SteelWeightCalculator.model;

public class SteelSheet {
	private String type;
	private double length;
	private double width;
	private double thickness;
		
	public SteelSheet(String type, double lenght,double width, double thickness) {
		this.type = type;
		this.length = lenght;
		this.width = width;
		this.thickness = thickness;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getThickness() {
		return thickness;
	}
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	
	
}
