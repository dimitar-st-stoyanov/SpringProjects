package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.RoutingLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoutingLineRepository extends JpaRepository<RoutingLine, Long> {

    Page<RoutingLine> findByRoutingIdOrderBySequence(Long routingId, Pageable pageable);
}
