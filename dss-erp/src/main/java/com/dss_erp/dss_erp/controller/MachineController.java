package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.Machine;
import com.dss_erp.dss_erp.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @GetMapping
    public List<Machine> getAll() {
        return machineService.getAll();
    }

    @GetMapping("/{id}")
    public Machine getById(@PathVariable Long id) {
        return machineService.getById(id);
    }

    @PostMapping
    public Machine create(@RequestBody Machine machine) {
        return machineService.save(machine);
    }

    @PutMapping("/{id}")
    public Machine update(@PathVariable Long id, @RequestBody Machine machine) {
        machine.setId(id);
        return machineService.save(machine);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        machineService.delete(id);
    }
}
