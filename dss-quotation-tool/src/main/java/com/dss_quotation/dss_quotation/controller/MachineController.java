package com.dss_quotation.dss_quotation.controller;

import com.dss_quotation.dss_quotation.payload.MachineDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MachineRequest;
import com.dss_quotation.dss_quotation.payload.MachineResponse;
import com.dss_quotation.dss_quotation.service.MachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @GetMapping
    public Page<MachineResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return machineService.getPaginated(page, size);
    }

    @GetMapping("/{id}")
    public MachineDetailsResponse getById(@PathVariable Long id) {
        return machineService.getDetails(id);
    }

    @PostMapping
    public MachineDetailsResponse create(@Valid @RequestBody MachineRequest request) {
        return machineService.create(request);
    }

    @PutMapping("/{id}")
    public MachineDetailsResponse update(@PathVariable Long id,
                                         @Valid @RequestBody MachineRequest request) {
        return machineService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        machineService.delete(id);
    }
}
