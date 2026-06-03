package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.WorkCenterDTO;
import com.dss_erp.dss_erp.payload.WorkCenterResponse;
import org.springframework.data.domain.Pageable;

public interface WorkCenterService {

    WorkCenterDTO create(WorkCenterDTO dto);

    WorkCenterDTO update(Long id, WorkCenterDTO dto);

    WorkCenterDTO getById(Long id);

    WorkCenterResponse getAll(Pageable pageable);

    void delete(Long id);
}