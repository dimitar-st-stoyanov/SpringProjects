package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    Optional<Vendor> findByVendorCode(String vendorCode);

    boolean existsByVendorCode(String vendorCode);
}