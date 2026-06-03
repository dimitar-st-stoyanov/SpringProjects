package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Bar;
import com.dss_erp.dss_erp.models.BarPiece;
import com.dss_erp.dss_erp.models.BarUsage;
import com.dss_erp.dss_erp.models.Sheet;
import com.dss_erp.dss_erp.payload.BarDTO;
import com.dss_erp.dss_erp.payload.BarPieceDTO;
import com.dss_erp.dss_erp.payload.BaseMaterialResponse;
import com.dss_erp.dss_erp.payload.SheetDTO;
import com.dss_erp.dss_erp.repositories.BarRepository;
import com.dss_erp.dss_erp.repositories.BarPieceRepository;
import com.dss_erp.dss_erp.repositories.BarUsageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarServiceImpl implements BarService {

    private final BarRepository barRepository;
    private final BarPieceRepository barPieceRepository;
    private final ModelMapper modelMapper;
    private final BarUsageRepository barUsageRepository;

    // -------------------------------------------------------------------------
    // CREATE BAR
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public BarDTO create(BarDTO dto) {

        Bar bar = modelMapper.map(dto, Bar.class);
        bar.calculateWeight();
        bar.generateName();

        // Create initial piece if quantity & standard length exist
        if (bar.getQuantity() != null && bar.getStandardLength() != null) {

            BarPiece piece = new BarPiece();
            piece.setLength(bar.getStandardLength());
            piece.setQuantity(1);
            piece.setBar(bar);

            bar.getPieces().add(piece);
            bar.updateQuantityFromPieces();
        }

        Bar saved = barRepository.save(bar);
        return mapToDTO(saved);
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    @Override
    public void delete(Long id) {
        barRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Override
    public BarDTO getById(Long id) {
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bar not found with ID: " + id));
        return mapToDTO(bar);
    }

    @Override
    public BaseMaterialResponse<BarDTO> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword) {
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        Sort sort = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<Bar> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }

        Page<Bar> page = barRepository.findAll(spec, pageable);

        List<BarDTO> dtoList = page.getContent()
                .stream()
                .map(item -> modelMapper.map(item, BarDTO.class))
                .toList();

        return new BaseMaterialResponse<BarDTO>(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // -------------------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------------------
//    @Override
//    public List<BarDTO> getAll() {
//        return barRepository.findAll().stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }

    // -------------------------------------------------------------------------
    // RECEIVE DELIVERY (same logic as RectTube)
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public BarDTO receiveDelivery(Long barId, Integer piecesReceived, Double lengthPerPieceMm) {

        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found with id " + barId));

        if (piecesReceived == null || piecesReceived <= 0)
            throw new IllegalArgumentException("piecesReceived must be > 0");

        if (lengthPerPieceMm == null || lengthPerPieceMm <= 0)
            lengthPerPieceMm = bar.getStandardLength() != null ? bar.getStandardLength() : 6000.0;

        // Try to merge with existing entry
        Optional<BarPiece> existingOpt =
                barPieceRepository.findByBarIdAndLength(barId, lengthPerPieceMm);

        if (existingOpt.isPresent()) {
            BarPiece existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + piecesReceived);
            barPieceRepository.save(existing);
        } else {
            BarPiece newPiece = new BarPiece();
            newPiece.setLength(lengthPerPieceMm);
            newPiece.setQuantity(piecesReceived);
            newPiece.setBar(bar);
            bar.getPieces().add(newPiece);
        }

        bar.updateQuantityFromPieces();
        barRepository.save(bar);

        return mapToDTO(bar);
    }

    // -------------------------------------------------------------------------
    // MANUAL UPDATE OF QUANTITY
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public BarDTO updateQuantity(Long id, Double totalLengthMm) {
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bar not found with id " + id));

        bar.setQuantity(totalLengthMm);
        barRepository.save(bar);

        return mapToDTO(bar);
    }

    // -------------------------------------------------------------------------
    // CUT MATERIAL (same behavior as RectTube)
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public BarPiece consumeBarMaterial(Long barId, double requiredLengthMm, String usedFor) {

        if (requiredLengthMm <= 0)
            throw new IllegalArgumentException("Required length must be > 0");

        if (usedFor == null || usedFor.isBlank())
            throw new IllegalArgumentException("usedFor must be provided");

        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));

        // sorted by shortest first
        List<BarPiece> available =
                barPieceRepository.findByBarIdAndIsScrapFalseOrderByLengthAsc(barId);

        BarPiece pieceToCut =
                available.stream()
                        .filter(p -> p.getLength() >= requiredLengthMm && p.getQuantity() > 0)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No piece long enough"));

        // Cut one
        BarPiece leftover = pieceToCut.cut(requiredLengthMm);

        // Handle leftover
        if (leftover != null) {

            Optional<BarPiece> existing =
                    barPieceRepository.findByBarIdAndLength(barId, leftover.getLength());

            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + 1);
            } else {
                leftover.setBar(bar);
                bar.getPieces().add(leftover);
            }
        }

        bar.updateQuantityFromPieces();
        barRepository.save(bar);

        BarUsage usage = new BarUsage();
        usage.setBarId(barId);
        usage.setLengthUsed(requiredLengthMm);
        usage.setUsedFor(usedFor);
        barUsageRepository.save(usage);

        return pieceToCut;
    }

    // -------------------------------------------------------------------------
    // GET PIECES
    // -------------------------------------------------------------------------
    @Override
    public List<BarPiece> getPieces(Long barId) {
        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));
        return bar.getPieces();
    }

    // -------------------------------------------------------------------------
    // DTO MAPPING
    // -------------------------------------------------------------------------
    private BarDTO mapToDTO(Bar bar) {

        BarDTO dto = modelMapper.map(bar, BarDTO.class);

        dto.setTotalLength(bar.computeTotalLengthFromPieces());
        dto.setAvailableLength(bar.computeTotalLengthFromPieces());
        dto.setAvailablePieces(bar.getAvailablePieces());

        dto.setPieces(
                bar.getPieces().stream()
                        .map(p -> modelMapper.map(p, BarPieceDTO.class))
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
