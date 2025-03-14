package com.erpdesing.dss_erp_system.rawmaterials.sheets;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sheets")
@CrossOrigin("*")

public class SheetsController {
	private final SheetsService sheetsService;

    public SheetsController(SheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @GetMapping
    public ResponseEntity<List<Sheets>> findAllSheets() {
        return ResponseEntity.ok(sheetsService.getAllSheets());
    }

    @PostMapping
    public ResponseEntity<Sheets> addOneSheet(@RequestBody Sheets sheet) {
        Sheets savedSheet = sheetsService.addSheet(sheet);
        return ResponseEntity.status(201).body(savedSheet);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSheet(@PathVariable int id) {
        boolean isDeleted = sheetsService.deleteSheet(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content if deleted
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found if sheet not found
        }
    }
}
