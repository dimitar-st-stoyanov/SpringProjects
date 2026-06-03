package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.payload.VendorMaterialDTO;
import com.dss_erp.dss_erp.payload.VendorMaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VendorMaterialService {

    // -------------------------
    // CRUD
    // -------------------------

    VendorMaterialResponse createVendorMaterial(VendorMaterialDTO dto);

    VendorMaterialResponse updateVendorMaterial(UUID vendorMaterialId, VendorMaterialDTO dto);

    VendorMaterialResponse getVendorMaterialById(UUID vendorMaterialId);

    void deactivateVendorMaterial(UUID vendorMaterialId);

    // -------------------------
    // Business lookups
    // -------------------------

    // All materials of a vendor
    Page<VendorMaterialResponse> getMaterialsByVendor(
            UUID vendorId,
            Pageable pageable
    );

    // All vendors for a component
    Page<VendorMaterialResponse> getVendorsByComponent(
            BomComponentType componentType,
            Long componentId,
            Pageable pageable
    );

    // Direct business-key lookup
    VendorMaterialResponse getByVendorAndComponent(
            UUID vendorId,
            BomComponentType componentType,
            Long componentId
    );
}
