package com.dss_erp.dss_erp.controller;



import com.dss_erp.dss_erp.payload.SheetDTO;
import com.dss_erp.dss_erp.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials/sheets")
@RequiredArgsConstructor
public class SheetController {

    private final SheetService sheetService;

    // ✅ CREATE new sheet
    @PostMapping
    public ResponseEntity<SheetDTO> createSheet(@RequestBody SheetDTO sheetDTO) {
        SheetDTO created = sheetService.create(sheetDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ✅ GET all sheets
    @GetMapping
    public ResponseEntity<List<SheetDTO>> getAllSheets() {
        List<SheetDTO> sheets = sheetService.getAll();
        return ResponseEntity.ok(sheets);
    }

    // ✅ GET sheet by ID
    @GetMapping("/{id}")
    public ResponseEntity<SheetDTO> getSheetById(@PathVariable Long id) {
        SheetDTO sheet = sheetService.getById(id);
        return ResponseEntity.ok(sheet);
    }

    // ✅ RECEIVE delvery for sheet by ID
    @PutMapping("/{id}/increase")
    public ResponseEntity<SheetDTO> increaseQuantity(
            @PathVariable Long id,
            @RequestParam Integer amount) {

        SheetDTO updatedSheet = sheetService.increaseQuantity(id, amount);
        return ResponseEntity.ok(updatedSheet);
    }

    // ✅ DELETE sheet by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSheet(@PathVariable Long id) {
        sheetService.delete(id);
        return ResponseEntity.ok("Sheet with ID " + id + " deleted successfully.");
    }

    @PutMapping("/{id}/consume")
    public ResponseEntity<SheetDTO> consumeSheet(
            @PathVariable Long id,
            @RequestParam Integer amount,
            @RequestParam String usedFor) {

        SheetDTO updatedSheet = sheetService.consumeSheets(id, amount, usedFor);
        return ResponseEntity.ok(updatedSheet);
    }



}