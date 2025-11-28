package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.RodDTO;
import com.dss_erp.dss_erp.models.RodPiece;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RodService extends BaseMaterialService<RodDTO> {

    @Transactional
    RodDTO receiveDelivery(Long rodId, Integer piecesReceived, Double lengthPerPieceMm);

    @Transactional
    RodDTO updateQuantity(Long id, Double totalLengthMm);

    @Transactional
    RodPiece consumeRodMaterial(Long rodId, double requiredLengthMm);

    List<RodPiece> getPieces(Long rodId);
}
