package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    Optional<Machine> findByName(String name);
}
