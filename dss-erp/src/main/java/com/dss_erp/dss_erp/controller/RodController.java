package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.config.AppConstants;
import com.dss_erp.dss_erp.models.Rod;
import com.dss_erp.dss_erp.models.RodPiece;
import com.dss_erp.dss_erp.payload.*;
import com.dss_erp.dss_erp.service.RodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materials/rods")
@RequiredArgsConstructor
public class RodController {

    private final RodService rodService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<RodDTO> createRod(@RequestBody RodDTO rodDTO) {
        RodDTO created = rodService.create(rodDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseMaterialResponse<RodDTO>> getAllRods(@RequestParam(required=false) String keyword,
                                                                       @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
                                                                       @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
                                                                       @RequestParam(defaultValue = AppConstants.SORT_ITEMS_BY) String sortBy,
                                                                       @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        BaseMaterialResponse<RodDTO> response = rodService.getAll(pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RodDTO> getRodById(@PathVariable Long id) {
        RodDTO rod = rodService.getById(id);
        return ResponseEntity.ok(rod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRod(@PathVariable Long id) {
        rodService.delete(id);
        return ResponseEntity.ok("Rod with ID " + id + " deleted successfully.");
    }

    @GetMapping("/{rodId}/pieces")
    public ResponseEntity<List<RodPieceDTO>> getPieces(@PathVariable Long rodId) {
        List<RodPiece> pieces = rodService.getPieces(rodId);
        List<RodPieceDTO> dtos = pieces.stream()
                .map(p -> modelMapper.map(p, RodPieceDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<RodDTO> receiveDelivery(
            @PathVariable Long id,
            @RequestBody PiecesDeliveryDTO deliveryDTO) {

        RodDTO updated = rodService.receiveDelivery(
                id,
                deliveryDTO.getPiecesReceived(),
                deliveryDTO.getLengthPerPieceMm()
        );

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/consume")
    public ResponseEntity<RodPieceDTO> consumeRod(
            @PathVariable Long id,
            @RequestParam double length,
            @RequestParam String usedFor) {

        RodPiece piece = rodService.consumeRodMaterial(id, length, usedFor);
        RodPieceDTO dto = modelMapper.map(piece, RodPieceDTO.class);
        return ResponseEntity.ok(dto);
    }
}
