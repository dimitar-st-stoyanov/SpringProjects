package com.yambol_health.yambol_health.services;

import com.yambol_health.yambol_health.payloads.WorkingHoursDTO;

import java.util.List;

public interface WorkingHoursService {
    WorkingHoursDTO create(WorkingHoursDTO workingHoursDTO);
    List<WorkingHoursDTO> getByPharmacyId(Long pharmacyId);
    WorkingHoursDTO update(Long id, WorkingHoursDTO workingHoursDTO);
    void delete(Long id);
}
