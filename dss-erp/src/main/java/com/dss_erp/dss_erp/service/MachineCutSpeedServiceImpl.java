package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.MachineCutSpeed;
import com.dss_erp.dss_erp.repositories.MachineCutSpeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineCutSpeedServiceImpl implements MachineCutSpeedService {

    private final MachineCutSpeedRepository repository;

    @Override
    public double getCutSpeed(Long machineId, double thickness) {

        List<MachineCutSpeed> speeds =
                repository.findByMachineIdOrderByThicknessAsc(machineId);

        if (speeds.isEmpty()) return 1000;

        MachineCutSpeed closest = speeds.get(0);
        double minDiff = Math.abs(thickness - closest.getThickness());

        for (MachineCutSpeed s : speeds) {
            double diff = Math.abs(thickness - s.getThickness());

            if (diff < minDiff) {
                minDiff = diff;
                closest = s;
            }
        }

        return closest.getSpeed();
    }

    @Override
    public List<MachineCutSpeed> getByMachine(Long machineId) {
        return repository.findByMachineIdOrderByThicknessAsc(machineId);
    }

    @Override
    public MachineCutSpeed save(MachineCutSpeed speed) {
        return repository.save(speed);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
