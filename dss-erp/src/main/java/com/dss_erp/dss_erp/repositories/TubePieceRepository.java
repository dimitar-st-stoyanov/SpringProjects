package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.TubePiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TubePieceRepository extends JpaRepository<TubePiece, Long> {

    List<TubePiece> findByTubeIdAndIsScrapFalseOrderByLengthAsc(Long tubeId);

    Optional<TubePiece> findByTubeIdAndLength(Long tubeId, Double originalLength);
}
