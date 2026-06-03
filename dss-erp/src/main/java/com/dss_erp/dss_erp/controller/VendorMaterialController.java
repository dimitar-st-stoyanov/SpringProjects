package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.payload.VendorMaterialDTO;
import com.dss_erp.dss_erp.payload.VendorMaterialResponse;
import com.dss_erp.dss_erp.service.VendorMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vendor-materials")
@RequiredArgsConstructor
public class VendorMaterialController {

    private final VendorMaterialService vendorMaterialService;

    // -------------------------
    // CRUD
    // -------------------------

    @PostMapping
    public ResponseEntity<VendorMaterialResponse> createVendorMaterial(
            @Valid @RequestBody VendorMaterialDTO dto
    ) {
        VendorMaterialResponse response = vendorMaterialService.createVendorMaterial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorMaterialResponse> updateVendorMaterial(
            @PathVariable UUID id,
            @Valid @RequestBody VendorMaterialDTO dto
    ) {
        VendorMaterialResponse response = vendorMaterialService.updateVendorMaterial(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorMaterialResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(vendorMaterialService.getVendorMaterialById(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        vendorMaterialService.deactivateVendorMaterial(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------
    // Business lookups
    // -------------------------

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<Page<VendorMaterialResponse>> getMaterialsByVendor(
            @PathVariable UUID vendorId,
            @PageableDefault(size = 20, sort = "vendorMaterialName") Pageable pageable
    ) {
        Page<VendorMaterialResponse> page = vendorMaterialService.getMaterialsByVendor(vendorId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/component")
    public ResponseEntity<Page<VendorMaterialResponse>> getVendorsByComponent(
            @RequestParam BomComponentType componentType,
            @RequestParam Long componentId,
            @PageableDefault(size = 20, sort = "vendorMaterialCode") Pageable pageable
    ) {
        Page<VendorMaterialResponse> page = vendorMaterialService.getVendorsByComponent(
                componentType, componentId, pageable
        );
        return ResponseEntity.ok(page);
    }

    @GetMapping("/vendor/{vendorId}/component")
    public ResponseEntity<VendorMaterialResponse> getByVendorAndComponent(
            @PathVariable UUID vendorId,
            @RequestParam BomComponentType componentType,
            @RequestParam Long componentId
    ) {
        VendorMaterialResponse response = vendorMaterialService.getByVendorAndComponent(
                vendorId, componentType, componentId
        );
        return ResponseEntity.ok(response);
    }
}
