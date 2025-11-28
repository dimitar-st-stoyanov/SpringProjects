package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.RectTubePiece;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RectTubePieceRepository extends JpaRepository<RectTubePiece, Long> {

    List<RectTubePiece> findByRectTubeIdAndIsScrapFalseOrderByLengthAsc(Long tubeId);

    Optional<RectTubePiece> findByRectTubeIdAndLength(Long tubeId, Double originalLength);
}
