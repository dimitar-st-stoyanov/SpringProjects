package com.dss_quotation.dss_quotation.repositories;

import com.dss_quotation.dss_quotation.models.AppRole;
import com.dss_quotation.dss_quotation.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
