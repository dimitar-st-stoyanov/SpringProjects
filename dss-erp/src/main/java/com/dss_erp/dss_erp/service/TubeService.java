package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.TubeDTO;
import com.dss_erp.dss_erp.models.TubePiece;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TubeService extends BaseMaterialService<TubeDTO> {

    @Transactional
    TubeDTO receiveDelivery(Long tubeId, Integer piecesReceived, Double lengthPerPieceMm);

    @Transactional
    TubeDTO updateQuantity(Long id, Double totalLengthMm);

    @Transactional
    TubePiece consumeTubeMaterial(Long tubeId, double requiredLengthMm, String usedFor);

    List<TubePiece> getPieces(Long tubeId);
}
