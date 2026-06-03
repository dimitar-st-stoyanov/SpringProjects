package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.Vendor;
import com.dss_erp.dss_erp.models.VendorMaterial;
import com.dss_erp.dss_erp.payload.VendorMaterialDTO;
import com.dss_erp.dss_erp.payload.VendorMaterialResponse;
import com.dss_erp.dss_erp.repositories.VendorMaterialRepository;
import com.dss_erp.dss_erp.repositories.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorMaterialServiceImpl implements VendorMaterialService {

    private final VendorMaterialRepository vendorMaterialRepository;
    private final VendorRepository vendorRepository;

    @Override
    @Transactional
    public VendorMaterialResponse createVendorMaterial(VendorMaterialDTO dto) {

        Vendor vendor = vendorRepository.findById(UUID.fromString(dto.getVendorId().toString()))
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        boolean exists = vendorMaterialRepository
                .existsByVendor_IdAndComponentTypeAndComponentId(
                        vendor.getId(),
                        dto.getComponentType(),
                        dto.getComponentId()
                );

        if (exists) {
            throw new IllegalArgumentException(
                    "Vendor material already exists for this vendor and component"
            );
        }

        boolean preferredRequested = dto.getPreferred() != null && dto.getPreferred();

        boolean preferredExists = vendorMaterialRepository
                .existsByVendor_IdAndComponentTypeAndComponentIdAndPreferredTrue(
                        vendor.getId(),
                        dto.getComponentType(),
                        dto.getComponentId()
                );

        boolean finalPreferred;

        if (preferredRequested) {
            // If new one is preferred → reset others
            vendorMaterialRepository.resetPreferredForVendorAndComponent(
                    vendor.getId(),
                    dto.getComponentType(),
                    dto.getComponentId()
            );
            finalPreferred = true;
        } else {
            // If no preferred exists → force this one to true
            finalPreferred = !preferredExists;
        }

        VendorMaterial vendorMaterial = VendorMaterial.builder()
                .vendor(vendor)
                .componentType(dto.getComponentType())
                .componentId(dto.getComponentId())
                .vendorMaterialCode(dto.getVendorMaterialCode())
                .vendorMaterialName(dto.getVendorMaterialName())
                .leadTimeDays(dto.getLeadTimeDays())
                .minimumOrderQuantity(dto.getMinimumOrderQuantity())
                .orderMultiple(dto.getOrderMultiple())
                .preferred(finalPreferred)
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();

        VendorMaterial saved = vendorMaterialRepository.save(vendorMaterial);

        return mapToResponse(saved);
    }

    @Override
    public VendorMaterialResponse updateVendorMaterial(
            UUID vendorMaterialId,
            VendorMaterialDTO dto
    ) {

        VendorMaterial vendorMaterial = vendorMaterialRepository.findById(vendorMaterialId)
                .orElseThrow(() -> new IllegalArgumentException("VendorMaterial not found"));

        if (dto.getVendorMaterialCode() != null) {
            vendorMaterial.setVendorMaterialCode(dto.getVendorMaterialCode());
        }
        if (dto.getVendorMaterialName() != null) {
            vendorMaterial.setVendorMaterialName(dto.getVendorMaterialName());
        }
        if (dto.getLeadTimeDays() != null) {
            vendorMaterial.setLeadTimeDays(dto.getLeadTimeDays());
        }
        if (dto.getMinimumOrderQuantity() != null) {
            vendorMaterial.setMinimumOrderQuantity(dto.getMinimumOrderQuantity());
        }
        if (dto.getOrderMultiple() != null) {
            vendorMaterial.setOrderMultiple(dto.getOrderMultiple());
        }
        if (dto.getPreferred() != null) {
            vendorMaterial.setPreferred(dto.getPreferred());
        }
        if (dto.getActive() != null) {
            vendorMaterial.setActive(dto.getActive());
        }

        VendorMaterial updated = vendorMaterialRepository.save(vendorMaterial);

        return mapToResponse(updated);
    }

    @Override
    public VendorMaterialResponse getVendorMaterialById(UUID vendorMaterialId) {
        VendorMaterial vendorMaterial = vendorMaterialRepository.findById(vendorMaterialId)
                .orElseThrow(() -> new IllegalArgumentException("VendorMaterial not found"));

        return mapToResponse(vendorMaterial);
    }

    @Override
    public void deactivateVendorMaterial(UUID vendorMaterialId) {
        VendorMaterial vendorMaterial = vendorMaterialRepository.findById(vendorMaterialId)
                .orElseThrow(() -> new IllegalArgumentException("VendorMaterial not found"));

        vendorMaterial.setActive(false);
        vendorMaterialRepository.save(vendorMaterial);
    }

    @Override
    public Page<VendorMaterialResponse> getMaterialsByVendor(
            UUID vendorId,
            Pageable pageable
    ) {
        return vendorMaterialRepository.findAllByVendor_Id(vendorId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<VendorMaterialResponse> getVendorsByComponent(
            BomComponentType componentType,
            Long componentId,
            Pageable pageable
    ) {
        return vendorMaterialRepository
                .findAllByComponentTypeAndComponentId(componentType, componentId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public VendorMaterialResponse getByVendorAndComponent(
            UUID vendorId,
            BomComponentType componentType,
            Long componentId
    ) {
        VendorMaterial vendorMaterial = vendorMaterialRepository
                .findByVendor_IdAndComponentTypeAndComponentId(
                        vendorId,
                        componentType,
                        componentId
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "VendorMaterial not found for given vendor and component"
                ));

        return mapToResponse(vendorMaterial);
    }

    // -------------------------
    // Mapping
    // -------------------------

    private VendorMaterialResponse mapToResponse(VendorMaterial entity) {
        return VendorMaterialResponse.builder()
                .id(entity.getId())
                .vendorId(entity.getVendor().getId())
                .vendorName(entity.getVendor().getName())
                .componentType(entity.getComponentType())
                .componentId(entity.getComponentId())
                .vendorMaterialCode(entity.getVendorMaterialCode())
                .vendorMaterialName(entity.getVendorMaterialName())
                .leadTimeDays(entity.getLeadTimeDays())
                .minimumOrderQuantity(entity.getMinimumOrderQuantity())
                .orderMultiple(entity.getOrderMultiple())
                .preferred(entity.isPreferred())
                .active(entity.isActive())
                .build();
    }
}
