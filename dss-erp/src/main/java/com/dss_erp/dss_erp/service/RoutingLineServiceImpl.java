package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Routing;
import com.dss_erp.dss_erp.models.RoutingLine;
import com.dss_erp.dss_erp.models.RoutingStatus;
import com.dss_erp.dss_erp.models.WorkCenter;
import com.dss_erp.dss_erp.payload.RoutingLineDTO;
import com.dss_erp.dss_erp.payload.RoutingLineResponse;
import com.dss_erp.dss_erp.repositories.RoutingLineRepository;
import com.dss_erp.dss_erp.repositories.RoutingRepository;
import com.dss_erp.dss_erp.repositories.WorkCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingLineServiceImpl implements RoutingLineService {

    private final RoutingLineRepository repository;
    private final RoutingRepository routingRepository;
    private final WorkCenterRepository workCenterRepository;

    @Override
    public RoutingLineDTO create(RoutingLineDTO dto) {

        Routing routing = getEditableRouting(dto.getRoutingId());

        WorkCenter workCenter = workCenterRepository.findById(dto.getWorkCenterId())
                .orElseThrow(() -> new RuntimeException("WorkCenter not found"));

        RoutingLine entity = new RoutingLine(
                null,
                dto.getSequence(),
                dto.getDescription(),
                dto.getSetupTime(),
                dto.getRunTime(),
                routing,
                workCenter
        );

        return mapToDTO(repository.save(entity));
    }

    @Override
    public RoutingLineDTO update(Long id, RoutingLineDTO dto) {

        RoutingLine entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoutingLine not found"));

        getEditableRouting(entity.getRouting().getId());

        entity.setSequence(dto.getSequence());
        entity.setDescription(dto.getDescription());
        entity.setSetupTime(dto.getSetupTime());
        entity.setRunTime(dto.getRunTime());

        if (dto.getWorkCenterId() != null) {
            WorkCenter wc = workCenterRepository.findById(dto.getWorkCenterId())
                    .orElseThrow(() -> new RuntimeException("WorkCenter not found"));
            entity.setWorkCenter(wc);
        }

        return mapToDTO(repository.save(entity));
    }

    @Override
    public RoutingLineDTO getById(Long id) {
        RoutingLine entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoutingLine not found"));

        return mapToDTO(entity);
    }

    @Override
    public RoutingLineResponse getByRouting(Long routingId, Pageable pageable) {

        Page<RoutingLine> page =
                repository.findByRoutingIdOrderBySequence(routingId, pageable);

        return new RoutingLineResponse(
                page.getContent().stream().map(this::mapToDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public void delete(Long id) {

        RoutingLine entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoutingLine not found"));

        getEditableRouting(entity.getRouting().getId());

        repository.delete(entity);
    }

    // 🔥 CORE RULE
    private Routing getEditableRouting(Long routingId) {
        Routing routing = routingRepository.findById(routingId)
                .orElseThrow(() -> new RuntimeException("Routing not found"));

        if (routing.getStatus() != RoutingStatus.DRAFT) {
            throw new RuntimeException("Cannot modify routing unless it is in DRAFT status");
        }

        return routing;
    }

    private RoutingLineDTO mapToDTO(RoutingLine entity) {
        return new RoutingLineDTO(
                entity.getId(),
                entity.getSequence(),
                entity.getDescription(),
                entity.getSetupTime(),
                entity.getRunTime(),
                entity.getRouting().getId(),
                entity.getWorkCenter().getId()
        );
    }
}