package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCode(String code);
}
