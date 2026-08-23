package com.yambol_health.yambol_health.repositories;

import com.yambol_health.yambol_health.models.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
