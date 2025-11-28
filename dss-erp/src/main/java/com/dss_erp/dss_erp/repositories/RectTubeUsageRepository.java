package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.RectTubeUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RectTubeUsageRepository extends JpaRepository<RectTubeUsage, Long> {
}
