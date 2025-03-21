package com.erpdesing.dss_erp_system.vendors;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class VendorService {
	
	private final VendorRepository vendorRepository;
 
	public  VendorService(VendorRepository vendorRepository) {
		this.vendorRepository = vendorRepository;
	}
	
	public List<Vendor> getAllVendors(){
		return (List<Vendor>) vendorRepository.findAll();
	}
	
	public Vendor addVendor(Vendor vendor) {
		return vendorRepository.save(vendor);
	}
	
	public boolean deleteVendor(int id) {
		Optional<Vendor> vendor = vendorRepository.findById(id);
		
		if (vendor.isPresent()) {
			vendorRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

}
