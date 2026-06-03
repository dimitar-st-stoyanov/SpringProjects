package com.dss_quotation.dss_quotation.controller;

import com.dss_quotation.dss_quotation.models.QuoteDxfItem;
import com.dss_quotation.dss_quotation.payload.CreateQuoteRequest;
import com.dss_quotation.dss_quotation.payload.QuoteDetailsResponse;
import com.dss_quotation.dss_quotation.payload.QuoteFilterRequest;
import com.dss_quotation.dss_quotation.payload.QuoteListResponse2;
import com.dss_quotation.dss_quotation.payload.UpdateQuoteRequest;
import com.dss_quotation.dss_quotation.payload.UpdateQuoteStatusRequest;
import com.dss_quotation.dss_quotation.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quote")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuoteDetailsResponse> generateQuote(
            @ModelAttribute CreateQuoteRequest request
    ) {
        return ResponseEntity.ok(quoteService.generateQuote(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteDetailsResponse> getQuoteDetails(@PathVariable Long id) {
        return ResponseEntity.ok(quoteService.getDetails(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuoteDetailsResponse> updateQuote(
            @PathVariable Long id,
            @RequestBody UpdateQuoteRequest request
    ) {
        return ResponseEntity.ok(quoteService.updateQuote(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<QuoteDetailsResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateQuoteStatusRequest request
    ) {
        return ResponseEntity.ok(quoteService.updateStatus(id, request));
    }

    @GetMapping
    public ResponseEntity<Page<QuoteListResponse2>> getQuotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(quoteService.getPaginated(page, size));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<QuoteListResponse2>> filterQuotes(
            @RequestBody QuoteFilterRequest request
    ) {
        return ResponseEntity.ok(quoteService.filter(request));
    }

    @GetMapping("/{quoteId}/items/{itemId}/download")
    public ResponseEntity<byte[]> downloadDxf(
            @PathVariable Long quoteId,
            @PathVariable Long itemId
    ) {
        QuoteDxfItem item = quoteService.getDxfItem(quoteId, itemId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + item.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(item.getDxfFile());
    }
}
