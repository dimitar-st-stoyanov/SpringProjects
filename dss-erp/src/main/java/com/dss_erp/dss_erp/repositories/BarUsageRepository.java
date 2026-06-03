package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.BarUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarUsageRepository extends JpaRepository<BarUsage,Long> {
}
