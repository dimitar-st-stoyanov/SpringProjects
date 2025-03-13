package com.erpdesing.dss_erp_system.rawmaterial.bars;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bars")
public class Bars {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	private String internalName;
	private String material;
	private double diameter;
	private double length;
	private double weight;
	
	private Bars() {};
	
	
    
	public Bars(String material, double diameter, double length, double weight) {
		
		this.setMaterial(material);
		this.setDiameter(diameter);
		
		this.setLength(length);
		this.setWeight(weight);	
		updateInternalName();
		
	}
	
	public void updateInternalName() {
		
			if(diameter%1==0) {
				this.internalName = String.format("Bar D%.0f %s", diameter, material);
			}else {
				this.internalName = String.format("Bar D%.1f %s", diameter, material);
			}
		
		
	}
	
	public int getId() {
		return id;
	}

	public String getInternalName() {
		return internalName;
	}

	
	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
		updateInternalName();
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
		updateInternalName();
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
