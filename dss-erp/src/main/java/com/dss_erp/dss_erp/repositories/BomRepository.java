package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Bom;
import com.dss_erp.dss_erp.models.BomExplosionRow;
import com.dss_erp.dss_erp.models.BomStatus;
import com.dss_erp.dss_erp.models.MaterialRequirementRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BomRepository extends JpaRepository<Bom, Long> {

    Optional<Bom> findFirstByProductIdAndStatusOrderByVersionDesc(
            Long productId,
            BomStatus status
    );

    List<Bom> findByProductIdOrderByVersionDesc(Long productId);

    boolean existsByProductIdAndStatus(Long productId, BomStatus status);
    boolean existsByIdAndProductId(Long bomId, Long productId);


    @Query(
            value = """
  WITH RECURSIVE bom_tree AS (

      -- 1️⃣ Anchor: start from selected BOM
      SELECT
          bl.component_id,
          bl.component_type,
          bl.quantity,
          1 AS level,
          ARRAY[b.product_id]::BIGINT[] AS path
      FROM boms b
      JOIN bom_lines bl ON bl.bom_id = b.id
      WHERE b.id = :bomId

      UNION ALL

      -- 2️⃣ Recursive: explode PRODUCT components (cycle-safe)
      SELECT
          bl.component_id,
          bl.component_type,
          bl.quantity,
          bt.level + 1,
          bt.path || b.product_id
      FROM bom_tree bt
      JOIN boms b
        ON bt.component_type = 'PRODUCT'
       AND b.product_id = bt.component_id
       AND b.status = 'RELEASED'
       AND NOT b.product_id = ANY(bt.path)
      JOIN bom_lines bl ON bl.bom_id = b.id
      WHERE b.version = (
          SELECT MAX(b2.version)
          FROM boms b2
          WHERE b2.product_id = b.product_id
            AND b2.status = 'RELEASED'
      )
  )
  SELECT
      component_id,
      component_type,
      quantity,
      level
  FROM bom_tree
  """,
            nativeQuery = true
    )

    List<BomExplosionRow> explodeBom(@Param("bomId") Long bomId);

    @Query(
            value = """
      WITH RECURSIVE bom_tree AS (

          -- Anchor: selected BOM
          SELECT
              bl.component_id,
              bl.component_type,
              bl.quantity                       AS total_quantity,
              ARRAY[b.product_id]::BIGINT[]    AS path
          FROM boms b
          JOIN bom_lines bl ON bl.bom_id = b.id
          WHERE b.id = :bomId

          UNION ALL

          -- Recursive: explode PRODUCT components (cycle-safe)
          SELECT
              bl.component_id,
              bl.component_type,
              bt.total_quantity * bl.quantity  AS total_quantity,
              bt.path || b.product_id
          FROM bom_tree bt
          JOIN boms b
            ON bt.component_type = 'PRODUCT'
           AND b.product_id = bt.component_id
           AND b.status = 'RELEASED'
           AND NOT b.product_id = ANY(bt.path)
          JOIN bom_lines bl ON bl.bom_id = b.id
          WHERE b.version = (
              SELECT MAX(b2.version)
              FROM boms b2
              WHERE b2.product_id = b.product_id
                AND b2.status = 'RELEASED'
          )
      )

      SELECT
          component_id,
          component_type,
          SUM(total_quantity) AS total_quantity
      FROM bom_tree
      WHERE component_type <> 'PRODUCT'
      GROUP BY component_id, component_type
      """,
            nativeQuery = true
    )
    List<MaterialRequirementRow> explodeMaterials(@Param("bomId") Long bomId);
}
