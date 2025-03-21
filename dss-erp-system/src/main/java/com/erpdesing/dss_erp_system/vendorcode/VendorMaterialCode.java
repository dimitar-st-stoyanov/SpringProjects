package com.erpdesing.dss_erp_system.vendorcode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendor_codes")
public class VendorMaterialCode {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;
	
	private int material_id;
	
    private int vendor_id;
	
	private String vendorCode;
	
	public VendorMaterialCode () {};
	
	public VendorMaterialCode(int material_id, int vendor, String code) {
		this.setVendor_id(vendor);
		this.setMaterial_id(material_id);
		this.setVendorCode(code);
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	
	public int getId() {
		return id;
	}

	public int getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(int vendor_id) {
		this.vendor_id = vendor_id;
	}

	public int getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(int material_id) {
		this.material_id = material_id;
	}
}
