package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Bar;
import com.dss_erp.dss_erp.models.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BarRepository extends JpaRepository<Bar, Long>, JpaSpecificationExecutor<Bar> {
    Page<Bar> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
