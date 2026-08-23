package com.yambol_health.yambol_health.controllers;

import com.yambol_health.yambol_health.payloads.APIResponse;
import com.yambol_health.yambol_health.payloads.WorkingHoursDTO;
import com.yambol_health.yambol_health.services.WorkingHoursService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/working-hours")
public class WorkingHoursController {
    private final WorkingHoursService workingHoursService;

    public WorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
    }

    @PostMapping
    public ResponseEntity<WorkingHoursDTO> create(@Valid @RequestBody WorkingHoursDTO dto) {
        return new ResponseEntity<>(workingHoursService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkingHoursDTO>> getByPharmacyId(@RequestParam Long pharmacyId) {
        return ResponseEntity.ok(workingHoursService.getByPharmacyId(pharmacyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkingHoursDTO> update(@PathVariable Long id, @Valid @RequestBody WorkingHoursDTO dto) {
        return ResponseEntity.ok(workingHoursService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable Long id) {
        workingHoursService.delete(id);
        return ResponseEntity.ok(new APIResponse("Working hours deleted successfully", true));
    }
}
