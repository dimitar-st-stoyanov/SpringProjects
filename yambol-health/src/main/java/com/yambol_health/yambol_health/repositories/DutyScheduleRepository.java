package com.yambol_health.yambol_health.repositories;

import com.yambol_health.yambol_health.models.DutySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DutyScheduleRepository extends JpaRepository<DutySchedule, Long> {
    List<DutySchedule> findByDateOrderByStartTimeAsc(LocalDate date);
}
