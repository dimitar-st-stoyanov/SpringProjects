package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.dss_quotation.dss_quotation.exceptions.ResourceNotFoundException;
import com.dss_quotation.dss_quotation.models.Operation;
import com.dss_quotation.dss_quotation.payload.OperationDetailsResponse;
import com.dss_quotation.dss_quotation.payload.OperationRequest;
import com.dss_quotation.dss_quotation.payload.OperationResponse;
import com.dss_quotation.dss_quotation.repositories.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Override
    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    @Override
    public List<Operation> getActive() {
        return operationRepository.findByActiveTrue();
    }

    @Override
    public Operation getById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operation", "id", id));
    }

    @Override
    public Page<OperationResponse> getPaginated(int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return operationRepository.findAllByOrderByNameAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<OperationResponse> getActivePaginated(int page, int size) {
        validatePagination(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return operationRepository.findByActiveTrueOrderByNameAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public OperationDetailsResponse getDetails(Long id) {
        return mapToDetailsResponse(getById(id));
    }

    @Override
    public OperationDetailsResponse create(OperationRequest request) {
        validateRequest(request);

        String name = request.getName().trim();

        if (operationRepository.existsByNameIgnoreCase(name)) {
            throw new APIException("Operation with this name already exists");
        }

        Operation operation = Operation.builder()
                .name(name)
                .pricingMode(request.getPricingMode())
                .rate(request.getRate())
                .active(true)
                .build();

        return mapToDetailsResponse(operationRepository.save(operation));
    }

    @Override
    public OperationDetailsResponse update(Long id, OperationRequest request) {
        validateRequest(request);

        Operation existing = getById(id);
        String name = request.getName().trim();

        if (operationRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new APIException("Operation with this name already exists");
        }

        existing.setName(name);
        existing.setPricingMode(request.getPricingMode());
        existing.setRate(request.getRate());

        return mapToDetailsResponse(operationRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Operation operation = getById(id);
        operation.setActive(false);
        operationRepository.save(operation);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new APIException("Page number cannot be negative");
        }

        if (size <= 0) {
            throw new APIException("Page size must be greater than zero");
        }
    }

    private void validateRequest(OperationRequest request) {
        if (request == null) {
            throw new APIException("Operation request is required");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new APIException("Operation name is required");
        }

        if (request.getPricingMode() == null) {
            throw new APIException("Pricing mode is required");
        }

        if (request.getRate() == null || request.getRate() <= 0) {
            throw new APIException("Rate must be greater than zero");
        }
    }

    private OperationResponse mapToResponse(Operation operation) {
        return OperationResponse.builder()
                .id(operation.getId())
                .name(operation.getName())
                .pricingMode(operation.getPricingMode())
                .rate(operation.getRate())
                .active(operation.isActive())
                .build();
    }

    private OperationDetailsResponse mapToDetailsResponse(Operation operation) {
        return OperationDetailsResponse.builder()
                .id(operation.getId())
                .name(operation.getName())
                .pricingMode(operation.getPricingMode())
                .rate(operation.getRate())
                .active(operation.isActive())
                .build();
    }
}
