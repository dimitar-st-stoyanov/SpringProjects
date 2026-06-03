package com.dss_quotation.dss_quotation.controller;

import com.dss_quotation.dss_quotation.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quote")
@RequiredArgsConstructor
public class QuotePdfController {

    private final PdfService pdfService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportCustomerOfferPdf(@PathVariable Long id) {
        byte[] pdf = pdfService.exportCustomerOffer(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"customer-offer-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}/customer-offer-pdf")
    public ResponseEntity<byte[]> exportCustomerOfferPdfAlias(@PathVariable Long id) {
        return exportCustomerOfferPdf(id);
    }

    @GetMapping("/{id}/details-pdf")
    public ResponseEntity<byte[]> exportQuoteDetailsPdf(@PathVariable Long id) {
        byte[] pdf = pdfService.exportQuoteDetails(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"quote-details-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
