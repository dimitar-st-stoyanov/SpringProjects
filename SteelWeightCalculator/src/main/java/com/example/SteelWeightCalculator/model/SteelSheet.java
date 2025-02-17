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
	
	public double getDensity() {
		double density = 0;
		switch (type) {
		case "304": density = 7.93; break;
		case "316":
		case "310":
		case "317": density = 7.98; break;
		case "321": density = 7.92; break;
		case "17-4-PH":
		case "409":
		case "410":
		case "420": density = 7.75; break;
		case "430":
		case "439": density = 7.70; break;
		case "444":
		case "440C":
		case "2205": density = 7.80; break;
		case "2507": density = 7.85; break;
		case "Carbon Steel": density = 7.85; break;
		
		default: System.out.println("No such as type"); break;
		}
		return density;
	}
	
}

/*1. Austenitic Stainless Steels
304 → 7.93 g/cm³//
316 → 7.98 g/cm³//
321 → 7.92 g/cm³//
310 → 7.98 g/cm³//
317 → 7.98 g/cm³//
2. Ferritic Stainless Steels
409 → 7.75 g/cm³//
430 → 7.70 g/cm³//
439 → 7.70 g/cm³//
444 → 7.80 g/cm³
3. Martensitic Stainless Steels
410 → 7.75 g/cm³//
420 → 7.75 g/cm³//
440C → 7.80 g/cm³
4. Duplex Stainless Steels
2205 → 7.80 g/cm³
2507 → 7.85 g/cm³
5. Precipitation Hardening (PH) Stainless Steels
17-4 PH → 7.75 g/cm³*/