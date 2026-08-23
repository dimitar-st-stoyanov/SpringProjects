package com.dss_quotation.dss_quotation.repositories;

import com.dss_quotation.dss_quotation.models.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByActiveTrue();

    Page<Operation> findByActiveTrueOrderByNameAsc(Pageable pageable);

    Page<Operation> findAllByOrderByNameAsc(Pageable pageable);

    Optional<Operation> findByName(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    Optional<Operation> findByNameIgnoreCase(String name);

}
