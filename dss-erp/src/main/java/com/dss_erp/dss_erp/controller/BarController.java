package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.BarPiece;
import com.dss_erp.dss_erp.payload.BarDTO;
import com.dss_erp.dss_erp.payload.BarPieceDTO;
import com.dss_erp.dss_erp.payload.PiecesDeliveryDTO;
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

    // ✅ CREATE new rectangular bar
    @PostMapping
    public ResponseEntity<BarDTO> createBar(@RequestBody BarDTO barDTO) {
        BarDTO created = barService.create(barDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ✅ GET all bars
    @GetMapping
    public ResponseEntity<List<BarDTO>> getAllBars() {
        List<BarDTO> bars = barService.getAll();
        return ResponseEntity.ok(bars);
    }

    // ✅ GET bar by ID
    @GetMapping("/{id}")
    public ResponseEntity<BarDTO> getBarById(@PathVariable Long id) {
        BarDTO bar = barService.getById(id);
        return ResponseEntity.ok(bar);
    }


    // ✅ DELETE bar by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBar(@PathVariable Long id) {
        barService.delete(id);
        return ResponseEntity.ok("Bar with ID " + id + " deleted successfully.");
    }

    // ✅ GET all pieces of a bar
    @GetMapping("/{barId}/pieces")
    public ResponseEntity<List<BarPieceDTO>> getPieces(@PathVariable Long barId) {
        List<BarPiece> pieces = barService.getPieces(barId);
        List<BarPieceDTO> dtos = pieces.stream()
                .map(p -> modelMapper.map(p, BarPieceDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ✅ NEW: Receive delivery of N pieces x N lenght of a tube
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
}
