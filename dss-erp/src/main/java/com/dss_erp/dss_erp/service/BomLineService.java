package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.BomLine;
import com.dss_erp.dss_erp.models.UnitOfMeasure;
import com.dss_erp.dss_erp.payload.BomLineDTO;
import com.dss_erp.dss_erp.payload.BomLineViewDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BomLineService {


    @Transactional
    BomLine addBomLine(Long bomId, BomLineDTO dto);


    List<BomLineViewDTO> getBom(Long bomId);

    BomLineDTO deleteBomLine(Long id);

    BomLineDTO updateBomLine(Long id,BomLineDTO dto );
}
