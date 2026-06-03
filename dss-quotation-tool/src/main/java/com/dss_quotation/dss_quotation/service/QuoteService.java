package com.dss_quotation.dss_quotation.service;


import com.dss_quotation.dss_quotation.models.Quote;
import com.dss_quotation.dss_quotation.models.QuoteDxfItem;
import com.dss_quotation.dss_quotation.payload.*;
import org.springframework.data.domain.Page;

public interface QuoteService {

    QuoteDetailsResponse generateQuote(CreateQuoteRequest request);

    QuoteDetailsResponse updateQuote(Long id, UpdateQuoteRequest request);

    QuoteDetailsResponse updateStatus(Long id, UpdateQuoteStatusRequest request);

    QuoteDetailsResponse getDetails(Long id);

    Page<QuoteListResponse2> getPaginated(int page, int size);

    Page<QuoteListResponse2> filter(QuoteFilterRequest request);

    Quote getById(Long id);

    QuoteDxfItem getDxfItem(Long quoteId, Long itemId);


}
