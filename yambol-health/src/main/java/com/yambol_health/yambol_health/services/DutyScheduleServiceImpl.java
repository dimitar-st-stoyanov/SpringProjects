package com.yambol_health.yambol_health.services;

import com.yambol_health.yambol_health.exceptions.APIException;
import com.yambol_health.yambol_health.exceptions.ResourceNotFoundException;
import com.yambol_health.yambol_health.models.DutySchedule;
import com.yambol_health.yambol_health.models.Pharmacy;
import com.yambol_health.yambol_health.payloads.DutyScheduleDTO;
import com.yambol_health.yambol_health.repositories.DutyScheduleRepository;
import com.yambol_health.yambol_health.repositories.PharmacyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DutyScheduleServiceImpl implements DutyScheduleService {
    private final DutyScheduleRepository dutyScheduleRepository;
    private final PharmacyRepository pharmacyRepository;

    public DutyScheduleServiceImpl(DutyScheduleRepository dutyScheduleRepository, PharmacyRepository pharmacyRepository) {
        this.dutyScheduleRepository = dutyScheduleRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public DutyScheduleDTO create(DutyScheduleDTO dto) {
        validateTimes(dto);
        DutySchedule dutySchedule = DutySchedule.builder().pharmacy(findPharmacy(dto.getPharmacyId()))
                .date(dto.getDate()).startTime(dto.getStartTime()).endTime(dto.getEndTime()).build();
        return toDto(dutyScheduleRepository.save(dutySchedule));
    }

    @Override
    public List<DutyScheduleDTO> getByDate(LocalDate date) {
        return dutyScheduleRepository.findByDateOrderByStartTimeAsc(date).stream().map(this::toDto).toList();
    }

    @Override
    public DutyScheduleDTO update(Long id, DutyScheduleDTO dto) {
        validateTimes(dto);
        DutySchedule dutySchedule = findDutySchedule(id);
        dutySchedule.setPharmacy(findPharmacy(dto.getPharmacyId()));
        dutySchedule.setDate(dto.getDate());
        dutySchedule.setStartTime(dto.getStartTime());
        dutySchedule.setEndTime(dto.getEndTime());
        return toDto(dutyScheduleRepository.save(dutySchedule));
    }

    @Override
    public void delete(Long id) {
        dutyScheduleRepository.delete(findDutySchedule(id));
    }

    private void validateTimes(DutyScheduleDTO dto) {
        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new APIException("Start time must be before end time");
        }
    }

    private Pharmacy findPharmacy(Long id) {
        return pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy", "id", id));
    }

    private DutySchedule findDutySchedule(Long id) {
        return dutyScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Duty schedule", "id", id));
    }

    private DutyScheduleDTO toDto(DutySchedule dutySchedule) {
        return DutyScheduleDTO.builder().id(dutySchedule.getId()).pharmacyId(dutySchedule.getPharmacy().getId())
                .date(dutySchedule.getDate()).startTime(dutySchedule.getStartTime()).endTime(dutySchedule.getEndTime()).build();
    }
}
