package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Tube;
import com.dss_erp.dss_erp.models.TubePiece;
import com.dss_erp.dss_erp.models.TubeUsage;
import com.dss_erp.dss_erp.payload.TubeDTO;
import com.dss_erp.dss_erp.payload.TubePieceDTO;
import com.dss_erp.dss_erp.repositories.TubePieceRepository;
import com.dss_erp.dss_erp.repositories.TubeRepository;
import com.dss_erp.dss_erp.repositories.TubeUsageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TubeServiceImpl implements TubeService {

    private final TubeRepository tubeRepository;
    private final TubePieceRepository tubePieceRepository;
    private final TubeUsageRepository tubeUsageRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TubeDTO create(TubeDTO dto) {
        Tube tube = modelMapper.map(dto, Tube.class);
        tube.calculateWeight();
        tube.generateName();

        if (tube.getQuantity() != null && tube.getStandardLength() != null) {
            TubePiece piece = new TubePiece();
            piece.setLength(tube.getStandardLength());
            piece.setQuantity(1);
            piece.setTube(tube);
            tube.getPieces().add(piece);
            tube.updateQuantityFromPieces();
        }

        Tube saved = tubeRepository.save(tube);
        return mapToDTO(saved);
    }

    @Override
    public void delete(Long id) {
        tubeRepository.deleteById(id);
    }

    @Override
    public TubeDTO getById(Long id) {
        Tube tube = tubeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tube not found with ID: " + id));
        return mapToDTO(tube);
    }

    @Override
    public List<TubeDTO> getAll() {
        return tubeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TubeDTO receiveDelivery(Long tubeId, Integer piecesReceived, Double lengthPerPieceMm) {
        Tube tube = tubeRepository.findById(tubeId)
                .orElseThrow(() -> new RuntimeException("Tube not found with id " + tubeId));

        if (piecesReceived == null || piecesReceived <= 0)
            throw new IllegalArgumentException("piecesReceived must be greater than zero");

        if (lengthPerPieceMm == null || lengthPerPieceMm <= 0)
            lengthPerPieceMm = tube.getStandardLength() != null ? tube.getStandardLength() : 6000.0;

        Optional<TubePiece> existingPieceOpt = tubePieceRepository.findByTubeIdAndLength(tubeId, lengthPerPieceMm);
        if (existingPieceOpt.isPresent()) {
            TubePiece existing = existingPieceOpt.get();
            existing.setQuantity(existing.getQuantity() + piecesReceived);
            tubePieceRepository.save(existing);
        } else {
            TubePiece newPiece = new TubePiece();
            newPiece.setLength(lengthPerPieceMm);
            newPiece.setQuantity(piecesReceived);
            newPiece.setTube(tube);
            tube.getPieces().add(newPiece);
        }

        tube.updateQuantityFromPieces();
        tubeRepository.save(tube);

        return mapToDTO(tube);
    }

    @Transactional
    @Override
    public TubeDTO updateQuantity(Long id, Double totalLengthMm) {
        Tube tube = tubeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tube not found with id " + id));

        tube.setQuantity(totalLengthMm);
        tubeRepository.save(tube);

        return mapToDTO(tube);
    }

    @Transactional
    @Override
    public TubePiece consumeTubeMaterial(Long tubeId, double requiredLengthMm, String usedFor) {

        if (requiredLengthMm <= 0)
            throw new IllegalArgumentException("Required length must be > 0");

        if (usedFor == null || usedFor.isBlank())
            throw new IllegalArgumentException("usedFor must be provided");

        Tube tube = tubeRepository.findById(tubeId)
                .orElseThrow(() -> new RuntimeException("Tube not found"));

        // Get all usable pieces sorted by length ascending
        List<TubePiece> available = tubePieceRepository
                .findByTubeIdAndIsScrapFalseOrderByLengthAsc(tubeId);

        // Choose the shortest piece that can satisfy the cut length
        TubePiece pieceToCut = available.stream()
                .filter(p -> p.getLength() >= requiredLengthMm && p.getQuantity() > 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available piece long enough"));

        // Cut ONE piece (pieceToCut quantity decreases by 1)
        TubePiece leftover = pieceToCut.cut(requiredLengthMm);

        // Handle leftover piece
        if (leftover != null) {

            // Try to merge with existing piece of same leftover length
            Optional<TubePiece> existing =
                    tubePieceRepository.findByTubeIdAndLength(tubeId, leftover.getLength());

            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + 1);
            } else {
                // No existing → add new leftover piece
                tube.getPieces().add(leftover);
            }
        }

        // Recalculate tube totals
        tube.updateQuantityFromPieces();
        tubeRepository.save(tube);

        // Save usage record
        TubeUsage usage = new TubeUsage();
        usage.setTubeId(tubeId);
        usage.setLengthUsed(requiredLengthMm);
        usage.setUsedFor(usedFor);
        tubeUsageRepository.save(usage);

        // Return the source piece (not the used piece)
        return pieceToCut;
    }


    @Override
    public List<TubePiece> getPieces(Long tubeId) {
        Tube tube = tubeRepository.findById(tubeId)
                .orElseThrow(() -> new RuntimeException("Tube not found"));
        return tube.getPieces();
    }

    private TubeDTO mapToDTO(Tube tube) {
        TubeDTO dto = modelMapper.map(tube, TubeDTO.class);
        dto.setTotalLength(tube.computeTotalLengthFromPieces());
        dto.setAvailableLength(tube.computeTotalLengthFromPieces());
        dto.setAvailablePieces(tube.getAvailablePieces());
        dto.setPieces(tube.getPieces().stream()
                .map(p -> modelMapper.map(p, TubePieceDTO.class))
                .collect(Collectors.toList()));
        return dto;
    }
}
