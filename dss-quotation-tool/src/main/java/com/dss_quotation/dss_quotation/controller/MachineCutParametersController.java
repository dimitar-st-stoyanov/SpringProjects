package com.dss_quotation.dss_quotation.controller;

import com.dss_quotation.dss_quotation.payload.MachineCutParametersDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersRequest;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersResponse;
import com.dss_quotation.dss_quotation.service.MachineCutParametersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machine-speeds")
@RequiredArgsConstructor
public class MachineCutParametersController {

    private final MachineCutParametersService service;

    @GetMapping
    public Page<MachineCutParametersResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return service.getAll(page, size);
    }

    @GetMapping("/{machineId}")
    public Page<MachineCutParametersResponse> getByMachine(@PathVariable Long machineId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return service.getByMachine(machineId, page, size);
    }

    @GetMapping("/{machineId}/materials/{materialId}")
    public Page<MachineCutParametersResponse> getByMachineAndMaterial(@PathVariable Long machineId,
                                                                      @PathVariable Long materialId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return service.getByMachineAndMaterial(machineId, materialId, page, size);
    }

    @GetMapping("/details/{id}")
    public MachineCutParametersDetailsResponse getDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }

    @GetMapping("/{machineId}/materials/{materialId}/closest")
    public MachineCutParametersDetailsResponse getClosest(@PathVariable Long machineId,
                                                          @PathVariable Long materialId,
                                                          @RequestParam double thickness) {
        return service.getClosestDetails(machineId, materialId, thickness);
    }

    @PostMapping
    public MachineCutParametersDetailsResponse save(@Valid @RequestBody MachineCutParametersRequest request) {
        return service.save(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
