package com.erpdesing.dss_erp_system.rawmaterial.tubes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tubes")
public class Tube {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	private String internalName;
	private String material;
	private double diameter;
	private double thickness;
	private double length;
	private double weight;
	
	private Tube() {};
	
	public Tube(String internalName, String material, double diameter, double thickness, double length, double weight) {
		this.setInternalName(internalName);
		this.setMaterial(material);
		this.setDiameter(diameter);
		this.setThickness(thickness);
		this.setLength(length);
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

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}