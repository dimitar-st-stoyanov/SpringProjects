package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.config.AppConstants;
import com.dss_erp.dss_erp.models.BarPiece;
import com.dss_erp.dss_erp.payload.*;
import com.dss_erp.dss_erp.service.BarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materials/bars")
@RequiredArgsConstructor
public class BarController {

    private final BarService barService;
    private final ModelMapper modelMapper;

    // CREATE BAR
    @PostMapping
    public ResponseEntity<BarDTO> createBar(@RequestBody BarDTO barDTO) {
        BarDTO created = barService.create(barDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<BaseMaterialResponse<BarDTO>> getAllBars(@RequestParam(required=false) String keyword,
                                                                       @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
                                                                       @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
                                                                       @RequestParam(defaultValue = AppConstants.SORT_ITEMS_BY) String sortBy,
                                                                       @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        BaseMaterialResponse<BarDTO> response = barService.getAll(pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(response);
    }

        // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BarDTO> getBarById(@PathVariable Long id) {
        BarDTO bar = barService.getById(id);
        return ResponseEntity.ok(bar);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBar(@PathVariable Long id) {
        barService.delete(id);
        return ResponseEntity.ok("Bar with ID " + id + " deleted successfully.");
    }

    // GET PIECES
    @GetMapping("/{barId}/pieces")
    public ResponseEntity<List<BarPieceDTO>> getPieces(@PathVariable Long barId) {
        List<BarPiece> pieces = barService.getPieces(barId);
        List<BarPieceDTO> dtos = pieces.stream()
                .map(p -> modelMapper.map(p, BarPieceDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // RECEIVE DELIVERY
    @PostMapping("/{id}/receive")
    public ResponseEntity<BarDTO> receiveDelivery(
            @PathVariable Long id,
            @RequestBody PiecesDeliveryDTO deliveryDTO) {

        BarDTO updated = barService.receiveDelivery(
                id,
                deliveryDTO.getPiecesReceived(),
                deliveryDTO.getLengthPerPieceMm()
        );

        return ResponseEntity.ok(updated);
    }

    // CUT / CONSUME MATERIAL
    @PutMapping("/{id}/consume")
    public ResponseEntity<BarPieceDTO> consumeBar(
            @PathVariable Long id,
            @RequestParam double length,
            @RequestParam String usedFor) {

        BarPiece piece = barService.consumeBarMaterial(id, length, usedFor);
        BarPieceDTO dto = modelMapper.map(piece, BarPieceDTO.class);
        return ResponseEntity.ok(dto);
    }
}
