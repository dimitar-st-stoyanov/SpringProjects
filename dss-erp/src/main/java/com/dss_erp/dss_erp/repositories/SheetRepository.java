package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<Sheet, Long> {
}
