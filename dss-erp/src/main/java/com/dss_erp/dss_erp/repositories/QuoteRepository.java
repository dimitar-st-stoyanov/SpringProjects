package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
