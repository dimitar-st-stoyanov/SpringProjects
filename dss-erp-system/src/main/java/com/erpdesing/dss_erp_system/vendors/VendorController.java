package com.erpdesing.dss_erp_system.vendors;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendors")
@CrossOrigin ("*")
public class VendorController {


    private final VendorService vendorService;
    
    public VendorController (VendorService vendorService) {
    	this.vendorService = vendorService;
    }
    
    @GetMapping
    public ResponseEntity<List<Vendor>> findAllVendors(){
    	return ResponseEntity.ok(vendorService.getAllVendors());
    }
    
    @PostMapping
    public ResponseEntity<Vendor> addVendor(@RequestBody Vendor vendor){
    	Vendor savedVendor = vendorService.addVendor(vendor);
    	return ResponseEntity.status(201).body(savedVendor);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable int id){
    	boolean isDeleted = vendorService.deleteVendor(id);
    	if(isDeleted) {
    		return ResponseEntity.noContent().build();
    	}else {
    		return ResponseEntity.notFound().build();
    	}
    }
    
   /* @GetMapping("/{id}")
    public Vendor findVendorById(@PathVariable int id) {
        return vendorRepository.findById(id).orElse(null);
    }


    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable int id) {
        vendorRepository.deleteById(id);
    }

    
    @PutMapping("/{id}")
    public Vendor updateVendor(@PathVariable int id, @RequestBody Vendor vendor) {
        return vendorRepository.save(vendor);
    }*/
}
