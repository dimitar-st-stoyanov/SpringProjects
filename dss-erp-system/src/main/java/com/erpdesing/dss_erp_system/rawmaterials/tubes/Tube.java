package com.erpdesing.dss_erp_system.rawmaterials.tubes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tubes")
public class Tube {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;
	private String internalName;
	private String material;
	private double diameter;
	private double thickness;
	private double weight;
	
	private Tube() {};
	
	
    
	public Tube(String material, double diameter, double thickness, double weight) {
		
		this.setMaterial(material);
		this.setDiameter(diameter);
		this.setThickness(thickness);
		this.setWeight(weight);	
		updateInternalName();
		
	}
	
	public void updateInternalName() {
		if(thickness%1==0) {
			if(diameter%1==0) {
				this.internalName = String.format("Tube D%.0fx%.0f %s", diameter, thickness, material);
			}else {
				this.internalName = String.format("Tube D%.1fx%.0f %s", diameter, thickness, material);
			}
		}else {
			if(diameter%1==0) {
				this.internalName = String.format("Tube D%.0fx%.1f %s", diameter, thickness, material);
			}else {
				this.internalName = String.format("Tube D%.1fx%.1f %s", diameter, thickness, material);
			}
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

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
		updateInternalName();
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}