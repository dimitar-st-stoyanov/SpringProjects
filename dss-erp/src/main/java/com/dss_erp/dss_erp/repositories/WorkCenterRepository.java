package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {

    Optional<WorkCenter> findByName(String name);

    boolean existsByName(String name);
}
