package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.BarPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarPieceRepository extends JpaRepository<BarPiece, Long> {

    /** Get all non-scrap pieces of a specific Bar, sorted from shortest to longest */
    List<BarPiece> findByBarIdAndIsScrapFalseOrderByLengthAsc(Long barId);

    /** Find a piece by exact length for a given Bar (optional helper) */
    Optional<BarPiece> findByBarIdAndLength(Long barId, Double length);
}
