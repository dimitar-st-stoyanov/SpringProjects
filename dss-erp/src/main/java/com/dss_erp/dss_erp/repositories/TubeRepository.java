package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Tube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TubeRepository  extends JpaRepository<Tube, Long> {
}
