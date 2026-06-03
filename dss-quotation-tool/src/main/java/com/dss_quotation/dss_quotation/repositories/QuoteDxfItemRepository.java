package com.dss_quotation.dss_quotation.repositories;

import com.dss_quotation.dss_quotation.models.QuoteDxfItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteDxfItemRepository extends JpaRepository<QuoteDxfItem, Long> {

    Optional<QuoteDxfItem> findByIdAndQuoteId(Long id, Long quoteId);
}
