package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.BarDTO;
import com.dss_erp.dss_erp.models.BarPiece;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BarService extends BaseMaterialService<BarDTO> {

    // -------------------------------------------------------------------------
    // RECEIVE DELIVERY (matching RectTube)
    // -------------------------------------------------------------------------
    @Transactional
    BarDTO receiveDelivery(Long barId, Integer piecesReceived, Double lengthPerPieceMm);

    // -------------------------------------------------------------------------
    // MANUAL UPDATE OF QUANTITY (same as RectTube)
    // -------------------------------------------------------------------------
    @Transactional
    BarDTO updateQuantity(Long id, Double totalLengthMm);

    // -------------------------------------------------------------------------
    // CUT MATERIAL (matching RectTube)
    // -------------------------------------------------------------------------
    @Transactional
    BarPiece consumeBarMaterial(Long barId, double requiredLengthMm, String usedFor);

    // -------------------------------------------------------------------------
    // GET PIECES (same as RectTube)
    // -------------------------------------------------------------------------
    List<BarPiece> getPieces(Long barId);
}
