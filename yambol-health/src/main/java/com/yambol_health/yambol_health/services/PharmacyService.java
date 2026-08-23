package com.yambol_health.yambol_health.services;

import com.yambol_health.yambol_health.payloads.PharmacyDTO;
import com.yambol_health.yambol_health.payloads.PharmacyResponse;

public interface PharmacyService {
    PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO);

    PharmacyResponse getAllPharmacies(int pageNumber, int pageSize, String sortBy, String sortOrder);

    PharmacyDTO getPharmacyById(Long pharmacyId);

    PharmacyDTO updatePharmacy(Long pharmacyId, PharmacyDTO pharmacyDTO);

    void deletePharmacy(Long pharmacyId);
}
