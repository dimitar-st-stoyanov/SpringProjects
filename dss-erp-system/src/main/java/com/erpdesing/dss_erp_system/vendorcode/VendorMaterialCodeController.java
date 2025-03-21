package com.erpdesing.dss_erp_system.vendorcode;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor-code")
public class VendorMaterialCodeController {

	private VendorMaterialCodeRepository vendorMat;
	
	public VendorMaterialCodeController(VendorMaterialCodeRepository vendorMat) {
		this.vendorMat = vendorMat;
	}
	
	@GetMapping
	public List<VendorMaterialCode> getAllCodes(){
		return vendorMat.findAll();
	}
	
	@PostMapping
	public VendorMaterialCode addVendorCode(@RequestBody VendorMaterialCode vendMat) {
		return vendorMat.save(vendMat);
	}
	
	@GetMapping ("/{material_id}")
	public List<VendorMaterialCode> getMaterialVendorCodes(@PathVariable int material_id){
		return vendorMat.findTubeById(material_id);
	}
	
}
