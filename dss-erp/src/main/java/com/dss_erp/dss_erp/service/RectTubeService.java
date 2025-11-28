package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.RectTubeDTO;
import com.dss_erp.dss_erp.models.RectTubePiece;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RectTubeService extends BaseMaterialService<RectTubeDTO> {


    // -------------------------------------------------------------------------
    // RECEIVE DELIVERY (matching Tube)
    // -------------------------------------------------------------------------
    @Transactional
    RectTubeDTO receiveDelivery(Long tubeId, Integer piecesReceived, Double lengthPerPieceMm);

    // -------------------------------------------------------------------------
    // MANUAL UPDATE OF QUANTITY
    // (same structure as Tube)
    // -------------------------------------------------------------------------
    @Transactional
    RectTubeDTO updateQuantity(Long id, Double totalLengthMm);

    // -------------------------------------------------------------------------
    // CUT MATERIAL (matching Tube)
    // -------------------------------------------------------------------------
    @Transactional
    RectTubePiece consumeRectTubeMaterial(Long tubeId, double requiredLengthMm, String usedFor);

    // -------------------------------------------------------------------------
    // GET PIECES
    // -------------------------------------------------------------------------
    List<RectTubePiece> getPieces(Long tubeId);
}
