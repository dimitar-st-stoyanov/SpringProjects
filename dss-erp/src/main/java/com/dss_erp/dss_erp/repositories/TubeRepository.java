package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Tube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TubeRepository  extends JpaRepository<Tube, Long>, JpaSpecificationExecutor<Tube> {
    Page<Tube> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
