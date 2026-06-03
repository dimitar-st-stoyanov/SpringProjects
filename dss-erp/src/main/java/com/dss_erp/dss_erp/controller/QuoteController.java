package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.Quote;
import com.dss_erp.dss_erp.payload.QuoteListResponse;
import com.dss_erp.dss_erp.payload.QuoteResponse;
import com.dss_erp.dss_erp.repositories.QuoteRepository;
import com.dss_erp.dss_erp.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/quote")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;
    private final QuoteRepository quoteRepository;

    @PostMapping
    public ResponseEntity<QuoteResponse> generateQuote(
            @RequestParam("file") MultipartFile file,
            @RequestParam double thickness,
            @RequestParam int bends,
            @RequestParam String material,
            @RequestParam Long machineId   // ✅ NEW
    ) {
        return ResponseEntity.ok(
                quoteService.generateQuote(file, thickness, bends, material, machineId)
        );
    }

    /* ===============================
       GET ALL (simple for now)
    =============================== */
    @GetMapping("/all")
    public List<Quote> getAllQuotes() {
        return quoteService.getAll();
    }

    /* ===============================
       PAGINATED (better later)
    =============================== */
    @GetMapping
    public Page<Quote> getQuotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return quoteService.getPaginated(page, size);
    }

}
