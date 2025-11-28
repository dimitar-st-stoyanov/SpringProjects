package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.RodPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RodPieceRepository extends JpaRepository<RodPiece, Long> {

    List<RodPiece> findByRodIdAndIsScrapFalseOrderByLengthAsc(Long rodId);

    Optional<RodPiece> findByRodIdAndLength(Long rodId, Double originalLength);
}
