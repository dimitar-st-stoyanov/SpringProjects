package com.dss_erp.dss_erp.payload;

import lombok.Data;

@Data
public class PiecesDeliveryDTO {
        private Integer piecesReceived;     // how many tubes
        private Double lengthPerPieceMm;    // e.g., 6000
    }