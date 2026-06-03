package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.payload.PurchasePriceDTO;
import com.dss_erp.dss_erp.payload.PurchasePriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public interface PurchasePriceService {

    PurchasePriceResponse createPurchasePrice(PurchasePriceDTO dto);

    Page<PurchasePriceResponse> getAllPurchasePrices(Pageable pageable);

    PurchasePriceResponse getPriceById(UUID priceId);

    Page<PurchasePriceResponse> getPricesByVendorMaterial(UUID vendorMaterialId,Pageable pageable);

    PurchasePriceResponse getValidPriceOnDate(UUID vendorMaterialId,LocalDate date);

    PurchasePriceResponse getReferencePriceForComponent(BomComponentType componentType,Long componentId);

    void deactivatePrice(UUID priceId);

    Map<UUID, PurchasePriceResponse> getReferencePricesForVendorMaterials(
            Set<UUID> vendorMaterialIds
    );
}