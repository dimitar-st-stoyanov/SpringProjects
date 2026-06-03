package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<Sheet, Long>, JpaSpecificationExecutor<Sheet> {

    Page<Sheet> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
