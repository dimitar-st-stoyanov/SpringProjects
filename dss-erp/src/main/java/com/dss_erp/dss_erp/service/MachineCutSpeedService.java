package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Machine;
import com.dss_erp.dss_erp.models.MachineCutSpeed;

import java.util.List;

public interface MachineCutSpeedService {

    double getCutSpeed(Long machineId, double thickness);

    List<MachineCutSpeed> getByMachine(Long machineId);

    MachineCutSpeed save(MachineCutSpeed speed);

    void delete(Long id);

}