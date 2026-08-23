package com.yambol_health.yambol_health.services;

import com.yambol_health.yambol_health.payloads.DutyScheduleDTO;

import java.time.LocalDate;
import java.util.List;

public interface DutyScheduleService {
    DutyScheduleDTO create(DutyScheduleDTO dutyScheduleDTO);
    List<DutyScheduleDTO> getByDate(LocalDate date);
    DutyScheduleDTO update(Long id, DutyScheduleDTO dutyScheduleDTO);
    void delete(Long id);
}
