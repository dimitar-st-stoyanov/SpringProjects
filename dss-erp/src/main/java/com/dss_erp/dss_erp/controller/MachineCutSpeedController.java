package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.Machine;
import com.dss_erp.dss_erp.models.MachineCutSpeed;
import com.dss_erp.dss_erp.payload.MachineCutSpeedRequest;
import com.dss_erp.dss_erp.service.MachineCutSpeedService;
import com.dss_erp.dss_erp.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machine-speeds")
@RequiredArgsConstructor
public class MachineCutSpeedController {

    private final MachineCutSpeedService service;
    private final MachineService machineService;

    @GetMapping("/{machineId}")
    public List<MachineCutSpeed> getByMachine(@PathVariable Long machineId) {
        return service.getByMachine(machineId);
    }

    @PostMapping
    public MachineCutSpeed save(@RequestBody MachineCutSpeedRequest request) {

        Machine machine = machineService.getById(request.getMachineId());

        MachineCutSpeed speed = MachineCutSpeed.builder()
                .machine(machine)
                .thickness(request.getThickness())
                .speed(request.getSpeed())
                .build();

        return service.save(speed);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
