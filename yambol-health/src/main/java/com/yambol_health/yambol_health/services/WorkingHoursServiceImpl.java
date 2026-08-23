package com.yambol_health.yambol_health.services;

import com.yambol_health.yambol_health.exceptions.APIException;
import com.yambol_health.yambol_health.exceptions.ResourceNotFoundException;
import com.yambol_health.yambol_health.models.Pharmacy;
import com.yambol_health.yambol_health.models.WorkingHours;
import com.yambol_health.yambol_health.payloads.WorkingHoursDTO;
import com.yambol_health.yambol_health.repositories.PharmacyRepository;
import com.yambol_health.yambol_health.repositories.WorkingHoursRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkingHoursServiceImpl implements WorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;
    private final PharmacyRepository pharmacyRepository;

    public WorkingHoursServiceImpl(WorkingHoursRepository workingHoursRepository, PharmacyRepository pharmacyRepository) {
        this.workingHoursRepository = workingHoursRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public WorkingHoursDTO create(WorkingHoursDTO dto) {
        validateTimes(dto);
        WorkingHours workingHours = WorkingHours.builder()
                .pharmacy(findPharmacy(dto.getPharmacyId())).dayOfWeek(dto.getDayOfWeek())
                .openTime(dto.getOpenTime()).closeTime(dto.getCloseTime()).build();
        return toDto(workingHoursRepository.save(workingHours));
    }

    @Override
    public List<WorkingHoursDTO> getByPharmacyId(Long pharmacyId) {
        findPharmacy(pharmacyId);
        return workingHoursRepository.findByPharmacyIdOrderByDayOfWeekAsc(pharmacyId).stream().map(this::toDto).toList();
    }

    @Override
    public WorkingHoursDTO update(Long id, WorkingHoursDTO dto) {
        validateTimes(dto);
        WorkingHours workingHours = findWorkingHours(id);
        workingHours.setPharmacy(findPharmacy(dto.getPharmacyId()));
        workingHours.setDayOfWeek(dto.getDayOfWeek());
        workingHours.setOpenTime(dto.getOpenTime());
        workingHours.setCloseTime(dto.getCloseTime());
        return toDto(workingHoursRepository.save(workingHours));
    }

    @Override
    public void delete(Long id) {
        workingHoursRepository.delete(findWorkingHours(id));
    }

    private void validateTimes(WorkingHoursDTO dto) {
        if (!dto.getOpenTime().isBefore(dto.getCloseTime())) {
            throw new APIException("Opening time must be before closing time");
        }
    }

    private Pharmacy findPharmacy(Long id) {
        return pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy", "id", id));
    }

    private WorkingHours findWorkingHours(Long id) {
        return workingHoursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Working hours", "id", id));
    }

    private WorkingHoursDTO toDto(WorkingHours workingHours) {
        return WorkingHoursDTO.builder().id(workingHours.getId()).pharmacyId(workingHours.getPharmacy().getId())
                .dayOfWeek(workingHours.getDayOfWeek()).openTime(workingHours.getOpenTime())
                .closeTime(workingHours.getCloseTime()).build();
    }
}
