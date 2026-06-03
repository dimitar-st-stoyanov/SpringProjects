package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.RoutingLineDTO;
import com.dss_erp.dss_erp.payload.RoutingLineResponse;
import org.springframework.data.domain.Pageable;

public interface RoutingLineService {

    RoutingLineDTO create(RoutingLineDTO dto);

    RoutingLineDTO update(Long id, RoutingLineDTO dto);

    RoutingLineDTO getById(Long id);

    RoutingLineResponse getByRouting(Long routingId, Pageable pageable);

    void delete(Long id);
}
