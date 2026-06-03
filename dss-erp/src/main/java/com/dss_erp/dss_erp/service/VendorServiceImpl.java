package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Vendor;
import com.dss_erp.dss_erp.payload.VendorDTO;
import com.dss_erp.dss_erp.payload.VendorResponse;
import com.dss_erp.dss_erp.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public VendorResponse createVendor(VendorDTO vendorDTO) {

        if (vendorRepository.existsByVendorCode(vendorDTO.getVendorCode())) {
            throw new IllegalArgumentException(
                    "Vendor with code " + vendorDTO.getVendorCode() + " already exists"
            );
        }

        Vendor vendor = Vendor.builder()
                .vendorCode(vendorDTO.getVendorCode())
                .name(vendorDTO.getName())
                .vatNumber(vendorDTO.getVatNumber())
                .registrationNumber(vendorDTO.getRegistrationNumber())
                .email(vendorDTO.getEmail())
                .phone(vendorDTO.getPhone())
                .currency(vendorDTO.getCurrency())
                .paymentTerms(vendorDTO.getPaymentTerms())
                .active(vendorDTO.getActive() != null ? vendorDTO.getActive() : true)
                .build();

        Vendor savedVendor = vendorRepository.save(vendor);

        return mapToResponse(savedVendor);
    }

    @Override
    public VendorResponse updateVendor(UUID vendorId, VendorDTO vendorDTO) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        if (vendorDTO.getVendorCode() != null &&
                !vendorDTO.getVendorCode().equals(vendor.getVendorCode())) {

            if (vendorRepository.existsByVendorCode(vendorDTO.getVendorCode())) {
                throw new IllegalArgumentException(
                        "Vendor with code " + vendorDTO.getVendorCode() + " already exists"
                );
            }

            vendor.setVendorCode(vendorDTO.getVendorCode());
        }

        if (vendorDTO.getName() != null) {
            vendor.setName(vendorDTO.getName());
        }
        if (vendorDTO.getVatNumber() != null) {
            vendor.setVatNumber(vendorDTO.getVatNumber());
        }
        if (vendorDTO.getRegistrationNumber() != null) {
            vendor.setRegistrationNumber(vendorDTO.getRegistrationNumber());
        }
        if (vendorDTO.getEmail() != null) {
            vendor.setEmail(vendorDTO.getEmail());
        }
        if (vendorDTO.getPhone() != null) {
            vendor.setPhone(vendorDTO.getPhone());
        }
        if (vendorDTO.getCurrency() != null) {
            vendor.setCurrency(vendorDTO.getCurrency());
        }
        if (vendorDTO.getPaymentTerms() != null) {
            vendor.setPaymentTerms(vendorDTO.getPaymentTerms());
        }
        if (vendorDTO.getActive() != null) {
            vendor.setActive(vendorDTO.getActive());
        }

        Vendor updatedVendor = vendorRepository.save(vendor);

        return mapToResponse(updatedVendor);
    }

    @Override
    public VendorResponse getVendorById(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        return mapToResponse(vendor);
    }

    @Override
    public VendorResponse getVendorByCode(String vendorCode) {
        Vendor vendor = vendorRepository.findByVendorCode(vendorCode)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        return mapToResponse(vendor);
    }

    @Override
    public Page<VendorResponse> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public void deactivateVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        vendor.setActive(false);
        vendorRepository.save(vendor);
    }

    // -------------------------
    // Mapping
    // -------------------------

    private VendorResponse mapToResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .vendorCode(vendor.getVendorCode())
                .name(vendor.getName())
                .vatNumber(vendor.getVatNumber())
                .registrationNumber(vendor.getRegistrationNumber())
                .email(vendor.getEmail())
                .phone(vendor.getPhone())
                .currency(vendor.getCurrency())
                .paymentTerms(vendor.getPaymentTerms())
                .active(vendor.isActive())
                .build();
    }
}
