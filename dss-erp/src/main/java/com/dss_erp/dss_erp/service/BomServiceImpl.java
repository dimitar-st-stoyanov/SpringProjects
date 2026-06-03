package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.*;
import com.dss_erp.dss_erp.repositories.BomRepository;
import com.dss_erp.dss_erp.repositories.BomLineRepository;

import com.dss_erp.dss_erp.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BomServiceImpl implements BomService {

    private final BomRepository bomRepository;
    private final BomLineRepository bomLineRepository;

    public BomServiceImpl(BomRepository bomRepository, BomLineRepository bomLineRepository) {
        this.bomRepository = bomRepository;
        this.bomLineRepository = bomLineRepository;
    }

    @Override
    public Bom createBom(Product product, String note) {
        Bom bom = new Bom();
        bom.setProduct(product);
        bom.setVersion(1);
        bom.setStatus(BomStatus.DRAFT);
        bom.setNote(note);
        return bomRepository.save(bom);
    }

    @Override
    @Transactional
    public Bom cloneBom(Long sourceBomId, String note) {

        Bom sourceBom = bomRepository.findById(sourceBomId)
                .orElseThrow(() -> new RuntimeException("BOM not found"));

        // 1️⃣ Create new BOM
        Bom newBom = new Bom();
        newBom.setProduct(sourceBom.getProduct());
        newBom.setVersion(sourceBom.getVersion() + 1);
        newBom.setStatus(BomStatus.DRAFT);
        newBom.setNote(note);

        bomRepository.save(newBom);

        // 2️⃣ Clone lines PROPERLY
        for (BomLine sourceLine : sourceBom.getLines()) {

            BomLine clonedLine = new BomLine();   // ✅ NEW ENTITY

            clonedLine.setBom(newBom);            // ✅ new parent
            clonedLine.setComponentType(sourceLine.getComponentType());
            clonedLine.setComponentId(sourceLine.getComponentId());
            clonedLine.setQuantity(sourceLine.getQuantity());

            // ❌ DO NOT SET ID
            // clonedLine.setId(null); ❌ NEVER

            bomLineRepository.save(clonedLine);
        }

        return newBom;
    }

    @Override
    public Bom getLatestBom(Long productId) {
        return bomRepository.findFirstByProductIdAndStatusOrderByVersionDesc(productId, BomStatus.RELEASED)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "productId", productId));
    }

    @Override
    public List<Bom> getAllBoms(Long productId) {
        return bomRepository.findByProductIdOrderByVersionDesc(productId);
    }

    @Override
    public Bom getBomById(Long bomId) {
        return bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", bomId));
    }

    @Override
    public Bom releaseBom(Long bomId) {
        Bom bom = getBomById(bomId);
        bom.setStatus(BomStatus.RELEASED);
        return bomRepository.save(bom);
    }

    @Override
    public List<BomExplosionRow> explodeBom(Long productId, Long bomId) {

        // Optional but recommended safety check
        if (!bomRepository.existsByIdAndProductId(bomId, productId)) {
            throw new IllegalArgumentException(
                    "BOM does not belong to the given product"
            );
        }

        return bomRepository.explodeBom(bomId);
    }

    @Override
    public List<MaterialRequirementRow> explodeMaterials(
            Long productId,
            Long bomId
    ) {
        if (!bomRepository.existsByIdAndProductId(bomId, productId)) {
            throw new IllegalArgumentException(
                    "BOM does not belong to product"
            );
        }

        return bomRepository.explodeMaterials(bomId);
    }
}
