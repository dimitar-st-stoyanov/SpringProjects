package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.payload.WorkCenterDTO;
import com.dss_erp.dss_erp.payload.WorkCenterResponse;
import com.dss_erp.dss_erp.service.WorkCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-centers")
@RequiredArgsConstructor
public class WorkCenterController {

    private final WorkCenterService service;

    @PostMapping
    public ResponseEntity<WorkCenterDTO> create(@RequestBody WorkCenterDTO dto) {
        WorkCenterDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkCenterDTO> update(@PathVariable Long id,
                                                @RequestBody WorkCenterDTO dto) {
        WorkCenterDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkCenterDTO> getById(@PathVariable Long id) {
        WorkCenterDTO result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<WorkCenterResponse> getAll(Pageable pageable) {
        WorkCenterResponse response = service.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
