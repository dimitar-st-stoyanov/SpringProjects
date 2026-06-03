package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.dss_quotation.dss_quotation.exceptions.ResourceNotFoundException;
import com.dss_quotation.dss_quotation.models.Machine;
import com.dss_quotation.dss_quotation.models.MachineCutParameters;
import com.dss_quotation.dss_quotation.models.Material;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersRequest;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersResponse;
import com.dss_quotation.dss_quotation.repositories.MachineCutParametersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineCutParametersServiceImpl implements MachineCutParametersService {

    private final MachineCutParametersRepository repository;
    private final MachineService machineService;
    private final MaterialService materialService;

    @Override
    public double getCutSpeed(Long machineId, double thickness) {
        MachineCutParameters closest = getClosest(repository.findByMachineIdOrderByThicknessAsc(machineId), thickness);

        if (closest == null) {
            return 1000;
        }

        return closest.getSpeed();
    }

    @Override
    public MachineCutParameters getCutParameters(Long machineId, Long materialId, double thickness) {
        MachineCutParameters closest = getClosest(repository.findByMachineIdAndMaterialIdOrderByThicknessAsc(machineId, materialId), thickness);

        if (closest == null) {
            throw new ResourceNotFoundException("Machine cut parameters not found for selected machine and material");
        }

        return closest;
    }

    private MachineCutParameters getClosest(List<MachineCutParameters> parameters, double thickness) {
        if (parameters.isEmpty()) {
            return null;
        }

        MachineCutParameters closest = parameters.get(0);
        double minDiff = Math.abs(thickness - closest.getThickness());

        for (MachineCutParameters parameter : parameters) {
            double diff = Math.abs(thickness - parameter.getThickness());

            if (diff < minDiff) {
                minDiff = diff;
                closest = parameter;
            }
        }

        return closest;
    }

    @Override
    public Page<MachineCutParametersResponse> getAll(int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<MachineCutParametersResponse> getByMachine(Long machineId, int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByMachineIdOrderByThicknessAsc(machineId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<MachineCutParametersResponse> getByMachineAndMaterial(Long machineId, Long materialId, int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByMachineIdAndMaterialIdOrderByThicknessAsc(machineId, materialId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public MachineCutParametersDetailsResponse getDetails(Long id) {
        MachineCutParameters parameters = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine cut parameters", "id", id));

        return mapToDetailsResponse(parameters);
    }

    @Override
    public MachineCutParametersDetailsResponse getClosestDetails(Long machineId, Long materialId, double thickness) {
        if (thickness <= 0) {
            throw new APIException("Thickness must be greater than zero");
        }

        return mapToDetailsResponse(getCutParameters(machineId, materialId, thickness));
    }

    @Override
    public MachineCutParametersDetailsResponse save(MachineCutParametersRequest request) {
        validateRequest(request);

        Machine machine = machineService.getById(request.getMachineId());
        Material material = materialService.getById(request.getMaterialId());

        MachineCutParameters parameters = MachineCutParameters.builder()
                .machine(machine)
                .material(material)
                .thickness(request.getThickness())
                .speed(request.getSpeed())
                .pierceTime(request.getPierceTime())
                .pierceHeight(request.getPierceHeight())
                .gasType(request.getGasType())
                .build();

        return mapToDetailsResponse(repository.save(parameters));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Machine cut parameters", "id", id);
        }

        repository.deleteById(id);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new APIException("Page number cannot be negative");
        }

        if (size <= 0) {
            throw new APIException("Page size must be greater than zero");
        }
    }

    private void validateRequest(MachineCutParametersRequest request) {
        if (request == null) {
            throw new APIException("Machine cut parameters request is required");
        }

        if (request.getMachineId() == null) {
            throw new APIException("Machine is required");
        }

        if (request.getMaterialId() == null) {
            throw new APIException("Material is required");
        }

        if (request.getThickness() == null || request.getThickness() <= 0) {
            throw new APIException("Thickness must be greater than zero");
        }

        if (request.getSpeed() == null || request.getSpeed() <= 0) {
            throw new APIException("Speed must be greater than zero");
        }

        if (request.getPierceTime() == null || request.getPierceTime() < 0) {
            throw new APIException("Pierce time cannot be negative");
        }

        if (request.getPierceHeight() == null || request.getPierceHeight() < 0) {
            throw new APIException("Pierce height cannot be negative");
        }

        if (request.getGasType() == null) {
            throw new APIException("Gas type is required");
        }
    }

    private MachineCutParametersResponse mapToResponse(MachineCutParameters parameters) {
        return MachineCutParametersResponse.builder()
                .id(parameters.getId())
                .machineId(parameters.getMachine().getId())
                .machineName(parameters.getMachine().getName())
                .materialId(parameters.getMaterial().getId())
                .materialName(parameters.getMaterial().getName())
                .thickness(parameters.getThickness())
                .speed(parameters.getSpeed())
                .pierceTime(parameters.getPierceTime())
                .pierceHeight(parameters.getPierceHeight())
                .gasType(parameters.getGasType())
                .build();
    }

    private MachineCutParametersDetailsResponse mapToDetailsResponse(MachineCutParameters parameters) {
        return MachineCutParametersDetailsResponse.builder()
                .id(parameters.getId())
                .machineId(parameters.getMachine().getId())
                .machineName(parameters.getMachine().getName())
                .machinePower(parameters.getMachine().getPower())
                .materialId(parameters.getMaterial().getId())
                .materialName(parameters.getMaterial().getName())
                .materialType(parameters.getMaterial().getType())
                .thickness(parameters.getThickness())
                .speed(parameters.getSpeed())
                .pierceTime(parameters.getPierceTime())
                .pierceHeight(parameters.getPierceHeight())
                .gasType(parameters.getGasType())
                .build();
    }
}
