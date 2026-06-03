package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.payload.RoutingLineDTO;
import com.dss_erp.dss_erp.payload.RoutingLineResponse;
import com.dss_erp.dss_erp.service.RoutingLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routing-lines")
@RequiredArgsConstructor
public class RoutingLineController {

    private final RoutingLineService service;

    @PostMapping
    public ResponseEntity<RoutingLineDTO> create(@RequestBody RoutingLineDTO dto) {
        RoutingLineDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutingLineDTO> update(@PathVariable Long id,
                                                 @RequestBody RoutingLineDTO dto) {
        RoutingLineDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutingLineDTO> getById(@PathVariable Long id) {
        RoutingLineDTO result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/routing/{routingId}")
    public ResponseEntity<RoutingLineResponse> getByRouting(@PathVariable Long routingId,
                                                            Pageable pageable) {
        RoutingLineResponse response = service.getByRouting(routingId, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
