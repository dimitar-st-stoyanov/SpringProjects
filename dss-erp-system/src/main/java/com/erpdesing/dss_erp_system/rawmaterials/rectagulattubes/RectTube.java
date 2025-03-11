package com.erpdesing.dss_erp_system.rawmaterials.rectagulattubes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name="rectangulartubes")
public class RectTube {
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	
	private int id;
	private String internalName;
	private String material;
	private double sideA;
	private double sideB;
	private double thickness;
	private double length;
	private double weight;
	
	private RectTube() {};
	
	public RectTube(String internalName, String material, double sideA, double sideB, double thickness, double length, double weight) {
		this.setInternalName(internalName);
		this.setMaterial(material);
		this.setSideA(sideA);
		this.setSideB(sideB);
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

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public double getSideA() {
		return sideA;
	}

	public void setSideA(double sideA) {
		this.sideA = sideA;
	}

	public double getSideB() {
		return sideB;
	}

	public void setSideB(double sideB) {
		this.sideB = sideB;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
}
