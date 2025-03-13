package com.erpdesing.dss_erp_system.rawmaterials.sheets;

import java.util.Random;

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
	
		this.setMaterial(material);
		this.setThickness(thickness);
		this.setLength(length);
		this.setWidth(width);
		this.setWeight(weight);
		updateName();
		
	}
	
	
	public int getId() {
		return id;
	}

	public void updateName() {
		
		if(thickness%1==0) {
			this.internalName = String.format("Sheet %s %.0f mm %.0fx%.0f", material, thickness, length, width);
			}else {
				this.internalName = String.format("Sheet %s %.1f mm %.0fx%.0f", material, thickness, length, width);
			}
		
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
		updateName();
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
		updateName();
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
		updateName();
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		updateName();
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}