package com.dss_quotation.dss_quotation.controller;

import com.dss_quotation.dss_quotation.payload.OperationDetailsResponse;
import com.dss_quotation.dss_quotation.payload.OperationRequest;
import com.dss_quotation.dss_quotation.payload.OperationResponse;
import com.dss_quotation.dss_quotation.service.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
@CrossOrigin
public class OperationController {

    private final OperationService operationService;

    @GetMapping("/all")
    public Page<OperationResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return operationService.getPaginated(page, size);
    }

    @GetMapping
    public Page<OperationResponse> getActive(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return operationService.getActivePaginated(page, size);
    }

    @GetMapping("/{id}")
    public OperationDetailsResponse getById(@PathVariable Long id) {
        return operationService.getDetails(id);
    }

    @PostMapping
    public OperationDetailsResponse create(@Valid @RequestBody OperationRequest request) {
        return operationService.create(request);
    }

    @PutMapping("/{id}")
    public OperationDetailsResponse update(@PathVariable Long id,
                                           @Valid @RequestBody OperationRequest request) {
        return operationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        operationService.delete(id);
    }
}
