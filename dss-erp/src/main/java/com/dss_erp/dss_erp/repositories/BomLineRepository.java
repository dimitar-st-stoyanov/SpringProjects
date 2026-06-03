package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.BomLine;
import com.dss_erp.dss_erp.models.BomStatus;
import com.dss_erp.dss_erp.models.Product;
import org.hibernate.type.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BomLineRepository extends JpaRepository<BomLine, Long> {
    List<BomLine> findByBomId(Long bomId);


    boolean existsByBomIdAndComponentTypeAndComponentIdAndIdNot(
            Long bomId,
            BomComponentType componentType,
            Long componentId,
            Long id
    );
    Optional<BomLine> findByIdAndBomId(Long id, Long bomId);

    boolean existsByBomIdAndComponentTypeAndComponentId(Long bomId, BomComponentType componentType, Long componentId);

    @Query("""
        SELECT DISTINCT b.product
        FROM BomLine bl
        JOIN bl.bom b
        WHERE
            bl.componentId = :componentId
            AND bl.componentType = :componentType
            AND b.status = :status
            AND b.version = (
                SELECT MAX(b2.version)
                FROM Bom b2
                WHERE b2.product = b.product
                AND b2.status = :status
            )
    """)
        List<Product> findWhereUsed(
                @Param("componentId") Long componentId,
                @Param("componentType") BomComponentType componentType,
                @Param("status") BomStatus status
        );


}
