package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Rod;
import com.dss_erp.dss_erp.models.RodPiece;
import com.dss_erp.dss_erp.payload.RodDTO;
import com.dss_erp.dss_erp.payload.RodPieceDTO;
import com.dss_erp.dss_erp.repositories.RodPieceRepository;
import com.dss_erp.dss_erp.repositories.RodRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RodServiceImpl implements RodService {

    private final RodRepository rodRepository;
    private final RodPieceRepository rodPieceRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RodDTO create(RodDTO dto) {
        Rod rod = modelMapper.map(dto, Rod.class);
        rod.calculateWeight();
        rod.generateName();

        if (rod.getQuantity() != null && rod.getStandardLength() != null) {
            RodPiece piece = new RodPiece();
            piece.setLength(rod.getStandardLength());
            piece.setQuantity(1);
            piece.setRod(rod);
            rod.getPieces().add(piece);
            rod.updateQuantityFromPieces();
        }

        Rod saved = rodRepository.save(rod);
        return mapToDTO(saved);
    }

    @Override
    public void delete(Long id) {
        rodRepository.deleteById(id);
    }

    @Override
    public RodDTO getById(Long id) {
        Rod rod = rodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rod not found with ID: " + id));
        return mapToDTO(rod);
    }

    @Override
    public List<RodDTO> getAll() {
        return rodRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RodDTO receiveDelivery(Long rodId, Integer piecesReceived, Double lengthPerPieceMm) {
        Rod rod = rodRepository.findById(rodId)
                .orElseThrow(() -> new RuntimeException("Rod not found with id " + rodId));

        if (piecesReceived == null || piecesReceived <= 0)
            throw new IllegalArgumentException("piecesReceived must be greater than zero");

        if (lengthPerPieceMm == null || lengthPerPieceMm <= 0)
            lengthPerPieceMm = rod.getStandardLength() != null ? rod.getStandardLength() : 6000.0;

        Optional<RodPiece> existingPieceOpt = rodPieceRepository.findByRodIdAndLength(rodId, lengthPerPieceMm);
        if (existingPieceOpt.isPresent()) {
            RodPiece existing = existingPieceOpt.get();
            existing.setQuantity(existing.getQuantity() + piecesReceived);
            rodPieceRepository.save(existing);
        } else {
            RodPiece newPiece = new RodPiece();
            newPiece.setLength(lengthPerPieceMm);
            newPiece.setQuantity(piecesReceived);
            newPiece.setRod(rod);
            rod.getPieces().add(newPiece);
        }

        rod.updateQuantityFromPieces();
        rodRepository.save(rod);

        return mapToDTO(rod);
    }

    @Transactional
    @Override
    public RodDTO updateQuantity(Long id, Double totalLengthMm) {
        Rod rod = rodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rod not found with id " + id));

        rod.setQuantity(totalLengthMm);
        rodRepository.save(rod);

        return mapToDTO(rod);
    }

    @Transactional
    @Override
    public RodPiece consumeRodMaterial(Long rodId, double requiredLengthMm) {
        if (requiredLengthMm <= 0)
            throw new IllegalArgumentException("Required length must be > 0");

        Rod rod = rodRepository.findById(rodId)
                .orElseThrow(() -> new RuntimeException("Rod not found"));

        // Get all usable pieces sorted by length ascending
        List<RodPiece> available = rodPieceRepository
                .findByRodIdAndIsScrapFalseOrderByLengthAsc(rodId);

        // Choose the shortest piece that can satisfy the cut length
        RodPiece pieceToCut = available.stream()
                .filter(p -> p.getLength() >= requiredLengthMm && p.getQuantity() > 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available piece long enough"));

        // Cut ONE piece
        RodPiece leftover = pieceToCut.cut(requiredLengthMm);

        // Handle leftover
        if (leftover != null) {
            Optional<RodPiece> existing =
                    rodPieceRepository.findByRodIdAndLength(rodId, leftover.getLength());

            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + 1);
            } else {
                rod.getPieces().add(leftover);
            }
        }

        rod.updateQuantityFromPieces();
        rodRepository.save(rod);

        return pieceToCut;
    }

    @Override
    public List<RodPiece> getPieces(Long rodId) {
        Rod rod = rodRepository.findById(rodId)
                .orElseThrow(() -> new RuntimeException("Rod not found"));
        return rod.getPieces();
    }

    private RodDTO mapToDTO(Rod rod) {
        RodDTO dto = modelMapper.map(rod, RodDTO.class);
        dto.setTotalLength(rod.computeTotalLengthFromPieces());
        dto.setAvailableLength(rod.computeTotalLengthFromPieces());
        dto.setAvailablePieces(rod.getAvailablePieces());
        dto.setPieces(rod.getPieces().stream()
                .map(p -> modelMapper.map(p, RodPieceDTO.class))
                .collect(Collectors.toList()));
        return dto;
    }
}
