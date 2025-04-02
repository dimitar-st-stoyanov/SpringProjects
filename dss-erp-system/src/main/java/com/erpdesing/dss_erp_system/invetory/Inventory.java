package com.erpdesing.dss_erp_system.invetory;

import java.security.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "invetory")
public class Inventory {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	

	private int material_id;
	
	private int warehouse_id;
	private int vendor_id;
	private double quantity;
	private String unit;
	private Timestamp lastPriceUpdate;
	
	public Inventory () {};
	
	
	
	

}
