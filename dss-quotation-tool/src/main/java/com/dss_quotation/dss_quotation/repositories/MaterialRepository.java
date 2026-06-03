package com.dss_quotation.dss_quotation.repositories;

import com.dss_quotation.dss_quotation.models.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material,Long> {

    // Find only active materials (for dropdowns)
    List<Material> findByActiveTrue();

    Page<Material> findByActiveTrueOrderByNameAsc(Pageable pageable);

    Page<Material> findAllByOrderByNameAsc(Pageable pageable);

    // Optional: find by name (useful for init / validation)
    Optional<Material> findByName(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
