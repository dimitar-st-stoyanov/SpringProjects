package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.RectTube;
import com.dss_erp.dss_erp.models.RectTubePiece;
import com.dss_erp.dss_erp.payload.RectTubeDTO;
import com.dss_erp.dss_erp.payload.RectTubePieceDTO;
import com.dss_erp.dss_erp.repositories.RectTubeRepository;
import com.dss_erp.dss_erp.repositories.RectTubePieceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RectTubeServiceImpl implements RectTubeService {

    private final RectTubeRepository rectTubeRepository;
    private final RectTubePieceRepository rectTubePieceRepository;
    private final ModelMapper modelMapper;

    // -------------------------------------------------------------------------
    // CREATE RECT TUBE
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public RectTubeDTO create(RectTubeDTO dto) {

        RectTube rectTube = modelMapper.map(dto, RectTube.class);
        rectTube.calculateWeight();
        rectTube.generateName();

        // Create initial piece if quantity & standard length exist
        if (rectTube.getQuantity() != null && rectTube.getStandardLength() != null) {

            RectTubePiece piece = new RectTubePiece();
            piece.setLength(rectTube.getStandardLength());
            piece.setQuantity(1);
            piece.setRectTube(rectTube);

            rectTube.getPieces().add(piece);
            rectTube.updateQuantityFromPieces();
        }

        RectTube saved = rectTubeRepository.save(rectTube);
        return mapToDTO(saved);
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    @Override
    public void delete(Long id) {
        rectTubeRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Override
    public RectTubeDTO getById(Long id) {
        RectTube tube = rectTubeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RectTube not found with ID: " + id));
        return mapToDTO(tube);
    }

    // -------------------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------------------
    @Override
    public List<RectTubeDTO> getAll() {
        return rectTubeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // RECEIVE DELIVERY (matching Tube)
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public RectTubeDTO receiveDelivery(Long tubeId, Integer piecesReceived, Double lengthPerPieceMm) {

        RectTube tube = rectTubeRepository.findById(tubeId)
                .orElseThrow(() -> new RuntimeException("RectTube not found with id " + tubeId));

        if (piecesReceived == null || piecesReceived <= 0)
            throw new IllegalArgumentException("piecesReceived must be > 0");

        if (lengthPerPieceMm == null || lengthPerPieceMm <= 0)
            lengthPerPieceMm = tube.getStandardLength() != null ? tube.getStandardLength() : 6000.0;

        // Try to merge with existing entry
        Optional<RectTubePiece> existingOpt =
                rectTubePieceRepository.findByRectTubeIdAndLength(tubeId, lengthPerPieceMm);

        if (existingOpt.isPresent()) {
            RectTubePiece existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + piecesReceived);
            rectTubePieceRepository.save(existing);
        } else {
            RectTubePiece newPiece = new RectTubePiece();
            newPiece.setLength(lengthPerPieceMm);
            newPiece.setQuantity(piecesReceived);
            newPiece.setRectTube(tube);
            tube.getPieces().add(newPiece);
        }

        tube.updateQuantityFromPieces();
        rectTubeRepository.save(tube);

        return mapToDTO(tube);
    }

    // -------------------------------------------------------------------------
    // MANUAL UPDATE OF QUANTITY
    // (same structure as Tube)
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public RectTubeDTO updateQuantity(Long id, Double totalLengthMm) {
        RectTube tube = rectTubeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RectTube not found with id " + id));

        tube.setQuantity(totalLengthMm);
        rectTubeRepository.save(tube);

        return mapToDTO(tube);
    }

    // -------------------------------------------------------------------------
    // CUT MATERIAL (matching Tube)
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public RectTubePiece consumeRectTubeMaterial(Long tubeId, double requiredLengthMm, String usedFor) {

        if (requiredLengthMm <= 0)
            throw new IllegalArgumentException("Required length must be > 0");

        if (usedFor == null || usedFor.isBlank())
            throw new IllegalArgumentException("usedFor must be provided");

        RectTube tube = rectTubeRepository.findById(tubeId)
                .orElseThrow(() -> new RuntimeException("RectTube not found"));

        // sorted by shortest first
        List<RectTubePiece> available =
                rectTubePieceRepository.findByRectTubeIdAndIsScrapFalseOrderByLengthAsc(tubeId);

        RectTubePiece pieceToCut =
                available.stream()
                        .filter(p -> p.getLength() >= requiredLengthMm && p.getQuantity() > 0)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No piece long enough"));

        // Cut one piece
        RectTubePiece leftover = pieceToCut.cut(requiredLengthMm);

        // Handle leftover
        if (leftover != null) {

            Optional<RectTubePiece> existing =
                    rectTubePieceRepository.findByRectTubeIdAndLength(tubeId, leftover.getLength());

            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + 1);
            } else {
                leftover.setRectTube(tube);
                tube.getPieces().add(leftover);
            }
        }

        tube.updateQuantityFromPieces();
        rectTubeRepository.save(tube);

        return pieceToCut;
    }

    // -------------------------------------------------------------------------
    // GET PIECES
    // -------------------------------------------------------------------------
    @Override
    public List<RectTubePiece> getPieces(Long tubeId) {
        RectTube tube = rectTubeRepository.findById(tubeId)
                .orElseThrow(() -> new RuntimeException("RectTube not found"));
        return tube.getPieces();
    }

    // -------------------------------------------------------------------------
    // DTO MAPPING
    // -------------------------------------------------------------------------
    private RectTubeDTO mapToDTO(RectTube tube) {

        RectTubeDTO dto = modelMapper.map(tube, RectTubeDTO.class);

        dto.setTotalLength(tube.computeTotalLengthFromPieces());
        dto.setAvailableLength(tube.computeTotalLengthFromPieces());
        dto.setAvailablePieces(tube.getAvailablePieces());

        dto.setPieces(
                tube.getPieces().stream()
                        .map(p -> modelMapper.map(p, RectTubePieceDTO.class))
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
