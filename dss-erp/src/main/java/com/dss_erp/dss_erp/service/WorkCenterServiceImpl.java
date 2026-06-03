package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.WorkCenter;
import com.dss_erp.dss_erp.payload.WorkCenterDTO;
import com.dss_erp.dss_erp.payload.WorkCenterResponse;
import com.dss_erp.dss_erp.repositories.WorkCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkCenterServiceImpl implements WorkCenterService {

    private final WorkCenterRepository repository;

    @Override
    public WorkCenterDTO create(WorkCenterDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new RuntimeException("WorkCenter already exists");
        }

        WorkCenter entity = new WorkCenter(
                null,
                dto.getName(),
                dto.getRatePerHour()
        );

        return mapToDTO(repository.save(entity));
    }

    @Override
    public WorkCenterDTO update(Long id, WorkCenterDTO dto) {
        WorkCenter entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkCenter not found"));

        entity.setName(dto.getName());
        entity.setRatePerHour(dto.getRatePerHour());

        return mapToDTO(repository.save(entity));
    }

    @Override
    public WorkCenterDTO getById(Long id) {
        WorkCenter entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkCenter not found"));

        return mapToDTO(entity);
    }

    @Override
    public WorkCenterResponse getAll(Pageable pageable) {
        Page<WorkCenter> page = repository.findAll(pageable);

        return new WorkCenterResponse(
                page.getContent().stream().map(this::mapToDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private WorkCenterDTO mapToDTO(WorkCenter entity) {
        return new WorkCenterDTO(
                entity.getId(),
                entity.getName(),
                entity.getRatePerHour()
        );
    }
}