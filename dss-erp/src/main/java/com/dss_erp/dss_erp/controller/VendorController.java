package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.payload.VendorDTO;
import com.dss_erp.dss_erp.payload.VendorResponse;
import com.dss_erp.dss_erp.service.VendorService;
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
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<VendorResponse> createVendor(
            @Valid @RequestBody VendorDTO vendorDTO
    ) {
        VendorResponse response = vendorService.createVendor(vendorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{vendorId}")
    public ResponseEntity<VendorResponse> updateVendor(
            @PathVariable UUID vendorId,
            @Valid @RequestBody VendorDTO vendorDTO
    ) {
        VendorResponse response = vendorService.updateVendor(vendorId, vendorDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorResponse> getVendorById(
            @PathVariable UUID vendorId
    ) {
        return ResponseEntity.ok(
                vendorService.getVendorById(vendorId)
        );
    }

    @GetMapping("/code/{vendorCode}")
    public ResponseEntity<VendorResponse> getVendorByCode(
            @PathVariable String vendorCode
    ) {
        return ResponseEntity.ok(
                vendorService.getVendorByCode(vendorCode)
        );
    }

    @GetMapping
    public ResponseEntity<Page<VendorResponse>> getAllVendors(
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(
                vendorService.getAllVendors(pageable)
        );
    }

    @PatchMapping("/{vendorId}/deactivate")
    public ResponseEntity<Void> deactivateVendor(
            @PathVariable UUID vendorId
    ) {
        vendorService.deactivateVendor(vendorId);
        return ResponseEntity.noContent().build();
    }
}
