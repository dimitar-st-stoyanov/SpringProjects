package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.models.MachineCutParameters;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersRequest;
import com.dss_quotation.dss_quotation.payload.MachineCutParametersResponse;
import org.springframework.data.domain.Page;

public interface MachineCutParametersService {

    double getCutSpeed(Long machineId, double thickness);

    MachineCutParameters getCutParameters(Long machineId, Long materialId, double thickness);

    Page<MachineCutParametersResponse> getAll(int page, int size);

    Page<MachineCutParametersResponse> getByMachine(Long machineId, int page, int size);

    Page<MachineCutParametersResponse> getByMachineAndMaterial(Long machineId, Long materialId, int page, int size);

    MachineCutParametersDetailsResponse getDetails(Long id);

    MachineCutParametersDetailsResponse getClosestDetails(Long machineId, Long materialId, double thickness);

    MachineCutParametersDetailsResponse save(MachineCutParametersRequest request);

    void delete(Long id);

}
