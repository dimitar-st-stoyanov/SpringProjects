package com.erpdesing.dss_erp_system.vendorcode;

import com.erpdesing.dss_erp_system.rawmaterials.sheets.Sheets;
import com.erpdesing.dss_erp_system.vendors.Vendor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendor_codes")
public class VendorMaterialCode {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "sheet_id", nullable = false)
    private Sheets sheet;

    
    private int  vendor_id;

    private String vendorCode;

    
    public VendorMaterialCode() {}

    
    public VendorMaterialCode(Sheets material_id, int vendor_id, String vendorCode) {
        this.sheet = material_id;
        this.vendor_id = vendor_id;
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

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }


	public Sheets getMaterial_id() {
		return sheet;
	}


	public void setMaterial_id(Sheets material_id) {
		this.sheet = material_id;
	}
}