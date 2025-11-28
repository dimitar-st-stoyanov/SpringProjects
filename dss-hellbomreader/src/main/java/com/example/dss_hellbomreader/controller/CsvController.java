package com.example.dss_hellbomreader.controller;

import com.example.dss_hellbomreader.model.AggregatedRow;
import com.example.dss_hellbomreader.model.CsvRow;
import com.example.dss_hellbomreader.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/csv")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CsvController {

    private final CsvService csvService;

    /**
     * Upload CSV, aggregate by component, and export as a comma-separated CSV file.
     * Returns the path of the exported CSV.
     */
    @PostMapping("/process")
    public ResponseEntity<?> processCsv(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            // Save uploaded file temporarily
            Path tempInput = Files.createTempFile("input-", ".csv");
            file.transferTo(tempInput.toFile());

            // Read CSV
            List<CsvRow> rows = csvService.readCsv(tempInput.toString());
            if (rows.isEmpty()) {
                return ResponseEntity.badRequest().body("CSV is empty or invalid.");
            }

            // Aggregate rows
            List<AggregatedRow> aggregated = csvService.aggregateRows(rows);

            // Export CSV
            Path tempOutput = Files.createTempFile("aggregated-", ".csv");
            csvService.exportCsv(aggregated, tempOutput.toString());

            // Return file as bytes for download
            byte[] fileBytes = Files.readAllBytes(tempOutput);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=aggregated.csv")
                    .header("Content-Type", "text/csv")
                    .body(fileBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing CSV: " + e.getMessage());
        }
    }
}
