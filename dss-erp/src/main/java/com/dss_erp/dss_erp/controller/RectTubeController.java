package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.config.AppConstants;
import com.dss_erp.dss_erp.models.RectTubePiece;
import com.dss_erp.dss_erp.payload.*;
import com.dss_erp.dss_erp.service.RectTubeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materials/rect-tubes")
@RequiredArgsConstructor
public class RectTubeController {

    private final RectTubeService rectTubeService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<RectTubeDTO> createRectTube(@RequestBody RectTubeDTO rectTubeDTO) {
        RectTubeDTO created = rectTubeService.create(rectTubeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseMaterialResponse<RectTubeDTO>> getAllRectTubes(@RequestParam(required=false) String keyword,
                                                                       @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
                                                                       @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
                                                                       @RequestParam(defaultValue = AppConstants.SORT_ITEMS_BY) String sortBy,
                                                                       @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        BaseMaterialResponse<RectTubeDTO> response = rectTubeService.getAll(pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RectTubeDTO> getRectTubeById(@PathVariable Long id) {
        RectTubeDTO tube = rectTubeService.getById(id);
        return ResponseEntity.ok(tube);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRectTube(@PathVariable Long id) {
        rectTubeService.delete(id);
        return ResponseEntity.ok("RectTube with ID " + id + " deleted successfully.");
    }

    @GetMapping("/{tubeId}/pieces")
    public ResponseEntity<List<RectTubePieceDTO>> getPieces(@PathVariable Long tubeId) {
        List<RectTubePiece> pieces = rectTubeService.getPieces(tubeId);
        List<RectTubePieceDTO> dtos = pieces.stream()
                .map(p -> modelMapper.map(p, RectTubePieceDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<RectTubeDTO> receiveDelivery(
            @PathVariable Long id,
            @RequestBody PiecesDeliveryDTO deliveryDTO) {

        RectTubeDTO updated = rectTubeService.receiveDelivery(
                id,
                deliveryDTO.getPiecesReceived(),
                deliveryDTO.getLengthPerPieceMm()
        );

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/consume")
    public ResponseEntity<RectTubePieceDTO> consumeRectTube(
            @PathVariable Long id,
            @RequestParam double length,
            @RequestParam String usedFor) {

        RectTubePiece piece = rectTubeService.consumeRectTubeMaterial(id, length, usedFor);
        RectTubePieceDTO dto = modelMapper.map(piece, RectTubePieceDTO.class);
        return ResponseEntity.ok(dto);
    }
}
