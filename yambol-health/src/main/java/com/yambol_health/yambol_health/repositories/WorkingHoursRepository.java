package com.yambol_health.yambol_health.repositories;

import com.yambol_health.yambol_health.models.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findByPharmacyIdOrderByDayOfWeekAsc(Long pharmacyId);
}
