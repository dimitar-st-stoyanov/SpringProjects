package com.erpdesing.dss_erp_system.vendors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "vendors")
public class Vendor {
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	
	private int id;
	private String name;
	private String contactNumber;
	private String email;
	private String address;
	private String country;
	private String VATnumber;
	
	private Vendor() {};
	
	public Vendor(String name, String contactNumber, String email, String address, String country, String VATumber) {
		this.name = name;
		this.contactNumber = contactNumber;
		this.email = email;
		this.address = address;
		this.country = country;
		this.VATnumber = VATumber;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getVATnumber() {
		return VATnumber;
	}
	public void setVATnumber(String vATnumber) {
		VATnumber = vATnumber;
	}
	
	
}
