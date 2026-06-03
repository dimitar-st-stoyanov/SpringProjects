package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.payload.PurchasePriceDTO;
import com.dss_erp.dss_erp.payload.PurchasePriceResponse;
import com.dss_erp.dss_erp.service.PurchasePriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/purchasing/purchase-prices")
@RequiredArgsConstructor
public class PurchasePriceController {

    private final PurchasePriceService purchasePriceService;

    @PostMapping
    public ResponseEntity<PurchasePriceResponse> createPrice(
            @Valid @RequestBody PurchasePriceDTO dto
    ) {
        PurchasePriceResponse response = purchasePriceService.createPurchasePrice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------
    // Read
    // -------------------------

    @GetMapping
    public ResponseEntity<Page<PurchasePriceResponse>> getAll(@PageableDefault (size = 20, sort = "validFrom") Pageable pageable){
        Page<PurchasePriceResponse> page = purchasePriceService.getAllPurchasePrices(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchasePriceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(purchasePriceService.getPriceById(id));
    }

    @GetMapping("/vendor-material/{vendorMaterialId}")
    public ResponseEntity<Page<PurchasePriceResponse>> getPricesByVendorMaterial(
            @PathVariable UUID vendorMaterialId,
            @PageableDefault(size = 20, sort = "validFrom") Pageable pageable
    ) {
        Page<PurchasePriceResponse> page =
                purchasePriceService.getPricesByVendorMaterial(vendorMaterialId, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * BOM costing endpoint
     * Example:
     * /api/purchase-prices/valid?vendorMaterialId=...&date=2026-01-01
     */
    @GetMapping("/valid")
    public ResponseEntity<PurchasePriceResponse> getValidPrice(
            @RequestParam UUID vendorMaterialId,
            @RequestParam(required = false) LocalDate date
    ) {
        LocalDate effectiveDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(
                purchasePriceService.getValidPriceOnDate(vendorMaterialId, effectiveDate)
        );
    }

    @GetMapping("component_price")
    public ResponseEntity<PurchasePriceResponse> getReferencePriceForComponent(
            @RequestParam BomComponentType componentType,
            @RequestParam  Long componentId){

               return ResponseEntity.ok(
                purchasePriceService.getReferencePriceForComponent(componentType, componentId)
        );
    }

    // -------------------------
    // Lifecycle
    // -------------------------

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        purchasePriceService.deactivatePrice(id);
        return ResponseEntity.noContent().build();
    }
}
