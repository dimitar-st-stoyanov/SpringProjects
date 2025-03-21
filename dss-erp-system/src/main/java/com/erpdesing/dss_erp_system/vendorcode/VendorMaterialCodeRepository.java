package com.erpdesing.dss_erp_system.vendorcode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorMaterialCodeRepository extends JpaRepository<VendorMaterialCode, Integer>{

	List<VendorMaterialCode> findTubeById(int material_id);
	
}
