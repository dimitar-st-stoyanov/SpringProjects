package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.models.Operation;
import com.dss_quotation.dss_quotation.payload.OperationDetailsResponse;
import com.dss_quotation.dss_quotation.payload.OperationRequest;
import com.dss_quotation.dss_quotation.payload.OperationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OperationService {

    List<Operation> getAll();

    List<Operation> getActive();

    Operation getById(Long id);

    Page<OperationResponse> getPaginated(int page, int size);

    Page<OperationResponse> getActivePaginated(int page, int size);

    OperationDetailsResponse getDetails(Long id);

    OperationDetailsResponse create(OperationRequest request);

    OperationDetailsResponse update(Long id, OperationRequest request);

    void delete(Long id);
}
