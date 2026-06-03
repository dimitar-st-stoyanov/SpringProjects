package com.dss_quotation.dss_quotation.controller;

import com.dss_quotation.dss_quotation.payload.MaterialDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MaterialRequest;
import com.dss_quotation.dss_quotation.payload.MaterialResponse;
import com.dss_quotation.dss_quotation.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@CrossOrigin
public class MaterialController {

    private final MaterialService materialService;

    /* ===============================
       GET ALL (ADMIN)
    =============================== */
    @GetMapping("/all")
    public Page<MaterialResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return materialService.getPaginated(page, size);
    }

    /* ===============================
       GET ACTIVE (FOR DROPDOWN)
    =============================== */
    @GetMapping
    public Page<MaterialResponse> getActive(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return materialService.getActivePaginated(page, size);
    }

    /* ===============================
       GET BY ID
    =============================== */
    @GetMapping("/{id}")
    public MaterialDetailsResponse getById(@PathVariable Long id) {
        return materialService.getDetails(id);
    }

    /* ===============================
       CREATE
    =============================== */
    @PostMapping
    public MaterialDetailsResponse create(@Valid @RequestBody MaterialRequest request) {
        return materialService.create(request);
    }

    /* ===============================
       UPDATE
    =============================== */
    @PutMapping("/{id}")
    public MaterialDetailsResponse update(@PathVariable Long id,
                                          @Valid @RequestBody MaterialRequest request) {
        return materialService.update(id, request);
    }

    /* ===============================
       DELETE (SOFT)
    =============================== */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materialService.delete(id);
    }
}
