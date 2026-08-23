package com.yambol_health.yambol_health.controllers;

import com.yambol_health.yambol_health.payloads.APIResponse;
import com.yambol_health.yambol_health.payloads.PharmacyDTO;
import com.yambol_health.yambol_health.payloads.PharmacyResponse;
import com.yambol_health.yambol_health.services.PharmacyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @PostMapping
    public ResponseEntity<PharmacyDTO> createPharmacy(@Valid @RequestBody PharmacyDTO pharmacyDTO) {
        return new ResponseEntity<>(pharmacyService.createPharmacy(pharmacyDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PharmacyResponse> getAllPharmacies(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") int pageNumber,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok(pharmacyService.getAllPharmacies(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{pharmacyId}")
    public ResponseEntity<PharmacyDTO> getPharmacyById(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(pharmacyService.getPharmacyById(pharmacyId));
    }

    @PutMapping("/{pharmacyId}")
    public ResponseEntity<PharmacyDTO> updatePharmacy(@PathVariable Long pharmacyId,
                                                        @Valid @RequestBody PharmacyDTO pharmacyDTO) {
        return ResponseEntity.ok(pharmacyService.updatePharmacy(pharmacyId, pharmacyDTO));
    }

    @DeleteMapping("/{pharmacyId}")
    public ResponseEntity<APIResponse> deletePharmacy(@PathVariable Long pharmacyId) {
        pharmacyService.deletePharmacy(pharmacyId);
        return ResponseEntity.ok(new APIResponse("Pharmacy deleted successfully", true));
    }
}
