package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.TubeUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TubeUsageRepository extends JpaRepository <TubeUsage, Long> {
}
