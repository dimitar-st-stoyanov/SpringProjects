package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.exceptions.ResourceNotFoundException;
import com.dss_erp.dss_erp.models.*;
import com.dss_erp.dss_erp.payload.RoutingDTO;
import com.dss_erp.dss_erp.payload.RoutingResponse;
import com.dss_erp.dss_erp.repositories.ProductRepository;
import com.dss_erp.dss_erp.repositories.RoutingLineRepository;
import com.dss_erp.dss_erp.repositories.RoutingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingServiceImpl implements RoutingService {

    private final RoutingRepository routingRepository;
    private final ProductRepository productRepository;
    private final RoutingLineRepository routingLineRepository;

    @Override
    public RoutingDTO createRouting(Product product, String note) {
        Routing entity = new Routing();
        entity.setProduct(product);
        entity.setVersion(1);
        entity.setStatus(RoutingStatus.DRAFT);
        entity.setNote(note);

        return mapToDTO(routingRepository.save(entity));
    }

    @Transactional
    @Override
    public Routing cloneRouting(Long sourceRoutingId, String note) {

        Routing sourceRouting = routingRepository.findById(sourceRoutingId)
                .orElseThrow(() -> new RuntimeException("Routing not found"));

        // 1️⃣ Create new Routing
        Routing newRouting = new Routing();
        newRouting.setProduct(sourceRouting.getProduct());
        newRouting.setVersion(sourceRouting.getVersion() + 1);
        newRouting.setStatus(RoutingStatus.DRAFT);
        newRouting.setNote(note);

        routingRepository.save(newRouting);

        // 2️⃣ Clone lines
        for (RoutingLine sourceLine : sourceRouting.getLines()) {

            RoutingLine clonedLine = new RoutingLine(); // ✅ NEW ENTITY

            clonedLine.setRouting(newRouting);          // ✅ new parent
            clonedLine.setSequence(sourceLine.getSequence());
            clonedLine.setDescription(sourceLine.getDescription());
            clonedLine.setSetupTime(sourceLine.getSetupTime());
            clonedLine.setRunTime(sourceLine.getRunTime());
            clonedLine.setWorkCenter(sourceLine.getWorkCenter());

            // ❌ DO NOT SET ID

            routingLineRepository.save(clonedLine);
        }

        return newRouting;
    }

    @Override
    public RoutingDTO releaseRouting(Long id) {

        Routing routing = routingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routing not found"));

        routingRepository.findByProductIdAndStatus(routing.getProduct().getId(), RoutingStatus.RELEASED)
                .ifPresent(existing -> {
                    existing.setStatus(RoutingStatus.OBSOLETE);
                    routingRepository.save(existing);
                });

        routing.setStatus(RoutingStatus.RELEASED);

        return mapToDTO(routingRepository.save(routing));
    }

    @Override
    public RoutingDTO getById(Long id) {
        Routing entity = routingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routing not found"));

        return mapToDTO(entity);
    }

    @Override
    public RoutingResponse getAll(Pageable pageable) {

        Page<Routing> page = routingRepository.findAll(pageable);

        return new RoutingResponse(
                page.getContent().stream().map(this::mapToDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public RoutingResponse getByProduct(Long productId, Pageable pageable) {

        Page<Routing> page = routingRepository.findByProductId(productId, pageable);

        return new RoutingResponse(
                page.getContent().stream().map(this::mapToDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public void delete(Long id) {
        routingRepository.deleteById(id);
    }

    @Override
    public Routing getLatestRouting(Long productId) {
        return routingRepository.findFirstByProductIdAndStatusOrderByVersionDesc(productId, RoutingStatus.RELEASED)
                .orElseThrow(() -> new ResourceNotFoundException("Routing", "productId", productId));
    }

    private RoutingDTO mapToDTO(Routing entity) {
        return new RoutingDTO(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getVersion(),
                entity.getStatus(),
                entity.getNote()
        );
    }
}