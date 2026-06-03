package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.Bom;
import com.dss_erp.dss_erp.models.BomExplosionRow;
import com.dss_erp.dss_erp.models.MaterialRequirementRow;
import com.dss_erp.dss_erp.models.Product;
import com.dss_erp.dss_erp.payload.BomDTO;
import com.dss_erp.dss_erp.payload.BomRequest;
import com.dss_erp.dss_erp.service.BomService;
import com.dss_erp.dss_erp.repositories.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/bom")
public class BomController {

    private final BomService bomService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public BomController(BomService bomService,
                         ProductRepository productRepository,
                         ModelMapper modelMapper) {
        this.bomService = bomService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<BomDTO> createBom(
            @PathVariable Long productId,
            @RequestBody BomRequest request
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Bom bom = bomService.createBom(product, request.getNote());
        return ResponseEntity.ok(modelMapper.map(bom, BomDTO.class));
    }

    @PostMapping("/clone/{bomId}")
    public ResponseEntity<BomDTO> cloneBom(
            @PathVariable Long productId,
            @PathVariable Long bomId,
            @RequestBody BomRequest request
    ) {
        Bom newBom = bomService.cloneBom(bomId, request.getNote());
        return ResponseEntity.ok(modelMapper.map(newBom, BomDTO.class));
    }

    @PostMapping("/release/{bomId}")
    public ResponseEntity<BomDTO> releaseBom(
            @PathVariable Long productId,
            @PathVariable Long bomId
    ) {
        Bom bom = bomService.releaseBom(bomId);
        return ResponseEntity.ok(modelMapper.map(bom, BomDTO.class));
    }

    @GetMapping("/latest")
    public ResponseEntity<BomDTO> getLatestBom(@PathVariable Long productId) {
        Bom bom = bomService.getLatestBom(productId);
        return ResponseEntity.ok(modelMapper.map(bom, BomDTO.class));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BomDTO>> getAllBoms(@PathVariable Long productId) {
        List<Bom> boms = bomService.getAllBoms(productId);
        return ResponseEntity.ok(
                boms.stream()
                        .map(b -> modelMapper.map(b, BomDTO.class))
                        .toList()
        );
    }

    @GetMapping("/{bomId}/explode")
    public ResponseEntity<List<BomExplosionRow>> explodeBom(
            @PathVariable Long productId,
            @PathVariable Long bomId
    ) {
        return ResponseEntity.ok(
                bomService.explodeBom(productId, bomId)
        );
    }

    @GetMapping("{bomId}/materials")
    public ResponseEntity<List<MaterialRequirementRow>> getMaterials(
            @PathVariable Long productId,
            @PathVariable Long bomId
    ) {
        return ResponseEntity.ok(
                bomService.explodeMaterials(productId, bomId)
        );
    }
}

