package com.erpdesing.dss_erp_system.rawmaterials.sheets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sheets")
public class Sheets {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	private String internalName;
	private String material;
	private double thickness;
	private double length;
	private double width;
	private double weight;
	
	private Sheets() {};
	
	public Sheets(String internalName, String material, double thickness, double length, double width, double weight) {
		this.setInternalName(internalName);
		this.setMaterial(material);
		this.setThickness(thickness);
		this.setLength(length);
		this.setWidth(width);
		this.setWeight(weight);
	}
	
	public int getId() {
		return id;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}