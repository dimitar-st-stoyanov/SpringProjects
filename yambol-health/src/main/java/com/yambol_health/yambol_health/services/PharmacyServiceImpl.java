package com.yambol_health.yambol_health.services;

import com.yambol_health.yambol_health.exceptions.ResourceNotFoundException;
import com.yambol_health.yambol_health.models.Pharmacy;
import com.yambol_health.yambol_health.payloads.PharmacyDTO;
import com.yambol_health.yambol_health.payloads.PharmacyResponse;
import com.yambol_health.yambol_health.repositories.PharmacyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO) {
        Pharmacy pharmacy = toEntity(pharmacyDTO);
        pharmacy.setId(null);
        if (pharmacy.getActive() == null) {
            pharmacy.setActive(true);
        }
        if (pharmacy.getLastVerifiedAt() == null) {
            pharmacy.setLastVerifiedAt(LocalDateTime.now());
        }
        return toDto(pharmacyRepository.save(pharmacy));
    }

    @Override
    public PharmacyResponse getAllPharmacies(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Pharmacy> page = pharmacyRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
        List<PharmacyDTO> pharmacies = page.getContent().stream().map(this::toDto).toList();

        return new PharmacyResponse(pharmacies, page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast());
    }

    @Override
    public PharmacyDTO getPharmacyById(Long pharmacyId) {
        return toDto(findPharmacy(pharmacyId));
    }

    @Override
    public PharmacyDTO updatePharmacy(Long pharmacyId, PharmacyDTO pharmacyDTO) {
        Pharmacy pharmacy = findPharmacy(pharmacyId);
        pharmacy.setName(pharmacyDTO.getName());
        pharmacy.setAddress(pharmacyDTO.getAddress());
        pharmacy.setPhone(pharmacyDTO.getPhone());
        pharmacy.setLatitude(pharmacyDTO.getLatitude());
        pharmacy.setLongitude(pharmacyDTO.getLongitude());
        pharmacy.setActive(pharmacyDTO.getActive() == null ? pharmacy.getActive() : pharmacyDTO.getActive());
        pharmacy.setLastVerifiedAt(pharmacyDTO.getLastVerifiedAt() == null
                ? LocalDateTime.now() : pharmacyDTO.getLastVerifiedAt());

        return toDto(pharmacyRepository.save(pharmacy));
    }

    @Override
    public void deletePharmacy(Long pharmacyId) {
        pharmacyRepository.delete(findPharmacy(pharmacyId));
    }

    private Pharmacy findPharmacy(Long pharmacyId) {
        return pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy", "id", pharmacyId));
    }

    private Pharmacy toEntity(PharmacyDTO dto) {
        return Pharmacy.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .active(dto.getActive())
                .lastVerifiedAt(dto.getLastVerifiedAt())
                .build();
    }

    private PharmacyDTO toDto(Pharmacy pharmacy) {
        return PharmacyDTO.builder()
                .id(pharmacy.getId())
                .name(pharmacy.getName())
                .address(pharmacy.getAddress())
                .phone(pharmacy.getPhone())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .active(pharmacy.getActive())
                .lastVerifiedAt(pharmacy.getLastVerifiedAt())
                .build();
    }
}
