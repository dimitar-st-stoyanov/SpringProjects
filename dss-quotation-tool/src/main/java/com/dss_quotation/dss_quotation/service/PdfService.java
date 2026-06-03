package com.dss_quotation.dss_quotation.service;

public interface PdfService {

    byte[] exportCustomerOffer(Long quoteId);

    byte[] exportQuoteDetails(Long quoteId);
}
