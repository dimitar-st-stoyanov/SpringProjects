package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Product;
import com.dss_erp.dss_erp.models.Routing;
import com.dss_erp.dss_erp.payload.RoutingDTO;
import com.dss_erp.dss_erp.payload.RoutingResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

public interface RoutingService {


    RoutingDTO createRouting(Product product, String note);

    @Transactional
    Routing cloneRouting(Long sourceRoutingId, String note);


    RoutingDTO releaseRouting(Long id);

    RoutingDTO getById(Long id);

    RoutingResponse getAll(Pageable pageable);

    RoutingResponse getByProduct(Long productId, Pageable pageable);

    void delete(Long id);

    Routing getLatestRouting(Long productId);
}
