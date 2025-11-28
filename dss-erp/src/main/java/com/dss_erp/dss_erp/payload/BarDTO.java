package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarDTO {
    private Long id;
    private String grade;
    private String unit;
    private Double density;

    private Double width;           // mm
    private Double height;          // mm
    private Double standardLength;  // mm per new bar (e.g. 6000)
    private Integer quantity;
    private Double weight;
    private String name;

    // Stock info
    private Double totalLength;
    private Double availableLength;
    private Long availablePieces;

    // Optional: nested per-piece details
    private List<BarPieceDTO> pieces;
}
