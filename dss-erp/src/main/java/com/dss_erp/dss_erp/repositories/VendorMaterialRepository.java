package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.VendorMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorMaterialRepository extends JpaRepository<VendorMaterial, UUID> {

    // Uniqueness / validation
    boolean existsByVendor_IdAndComponentTypeAndComponentId(
            UUID vendorId,
            BomComponentType componentType,
            Long componentId
    );

    Optional<VendorMaterial> findByVendor_IdAndComponentTypeAndComponentId(
            UUID vendorId,
            BomComponentType componentType,
            Long componentId
    );

    // Vendor → Materials
    Page<VendorMaterial> findAllByVendor_Id(
            UUID vendorId,
            Pageable pageable
    );

    // Component → Vendors
    Page<VendorMaterial> findAllByComponentTypeAndComponentId(
            BomComponentType componentType,
            Long componentId,
            Pageable pageable
    );

    boolean existsByVendor_IdAndComponentTypeAndComponentIdAndPreferredTrue(
            UUID vendorId,
            BomComponentType componentType,
            Long componentId
    );

    @Modifying
    @Query("""
       UPDATE VendorMaterial vm
       SET vm.preferred = false
       WHERE vm.vendor.id = :vendorId
         AND vm.componentType = :componentType
         AND vm.componentId = :componentId
       """)
    void resetPreferredForVendorAndComponent(
            @Param("vendorId") UUID vendorId,
            @Param("componentType") BomComponentType componentType,
            @Param("componentId") Long componentId
    );

    Page<VendorMaterial> findAllByComponentTypeAndComponentIdAndActiveTrue(
            BomComponentType componentType,
            Long componentId,
            Pageable pageable
    );

    Optional<VendorMaterial>findByComponentTypeAndComponentIdAndPreferredTrueAndActiveTrue(
            BomComponentType componentType,
            Long componentId
    );

    List<VendorMaterial> findAllByComponentTypeAndComponentIdInAndPreferredTrueAndActiveTrue(
            BomComponentType componentType,
            Collection<Long> componentIds
    );

}
