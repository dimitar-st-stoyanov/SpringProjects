package com.dss_erp.dss_erp.service;


import com.dss_erp.dss_erp.models.Quote;
import com.dss_erp.dss_erp.payload.QuoteListResponse;
import com.dss_erp.dss_erp.payload.QuoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuoteService {
    QuoteResponse generateQuote(MultipartFile file,
                                double thickness,
                                int bends,
                                String material,
                                Long machineId);
    List<Quote> getAll();

    Page<Quote> getPaginated(int page, int size);

}
