package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.RectTube;
import com.dss_erp.dss_erp.models.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RectTubeRepository extends JpaRepository<RectTube, Long>, JpaSpecificationExecutor<RectTube> {
    Page<RectTube> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
