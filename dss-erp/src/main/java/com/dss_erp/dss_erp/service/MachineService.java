package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Machine;

import java.util.List;

public interface MachineService {
    List<Machine> getAll();
    Machine getById(Long id);
    Machine save(Machine machine);
    void delete(Long id);
}
