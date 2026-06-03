package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.MachineCutSpeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineCutSpeedRepository extends JpaRepository<MachineCutSpeed, Long> {

    List<MachineCutSpeed> findByMachineIdOrderByThicknessAsc(Long machineId);


}
