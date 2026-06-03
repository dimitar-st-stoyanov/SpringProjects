package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.PriceSourceType;
import com.dss_erp.dss_erp.models.PurchasePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchasePriceRepository extends JpaRepository<PurchasePrice, UUID> {

    // -------------------------
    // VendorMaterial → Prices
    // -------------------------

    Page<PurchasePrice> findAllByVendorMaterial_Id(
            UUID vendorMaterialId,
            Pageable pageable
    );

    // -------------------------
    // Valid price resolution
    // -------------------------

    @Query("""
        select p from PurchasePrice p
        where p.vendorMaterial.id = :vendorMaterialId
          and p.active = true
          and p.validFrom <= :date
          and (p.validTo is null or p.validTo >= :date)
        order by p.validFrom desc
    """)
    Optional<PurchasePrice> findValidPriceOnDate(
            UUID vendorMaterialId,
            LocalDate date
    );

    // -------------------------
    // Overlap detection
    // -------------------------

    @Query("""
    select p from PurchasePrice p
    where p.vendorMaterial.id = :vendorMaterialId
      and p.priceSource = :priceSource
      and p.active = true
      and p.validFrom <= :date
      and (p.validTo is null or p.validTo >= :date)
    order by p.validFrom desc
""")
    Optional<PurchasePrice> findValidPriceByVendorMaterialAndPriceSource(
            UUID vendorMaterialId,
            PriceSourceType priceSource,
            LocalDate date
    );


    Optional<PurchasePrice>
    findTopByVendorMaterial_IdAndPriceSourceAndActiveTrueOrderByValidFromDesc(
            UUID vendorMaterialId,
            PriceSourceType priceSource
    );


    @Query("""
    select case when count(p) > 0 then true else false end
    from PurchasePrice p
    where p.vendorMaterial.id = :vendorMaterialId
      and p.active = true
      and p.validFrom <= :validTo
      and (p.validTo is null or p.validTo >= :validFrom)
""")
    boolean existsOverlappingPrice(
            UUID vendorMaterialId,
            LocalDate validFrom,
            LocalDate validTo
    );

    @Query("""
SELECT p
FROM PurchasePrice p
WHERE p.vendorMaterial.id IN :vendorMaterialIds
  AND p.priceSource = :source
  AND p.active = true
  AND p.validFrom <= :today
  AND (p.validTo IS NULL OR p.validTo >= :today)
""")
    List<PurchasePrice> findValidPricesByVendorMaterials(
            @Param("vendorMaterialIds") Collection<UUID> vendorMaterialIds,
            @Param("source") PriceSourceType source,
            @Param("today") LocalDate today
    );

    List<PurchasePrice> findByVendorMaterial_IdInAndPriceSourceAndActiveTrue(
            Collection<UUID> vendorMaterialIds,
            PriceSourceType priceSource
    );


}
