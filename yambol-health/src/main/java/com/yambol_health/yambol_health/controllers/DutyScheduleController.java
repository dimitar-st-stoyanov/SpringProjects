package com.yambol_health.yambol_health.controllers;

import com.yambol_health.yambol_health.payloads.APIResponse;
import com.yambol_health.yambol_health.payloads.DutyScheduleDTO;
import com.yambol_health.yambol_health.services.DutyScheduleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/duty-schedules")
public class DutyScheduleController {
    private final DutyScheduleService dutyScheduleService;

    public DutyScheduleController(DutyScheduleService dutyScheduleService) {
        this.dutyScheduleService = dutyScheduleService;
    }

    @PostMapping
    public ResponseEntity<DutyScheduleDTO> create(@Valid @RequestBody DutyScheduleDTO dto) {
        return new ResponseEntity<>(dutyScheduleService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DutyScheduleDTO>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(dutyScheduleService.getByDate(date));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DutyScheduleDTO> update(@PathVariable Long id, @Valid @RequestBody DutyScheduleDTO dto) {
        return ResponseEntity.ok(dutyScheduleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable Long id) {
        dutyScheduleService.delete(id);
        return ResponseEntity.ok(new APIResponse("Duty schedule deleted successfully", true));
    }
}
