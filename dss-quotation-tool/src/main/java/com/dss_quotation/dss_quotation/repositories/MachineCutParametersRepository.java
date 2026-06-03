package com.dss_quotation.dss_quotation.repositories;

import com.dss_quotation.dss_quotation.models.GasType;
import com.dss_quotation.dss_quotation.models.MachineCutParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineCutParametersRepository extends JpaRepository<MachineCutParameters, Long> {

    List<MachineCutParameters> findByMachineIdOrderByThicknessAsc(Long machineId);

    List<MachineCutParameters> findByMachineIdAndMaterialIdOrderByThicknessAsc(Long machineId, Long materialId);

    Page<MachineCutParameters> findByMachineIdOrderByThicknessAsc(Long machineId, Pageable pageable);

    Page<MachineCutParameters> findByMachineIdAndMaterialIdOrderByThicknessAsc(Long machineId, Long materialId, Pageable pageable);

    boolean existsByMachineIdAndMaterialIdAndThicknessAndGasType(Long machineId, Long materialId, double thickness, GasType gasType);
}
