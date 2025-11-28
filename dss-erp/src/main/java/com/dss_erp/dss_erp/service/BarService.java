package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.BarDTO;
import com.dss_erp.dss_erp.models.BarPiece;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface BarService extends BaseMaterialService<BarDTO> {

    @Transactional
    BarDTO receiveDelivery(Long barId, Integer piecesReceived, Double lengthPerPieceMm);
    /** Update quantity and adjust BarPiece records */
    BarDTO updateQuantity(Long id, Integer quantity);

    /** Cut a piece and return updated piece info */
    BarPiece cutPiece(Long barId, Long pieceId, double cutLengthMm);

    /** Optional: get all pieces of a bar */
    List<BarPiece> getPieces(Long barId);
}
