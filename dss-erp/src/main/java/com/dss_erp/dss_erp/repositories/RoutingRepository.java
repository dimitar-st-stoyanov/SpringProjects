package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.BomStatus;
import com.dss_erp.dss_erp.models.Routing;
import com.dss_erp.dss_erp.models.RoutingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface RoutingRepository extends JpaRepository<Routing, Long> {

    Page<Routing> findByProductId(Long productId, Pageable pageable);

    Optional<Routing> findByProductIdAndStatus(Long productId, RoutingStatus status);

    Optional<Routing> findFirstByProductIdAndStatusOrderByVersionDesc(Long productId, RoutingStatus routingStatus);
}
