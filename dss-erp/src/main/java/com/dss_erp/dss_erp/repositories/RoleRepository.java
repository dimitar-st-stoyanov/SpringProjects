package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.AppRole;
import com.dss_erp.dss_erp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
