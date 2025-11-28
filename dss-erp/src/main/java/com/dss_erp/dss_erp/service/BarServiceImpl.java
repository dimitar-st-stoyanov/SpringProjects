package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Bar;
import com.dss_erp.dss_erp.models.BarPiece;
import com.dss_erp.dss_erp.models.RectTube;
import com.dss_erp.dss_erp.models.RectTubePiece;
import com.dss_erp.dss_erp.payload.BarDTO;
import com.dss_erp.dss_erp.payload.BarPieceDTO;
import com.dss_erp.dss_erp.payload.RectTubeDTO;
import com.dss_erp.dss_erp.repositories.BarRepository;
import com.dss_erp.dss_erp.repositories.BarPieceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarServiceImpl implements BarService {

    private final BarRepository barRepository;
    private final BarPieceRepository barPieceRepository;
    private final ModelMapper modelMapper;

    /** Create a new Bar with pieces */
    @Override
    @Transactional
    public BarDTO create(BarDTO dto) {
        Bar bar = modelMapper.map(dto, Bar.class);
        bar.calculateWeight();
        bar.generateName();

        // create BarPieces for initial quantity
        if (bar.getQuantity() != null && bar.getStandardLength() != null) {
            for (int i = 0; i < bar.getQuantity(); i++) {
                BarPiece piece = new BarPiece();
                piece.setOriginalLength(bar.getStandardLength());
                piece.setRemainingLength(bar.getStandardLength());
                piece.setBar(bar);
                bar.getPieces().add(piece);
            }
        }

        Bar saved = barRepository.save(bar);
        return mapToDTO(saved);
    }


    /** Delete Bar */
    @Override
    public void delete(Long id) {
        barRepository.deleteById(id);
    }

    /** Get Bar by ID */
    @Override
    public BarDTO getById(Long id) {
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bar not found with ID: " + id));
        return mapToDTO(bar);
    }

    /** Get all Bars */
    @Override
    public List<BarDTO> getAll() {
        return barRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BarDTO receiveDelivery(Long barId, Integer piecesReceived, Double lengthPerPieceMm) {
        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Tube not found with id " + barId));

        if (piecesReceived == null || piecesReceived <= 0) {
            throw new IllegalArgumentException("piecesReceived must be greater than zero");
        }

        // Default to standard length if not provided
        if (lengthPerPieceMm == null || lengthPerPieceMm <= 0) {
            lengthPerPieceMm = bar.getStandardLength() != null ? bar.getStandardLength() : 6000.0;
        }

        // Update quantity
        if (bar.getQuantity() == null) bar.setQuantity(0);
        bar.setQuantity(bar.getQuantity() + piecesReceived);

        // Add new TubePieces
        for (int i = 0; i < piecesReceived; i++) {
            BarPiece piece = new BarPiece();
            piece.setOriginalLength(lengthPerPieceMm);
            piece.setRemainingLength(lengthPerPieceMm);
            piece.setBar(bar);
            bar.getPieces().add(piece);
        }

        bar.calculateWeight();
        Bar saved = barRepository.save(bar);
        return mapToDTO(saved);
    }

    /** Update quantity and adjust BarPieces */
    @Override
    @Transactional
    public BarDTO updateQuantity(Long id, Integer quantity) {
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bar not found with id " + id));

        int oldQuantity = bar.getQuantity() != null ? bar.getQuantity() : 0;
        bar.setQuantity(quantity);

        int diff = quantity - oldQuantity;
        if (diff > 0 && bar.getStandardLength() != null) {
            // add new pieces
            for (int i = 0; i < diff; i++) {
                BarPiece piece = new BarPiece();
                piece.setOriginalLength(bar.getStandardLength());
                piece.setRemainingLength(bar.getStandardLength());
                piece.setBar(bar);
                bar.getPieces().add(piece);
            }
        } else if (diff < 0) {
            // remove newest pieces
            bar.getPieces().sort((p1, p2) -> p2.getId().compareTo(p1.getId()));
            for (int i = 0; i < -diff; i++) {
                BarPiece piece = bar.getPieces().remove(0);
                if (piece.getId() != null) {
                    barPieceRepository.delete(piece);
                }
            }
        }

        bar.calculateWeight();
        Bar saved = barRepository.save(bar);
        return mapToDTO(saved);
    }

    /** Cut a piece of Bar */
    @Override
    @Transactional
    public BarPiece cutPiece(Long barId, Long pieceId, double cutLengthMm) {
        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));

        BarPiece piece = bar.getPieces().stream()
                .filter(p -> p.getId().equals(pieceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Piece not found"));

        if (cutLengthMm > piece.getRemainingLength()) {
            throw new IllegalArgumentException("Not enough length in this piece");
        }

        piece.setRemainingLength(piece.getRemainingLength() - cutLengthMm);
        if (piece.getRemainingLength() < 100.0) {
            piece.setIsScrap(true);
        }

        return barPieceRepository.save(piece);
    }

    /** List all pieces of a Bar */
    @Override
    public List<BarPiece> getPieces(Long barId) {
        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));
        return bar.getPieces();
    }

    /** Map Bar → BarDTO (null-safe) */
    private BarDTO mapToDTO(Bar bar) {
        BarDTO dto = modelMapper.map(bar, BarDTO.class);

        List<BarPiece> pieces = bar.getPieces() != null
                ? bar.getPieces()
                : List.of();

        dto.setTotalLength(bar.getTotalLength());
        dto.setAvailableLength(bar.getAvailableLength());
        dto.setAvailablePieces(bar.getAvailablePieces());

        dto.setPieces(
                pieces.stream()
                        .map(p -> modelMapper.map(p, BarPieceDTO.class))
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
