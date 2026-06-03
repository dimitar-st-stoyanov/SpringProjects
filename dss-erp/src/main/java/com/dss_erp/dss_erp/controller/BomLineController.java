package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.BomLine;
import com.dss_erp.dss_erp.payload.BomLineDTO;
import com.dss_erp.dss_erp.payload.BomLineViewDTO;
import com.dss_erp.dss_erp.service.BomLineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/boms/{bomId}/lines")
@RequiredArgsConstructor
public class BomLineController {

    private final BomLineService bomLineService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BomLineDTO> addBomLine(
            @PathVariable Long productId,
            @PathVariable Long bomId,
            @RequestBody BomLineDTO dto
    ) {
        BomLine saved = bomLineService.addBomLine(bomId, dto);
        BomLineDTO created = modelMapper.map(saved, BomLineDTO.class);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public List<BomLineViewDTO> getBomLines(
            @PathVariable Long productId,
            @PathVariable Long bomId
    ) {
        return bomLineService.getBom(bomId);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<BomLineDTO> deleteBomLine(
            @PathVariable Long productId,
            @PathVariable Long bomId,
            @PathVariable Long lineId) {
        BomLineDTO deleted = bomLineService.deleteBomLine(lineId);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<BomLineDTO> updateBomLine(
            @PathVariable Long productId,
            @PathVariable Long bomId,
            @PathVariable Long lineId,
            @RequestBody BomLineDTO dto
    ){
        BomLineDTO updated = bomLineService.updateBomLine(lineId, dto);
        return ResponseEntity.ok(updated);
    }


}

