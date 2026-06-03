package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.VendorDTO;
import com.dss_erp.dss_erp.payload.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface VendorService {

    VendorResponse createVendor(VendorDTO vendorDTO);

    VendorResponse updateVendor(UUID vendorId, VendorDTO vendorDTO);

    VendorResponse getVendorById(UUID vendorId);

    VendorResponse getVendorByCode(String vendorCode);

    Page<VendorResponse> getAllVendors(Pageable pageable);

    void deactivateVendor(UUID vendorId);
}
