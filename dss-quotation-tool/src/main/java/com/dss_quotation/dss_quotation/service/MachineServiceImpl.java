package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.dss_quotation.dss_quotation.exceptions.ResourceNotFoundException;
import com.dss_quotation.dss_quotation.models.Machine;
import com.dss_quotation.dss_quotation.payload.MachineDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MachineRequest;
import com.dss_quotation.dss_quotation.payload.MachineResponse;
import com.dss_quotation.dss_quotation.repositories.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    @Override
    public List<Machine> getAll() {
        return machineRepository.findAll();
    }

    @Override
    public Machine getById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", id));
    }

    @Override
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public Page<MachineResponse> getPaginated(int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return machineRepository.findAllByOrderByNameAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public MachineDetailsResponse getDetails(Long id) {
        return mapToDetailsResponse(getById(id));
    }

    @Override
    public MachineDetailsResponse create(MachineRequest request) {
        validateRequest(request);

        String name = request.getName().trim();

        if (machineRepository.existsByNameIgnoreCase(name)) {
            throw new APIException("Machine with this name already exists");
        }

        Machine machine = Machine.builder()
                .name(name)
                .power(request.getPower())
                .ratePerHour(request.getRatePerHour())
                .efficiencyFactor(request.getEfficiencyFactor())
                .build();

        return mapToDetailsResponse(machineRepository.save(machine));
    }

    @Override
    public MachineDetailsResponse update(Long id, MachineRequest request) {
        validateRequest(request);

        Machine machine = getById(id);
        String name = request.getName().trim();

        if (machineRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new APIException("Machine with this name already exists");
        }

        machine.setName(name);
        machine.setPower(request.getPower());
        machine.setRatePerHour(request.getRatePerHour());
        machine.setEfficiencyFactor(request.getEfficiencyFactor());

        return mapToDetailsResponse(machineRepository.save(machine));
    }

    @Override
    public void delete(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Machine", "id", id);
        }

        machineRepository.deleteById(id);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new APIException("Page number cannot be negative");
        }

        if (size <= 0) {
            throw new APIException("Page size must be greater than zero");
        }
    }

    private void validateRequest(MachineRequest request) {
        if (request == null) {
            throw new APIException("Machine request is required");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new APIException("Machine name is required");
        }

        if (request.getPower() == null || request.getPower() <= 0) {
            throw new APIException("Power must be greater than zero");
        }

        if (request.getRatePerHour() == null || request.getRatePerHour() <= 0) {
            throw new APIException("Rate per hour must be greater than zero");
        }

        if (request.getEfficiencyFactor() == null || request.getEfficiencyFactor() <= 0) {
            throw new APIException("Efficiency factor must be greater than zero");
        }
    }

    private MachineResponse mapToResponse(Machine machine) {
        return MachineResponse.builder()
                .id(machine.getId())
                .name(machine.getName())
                .power(machine.getPower())
                .ratePerHour(machine.getRatePerHour())
                .efficiencyFactor(machine.getEfficiencyFactor())
                .minimumCharge(machine.getMinimumCharge())
                .build();
    }

    private MachineDetailsResponse mapToDetailsResponse(Machine machine) {
        return MachineDetailsResponse.builder()
                .id(machine.getId())
                .name(machine.getName())
                .power(machine.getPower())
                .ratePerHour(machine.getRatePerHour())
                .efficiencyFactor(machine.getEfficiencyFactor())
                .minimumCharge(machine.getMinimumCharge())
                .build();
    }
}
