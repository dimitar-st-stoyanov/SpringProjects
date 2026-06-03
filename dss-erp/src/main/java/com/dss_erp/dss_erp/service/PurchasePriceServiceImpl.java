package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.*;
import com.dss_erp.dss_erp.payload.PurchasePriceDTO;
import com.dss_erp.dss_erp.payload.PurchasePriceResponse;
import com.dss_erp.dss_erp.repositories.PurchasePriceRepository;
import com.dss_erp.dss_erp.repositories.VendorMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchasePriceServiceImpl implements PurchasePriceService {

    private final PurchasePriceRepository purchasePriceRepository;
    private final VendorMaterialRepository vendorMaterialRepository;

    // -------------------------
    // Create
    // -------------------------

    @Override
    public PurchasePriceResponse createPurchasePrice(PurchasePriceDTO dto) {

        // 1️⃣ Resolve VendorMaterial using vendor + component
        VendorMaterial vendorMaterial = vendorMaterialRepository
                .findByVendor_IdAndComponentTypeAndComponentId(
                        dto.getVendorId(),
                        dto.getComponentType(),
                        dto.getComponentId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vendor is not assigned to this component"
                        )
                );

        if (!vendorMaterial.isActive()) {
            throw new IllegalArgumentException(
                    "VendorMaterial relation is not active"
            );
        }

        // 2️⃣ Validate dates
        validateDates(dto.getValidFrom(), dto.getValidTo());

        // 3️⃣ Check overlapping validity
        LocalDate effectiveValidTo = dto.getValidTo() != null
                ? dto.getValidTo()
                : LocalDate.of(9999, 12, 31);

        boolean overlapExists = purchasePriceRepository.existsOverlappingPrice(
                vendorMaterial.getId(),
                dto.getValidFrom(),
                effectiveValidTo
        );


        if (overlapExists) {
            throw new IllegalArgumentException(
                    "Overlapping price validity exists for this vendor material"
            );
        }

        // 4️⃣ Build entity
        PurchasePrice price = PurchasePrice.builder()
                .vendorMaterial(vendorMaterial)
                .price(dto.getPrice())
                .currency(dto.getCurrency())
                .priceSource(dto.getPriceSource())
                .validFrom(dto.getValidFrom())
                .validTo(dto.getValidTo())
                .referenceNumber(dto.getReferenceNumber())
                .minimumOrderQuantity(dto.getMinimumOrderQuantity())
                .active(true)
                .build();

        // 5️⃣ Save
        PurchasePrice saved = purchasePriceRepository.save(price);

        // 6️⃣ Return response
        return mapToResponse(saved);
    }

    @Override
    public Page<PurchasePriceResponse> getAllPurchasePrices(Pageable pageable) {
        return purchasePriceRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }


    // -------------------------
    // Read
    // -------------------------

    @Override
    public PurchasePriceResponse getPriceById(UUID priceId) {
        PurchasePrice price = purchasePriceRepository.findById(priceId)
                .orElseThrow(() -> new IllegalArgumentException("PurchasePrice not found"));

        return mapToResponse(price);
    }

    @Override
    public Page<PurchasePriceResponse> getPricesByVendorMaterial(
            UUID vendorMaterialId,
            Pageable pageable
    ) {
        return purchasePriceRepository
                .findAllByVendorMaterial_Id(vendorMaterialId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public PurchasePriceResponse getValidPriceOnDate(
            UUID vendorMaterialId,
            LocalDate date
    ) {
        PurchasePrice price = purchasePriceRepository
                .findValidPriceOnDate(vendorMaterialId, date)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No valid price found for given date"
                ));

        return mapToResponse(price);
    }

    @Override
    public PurchasePriceResponse getReferencePriceForComponent(BomComponentType componentType, Long componentId) {
        LocalDate today = LocalDate.now();

        // 1️⃣ Find preferred VendorMaterial
        VendorMaterial preferred = vendorMaterialRepository
                .findByComponentTypeAndComponentIdAndPreferredTrueAndActiveTrue(
                        componentType,
                        componentId
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No preferred vendor material found for component: "
                                        + componentType + " / " + componentId));

        UUID vendorMaterialId = preferred.getId();

        // 2️⃣ Try valid QUOTATION for today
        Optional<PurchasePrice> quotationPrice =
                purchasePriceRepository
                        .findValidPriceByVendorMaterialAndPriceSource(
                                vendorMaterialId,
                                PriceSourceType.QUOTATION,
                                today
                        );

        if (quotationPrice.isPresent()) {
            return mapToResponse(quotationPrice.get());
        }

        // 3️⃣ Fallback to latest PURCHASE_ORDER price
        Optional<PurchasePrice> latestPo =
                purchasePriceRepository
                        .findTopByVendorMaterial_IdAndPriceSourceAndActiveTrueOrderByValidFromDesc(
                                vendorMaterialId,
                                PriceSourceType.PURCHASE_ORDER
                        );

        return latestPo
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No reference price found (quotation or purchase order) for component: "
                                        + componentType + " / " + componentId));
    }

    // -------------------------
    // Lifecycle
    // -------------------------

    @Override
    public void deactivatePrice(UUID priceId) {
        PurchasePrice price = purchasePriceRepository.findById(priceId)
                .orElseThrow(() -> new IllegalArgumentException("PurchasePrice not found"));

        price.setActive(false);
        purchasePriceRepository.save(price);
    }

    // -------------------------
    // Validation
    // -------------------------

    private void validateDates(LocalDate validFrom, LocalDate validTo) {
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException(
                    "validTo must be after or equal to validFrom"
            );
        }
    }

    // -------------------------
    // Mapping
    // -------------------------

    private PurchasePriceResponse mapToResponse(PurchasePrice entity) {
        return PurchasePriceResponse.builder()
                .id(entity.getId())
                .vendorMaterialId(entity.getVendorMaterial().getId())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .priceSource(entity.getPriceSource())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .referenceNumber(entity.getReferenceNumber())
                .minimumOrderQuantity(entity.getMinimumOrderQuantity())
                .active(entity.isActive())
                .build();
    }

    @Override
    public Map<UUID, PurchasePriceResponse> getReferencePricesForVendorMaterials(
            Set<UUID> vendorMaterialIds
    ) {

        LocalDate today = LocalDate.now();

        Map<UUID, PurchasePriceResponse> result = new HashMap<>();

        if (vendorMaterialIds.isEmpty()) {
            return result;
        }

        // 1️⃣ Fetch valid QUOTATION prices
        List<PurchasePrice> quotations =
                purchasePriceRepository.findValidPricesByVendorMaterials(
                        vendorMaterialIds,
                        PriceSourceType.QUOTATION,
                        today
                );

        for (PurchasePrice price : quotations) {
            result.put(
                    price.getVendorMaterial().getId(),
                    mapToResponse(price)
            );
        }

        // 2️⃣ Find vendorMaterials without quotation
        Set<UUID> missingVendorMaterials = vendorMaterialIds.stream()
                .filter(id -> !result.containsKey(id))
                .collect(Collectors.toSet());

        if (missingVendorMaterials.isEmpty()) {
            return result;
        }

        // 3️⃣ Fetch purchase order prices
        List<PurchasePrice> purchaseOrders =
                purchasePriceRepository
                        .findByVendorMaterial_IdInAndPriceSourceAndActiveTrue(
                                missingVendorMaterials,
                                PriceSourceType.PURCHASE_ORDER
                        );

        // 4️⃣ Group and pick latest validFrom
        Map<UUID, PurchasePrice> latestPo = new HashMap<>();

        for (PurchasePrice price : purchaseOrders) {

            UUID vmId = price.getVendorMaterial().getId();

            PurchasePrice existing = latestPo.get(vmId);

            if (existing == null ||
                    price.getValidFrom().isAfter(existing.getValidFrom())) {

                latestPo.put(vmId, price);
            }
        }

        // 5️⃣ Map to response
        for (Map.Entry<UUID, PurchasePrice> entry : latestPo.entrySet()) {
            result.put(
                    entry.getKey(),
                    mapToResponse(entry.getValue())
            );
        }

        return result;
    }
}


