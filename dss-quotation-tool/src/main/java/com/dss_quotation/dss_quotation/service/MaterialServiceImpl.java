package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.dss_quotation.dss_quotation.exceptions.ResourceNotFoundException;
import com.dss_quotation.dss_quotation.models.Material;
import com.dss_quotation.dss_quotation.payload.MaterialDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MaterialRequest;
import com.dss_quotation.dss_quotation.payload.MaterialResponse;
import com.dss_quotation.dss_quotation.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public List<Material> getAll() {
        return materialRepository.findAll();
    }

    @Override
    public List<Material> getActive() {
        return materialRepository.findByActiveTrue();
    }

    @Override
    public Material getById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "id", id));
    }

    @Override
    public Page<MaterialResponse> getPaginated(int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return materialRepository.findAllByOrderByNameAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<MaterialResponse> getActivePaginated(int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return materialRepository.findByActiveTrueOrderByNameAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public MaterialDetailsResponse getDetails(Long id) {
        return mapToDetailsResponse(getById(id));
    }

    @Override
    public Material create(Material material) {
        material.setActive(true); // always active on create
        return materialRepository.save(material);
    }

    @Override
    public MaterialDetailsResponse create(MaterialRequest request) {
        validateRequest(request);

        String name = request.getName().trim();

        if (materialRepository.existsByNameIgnoreCase(name)) {
            throw new APIException("Material with this name already exists");
        }

        Material material = Material.builder()
                .name(name)
                .type(request.getType().trim())
                .density(request.getDensity())
                .pricePerKg(request.getPricePerKg())
                .active(true)
                .build();

        return mapToDetailsResponse(materialRepository.save(material));
    }

    @Override
    public Material update(Long id, Material material) {

        Material existing = getById(id);

        existing.setName(material.getName());
        existing.setType(material.getType());
        existing.setDensity(material.getDensity());
        existing.setPricePerKg(material.getPricePerKg());

        return materialRepository.save(existing);
    }

    @Override
    public MaterialDetailsResponse update(Long id, MaterialRequest request) {
        validateRequest(request);

        Material existing = getById(id);
        String name = request.getName().trim();

        if (materialRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new APIException("Material with this name already exists");
        }

        existing.setName(name);
        existing.setType(request.getType().trim());
        existing.setDensity(request.getDensity());
        existing.setPricePerKg(request.getPricePerKg());

        return mapToDetailsResponse(materialRepository.save(existing));
    }

    @Override
    public void delete(Long id) {

        Material material = getById(id);

        // soft delete
        material.setActive(false);

        materialRepository.save(material);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new APIException("Page number cannot be negative");
        }

        if (size <= 0) {
            throw new APIException("Page size must be greater than zero");
        }
    }

    private void validateRequest(MaterialRequest request) {
        if (request == null) {
            throw new APIException("Material request is required");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new APIException("Material name is required");
        }

        if (request.getType() == null || request.getType().isBlank()) {
            throw new APIException("Material type is required");
        }

        if (request.getDensity() == null || request.getDensity() <= 0) {
            throw new APIException("Density must be greater than zero");
        }

        if (request.getPricePerKg() == null || request.getPricePerKg() <= 0) {
            throw new APIException("Price per kg must be greater than zero");
        }
    }

    private MaterialResponse mapToResponse(Material material) {
        return MaterialResponse.builder()
                .id(material.getId())
                .name(material.getName())
                .type(material.getType())
                .density(material.getDensity())
                .pricePerKg(material.getPricePerKg())
                .active(material.isActive())
                .build();
    }

    private MaterialDetailsResponse mapToDetailsResponse(Material material) {
        return MaterialDetailsResponse.builder()
                .id(material.getId())
                .name(material.getName())
                .type(material.getType())
                .density(material.getDensity())
                .pricePerKg(material.getPricePerKg())
                .active(material.isActive())
                .build();
    }
}
