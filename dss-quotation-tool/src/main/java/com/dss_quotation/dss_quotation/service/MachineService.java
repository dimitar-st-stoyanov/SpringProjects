package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.models.Machine;
import com.dss_quotation.dss_quotation.payload.MachineDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MachineRequest;
import com.dss_quotation.dss_quotation.payload.MachineResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MachineService {
    List<Machine> getAll();
    Machine getById(Long id);
    Machine save(Machine machine);
    Page<MachineResponse> getPaginated(int page, int size);
    MachineDetailsResponse getDetails(Long id);
    MachineDetailsResponse create(MachineRequest request);
    MachineDetailsResponse update(Long id, MachineRequest request);
    void delete(Long id);
}
