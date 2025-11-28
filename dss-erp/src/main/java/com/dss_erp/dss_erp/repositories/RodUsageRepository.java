package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.RodUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RodUsageRepository extends JpaRepository<RodUsage, Long> {
}
