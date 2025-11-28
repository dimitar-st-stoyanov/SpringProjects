package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.TubePiece;
import com.dss_erp.dss_erp.payload.PiecesDeliveryDTO;
import com.dss_erp.dss_erp.payload.TubeDTO;
import com.dss_erp.dss_erp.payload.TubePieceDTO;
import com.dss_erp.dss_erp.service.TubeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materials/tubes")
@RequiredArgsConstructor
public class TubeController {

    private final TubeService tubeService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TubeDTO> createTube(@RequestBody TubeDTO tubeDTO) {
        TubeDTO created = tubeService.create(tubeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TubeDTO>> getAllTubes() {
        List<TubeDTO> tubes = tubeService.getAll();
        return ResponseEntity.ok(tubes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TubeDTO> getTubeById(@PathVariable Long id) {
        TubeDTO tube = tubeService.getById(id);
        return ResponseEntity.ok(tube);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTube(@PathVariable Long id) {
        tubeService.delete(id);
        return ResponseEntity.ok("Tube with ID " + id + " deleted successfully.");
    }

    @GetMapping("/{tubeId}/pieces")
    public ResponseEntity<List<TubePieceDTO>> getPieces(@PathVariable Long tubeId) {
        List<TubePiece> pieces = tubeService.getPieces(tubeId);
        List<TubePieceDTO> dtos = pieces.stream()
                .map(p -> modelMapper.map(p, TubePieceDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<TubeDTO> receiveDelivery(
            @PathVariable Long id,
            @RequestBody PiecesDeliveryDTO deliveryDTO) {

        TubeDTO updated = tubeService.receiveDelivery(
                id,
                deliveryDTO.getPiecesReceived(),
                deliveryDTO.getLengthPerPieceMm()
        );

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/consume")
    public ResponseEntity<TubePieceDTO> consumeTube(
            @PathVariable Long id,
            @RequestParam double length,
            @RequestParam String usedFor) {

        TubePiece piece = tubeService.consumeTubeMaterial(id, length, usedFor);
        TubePieceDTO dto = modelMapper.map(piece, TubePieceDTO.class);
        return ResponseEntity.ok(dto);
    }
}