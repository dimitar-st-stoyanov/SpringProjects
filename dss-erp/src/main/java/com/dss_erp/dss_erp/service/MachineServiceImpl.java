package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Machine;
import com.dss_erp.dss_erp.repositories.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    @Override
    public List<Machine> getAll() {
        return machineRepository.findAll();
    }

    @Override
    public Machine getById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));
    }

    @Override
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public void delete(Long id) {
        machineRepository.deleteById(id);
    }
}
